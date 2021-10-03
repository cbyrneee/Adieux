package dev.cbyrne.adieux.impl.spotify.player.credentials

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.core.spotify.player.AdeiuxSpotifyPlayer
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdeiuxCredentialsType
import xyz.gianlu.librespot.core.Session
import xyz.gianlu.librespot.player.Player
import java.io.File

class AdeiuxCredentialsPlayer(
    private val credentials: AdeiuxCredentialsType,
) : AdeiuxSpotifyPlayer() {
    private var session: Session? = null

    override fun connect(deviceName: String, deviceType: Connect.DeviceType) {
        val session = createSession(deviceName, deviceType)
        player = Player(config, session)

        this.session = session
    }

    override fun disconnect() {
        super.disconnect()

        session?.close()
        session = null
    }

    private fun createSession(deviceName: String, deviceType: Connect.DeviceType): Session {
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