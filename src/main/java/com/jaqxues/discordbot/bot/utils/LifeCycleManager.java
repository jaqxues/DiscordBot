package com.jaqxues.discordbot.bot.utils;

import com.jaqxues.discordbot.utils.Constants;
import com.jaqxues.discordbot.utils.FileUtils;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.JDA;

import org.json.JSONObject;

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
        IdsProvider.init();
        JSONObject object = FileUtils.fileToJSON(Constants.SESSION_MANAGER_JSON);
        if (object == null)
            object = new JSONObject();

        StatsManager.init(object.optJSONObject("Stats"));

        Variables.commandPrefix = object.optString("Prefix", ".");
        Variables.lockLevel = object.optInt("LockLevel", 4);
    }

    public static void refresh() {
        saveJSON();
        LogUtils.getMainLogger().debug("Refreshed BackupSessionManager File");
    }

    public static void onShutdown(JDA jda) {
        saveJSON();
        LogUtils.getMainLogger().info("Successfully saved the current Session");
        jda.shutdown();
    }

    private static void saveJSON() {
        StatsManager.savedJson();
        JSONObject jsonObject = new JSONObject()
                .put("Prefix", Variables.commandPrefix)
                .put("LockLevel", Variables.lockLevel)
                .put("Stats", StatsManager.getJSON())
                .put("LastSaved", System.currentTimeMillis());
        FileUtils.writeFile(Constants.SESSION_MANAGER_JSON, jsonObject.toString());
    }
}
