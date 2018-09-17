package com.jaqxues.discordbot.bot;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.CustomEventAdapter;
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

    public EventDispatcher() {
        setListeners();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (isCommand(event.getMessage().getContentRaw())) {
            String[] params = event.getMessage().getContentRaw().substring(commandPrefix.length()).trim().split(" ", 2);
        }
    }

    private boolean isCommand(String message) {
        return message.startsWith(commandPrefix);
    }

    private void setListeners() {
        listeners.clear();
        BaseCommand[] cmds = new BaseCommand[]{

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
