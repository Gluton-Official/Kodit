package discord.commands

import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import discord.config.Config

typealias SlashCommandCallback = suspend ChatInputCommandInteraction.() -> Unit

private val commands = mutableMapOf<String, SlashCommandCallback>()

context(Kord) suspend fun initApplicationCommands() {
    initConfigCommands()

    slashCommandEvent()
}

context(Kord) suspend fun initConfigCommands() {
    Config.commands.forEach { configCommand ->
        slashCommand(configCommand.name, configCommand.description,
            builder = {
                (configCommand.options)()
                string(
                    name = "scope",
                    description = "The space where the config is applied, or highest available scope if not specified"
                ) {
                    Scope.values().forEach {
                        choice(it.friendlyName, it.name)
                    }
                    required = false
                }
            },
            callback = configCommand.callback
        )
    }
}

context(Kord) suspend fun slashCommand(
    name: String,
    description: String,
    builder: ChatInputCreateBuilder.() -> Unit = {},
    callback: SlashCommandCallback = {}
) {
    createGlobalChatInputCommand(name, description, builder)
//    createGuildChatInputCommand(Snowflake(dotenv["TEST_GUILD_ID"]), name, description, builder)
    commands[name] = callback
}

context(Kord) private fun slashCommandEvent() = on<ChatInputCommandInteractionCreateEvent> {
    interaction.apply {
        commands[command.rootName]!!()
    }
}
