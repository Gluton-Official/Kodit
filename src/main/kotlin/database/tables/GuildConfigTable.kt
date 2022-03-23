package database.tables

import dev.kord.common.entity.Snowflake
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID

object GuildConfigTable : ConfigTable<GuildConfigTable.GuildConfig>("guild", { GuildConfig }) {
    class GuildConfig(guildId: EntityID<Snowflake>) : ConfigEntity(guildId) {
        companion object : EntityClass<Snowflake, GuildConfig>(GuildConfigTable)
    }
}

