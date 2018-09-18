package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.DiscordUtils;
import com.jaqxues.discordbot.bot.utils.LifeCycleManager;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 01:08.
 */

public class StopCommand implements BaseCommand {
    @Override
    public String getName() {
        return "Stop Command";
    }

    @Override
    public String getAlias() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Shuts the bot down. Only Bot Owner can use this command";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList(
                "===----Shuts the bot down"
        );
    }

    @Override
    public void onInvoke(@Nullable String str, MessageReceivedEvent event) {
        if (!DiscordUtils.checkBotOwner(event))
            return;
        event.getChannel().sendMessage("Bot is shutting down, good night " + event.getAuthor().getName() + ". :)").queue();
        LifeCycleManager.onShutdown(event.getJDA());
    }
}
