package com.jaqxues.discordbot.bot;

import com.jaqxues.discordbot.bot.commands.CompileModPackCommand;
import com.jaqxues.discordbot.bot.commands.DeleteMessagesCommand;
import com.jaqxues.discordbot.bot.commands.ListCommand;
import com.jaqxues.discordbot.bot.commands.LockCommand;
import com.jaqxues.discordbot.bot.commands.QuickPollCommand;
import com.jaqxues.discordbot.bot.commands.StatsCommand;
import com.jaqxues.discordbot.bot.commands.StopCommand;
import com.jaqxues.discordbot.bot.commands.StringUtilsCommand;
import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.CustomEventAdapter;
import com.jaqxues.discordbot.bot.utils.DiscordUtils;
import com.jaqxues.discordbot.bot.utils.IdsProvider;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.bot.utils.StatsManager;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.jaqxues.discordbot.bot.utils.Variables.commandPrefix;
import static com.jaqxues.discordbot.bot.utils.Variables.lockLevel;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 22:13.
 */

public class EventDispatcher extends CustomEventAdapter {
    private final HashMap<String, BaseCommand> listeners = new LinkedHashMap<>();

    public EventDispatcher() {
        setListeners();
    }

    private static boolean isCommand(String message) {
        return message.startsWith(commandPrefix);
    }

    private static boolean isHelpCommand(String message) {
        return message.toLowerCase().startsWith("help");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        StatsManager.messageEvent();
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

    private void invokeListener(String[] cmd, MessageReceivedEvent event, boolean isHelpCommand) {
        StatsManager.invokedCommand();
        if (listeners.containsKey(cmd[0])) {
            if (isHelpCommand)
                DiscordUtils.sendHelpCommand(commandPrefix, listeners.get(cmd[0]), event.getChannel());
            else {
                BaseCommand command = listeners.get(cmd[0]);

                if (command.ownerOnly() && DiscordUtils.checkBotOwner(event))
                    return;

                if (cmd.length == 2 && cmd[1] != null && !cmd[1].isEmpty()) {
                    command.onInvoke(cmd[1], event);
                } else {
                    if (command.requiresInput()) {
                        MessageFactory.basicWarnEmbed(
                                event.getChannel(),
                                "No Parameters?",
                                "This command is usually called with parameters. If you need information about how this command works, append \"help\" to this command"
                        );
                        return;
                    }
                    command.onInvoke("", event);
                }
            }
        }
    }

    private void setListeners() {
        listeners.clear();
        WeakReference<EventDispatcher> reference = new WeakReference<>(this);
        BaseCommand[] cmds = new BaseCommand[]{
                new CompileModPackCommand(),
                new DeleteMessagesCommand(),
                new ListCommand(reference),
                new LockCommand(),
                new QuickPollCommand(),
                new StatsCommand(),
                new StopCommand(),
                new StringUtilsCommand()
        };
        for (BaseCommand cmd : cmds) {
            if (listeners.containsKey(cmd.getAlias())) {
                LogUtils.getMainLogger().error("Listeners already contained BaseCommand: " + cmd.getName());
                continue;
            }
            listeners.put(cmd.getAlias(), cmd);
        }
    }

    public Collection<BaseCommand> getCommands() {
        return listeners.values();
    }
}
