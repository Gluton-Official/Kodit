import dev.kord.core.Kord
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

suspend fun Kord.initSettings() {
    val database = Database.connect(
        url = "jdbc:mysql://${dotenv["DB_IP"]}:${dotenv["DB_PORT"]}/${dotenv["DB_NAME"]}?autoReconnect=true",
//        driver = "com.mysql.jdbc.Driver",
        user = dotenv["DB_USER"],
        password = dotenv["DB_PASSWORD"],
        setupConnection = { connection ->
            println(connection.metaData.userName)
        }
    )
    println(database.name)
}
