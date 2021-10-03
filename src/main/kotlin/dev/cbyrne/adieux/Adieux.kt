package dev.cbyrne.adieux

import dev.cbyrne.adieux.core.spotify.player.AdeiuxEventListener
import dev.cbyrne.adieux.impl.spotify.player.credentials.AdeiuxCredentialsPlayer
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdeiuxCredentialsType

class Adieux : AdeiuxEventListener {
    fun start() {
        val player = AdeiuxCredentialsPlayer(AdeiuxCredentialsType.Stored())
        player.connect()
    }
}