package discord.commands

import dev.kord.core.behavior.interaction.ActionInteractionBehavior
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder

suspend fun ActionInteractionBehavior.ephemeralMessage(builder: InteractionResponseModifyBuilder.() -> Unit) =
    deferEphemeralMessage().edit(builder)

context(Interaction) val InteractionCommand.channelId
    get() = this@Interaction.channelId

context(Interaction) val InteractionCommand.guildId
    get() = this@Interaction.data.guildId.value

//context(Interaction) val InteractionCommand.scope: Scope
//    get() = strings["scope"]?.let { Scope.valueOf(it) } ?: guildId?.let { Scope.Guild } ?: Scope.Channel

