package dev.cbyrne.adieux

import dev.cbyrne.adieux.core.discord.AdieuxDiscordEventReceiver
import dev.cbyrne.adieux.core.spotify.player.AdieuxEventListener
import dev.cbyrne.adieux.impl.discord.AdieuxDiscordBot
import dev.cbyrne.adieux.impl.spotify.player.credentials.AdieuxCredentialsPlayer
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdieuxCredentialsType
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.VoiceChannel

class Adieux : AdieuxEventListener, AdieuxDiscordEventReceiver {
    val player = AdieuxCredentialsPlayer(AdieuxCredentialsType.Stored())
    val bot = AdieuxDiscordBot(System.getenv("adieux.token"))

    private val userIdToFollow = System.getenv("adieux.user")

    fun start() {
        bot.start()
        bot.receiver = this
    }

    override fun onVoiceJoin(
        guild: Guild,
        user: Member,
        channelJoined: VoiceChannel,
        channelLeft: VoiceChannel?
    ) {
        if (user.id != userIdToFollow) return
        player.connect(channelJoined.name)
    }

    override fun onVoiceLeave(guild: Guild, user: Member, channelLeft: VoiceChannel) {
        if (user.id != userIdToFollow) return
        player.disconnect()
    }
}