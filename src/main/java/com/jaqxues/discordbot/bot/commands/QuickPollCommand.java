package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 15:12.
 */

public class QuickPollCommand implements BaseCommand {
    @Override
    public String getName() {
        return "QuickPoll Command";
    }

    @Override
    public String getAlias() {
        return "qp";
    }

    @Override
    public String getDescription() {
        return "Make good looking polls as fast as you can type";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                "=== \"*<question>*\" \"*<option1>*\" \"*<option2>*\" ... ----Sends a generated Discord Embed"
        );
    }

    @Override
    public boolean requiresInput() {
        return true;
    }

    @Override
    public boolean ownerOnly() {
        return false;
    }

    @Override
    public void onInvoke(@NotNull String str, MessageReceivedEvent event) {
        if (str.startsWith("\"")) {
            String[] parts = str.split("\"");
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(parts[1]);
            for (int i = 3; i < parts.length; i += 2) {
                builder.addField(String.valueOf((i - 2) / 2), parts[i], false);
            }
            event.getChannel().sendMessage(builder.build()).queue();
            return;
        } else {
            // TODO Discord Reactions
        }
    }
}
