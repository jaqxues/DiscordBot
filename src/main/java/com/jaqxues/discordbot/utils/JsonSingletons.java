package com.jaqxues.discordbot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 21:19.
 */

@SuppressWarnings("unsued")
public class JsonSingletons {
    private static JsonParser parser = new JsonParser();
    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    public static JsonParser getParser() {
        return parser;
    }
}
