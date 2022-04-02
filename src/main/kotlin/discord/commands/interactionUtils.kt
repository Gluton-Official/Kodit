package discord.commands

import dev.kord.core.behavior.interaction.ActionInteractionBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder

suspend fun ActionInteractionBehavior.ephemeralMessage(builder: InteractionResponseModifyBuilder.() -> Unit) =
    deferEphemeralResponse().respond(builder)

context(Interaction) val InteractionCommand.channelId
    get() = this@Interaction.channelId

context(Interaction) val InteractionCommand.guildId
    get() = this@Interaction.data.guildId.value
