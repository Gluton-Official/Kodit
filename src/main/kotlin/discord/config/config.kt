package discord.config

import database.tables.ConfigTable
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.rest.builder.interaction.boolean
import org.jetbrains.exposed.sql.Column
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KMutableProperty1

typealias ConfigProperty<E, T> = KMutableProperty1<ConfigTable<E>.ConfigEntity, T>
typealias ArgAccessor<T> = InteractionCommand.() -> T?

inline fun <E : ConfigTable<E>.ConfigEntity, reified T> ConfigTable<E>.config(description: String, default: T) =
    PropertyDelegateProvider<ConfigTable<E>, ReadOnlyProperty<ConfigTable<E>, Column<T>>> { configTable, columnDelegate ->
        val (columnBuilder, options, argAccessor) = configTable.getConfigBuilder<T>()
        Config.commands += ConfigCommand(columnDelegate.name, description, default, options, argAccessor)
        val column = columnBuilder(columnDelegate.name).default(default)
        ReadOnlyProperty { _, _ -> column }
    }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> ConfigTable<*>.getConfigBuilder() = when (T::class) {
    Boolean::class -> ConfigBuilder(::bool, { boolean("enabled", "True to enable") }, { booleans["enabled"] })
    else -> error("Unknown column type: ${T::class.simpleName}")
} as ConfigBuilder<T>

object Config {
    val commands = mutableSetOf<ConfigCommand<*>>()
}
