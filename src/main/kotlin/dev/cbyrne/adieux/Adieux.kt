package dev.cbyrne.adieux

import dev.cbyrne.adieux.core.discord.AdieuxDiscordEventReceiver
import dev.cbyrne.adieux.core.spotify.player.AdieuxEventListener
import dev.cbyrne.adieux.impl.discord.AdieuxDiscordBot
import dev.cbyrne.adieux.impl.discord.audio.AdieuxAudioSendHandler
import dev.cbyrne.adieux.impl.spotify.player.credentials.AdieuxCredentialsPlayer
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdieuxCredentialsType
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.VoiceChannel
import xyz.gianlu.librespot.audio.MetadataWrapper
import xyz.gianlu.librespot.player.Player
import java.io.File

class Adieux : AdieuxEventListener, AdieuxDiscordEventReceiver {
    val player = AdieuxCredentialsPlayer(AdieuxCredentialsType.Stored())
    val bot = AdieuxDiscordBot(System.getenv("adieux.token"))

    private val userIdToFollow = System.getenv("adieux.user")

    fun start() {
        bot.start()

        bot.receiver = this
        player.listener = this
    }

    override fun onVoiceJoin(
        guild: Guild,
        user: Member,
        channelJoined: VoiceChannel,
        channelLeft: VoiceChannel?
    ) {
        if (user.id != userIdToFollow) return
        player.connect(channelJoined.name)

        val manager = guild.audioManager
        manager.sendingHandler = AdieuxAudioSendHandler(player.mixingManager.storageGetter)
        manager.openAudioConnection(channelJoined)
    }

    override fun onVoiceLeave(guild: Guild, user: Member, channelLeft: VoiceChannel) {
        if (user.id != userIdToFollow) return

        // debug dump
        runBlocking {
            try {
                player.mixingManager.audioProcessor.dumper?.apply {
                    dump(File("dump.wav"), false)
                    flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        player.disconnect()

        val manager = guild.audioManager
        manager.sendingHandler = null
        manager.closeAudioConnection()
    }

    override fun onMetadataAvailable(player: Player, metadata: MetadataWrapper) {
        bot.activity = Activity.listening("${metadata.name} by ${metadata.artist}")
    }
}