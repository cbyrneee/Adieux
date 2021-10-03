package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.impl.discord.audio.AdieuxAudioSendHandler.Companion.outputStream
import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput

class AdieuxSinkOutput : SinkOutput {
    override fun write(buffer: ByteArray, offset: Int, len: Int) = outputStream.write(buffer, offset, len)
    override fun close() = stop()

    override fun setVolume(volume: Float) = true
    override fun start(format: OutputAudioFormat) = true

    override fun release() = outputStream.close()
    override fun drain() = outputStream.flush()
    override fun flush() = outputStream.flush()
    override fun stop() = outputStream.close()
}