package database.tables

import database.SnowflakeColumnType
import dev.kord.common.entity.Snowflake
import discord.config.Config
import discord.config.config
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Transaction

abstract class ConfigTable<E : ConfigTable<E>.ConfigEntity>(
    name: String,
    entityClassSupplier: () -> EntityClass<Snowflake, E>
) : SimpleIdTable<Snowflake, E>("${name}_config", entityClassSupplier) {
    final override val id = registerColumn<Snowflake>("${name}_id", SnowflakeColumnType()).uniqueIndex().entityId()

    val auto_format by config(
        description = "Auto format messages with unformatted code when sent",
        default = true
    )
    val overwrite_message by config(
        description = "If autoFormat is enabled, the original message will be removed",
        default = true
    )

    override val primaryKey = PrimaryKey(id)

    abstract inner class ConfigEntity(id: EntityID<Snowflake>) : Entity<Snowflake>(id) {
        var auto_format by this@ConfigTable.auto_format
        var overwrite_message by this@ConfigTable.overwrite_message

        context(Transaction) fun pruneIfDefault(): ConfigEntity? {
            Config.commands.forEach {
                if (it.property != it.default) {
                    return this
                }
            }
            delete()
            return null
        }
    }
}


