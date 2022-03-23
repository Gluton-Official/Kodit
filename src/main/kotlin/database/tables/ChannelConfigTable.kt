package database.tables

import dev.kord.common.entity.Snowflake
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID

object ChannelConfigTable : ConfigTable<ChannelConfigTable.ChannelConfig>("channel", { ChannelConfig }) {
    class ChannelConfig(channelId: EntityID<Snowflake>) : ConfigEntity(channelId) {
        companion object : EntityClass<Snowflake, ChannelConfig>(ChannelConfigTable)
    }
}


