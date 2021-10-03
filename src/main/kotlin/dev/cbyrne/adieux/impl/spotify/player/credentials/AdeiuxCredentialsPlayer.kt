package dev.cbyrne.adieux.impl.spotify.player.credentials

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdeiuxCredentialsType
import dev.cbyrne.adieux.core.spotify.player.AdeiuxSpotifyPlayer
import xyz.gianlu.librespot.ZeroconfServer
import xyz.gianlu.librespot.core.Session
import xyz.gianlu.librespot.player.Player
import java.io.File

class AdeiuxCredentialsPlayer(
    private val credentials: AdeiuxCredentialsType,
    deviceName: String = "Adeiux",
    deviceType: Connect.DeviceType = Connect.DeviceType.CAST_AUDIO,
) : AdeiuxSpotifyPlayer(deviceName, deviceType) {
    override fun connect() {
        player = Player(config, createSession())
    }

    private fun createSession(): Session {
        val sessionBuilder = Session.Builder()
            .setPreferredLocale("en")
            .setDeviceType(deviceType)
            .setDeviceName(deviceName)
            .setDeviceId(null)

        when (credentials) {
            is AdeiuxCredentialsType.Stored -> sessionBuilder.stored(credentials.file)
            is AdeiuxCredentialsType.UserPass -> sessionBuilder.userPass(credentials.username, credentials.password)
        }

        return sessionBuilder.create()
    }

    private fun Session.Builder.stored(file: File? = null) {
        if (file != null) this.stored(file)
        else this.stored()
    }
}