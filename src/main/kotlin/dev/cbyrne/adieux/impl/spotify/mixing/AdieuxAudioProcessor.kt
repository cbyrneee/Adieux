package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.AudioDumper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import javax.sound.sampled.AudioFormat

/**
 * Processes and saves audio data obtained through a channel
 * @author UserTeemu
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
    val inputDumper: AudioDumper? = if (AudioDumper.dumpMode.inputEnabled) AudioDumper(inputAudioFormat) else null

    /**
     * Used for debugging audio data.
     * If you don't wish to dump the data, set this to null.
     */
    val outputDumper: AudioDumper? = if (AudioDumper.dumpMode.outputEnabled) AudioDumper(outputAudioFormat) else null

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
        inputDumper?.add(byteArray) // dump input, if dumper has been made
        val converted = converter.convert(byteArray) // convert
        storage.write(converted) // store output in storage
        outputDumper?.add(converted) // dump output, if dumper has been made
    }

    fun flush() {
        storage.flush()
    }

    fun close(reason: String) {
        queueHandlerScope.cancel("Processor was closed with reason: $reason")
    }
}
