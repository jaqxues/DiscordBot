package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.bot.utils.Variables;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 16:39.
 */

public class LockCommand implements BaseCommand {
    @Override
    public String getName() {
        return "Lock Command";
    }

    @Override
    public String getAlias() {
        return "lock";
    }

    @Override
    public String getDescription() {
        return "Locks the bot down in different grades";
    }

    @Override
    public List<String> getUsageInstructions() {
        return null;
    }

    @Override
    public boolean requiresInput() {
        return false;
    }

    @Override
    public boolean ownerOnly() {
        return true;
    }

    @Override
    public void onInvoke(@NotNull String str, MessageReceivedEvent event) {
        if (str.isEmpty()) {
            MessageFactory.basicSuccessEmbed(
                    event.getChannel(),
                    "Current LockLevel",
                    Variables.lockLevel + ", " + getLockLevelName(Variables.lockLevel)
            );
        } else if (str.startsWith("set")) {
            str = str.substring(3).trim();
            Integer integer;
            try {
                integer = Integer.parseInt(str);
                if (integer < 0 || integer > 4) {
                    MessageFactory.basicErrorEmbed(
                            event.getChannel(),
                            "Invalid value",
                            "A value between 0 and 4 is required"
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                MessageFactory.errorEmbed(
                        event.getChannel(),
                        "NumberFormatException",
                        "Could not parse int " + str + ". Please provide a valid Number",
                        e
                );
                return;
            }
            Variables.lockLevel = integer;
            MessageFactory.basicSuccessEmbed(
                    event.getChannel(),
                    "Success",
                    "Successfully set the LockLevel to " + integer + " (" + getLockLevelName(integer) + ")"
            );

        }
    }

    private static String getLockLevelName(int lockLevel) {
        switch (lockLevel) {
            case 3:
                return "Allowed Guilds";
            case 2:
                return "Allowed Users";
            case 1:
                return "Owner Guild";
            case 0:
                return "Bot Owner";
            default:
                return "Everyone";
        }
    }
}
