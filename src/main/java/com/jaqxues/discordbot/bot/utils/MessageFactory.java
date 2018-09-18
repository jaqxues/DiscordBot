package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Arrays;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 13:23.
 */

@SuppressWarnings("unused")
public class MessageFactory {

    public static void basicSuccessEmbed(MessageChannel channel, String title, @Nullable String message) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle(title)
                        .setDescription(message)
                        .build()
        ).queue();
    }

    public static void basicWarnEmbed(MessageChannel channel, String title, @Nullable String message) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.ORANGE)
                        .setTitle(title)
                        .setDescription(message)
                        .build()
        ).queue();
    }

    public static void basicErrorEmbed(MessageChannel channel, String title, @Nullable String message) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(title)
                        .setDescription(message)
                        .build()
        ).queue();
    }

    public static void errorEmbed(MessageChannel channel, String title, @Nullable String message, Throwable t) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(title)
                .setDescription(message);
        addThrowableField(builder, t);
        channel.sendMessage(
                builder.build()
        ).queue();
    }

    public static void basicNeutralEmbed(MessageChannel channel, String title, @Nullable String message) {
        channel.sendMessage(
                new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle(title)
                        .setDescription(message)
                        .build()
        ).queue();
    }

    private static void addThrowableField(EmbedBuilder builder, Throwable t) {
        builder.addField(
                t.getMessage(),
                Arrays.toString(t.getStackTrace()),
                false
        );
    }
}
