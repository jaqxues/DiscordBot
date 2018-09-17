package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 12:33.
 */

public class DiscordUtils {

    public static TextChannel getOwnerBotChannel(JDA jda) {
        return jda.getGuildById(434630982822395904L).getTextChannelById(461621973571731458L);
    }
}
