package dev.cbyrne.adieux.impl.discord.audio

import net.dv8tion.jda.api.audio.AudioSendHandler
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class AdieuxAudioSendHandler : AudioSendHandler {
    override fun canProvide() = false
    override fun provide20MsAudio(): ByteBuffer? = null
    override fun isOpus() = false
}