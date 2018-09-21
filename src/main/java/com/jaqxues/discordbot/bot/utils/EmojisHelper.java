package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.entities.Message;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 21.09.2018 - Time 18:40.
 */

public enum EmojisHelper {
    GRINNING("\uD83D\uDE00"), WHITE_CHECK_MARK("\u2705"), NEGATIVE_SQUARED_CROSS_MARK("\u274E");

    private final String unicode;

    EmojisHelper(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return unicode;
    }

    public void addReaction(Message msg) {
        msg.addReaction(getUnicode()).queue();
    }

    public static void addReactions(Message msg, EmojisHelper... emojis) {
        for (EmojisHelper emoji : emojis)
            emoji.addReaction(msg);
    }
}
