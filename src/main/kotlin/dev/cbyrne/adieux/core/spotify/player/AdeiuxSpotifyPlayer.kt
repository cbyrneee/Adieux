package dev.cbyrne.adieux.core.spotify.player

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.impl.spotify.mixing.AdeiuxSinkOutput
import xyz.gianlu.librespot.player.Player
import xyz.gianlu.librespot.player.PlayerConfiguration

abstract class AdeiuxSpotifyPlayer {
    abstract fun connect(deviceName: String = "Adeiux", deviceType: Connect.DeviceType = Connect.DeviceType.AUDIO_DONGLE)

    val config = PlayerConfiguration.Builder()
        .setOutput(PlayerConfiguration.AudioOutput.CUSTOM)
        .setOutputClass(AdeiuxSinkOutput::class.java.name)
//        .setOutput(PlayerConfiguration.AudioOutput.MIXER)
//        .setMixerSearchKeywords(arrayOf())
//        .setLogAvailableMixers(true)
        .build()

    var listener: AdeiuxEventListener? = null
        set(value) {
            if (value == null) return

            player?.apply { addEventsListener(value) }
            field = value
        }

    var player: Player? = null
        protected set(value) {
            if (value == null) return

            listener?.apply { value.addEventsListener(this) }
            field = value
        }

    fun disconnect() {
        player?.close()
        player = null
    }
}