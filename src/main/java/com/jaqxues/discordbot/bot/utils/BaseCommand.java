package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 12:39.
 * <p><strong>Every</strong> Command needs to implement this interface.</p>
 */

public interface BaseCommand {

    /**
     * @return The Name of this command.
     */
    String getName();

    /**
     * @return The alias that should be used to invoke this command.
     */
    String getAlias();

    /**
     * @return Some small description about this command.
     */
    String getDescription();

    /**
     * @return A list of Strings that will be used to build a help message.
     */
    List<String> getUsageInstructions();

    /**
     * This method handles every single Command Invocation.
     * @param str The Strings following the command. This string has the function of a parameter.
     * @param event The {@link MessageReceivedEvent} passed to this command.
     */
    void onInvoke(@Nullable String str, MessageReceivedEvent event);
}
