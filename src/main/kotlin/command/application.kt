package command

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.kordLogger
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.boolean

private val callbacks = mutableMapOf<String, suspend ChatInputCommandInteraction.() -> Unit>()

suspend fun Kord.initApplicationCommands() {
    // TODO: these should both be guild settings
    autoFormat()
    overwriteMessage()

    applicationCommandEvent()
}

private suspend fun Kord.autoFormat() = applicationCommand(
    name = "auto_format",
    description = "Enable or disable auto formatting of messages when sent",
    builder = {
        boolean("boolean", "True to enable auto format") {
            required = true
        }
    },
    callback = {
        deferEphemeralMessage().edit {
            content = "autoFormat: ${command.booleans["boolean"]}"
        }
    }
)

private suspend fun Kord.overwriteMessage() = applicationCommand(
    name = "overwrite_message",
    description = "If autoFormat is enabled, the original message will be removed",
    builder = {
        boolean("boolean", "True to overwrite messages") {
            required = true
        }
    },
    callback = {
        deferEphemeralMessage().edit {
            content = "overwriteMessage: ${command.booleans["boolean"]}"
        }
    }
)

private suspend fun Kord.applicationCommand(
    name: String,
    description: String,
    builder: ChatInputCreateBuilder.() -> Unit = {},
    callback: suspend ChatInputCommandInteraction.() -> Unit = {}
) {
    createGlobalChatInputCommand(name, description, builder)
    callbacks[name] = callback
}

private fun Kord.applicationCommandEvent() = on<ChatInputCommandInteractionCreateEvent> {
    interaction.apply {
        kordLogger.debug(command.rootName)
        callbacks[command.rootName]!!()
    }
}
