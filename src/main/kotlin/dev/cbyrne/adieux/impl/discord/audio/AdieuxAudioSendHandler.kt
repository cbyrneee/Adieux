package dev.cbyrne.adieux.impl.discord.audio

import dev.cbyrne.adieux.impl.spotify.mixing.AdieuxAudioStorage
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class AdieuxAudioSendHandler(var storage: () -> AdieuxAudioStorage?) : AudioSendHandler {
    override fun canProvide() =
        storage()?.anyFullChunks() ?: false

    override fun provide20MsAudio(): ByteBuffer? =
        storage()?.consumeNextChunk().also {
            println("Consuming chunk... Size: ${it?.position() ?: "no buffer"}")
            it?.rewind()
        }

    override fun isOpus() = false
}