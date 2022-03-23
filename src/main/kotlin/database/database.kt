package database

import com.mysql.cj.jdbc.MysqlDataSource
import database.tables.ChannelConfigTable
import database.tables.GuildConfigTable
import dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase() {
    connect()
    ensureTables(GuildConfigTable, ChannelConfigTable)
}

fun connect() = Database.connect(
    MysqlDataSource().apply {
        databaseName = dotenv["DB_NAME"]
        serverName = dotenv["DB_IP"]
        port = dotenv["DB_PORT"].toInt()
        user = dotenv["DB_USER"]
        password = dotenv["DB_PASSWORD"]
        autoReconnect = true
        rewriteBatchedStatements = true
    }
)

fun ensureTables(vararg tables: Table, database: Database? = null) = transaction(database) {
    SchemaUtils.create(*tables)
}

