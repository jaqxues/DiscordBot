package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 22:13.
 * <p>
 *     This bot uses CustomEventAdapter instead of {@link net.dv8tion.jda.core.hooks.ListenerAdapter}
 *     for the simple reason that it contains a lot of unnecessary <code>if (event instanceof ...)
 *     </code> checks. This bot only needs the events handled in this class.
 * </p>
 */

public class CustomEventAdapter implements EventListener {

    public void onMessageReceived(MessageReceivedEvent event) {}

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent)
            onMessageReceived((MessageReceivedEvent) event);
        else
            StatsManager.otherEvent();
    }
}
