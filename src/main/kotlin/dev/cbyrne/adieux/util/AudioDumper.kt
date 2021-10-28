package dev.cbyrne.adieux.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.logging.log4j.LogManager
import java.io.ByteArrayInputStream
import java.io.File
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

/**
 * A debugging tool, that saves provided audio data to a file.
 * Fed audio is saved into an array list.
 * Audio doesn't need to be fed all at once, and the audio to be dumped can be delivered gradually over time.
 * Data cannot be removed or changed after it has been added to the dump without flushing the whole dumper.
 * @param format The audio format that the dump will use. Audio input must also be in this format
 * @author UserTeemu
 */
class AudioDumper(private val format: AudioFormat) {
    private val logger = LogManager.getLogger()

    /**
     * A list of bytes that are going to be dumped.
     * @see mutex
     */
    private var dumpBytes = arrayListOf<Byte>()

    /**
     * Used to prevent concurrent modifications to the dump contents
     * @see dumpBytes
     */
    private val mutex = Mutex()

    /**
     * Stores the provided bytes to the chunked ByteBuffers
     */
    suspend fun add(bytes: ByteArray) {
        if (bytes.isEmpty()) return

        logger.debug("Dumper received ${bytes.size} more bytes.")

        val result = mutex.withLock {
            dumpBytes.addAll(bytes.toTypedArray())
        }

        if (!result) logger.warn("${bytes.size} bytes were not added to the dump.")
    }

    /**
     * Saves the received audio data to provided file.
     * Dump file is decided by findAvailableFile.
     * @param fileType The file type of the dump
     * @param file Destination file of the dump
     * @param overwrite True if dump should always be in the given file (and previous contents overwritten), otherwise an unused filename will be used
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun dump(
        fileType: AudioFileFormat.Type = if (format.isBigEndian) AudioFileFormat.Type.AIFF else AudioFileFormat.Type.WAVE,
        file: File = File("dump.${fileType.extension}"),
        overwrite: Boolean = false
    ) {
        if (mutex.withLock { dumpBytes.isEmpty() }) {
            logger.warn("Dump was supposed to be made, but all chunks are empty. Skipping dump...")
            return
        }
        if (!AudioSystem.isFileTypeSupported(fileType)) {
            logger.warn("Provided file type is not supported by Java's AudioSystem. Skipping dump...")
            return
        }
        if (file.extension != fileType.extension) {
            logger.warn("Provided file doesn't use the file extension as the provided audio file type. Continuing with given values anyway...")
        }

        val dumpFile = if (overwrite) file else file.findAvailableFile()
        logger.info("Dumping dump to file $dumpFile...")

        val array = mutex.withLock {
            dumpBytes.toByteArray()
        }

        try {
            AudioInputStream(ByteArrayInputStream(array), format, array.size.toLong()).use { stream ->
                AudioSystem.write(stream, fileType, dumpFile).also {
                    logger.info("Dumped $it bytes in $format.")
                }
            }
        } catch (e: Exception) {
            logger.warn("Exception while writing audio dump", e)
        }
    }

    /**
     * Finds an available file.
     * Behavior:
     * If the file doesn't exist, it will be created and audio will be saved to it.
     * If file exists, the audio will be saved to the next file. Similar operation will follow for the next file too.
     * Example: File = "path/to/dump.wav"; If the file exists already, the file will be changed to "path/to/dump2.wav". If also the new file exists, it will attempt to save to "path/to/dump3.wav" and so on.
     */
    private fun File.findAvailableFile(): File {
        var dumpFile = this
        var iteration = 1
        while (dumpFile.exists()) {
            if (iteration++ > 10000) {
                throw Exception("Failed to find an available file! Looked through 10000 files.")
            }

            dumpFile = File(parent, "$nameWithoutExtension$iteration.$extension")
        }
        return dumpFile
    }

    suspend fun flush() {
        mutex.withLock {
            dumpBytes.clear()
        }
        logger.info("Dumper was flushed.")
    }

    companion object DumpConfiguration {
        val dumpMode = runCatching {
                DumpMode.valueOf(System.getenv("adieux.dump.mode").uppercase())
            }.getOrDefault(DumpMode.DISABLED)

        val isDumpEnabled = dumpMode.inputEnabled || dumpMode.outputEnabled

        enum class DumpMode(val inputEnabled: Boolean, val outputEnabled: Boolean) {
            DISABLED(false, false),
            INPUT(true, false),
            OUTPUT(false, true),
            BOTH(true, true)
        }
    }
}
