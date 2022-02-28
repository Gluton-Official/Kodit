import command.initKoditCommands
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.github.cdimascio.dotenv.dotenv

// TODO: add git (not .env), push to server, use remote development, try to use database

val dotenv = dotenv()

suspend fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    Kord(dotenv["TOKEN"]).apply {
        initKoditCommands()
//        initSettings()

        cache.register()

        on<MessageCreateEvent> {
            message.apply {
                if (author?.isBot == true) return@on
                if (content != "!ping") return@on
                channel.createMessage("pong!")
            }
        }

        login {
            @OptIn(PrivilegedIntent::class)
            intents = Intents.nonPrivileged + Intents(Intent.GuildMessages)
        }
    }
}
