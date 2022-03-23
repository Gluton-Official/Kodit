package discord

import database.tables.ChannelConfigTable
import database.tables.GuildConfigTable
import dev.kord.core.Kord
import dev.kord.core.event.channel.ChannelDeleteEvent
import dev.kord.core.event.guild.GuildDeleteEvent
import dev.kord.core.on
import org.jetbrains.exposed.sql.transactions.transaction

context(Kord) fun initEvents() {
    guildLeaveEvent()
    channelDeleteEvent()
}

context(Kord) private fun guildLeaveEvent() = on<GuildDeleteEvent> {
    transaction {
        guild?.channelIds?.forEach { ChannelConfigTable.delete(it) }
        GuildConfigTable.delete(guildId)
    }
}

context(Kord) private fun channelDeleteEvent() = on<ChannelDeleteEvent> {
    transaction {
        ChannelConfigTable.delete(channel.id)
    }
}
