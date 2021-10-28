package dev.cbyrne.adieux.core.spotify.player

import com.spotify.connectstate.Connect
import dev.cbyrne.adieux.impl.spotify.mixing.AdieuxMixingManager
import dev.cbyrne.adieux.impl.spotify.mixing.AdieuxSinkOutput
import xyz.gianlu.librespot.player.Player
import xyz.gianlu.librespot.player.PlayerConfiguration

abstract class AdieuxSpotifyPlayer {
    abstract fun connect(deviceName: String = "Adieux", deviceType: Connect.DeviceType = Connect.DeviceType.AUDIO_DONGLE)

    val mixingManager = AdieuxMixingManager()

    val config = PlayerConfiguration.Builder()
        .setOutput(PlayerConfiguration.AudioOutput.CUSTOM)
        .setOutputClass(AdieuxSinkOutput::class.java.name)
        .setOutputClassParams(arrayOf(mixingManager))
//        .setOutput(PlayerConfiguration.AudioOutput.MIXER)
//        .setMixerSearchKeywords(arrayOf())
//        .setLogAvailableMixers(true)
        .build()

    var listener: AdieuxEventListener? = null
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

    open fun disconnect() {
        player?.close()
        player = null
    }
}