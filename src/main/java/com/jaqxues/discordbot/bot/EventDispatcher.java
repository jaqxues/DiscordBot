package com.jaqxues.discordbot.bot;

import com.jaqxues.discordbot.bot.commands.CompileModPackCommand;
import com.jaqxues.discordbot.bot.commands.StopCommand;
import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.CustomEventAdapter;
import com.jaqxues.discordbot.bot.utils.DiscordUtils;
import com.jaqxues.discordbot.bot.utils.IdsProvider;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.jaqxues.discordbot.bot.utils.Variables.commandPrefix;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 22:13.
 */

public class EventDispatcher extends CustomEventAdapter {
    private final HashMap<String, BaseCommand> listeners = new LinkedHashMap<>();
    private static int lockLevel = Integer.MAX_VALUE;

    public EventDispatcher() {
        setListeners();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isCommand(event.getMessage().getContentRaw())) {
            if (lockLevel >= 4 || IdsProvider.checkLock(lockLevel, event)) {
                String[] cmd = event.getMessage().getContentRaw()
                        .substring(commandPrefix.length())
                        .trim().split(" ", 2);
                if (cmd.length == 2) {
                    invokeListener(cmd, event, isHelpCommand(cmd[1]));
                } else
                    invokeListener(cmd, event, false);
            }
        }
    }

    private static boolean isCommand(String message) {
        return message.startsWith(commandPrefix);
    }

    private static boolean isHelpCommand(String message) {
        return message.toLowerCase().startsWith("help");
    }

    private void invokeListener(String[] cmd, MessageReceivedEvent event, boolean isHelpCommand) {
        if (listeners.containsKey(cmd[0])) {
            if (isHelpCommand)
                DiscordUtils.sendHelpCommand(commandPrefix, listeners.get(cmd[0]), event.getChannel());
            else {
                if (cmd.length == 2)
                    listeners.get(cmd[0]).onInvoke(cmd[1], event);
                else
                    listeners.get(cmd[0]).onInvoke(null, event);
            }
        }
    }

    private void setListeners() {
        listeners.clear();
        BaseCommand[] cmds = new BaseCommand[]{
                new CompileModPackCommand(),
                new StopCommand()
        };
        for (BaseCommand cmd : cmds) {
            if (listeners.containsKey(cmd.getAlias())) {
                LogUtils.getMainLogger().error("Listeners already contained BaseCommand: " + cmd.getName());
                continue;
            }
            listeners.put(cmd.getAlias(), cmd);
        }
    }
}
