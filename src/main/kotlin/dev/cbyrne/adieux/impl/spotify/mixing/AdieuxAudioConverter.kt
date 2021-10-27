package dev.cbyrne.adieux.impl.spotify.mixing

import dev.cbyrne.adieux.util.calculateByteSize
import org.apache.logging.log4j.LogManager
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

/**
 * Handles audio format conversions.
 */
class AdieuxAudioConverter(private val inputFormat: AudioFormat, private val outputFormat: AudioFormat) {
    private val logger = LogManager.getLogger()

    init {
        require(AudioSystem.isConversionSupported(inputFormat, outputFormat)) {
            "Trying to convert '$inputFormat' to '$outputFormat' is not supported!"
        }
    }

    /**
     * Takes audio data in input format, and returns the audio in output format
     * @param inputBytes audio data in format of inputFormat
     * @return audio data in format of outputFormat
     */
    fun convert(inputBytes: ByteArray): ByteArray {
        if (inputBytes.isEmpty()) {
            logger.debug("Nothing to convert, returning nothing as well.")
            return inputBytes
        }

        val zeroByte: Byte = 0
        println("Queue is full of zeroes: ${inputBytes.all { it == zeroByte }}")

        val inputStream = AudioInputStream(ByteArrayInputStream(inputBytes), inputFormat, inputBytes.size.toLong())
        val converted = AudioSystem.getAudioInputStream(outputFormat, inputStream)

        return converted.use { audioStream ->
            audioStream.readBytes().also { bytes ->
                logger.debug("Received more bytes! ${inputBytes.size / inputFormat.calculateByteSize(0.001F)} ms or ${inputBytes.size} bytes in, and ${bytes.size / outputFormat.calculateByteSize(0.001F)} ms or ${bytes.size} bytes out.")
                logger.debug("Converted equals input: ${bytes.contentEquals(inputBytes)}")
            }
        }
    }
}