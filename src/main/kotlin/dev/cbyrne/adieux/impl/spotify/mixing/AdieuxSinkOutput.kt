package dev.cbyrne.adieux.impl.spotify.mixing

import org.slf4j.LoggerFactory
import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput
import java.io.OutputStream

class AdieuxSinkOutput : SinkOutput {
    private val logger = LoggerFactory.getLogger(AdieuxSinkOutput::class.java)
    private lateinit var outputStream: OutputStream

    override fun start(format: OutputAudioFormat): Boolean {
        outputStream = OutputStream.nullOutputStream()
        return true
    }

    override fun setVolume(volume: Float) = true

    override fun close() = outputStream.close()
    override fun release() = outputStream.close()
    override fun drain() = outputStream.flush()
    override fun flush() = outputStream.flush()
    override fun stop() = outputStream.close()

    override fun write(buffer: ByteArray, offset: Int, len: Int) = outputStream.write(buffer, offset, len)
}