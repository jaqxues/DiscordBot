package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.StatsManager;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 18:17.
 */

public class StatsCommand implements BaseCommand {

    @Override
    public String getName() {
        return "Statistics Command";
    }

    @Override
    public String getAlias() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Gives some information about the Bot's uptime etc";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList("===----Sends an embed with some information",
                "=== full----Sends an embed with a bit more information"
        );
    }

    @Override
    public boolean requiresInput() {
        return false;
    }

    @Override
    public boolean ownerOnly() {
        return false;
    }

    @Override
    public void onInvoke(@NotNull String str, MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("Statistics")
                .addField("Current Session", "Details about the current session", false)
                .addField("Uptime", formatDuration(StatsManager.getUpTime()), true)
                .addField("StartTime", new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z").format(new Date(StatsManager.getStartTime())), true)
                .addField("Message Events", String.valueOf(StatsManager.getMessageEvents()), true)
                .addField("Executed Commands", String.valueOf(StatsManager.getInvokedCommands()), true)
                .addField("Other Events", String.valueOf(StatsManager.getOtherEvents()), false);
        if (str.startsWith("full")) {
            builder
                    .setTitle("Full Statistics")
                    .addBlankField(false)
                    .addField("Full Statistics", "Details about all sessions", false)
                    .addField("Total UpTime", formatDuration(StatsManager.getTotalUpTime()), true)
                    .addField("Total Reboots", String.valueOf(StatsManager.getTotalReboots()), true)
                    .addField("Total Message Events", String.valueOf(StatsManager.getTotalMessageEvents()), true)
                    .addField("Total Executed Commands", String.valueOf(StatsManager.getTotalInvokedCommands()), true)
                    .addField("Total Other Events", String.valueOf(StatsManager.getTotalOtherEvents()), true)
                    .addBlankField(false)
                    .addField("Last Saved SessionManager.json", formatDate(StatsManager.getLastSaved()), true);
        }
        try {
            event.getChannel().sendMessage(builder.build()).queue();
        } catch (InsufficientPermissionException e) {
            event.getChannel().sendMessage("Bot needs to be able to send Embeds for the Stats Command.").queue();
        }
    }

    private static String formatDuration(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        long milliseconds = millis % 1000;
        return String.format("%d Days %d Hours %d Minutes %d Seconds %d Milliseconds",
                days, hours, minutes, seconds, milliseconds);
    }

    private static String formatDate(long millis) {
        if (millis == 0) {
            return "";
        }
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        format.applyPattern("yyyy-MM-dd 'at' HH:mm ss SSS");
        return format.format(new Date(millis));
    }
}
