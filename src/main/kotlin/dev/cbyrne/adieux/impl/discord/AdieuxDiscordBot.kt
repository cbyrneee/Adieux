package dev.cbyrne.adieux.impl.discord

import dev.cbyrne.adieux.core.discord.AdieuxDiscordEventReceiver
import dev.cbyrne.adieux.impl.discord.listener.AdieuxDiscordEventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.apache.logging.log4j.LogManager

class AdieuxDiscordBot(private val token: String) {
    private val logger = LogManager.getLogger()
    private val jda = buildJDA()
    private val listener = AdieuxDiscordEventListener(logger)

    var receiver: AdieuxDiscordEventReceiver? = null
        set(value) {
            field = value
            listener.receiver = field
        }

    var activity: Activity = Activity.watching("things load")
        set(value) {
            jda.presence.activity = value
            field = value
        }

    fun start() {
        jda.addEventListener(listener)
        jda.awaitReady()

        activity = Activity.listening("music")
    }

    private fun buildJDA(): JDA {
        val builder = JDABuilder.createDefault(token)
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)

        return builder.build()
    }
}