package dev.cbyrne.adieux.impl.spotify.mixing

import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput
import java.io.OutputStream

class AdieuxSinkOutput(private val stream: OutputStream = OutputStream.nullOutputStream()) : SinkOutput {
    override fun write(buffer: ByteArray, offset: Int, len: Int) = stream.write(buffer, offset, len)
    override fun close() = stop()

    override fun setVolume(volume: Float) = true
    override fun start(format: OutputAudioFormat) = true

    override fun release() = stream.close()
    override fun drain() = stream.flush()
    override fun flush() = stream.flush()
    override fun stop() = stream.close()
}