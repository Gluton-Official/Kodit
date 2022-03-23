package discord

import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import discord.commands.initApplicationCommands
import discord.commands.initInteractionCommands

suspend fun initKodit(token: String) {
    Kord(token).apply {
        initCommands()
        initEvents()

//    cache.register()

//    on<MessageCreateEvent> {
//        message.apply {
//            if (author?.isBot == true) return@on
//            if (content != "!ping") return@on
//            channel.createMessage("pong!")
//        }
//    }

        login {
            intents = Intents.nonPrivileged + Intents(Intent.GuildMessages)
        }
    }
}

context(Kord) private suspend fun initCommands() {
    initApplicationCommands()
    initInteractionCommands()
}

