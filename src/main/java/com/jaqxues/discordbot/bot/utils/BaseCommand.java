package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 12:39.
 * <p><strong>Every</strong> Command that you want to add to {@link com.jaqxues.discordbot.bot.EventDispatcher#listeners}
 * needs to implement this interface.</p>
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
     * Called every time a user needs some explanation about how to use this command.
     * This list of Strings will be formatted following these rules.
     *
     * <table>
     * <tr>
     * <th>Rule</th>
     * <th>Effect</th>
     * </tr>
     * <tr>
     * <td>----</td>
     * <td>This is used to split the Field's Title and Text</td>
     * </tr>
     * <tr>
     * <td>_inline_</td>
     * <td>This is used <strong>at the end of the String</strong> to set the field inline</td>
     * </tr>
     * <tr>
     * <td>_blank_</td>
     * <td>This is used <strong>at the beginning of the String</strong> to add a new Blank Field to the Embed</td>
     * </tr>
     * <tr>
     * <td>===</td>
     * <td>Replaced by {@link Variables#commandPrefix} and {@link BaseCommand#getAlias()} </td>
     * </tr>
     * </table>
     *
     * @return A list of Strings that will be used to build a help message in form of a Discord Embed.
     */
    List<String> getUsageInstructions();

    /**
     * Handles empty Input Command Invocations.
     *
     * @return True if this command requires Input.
     */
    boolean requiresInput();

    /**
     * Automatically check if the Owner requested the Command invocation.
     *
     * @return True if this command can only be used by the BotOwner.
     */
    boolean ownerOnly();

    /**
     * This method handles every single Command Invocation.
     *
     * @param str   The Strings following the command. This string has the function of a parameter.
     * @param event The {@link MessageReceivedEvent} passed to this command.
     */
    void onInvoke(@NotNull String str, MessageReceivedEvent event);
}
