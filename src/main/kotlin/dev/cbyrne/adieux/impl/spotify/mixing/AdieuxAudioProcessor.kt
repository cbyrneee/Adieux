package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.AudioDumper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat

/**
 * Processes and saves audio data obtained through a channel
 */
class AdieuxAudioProcessor(private val channel: Channel<ByteArray>, inputAudioFormat: AudioFormat, outputAudioFormat: AudioFormat) {
    /**
     * Handles audio conversion
     */
    private val converter = AdieuxAudioConverter(inputAudioFormat, outputAudioFormat)

    /**
     * Handles storing the converted audio
     */
    internal val storage = AdieuxAudioStorage(outputAudioFormat)

    /**
     * Used for debugging audio data.
     * If you don't wish to dump the data, set this to null.
     */
    @Suppress("RedundantNullableReturnType")
    val dumper: AudioDumper? = AudioDumper(outputAudioFormat, AudioFileFormat.Type.AIFF)

    /**
     * Coroutine scope, in which the processing is done
     */
    private val queueHandlerScope = CoroutineScope(Dispatchers.Default)

    init {
        // start processing
        queueHandlerScope.launch {
            for (byteArray in channel) {
                if (isActive) {
                    process(byteArray)
                } else {
                    break
                }
            }
        }
    }

    private suspend fun process(byteArray: ByteArray) {
        val converted = converter.convert(byteArray)

        dumper?.add(converted)

        storage.write(converted)
    }

    fun flush() {
        storage.flush()
    }

    fun close(reason: String) {
        queueHandlerScope.cancel("Processor was closed with reason: $reason")
    }
}