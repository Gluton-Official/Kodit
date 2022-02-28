package command

import dev.kord.core.Kord

suspend fun Kord.initKoditCommands() {
    initApplicationCommands()
    initInteractionCommands()
}






