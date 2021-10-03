package dev.cbyrne.adieux.impl.discord.listener

import dev.cbyrne.adieux.core.discord.AdeiuxDiscordEventReceiver
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.apache.logging.log4j.Logger

class AdeiuxDiscordEventListener(private val logger: Logger) : ListenerAdapter() {
    var receiver: AdeiuxDiscordEventReceiver? = null

    override fun onReady(event: ReadyEvent) {
        logger.info("Ready! ${event.jda.selfUser}")
    }

    override fun onGuildVoiceJoin(event: GuildVoiceJoinEvent) {
        receiver?.onVoiceJoin(event.guild, event.member, event.channelJoined, event.channelLeft)
    }

    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {
        if (event.channelJoined == null) receiver?.onVoiceLeave(event.guild, event.member, event.channelLeft)
    }
}