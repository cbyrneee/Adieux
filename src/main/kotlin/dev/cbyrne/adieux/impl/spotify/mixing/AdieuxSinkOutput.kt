package dev.cbyrne.adieux.impl.spotify.mixing

import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import xyz.gianlu.librespot.player.mixing.output.SinkOutput
import java.io.OutputStream

class AdieuxSinkOutput : SinkOutput {
    companion object {
        val stream = AudioOutputStream()
    }

    override fun write(buffer: ByteArray, offset: Int, len: Int) = stream.write(buffer, offset, len)
    override fun close() = stop()

    override fun setVolume(volume: Float) = true
    override fun start(format: OutputAudioFormat) = true

    override fun release() = stream.close()
    override fun drain() = stream.flush()
    override fun flush() = stream.flush()
    override fun stop() = stream.close()

    /**
     * An [OutputStream] class which converts the received bytes from little endian to big endian.
     * We do this because Discord requires big endian, not little endian.
     */
    class AudioOutputStream : OutputStream() {
        val bytes = mutableListOf<Int>()

        override fun write(b: Int) {
            error("Unsupported")
        }

        override fun write(b: ByteArray) = write(b, 0, b.size)

        override fun write(array: ByteArray, off: Int, len: Int) {
            var i = 0
            while (i < len && i + 4 <= len) {
                val a = array[i].toInt()
                val b = array[i + 1].toInt()
                val c = array[i + 2].toInt()
                val d = array[i + 3].toInt()

                val int = (a and 0xFF) or (b and 0xFF shl 8) or (c and 0xFF shl 16) or (d and 0xFF shl 24)

                println("Bytes: [$a, $b, $c, $d]")
                println("Value: $int")

                bytes.add(int)

                i += 4
            }
        }

        override fun close() {
        }

        override fun flush() {
        }
    }
}