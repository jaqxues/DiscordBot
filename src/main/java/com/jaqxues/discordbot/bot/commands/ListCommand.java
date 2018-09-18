package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.EventDispatcher;
import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.bot.utils.Variables;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 13:58.
 */

public class ListCommand implements BaseCommand {

    private WeakReference<EventDispatcher> reference;

    public ListCommand(WeakReference<EventDispatcher> reference) {
        this.reference = reference;
    }

    @Override
    public String getName() {
        return "List Commands Command";
    }

    @Override
    public String getAlias() {
        return "commands";
    }

    @Override
    public String getDescription() {
        return "Gives a list of all currently available Commands";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Collections.singletonList("===----Gives a list of all currently available commands");
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
        EventDispatcher dispatcher;
        if (reference == null || (dispatcher = reference.get()) == null) {
            MessageFactory.basicErrorEmbed(
                    event.getChannel(),
                    "WeakReference null or referring to null",
                    "Unable to retrieve list of commands"
            );
            return;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Available Commands")
                .setDescription("To get help with each command, append \"help\" after the command's alias")
                .setColor(Color.GREEN)
                .addBlankField(false);
        for (BaseCommand cmd : dispatcher.getCommands()) {
            builder.addField(
                    cmd.getName() + "\t(*" + Variables.commandPrefix + cmd.getAlias() + "*)",
                    cmd.getDescription(),
                    false);
        }
    }
}
