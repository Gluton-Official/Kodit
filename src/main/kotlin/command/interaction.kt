package command

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.MessageCommandInteraction
import dev.kord.core.event.interaction.MessageCommandInteractionCreateEvent
import dev.kord.core.kordLogger
import dev.kord.core.on
import dev.kord.rest.builder.interaction.MessageCommandCreateBuilder

private val callbacks = mutableMapOf<String, suspend MessageCommandInteraction.() -> Unit>()

suspend fun Kord.initInteractionCommands() {
    kodit()

    messageInteractionEvent()
}

private suspend fun Kord.kodit() = messageInteraction(
    name = "Kodit",
    callback = {
        deferEphemeralMessage().edit {
            content = target.asMessage().content
        }
    }
)

private suspend fun Kord.messageInteraction(
    name: String,
    builder: MessageCommandCreateBuilder.() -> Unit = {},
    callback: suspend MessageCommandInteraction.() -> Unit = {}
) {
    createGlobalMessageCommand(name, builder)
    callbacks[name] = callback
}


private fun Kord.messageInteractionEvent() = on<MessageCommandInteractionCreateEvent> {
    interaction.apply {
        kordLogger.debug(invokedCommandName)
        callbacks[invokedCommandName]!!()
    }
}
