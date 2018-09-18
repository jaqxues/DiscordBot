package com.jaqxues.discordbot.utils;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 21:55.
 */

@SuppressWarnings("unused")
public class Assert {
    public static void notNull(String errorMessage, Object... args) throws IllegalArgumentException {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    public static String nonFatalNotNull(String errorMsg, Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                return errorMsg;
            }
        }
        return null;
    }
}
