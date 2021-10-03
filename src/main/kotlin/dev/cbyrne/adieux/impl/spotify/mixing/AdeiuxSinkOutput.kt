package dev.cbyrne.adieux.impl.spotify.mixing

import org.slf4j.LoggerFactory
import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput

class AdeiuxSinkOutput : SinkOutput {
    private val logger = LoggerFactory.getLogger(AdeiuxSinkOutput::class.java)

    override fun write(buffer: ByteArray, offset: Int, len: Int) {
        
    }

    override fun close() {
        logger.info("Closing")
    }

    override fun start(format: OutputAudioFormat) = true
    override fun setVolume(volume: Float) = true
}