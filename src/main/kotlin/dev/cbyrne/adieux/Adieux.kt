package dev.cbyrne.adieux

import dev.cbyrne.adieux.core.discord.AdeiuxDiscordEventReceiver
import dev.cbyrne.adieux.core.spotify.player.AdeiuxEventListener
import dev.cbyrne.adieux.impl.discord.AdeiuxDiscordBot
import dev.cbyrne.adieux.impl.spotify.player.credentials.AdeiuxCredentialsPlayer
import dev.cbyrne.adieux.impl.spotify.player.credentials.type.AdeiuxCredentialsType
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.VoiceChannel

class Adieux : AdeiuxEventListener, AdeiuxDiscordEventReceiver {
    val player = AdeiuxCredentialsPlayer(AdeiuxCredentialsType.Stored())
    val bot = AdeiuxDiscordBot(System.getenv("adeiux.token"))

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
        player.connect(channelJoined.name)
    }

    override fun onVoiceLeave(guild: Guild, user: Member, channelLeft: VoiceChannel) {
        player.disconnect()
    }
}