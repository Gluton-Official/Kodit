package discord.config

import database.tables.ChannelConfigTable
import database.tables.ConfigTable
import database.tables.GuildConfigTable
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import discord.commands.*
import discord.commands.Scope.Companion.scope
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.reflect.full.memberProperties

data class ConfigCommand<T>(
    val name: String,
    val description: String,
    val default: T,
    val options: ChatInputCreateBuilder.() -> Unit,
    val argAccessor: ArgAccessor<T>
) {
    @Suppress("UNCHECKED_CAST")
    private val _property = ConfigTable.ConfigEntity::class.memberProperties.find { it.name == name }!! as ConfigProperty<*, T>
    context(ConfigTable<*>.ConfigEntity) var property: T
        get() = _property.get(this@ConfigEntity)
        set(value) = _property.set(this@ConfigEntity, value)

    val callback: SlashCommandCallback = callback@{
        val (table, id) = try {
            getTableAndSnowflakeForScope(command.scope, command)
        } catch (e: IllegalArgumentException) {
            ephemeralMessage {
                content = e.message
            }
            return@callback
        }

        val message: String

        val argValue = command.argAccessor()
        if (argValue != null) {
            val configValue = transaction {
                table.createOrUpdate(id) {
                    property = argValue
                }.pruneIfDefault()?.run(::property) ?: default
            }
            message = "Set `$name` to `$configValue` in `${command.scope.friendlyName}`"
        } else {
            val configValue = transaction {
                table.get(id)?.run(::property) ?: default
            }
            message = "`$name`is `$configValue` in `${command.scope.friendlyName}`"
        }

        ephemeralMessage {
            content = message
        }
    }

    context(Interaction) private fun getTableAndSnowflakeForScope(scope: Scope, command: InteractionCommand): Pair<ConfigTable<*>, Snowflake> =
        when (scope) {
            Scope.Guild -> {
                if (command.guildId == null) throw IllegalArgumentException("Scope 'Server' is not applicable here!")
                Pair(GuildConfigTable, command.guildId!!)
            }
            Scope.Channel -> Pair(ChannelConfigTable, command.channelId)
        }
}
