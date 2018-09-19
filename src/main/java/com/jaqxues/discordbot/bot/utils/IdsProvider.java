package com.jaqxues.discordbot.bot.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jaqxues.discordbot.utils.Constants;
import com.jaqxues.discordbot.utils.FileUtils;
import com.jaqxues.discordbot.utils.JsonSingletons;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 22:08.
 */

public class IdsProvider {

    static final long OWNER_USER_ID = 368650055210500101L;
    private static final long OWNER_GUILD_ID = 434630982822395904L;

    private static Map<Integer, List<Long>> lockIdsMap;

    static void init() {
        JsonElement json = FileUtils.fileToJson(Constants.IDS_PROVIDER_JSON);
        if (json == null || !json.isJsonObject()) {

            lockIdsMapDefault();

            JsonElement element = JsonSingletons.getGson().toJsonTree(lockIdsMap);
            JsonObject object = new JsonObject();
            object.add("lock_ids_map", element);
            FileUtils.jsonToFile(Constants.IDS_PROVIDER_JSON, object);
            return;
        }
        if (!json.getAsJsonObject().has("lock_ids_map")) {
            JsonObject object = json.getAsJsonObject();
            LogUtils.getMainLogger().error("No \"lock_ids_map\" member found.");

            lockIdsMapDefault();

            object.add("lock_ids_map", JsonSingletons.getGson().toJsonTree(lockIdsMap));
            FileUtils.jsonToFile(Constants.IDS_PROVIDER_JSON, object);
            return;
        }
        lockIdsMap = JsonSingletons.getGson().fromJson(
                json.getAsJsonObject().get("lock_ids_map"),
                new TypeToken<Map<Integer, List<Long>>>() {
                }.getType()
        );
        LogUtils.getMainLogger().debug("Successfully loaded LockIdsMap");
    }

    private static void lockIdsMapDefault() {
        LogUtils.getMainLogger().info("Unable to load lockIdsMap, loading default settings");
        lockIdsMap = new HashMap<>();
        lockIdsMap.put(0, Collections.singletonList(OWNER_USER_ID));
        lockIdsMap.put(1, Collections.singletonList(OWNER_GUILD_ID));
        lockIdsMap.put(2, lockIdsMap.get(0));
        lockIdsMap.put(3, lockIdsMap.get(1));
        LogUtils.getMainLogger().info("Loaded default settings.");
    }

    public static boolean checkLock(Integer lockLevel, MessageReceivedEvent event) {
        long id =
                (lockLevel % 2 == 1 ?
                        event.getGuild().getIdLong() :
                        event.getAuthor().getIdLong()
                );
        return lockIdsMap.get(lockLevel).contains(id);
    }
}
