package discord.config

import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import org.jetbrains.exposed.sql.Column

data class ConfigBuilder<T>(
    val columnBuilder: (String) -> Column<T>,
    val options: ChatInputCreateBuilder.() -> Unit,
    val argAccessor: ArgAccessor<T>
)
