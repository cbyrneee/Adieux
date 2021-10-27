package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.asJavaxAudioFormat
import kotlinx.coroutines.runBlocking
import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput

class AdieuxSinkOutput(private val manager: AdieuxMixingManager) : SinkOutput {
    override fun write(buffer: ByteArray, offset: Int, len: Int) = runBlocking {
        manager.audioChannel.send(buffer.copyOfRange(offset, offset + len))
    }

    override fun close() {
        manager.audioProcessor.close("Sink output closed")
    }

    override fun flush() {
        manager.audioProcessor.flush()
    }

    override fun start(format: OutputAudioFormat): Boolean {
        manager.replaceAudioProcessor(format.asJavaxAudioFormat())
        return true
    }

    override fun setVolume(volume: Float) = true
    override fun release() = close()
    override fun drain() = flush()
    override fun stop() = close()
}
