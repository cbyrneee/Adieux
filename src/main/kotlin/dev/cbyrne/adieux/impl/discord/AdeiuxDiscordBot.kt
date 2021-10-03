package dev.cbyrne.adieux.impl.discord

import dev.cbyrne.adieux.core.discord.AdeiuxDiscordEventReceiver
import dev.cbyrne.adieux.impl.discord.listener.AdeiuxDiscordEventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.apache.logging.log4j.LogManager

class AdeiuxDiscordBot(private val token: String) {
    private val logger = LogManager.getLogger()
    private val jda = buildJDA()
    private val listener = AdeiuxDiscordEventListener(logger)

    var receiver: AdeiuxDiscordEventReceiver? = null
        set(value) {
            field = value
            listener.receiver = field
        }

    fun start() {
        jda.addEventListener(listener)
        jda.awaitReady()
    }

    private fun buildJDA(): JDA {
        val builder = JDABuilder.createDefault(token)
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
        builder.setActivity(Activity.listening("music"))

        return builder.build()
    }
}