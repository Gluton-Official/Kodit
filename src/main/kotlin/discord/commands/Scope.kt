package discord.commands

import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.InteractionCommand

enum class Scope(
    friendlyName: String? = null,
    val predicate: context(Interaction) (InteractionCommand) -> Boolean = { true },
    private val fallback: Scope? = null
) {
    Channel,
    Guild(
        friendlyName = "Server",
        predicate = { command -> command.guildId != null },
        fallback = Channel
    );

    val friendlyName = friendlyName ?: name

    companion object {
        context(Interaction) val InteractionCommand.scope: Scope
            get() = strings["scope"]?.let { valueOf(it) }?.getScope(this@InteractionCommand) ?: Guild
    }

    context(Interaction) private fun getScope(command: InteractionCommand): Scope {
        return if (predicate(this@Interaction, command)) this else fallback?.getScope(command) ?: error("")
    }
}
