package dev.cbyrne.adieux.core.spotify.player

import xyz.gianlu.librespot.audio.MetadataWrapper
import xyz.gianlu.librespot.metadata.PlayableId
import xyz.gianlu.librespot.player.Player

interface AdieuxEventListener : Player.EventsListener {
    override fun onContextChanged(player: Player, newUri: String) {
    }

    override fun onTrackChanged(player: Player, id: PlayableId, metadata: MetadataWrapper?, userInitiated: Boolean) {
    }

    override fun onPlaybackEnded(player: Player) {
    }

    override fun onPlaybackPaused(player: Player, trackTime: Long) {
    }

    override fun onPlaybackResumed(player: Player, trackTime: Long) {
    }

    override fun onTrackSeeked(player: Player, trackTime: Long) {
    }

    override fun onMetadataAvailable(player: Player, metadata: MetadataWrapper) {
    }

    override fun onPlaybackHaltStateChanged(player: Player, halted: Boolean, trackTime: Long) {
    }

    override fun onInactiveSession(player: Player, timeout: Boolean) {
    }

    override fun onVolumeChanged(player: Player, volume: Float) {
    }

    override fun onPanicState(player: Player) {
    }
}