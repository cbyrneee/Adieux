package dev.cbyrne.adieux.impl.spotify.player.zeroconf

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.core.spotify.player.AdieuxSpotifyPlayer
import xyz.gianlu.librespot.ZeroconfServer
import xyz.gianlu.librespot.core.Session
import xyz.gianlu.librespot.player.Player

class AdieuxZeroconfPlayer : AdieuxSpotifyPlayer(), ZeroconfServer.SessionListener {
    private var session: Session? = null
    private var server: ZeroconfServer? = null
        set(value) {
            if (value == null) return
            value.addSessionListener(this)

            field = value
        }

    override fun connect(deviceName: String, deviceType: Connect.DeviceType) {
        server = createServer(deviceName, deviceType)
    }

    override fun disconnect() {
        super.disconnect()

        server?.close()
        session?.close()

        server = null
        session = null
    }

    override fun sessionClosing(session: Session) {
        disconnect()
    }

    override fun sessionChanged(session: Session) {
        player = Player(config, session)
        this.session = session
    }

    private fun createServer(deviceName: String, deviceType: Connect.DeviceType) = ZeroconfServer.Builder()
        .setDeviceName(deviceName)
        .setDeviceType(deviceType)
        .setPreferredLocale("en")
        .setDeviceId(null)
        .create()
}