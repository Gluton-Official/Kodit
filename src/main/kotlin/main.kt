import database.initDatabase
import discord.initKodit
import io.github.cdimascio.dotenv.dotenv

// TODO: setup cuda processing on desktop

val dotenv = dotenv()

suspend fun main() {
    initDatabase()
    initKodit(dotenv["TOKEN"])
}
