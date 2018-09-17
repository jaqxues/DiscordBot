package com.jaqxues.discordbot.bot.utils;

import com.jaqxues.discordbot.utils.Constants;

import net.dv8tion.jda.core.JDA;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 22:27.
 */

public class LifeCycleManager {

    /**
     * A method that handles every single action that should be made just after the bot started.
     *
     * @param jda The {@link JDA} instance created in {@link com.jaqxues.discordbot.Main#main(String[])}
     */
    public static void onStartUp(JDA jda) {
        DiscordUtils.getOwnerBotChannel(jda).sendMessage("Started Bot. (Version: " + Constants.BOT_VERSION + ")").queue();

    }

    public static void refresh() {}

    public static void onShutdown(JDA jda) {

    }
}
