package dev.cbyrne.adieux.impl.spotify.player.zeroconf

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.core.spotify.player.AdeiuxSpotifyPlayer
import xyz.gianlu.librespot.ZeroconfServer
import xyz.gianlu.librespot.core.Session
import xyz.gianlu.librespot.player.Player

class AdeiuxZeroconfPlayer(
    deviceName: String = "Adeiux",
    deviceType: Connect.DeviceType = Connect.DeviceType.CAST_AUDIO,
) : AdeiuxSpotifyPlayer(deviceName, deviceType) {
    override fun connect() {
        val server = ZeroconfServer.Builder()
            .setDeviceName(deviceName)
            .setDeviceType(deviceType)
            .setPreferredLocale("en")
            .setDeviceId(null)
            .create()

        server.addSessionListener(object : ZeroconfServer.SessionListener {
            override fun sessionClosing(session: Session) = disconnect()

            override fun sessionChanged(session: Session) {
                player = Player(config, session)
            }
        })
    }
}