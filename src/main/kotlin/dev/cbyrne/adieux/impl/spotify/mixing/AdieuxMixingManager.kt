package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.asJavaxAudioFormat
import kotlinx.coroutines.channels.Channel
import net.dv8tion.jda.api.audio.AudioSendHandler
import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import javax.sound.sampled.AudioFormat

/**
 * Decides what format, channel and processor are used.
 * @author UserTeemu
 */
class AdieuxMixingManager {
    val audioChannel = Channel<ByteArray>(Channel.UNLIMITED)
    var audioProcessor = createAudioProcessor(OutputAudioFormat.DEFAULT_FORMAT.asJavaxAudioFormat())

    private fun createAudioProcessor(inputAudioFormat: AudioFormat): AdieuxAudioProcessor =
        AdieuxAudioProcessor(audioChannel, inputAudioFormat, AudioSendHandler.INPUT_FORMAT)

    fun replaceAudioProcessor(inputAudioFormat: AudioFormat) {
        audioProcessor.close("Audio processor is being replaced.")
        audioProcessor = createAudioProcessor(inputAudioFormat)
    }

    val storageGetter: () -> AdieuxAudioStorage? = {
        // We catch a possible throwable for safety. A throwable might be thrown for example when the audio processor is being replaced.
        runCatching { audioProcessor.storage }.getOrNull()
    }
}
