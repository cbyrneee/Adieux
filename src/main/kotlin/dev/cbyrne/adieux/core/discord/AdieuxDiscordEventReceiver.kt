package dev.cbyrne.adieux.core.discord

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.VoiceChannel

interface AdieuxDiscordEventReceiver {
    fun onVoiceJoin(guild: Guild, user: Member, channelJoined: VoiceChannel, channelLeft: VoiceChannel? = null)
    fun onVoiceLeave(guild: Guild, user: Member, channelLeft: VoiceChannel)
}