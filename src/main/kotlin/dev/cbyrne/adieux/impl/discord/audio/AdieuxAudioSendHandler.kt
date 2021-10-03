package dev.cbyrne.adieux.impl.discord.audio

import net.dv8tion.jda.api.audio.AudioSendHandler
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class AdieuxAudioSendHandler : AudioSendHandler {
    companion object {
        var outputStream = ByteArrayOutputStream(4096)
    }

    override fun canProvide() = false

    override fun provide20MsAudio(): ByteBuffer? {
        return ByteBuffer.wrap(outputStream.toByteArray())
    }

    override fun isOpus() = false
}