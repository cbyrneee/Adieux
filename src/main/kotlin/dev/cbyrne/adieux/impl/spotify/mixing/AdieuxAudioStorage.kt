package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.calculateByteSize
import dev.cbyrne.adieux.util.getByteOrder
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat

/**
 * Takes audio data and slices it to Discord-friendly slices (20 ms of audio)
 * Audio format is not converted.
 * @author UserTeemu
 */
class AdieuxAudioStorage(private val format: AudioFormat = AudioSendHandler.INPUT_FORMAT) {
    /**
     * A list of output ByteBuffers, where each buffer contains audio data for 20 milliseconds.
     * There should never be more than one buffers that have space left. This buffer should always be the last buffer on the list.
     * All buffers may be full.
     * @see chunkByteSize
     * @see mutex
     */
    private val chunks = mutableListOf<ByteBuffer>()

    /**
     * The amount of bytes it takes to store a 20 ms long audio clip in the specified format
     * This is the size of one chunk in bytes.
     * @see chunks
     */
    private val chunkByteSize = format.calculateByteSize(20 / 1000F) // 20 ms

    /**
     * Used to prevent concurrent modifications to the chunk list
     * @see chunks
     */
    private val mutex = Mutex()

    suspend fun write(array: ByteArray) {
        var bytes = array
        while (bytes.isNotEmpty()) {
            val lastChunk = chunks.lastOrNull()
            val remaining = lastChunk?.remaining()?.coerceAtMost(bytes.size) ?: 0

            if (lastChunk != null && remaining > 0) {
                val bytesToAdd = bytes.copyOf(remaining)

                mutex.withLock {
                    lastChunk.put(bytesToAdd)
                }

                // removes added bytes from the array, so next iteration can continue where this left off
                bytes = ByteArray(bytes.size - remaining) { i ->
                    bytes[i + remaining]
                }
            } else {
                // all chunks are full, so a new chunk and a new buffer will be created
                mutex.withLock {
                    chunks.add(ByteBuffer.allocate(chunkByteSize).order(format.getByteOrder()))

                    println("Created new ByteBuffer. Currently there are ${chunks.size} ByteBuffers.")
                }
            }
        }
    }

    fun anyFullChunks(): Boolean =
        runBlocking {
            mutex.withLock {
                chunks.any { it.remaining() == 0 }
            }
        }

    fun consumeNextChunk(): ByteBuffer? =
        runBlocking {
            mutex.withLock {
                chunks.getOrNull(0)?.also { chunks.removeAt(0) }
            }
        }

    fun flush() =
        runBlocking {
            mutex.withLock {
                chunks.clear()
            }
        }
}
