package com.jaqxues.discordbot.bot.utils;

import org.json.JSONObject;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 17:29.
 */

public class StatsManager {

    private static int messageEvents = 0;
    private static int totalMessageEvents = 0;
    private static int invokedCommands = 0;
    private static int totalInvokedCommands = 0;
    private static int otherEvents = 0;
    private static int totalOtherEvents = 0;
    private static long start = System.currentTimeMillis();
    private static long totalUpTime = 0;
    private static long lastSaved = System.currentTimeMillis();
    private static int totalReboots = 0;

    public static void messageEvent() {
        messageEvents++;
    }

    public static void invokedCommand() {
        invokedCommands++;
    }

    static void otherEvent() {
        otherEvents++;
    }

    public static int getMessageEvents() {
        return messageEvents;
    }

    public static int getInvokedCommands() {
        return invokedCommands;
    }

    public static int getOtherEvents() {
        return otherEvents;
    }

    public static long getStartTime() {
        return start;
    }

    public static long getUpTime() {
        return System.currentTimeMillis() - start;
    }

    static void savedJson() {
        lastSaved = System.currentTimeMillis();
    }

    public static int getTotalMessageEvents() {
        return totalMessageEvents + messageEvents;
    }

    public static int getTotalInvokedCommands() {
        return totalInvokedCommands + invokedCommands;
    }

    public static int getTotalOtherEvents() {
        return totalOtherEvents + otherEvents;
    }

    public static long getTotalUpTime() {
        return totalUpTime + (System.currentTimeMillis() - start);
    }

    public static long getLastSaved() {
        return lastSaved;
    }

    public static int getTotalReboots() {
        return totalReboots + 1;
    }

    static JSONObject getJSON() {
        return new JSONObject()
                .put("TotalReboots", getTotalReboots())
                .put("TotalMessageEvents", getTotalMessageEvents())
                .put("TotalInvokedCommands", getTotalInvokedCommands())
                .put("TotalOtherEvents", getTotalOtherEvents())
                .put("TotalUpTime", getTotalUpTime());
    }

    static void init(JSONObject object) {
        if (object == null)
            return;
        totalMessageEvents = object.optInt("TotalMessageEvents", 0);
        totalInvokedCommands = object.optInt("TotalInvokedCommands", 0);
        totalOtherEvents = object.optInt("TotalOtherEvents", 0);
        totalUpTime = object.optLong("TotalUpTime", 0);
        totalReboots = object.optInt("TotalReboots", 0);
    }
}
