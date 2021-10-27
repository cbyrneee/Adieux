package dev.cbyrne.adieux.util

import xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat

/**
 * Converts xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat to javax.sound.sampled.AudioFormat
 * @see xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
 * @see javax.sound.sampled.AudioFormat
 * @see AudioFormat.asLibrespotOutputAudioFormat
 */
fun OutputAudioFormat.asJavaxAudioFormat() =
    AudioFormat(AudioFormat.Encoding(encoding), sampleRate, sampleSizeInBits, channels, frameSize, frameRate, isBigEndian)

/**
 * Converts javax.sound.sampled.AudioFormat to xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
 * @see javax.sound.sampled.AudioFormat
 * @see xyz.gianlu.librespot.player.mixing.output.OutputAudioFormat
 * @see OutputAudioFormat.asJavaxAudioFormat
 */
fun AudioFormat.asLibrespotOutputAudioFormat(): OutputAudioFormat =
    if (encoding != AudioFormat.Encoding.PCM_SIGNED && encoding != AudioFormat.Encoding.PCM_UNSIGNED) {
        error("Unsupported encoding, $encoding!")
    } else {
        OutputAudioFormat(sampleRate, sampleSizeInBits, channels, encoding == AudioFormat.Encoding.PCM_SIGNED, isBigEndian)
    }

/**
 * A function, that calculates the number of bytes that an audio clip with the provided length will take, when stored in receiver audio format.
 * Based on https://stackoverflow.com/a/17702588
 * @receiver audio format
 * @param seconds Number of seconds
 * @return number of bytes that the recording will take
 * @see AudioFormat.calculateByteSize
 */
fun OutputAudioFormat.calculateByteSize(seconds: Float): Int =
    (sampleRate * seconds).toInt() * (sampleSizeInBits / 8) * channels


/**
 * A function, that calculates the number of bytes that an audio clip with the provided length will take, when stored in receiver audio format.
 * Based on https://stackoverflow.com/a/17702588
 * @receiver audio format
 * @param seconds Number of seconds
 * @return number of bytes that the recording will take
 * @see OutputAudioFormat.calculateByteSize
 */
fun AudioFormat.calculateByteSize(seconds: Float): Int =
    (sampleRate * seconds).toInt() * (sampleSizeInBits / 8) * channels

/**
 * @return receiver OutputAudioFormat, but it's endianness is replaced with a new value
 */
fun OutputAudioFormat.setEndianness(bigEndian: Boolean): OutputAudioFormat =
    if (encoding != "PCM_SIGNED" && encoding != "PCM_UNSIGNED") {
        error("Unsupported encoding, $encoding (not \"PCM_SIGNED\" or \"PCM_UNSIGNED\")! This shouldn't happen, so this code might be broken.")
    } else {
        OutputAudioFormat(sampleRate, sampleSizeInBits, channels, encoding == "PCM_SIGNED", bigEndian)
    }

/**
 * @return ByteOrder that represents the byte order of the receiver
 */
fun AudioFormat.getByteOrder(): ByteOrder =
    if (this.isBigEndian) {
        ByteOrder.BIG_ENDIAN
    } else {
        ByteOrder.LITTLE_ENDIAN
    }
