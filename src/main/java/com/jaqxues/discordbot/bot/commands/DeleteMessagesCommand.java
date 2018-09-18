package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.DiscordUtils;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 11:02.
 */

public class DeleteMessagesCommand implements BaseCommand {

    private static Message getMessageByIds(JDA jda, long[] ids) {
        Guild guild = jda.getGuildById(ids[2]);
        if (guild == null)
            return null;
        MessageChannel channel = guild.getTextChannelById(ids[1]);
        if (channel == null)
            return null;
        try {
            return channel.getMessageById(ids[0]).complete();
        } catch (InsufficientPermissionException e) {
            LogUtils.getMainLogger().error("Could not retrieve message", e);
            return null;
        }
    }

    @Override
    public String getName() {
        return "Delete Messages Command";
    }

    @Override
    public String getAlias() {
        return "del";
    }

    @Override
    public String getDescription() {
        return "Deletes messages, clears chat...";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(
                "=== id *<message_id>*----Self Explaining",
                "=== id *<message_id> <channel_id>*----Self Explaining",
                "=== id *<message_id> <channel_id> <guild_id>*----Self Explaining");
    }

    @Override
    public boolean requiresInput() {
        return true;
    }

    @Override
    public boolean ownerOnly() {
        return true;
    }

    @Override
    public void onInvoke(@NotNull String str, MessageReceivedEvent event) {
        boolean confirm = !str.endsWith(".");
        if (confirm)
            str = str.substring(0, str.length() - 1);

        if (str.startsWith("id ")) {
            str = str.substring(3);
            String[] parts = str.trim().split(" ", 4);
            long[] ids = new long[]{
                    0L,
                    event.getChannel().getIdLong(),
                    event.getGuild().getIdLong()
            };
            if (parts.length == 0) {
                LogUtils.getMainLogger().error("More input required");
                MessageFactory.basicErrorEmbed(event.getChannel(), "Insufficient Input", "This command requires more input");
                return;
            }
            for (int i = 0; i < parts.length && i < 3; i++) {
                try {
                    ids[i] = Long.valueOf(parts[i]);
                } catch (NumberFormatException e) {
                    LogUtils.getMainLogger().error("Could not parse Number " + parts[i], e);
                    MessageFactory.errorEmbed(
                            event.getChannel(),
                            "NumberFormatException",
                            "Unable to parse Number: " + parts[i],
                            e
                    );
                    return;
                }
            }

            Message message = getMessageByIds(event.getJDA(), ids);
            if (message == null)
                MessageFactory.basicErrorEmbed(
                        event.getChannel(),
                        "Null Message",
                        "Could not retrieve Message by Ids"
                );

            if (!DiscordUtils.tryDeleteMessage(message))
                MessageFactory.basicErrorEmbed(
                        event.getChannel(),
                        "Unable to delete",
                        "The specified Message could not be deleted"
                );
            else if (confirm)
                MessageFactory.basicSuccessEmbed(
                        event.getChannel(),
                        "Deleted the specified Message",
                        null
                );

        } else {
            try {
                int i = Integer.parseInt(str);
                if (i <= 100) {
                    DiscordUtils.deleteMessage(event.getMessage());
                    for (Message message : event.getChannel().getHistory().retrieveFuture(i).complete())
                        DiscordUtils.deleteMessage(message);
                    if (confirm)
                        MessageFactory.basicSuccessEmbed(
                                event.getChannel(),
                                "Deleting " + i + " Messages",
                                "This might take some time (Discord Rate Limit)"
                        );
                } else
                    MessageFactory.basicErrorEmbed(
                            event.getChannel(),
                            "Too many messages!",
                            "Due to Discord Restriction, only 100 or less messages can be deleted at once"
                    );
            } catch (NumberFormatException e) {
                LogUtils.getMainLogger().error("IllegalArgument. Unable to parse Integer " + str, e);
                MessageFactory.basicErrorEmbed(
                        event.getChannel(),
                        "Could not parse Integer " + str,
                        "You need to provide a valid number as Parameter"
                );
            }
        }
    }
}
