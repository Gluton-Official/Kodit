package discord.commands

import dev.kord.core.Kord
import dev.kord.core.entity.interaction.MessageCommandInteraction
import dev.kord.core.event.interaction.MessageCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.MessageCommandCreateBuilder

private val callbacks = mutableMapOf<String, suspend MessageCommandInteraction.() -> Unit>()

context(Kord) suspend fun initInteractionCommands() {
    kodit()

    messageInteractionEvent()
}

context(Kord) private suspend fun kodit() = messageInteraction(
    name = "Kodit",
    callback = {
        TODO("Manual formatting")
    }
)

context(Kord) private suspend fun messageInteraction(
    name: String,
    builder: MessageCommandCreateBuilder.() -> Unit = {},
    callback: suspend MessageCommandInteraction.() -> Unit = {}
) {
    createGlobalMessageCommand(name, builder)
    callbacks[name] = callback
}


context(Kord) private fun messageInteractionEvent() = on<MessageCommandInteractionCreateEvent> {
    interaction.apply {
        println(invokedCommandName)
        callbacks[invokedCommandName]!!()
    }
}
