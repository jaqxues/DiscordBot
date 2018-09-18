package com.jaqxues.discordbot.bot.utils;

import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

import javax.xml.soap.Text;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 12:33.
 */

public class DiscordUtils {

    public static TextChannel getOwnerBotChannel(JDA jda) {
        return jda.getGuildById(434630982822395904L).getTextChannelById(461621973571731458L);
    }

    public static void sendHelpCommand(String commandPrefix, BaseCommand command, MessageChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(String.format("The %s *(%s)*", command.getName(), command.getAlias()))
                .setDescription(command.getDescription());
        List<String> usageInstructions = command.getUsageInstructions();
        for (String str : usageInstructions) {
            str = str.replaceAll("===", commandPrefix);
            boolean inline = false;
            if (str.endsWith("_inline_"))
                inline = true;
            if (str.startsWith("_blank_")) {
                builder.addBlankField(inline);
                continue;
            }
            String[] parts = str.split("----", 2);
            builder.addField(parts[0], parts[1], inline);
        }
        channel.sendMessage(builder.build()).queue();
    }

    /**
     * Method to check if this message has been send by the Bot Owner
     *
     * @param event The corresponding MessageReceivedEvent.
     * @return True if the event has been consumed.
     */
    public static boolean checkBotOwner(MessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == IdsProvider.OWNER_USER_ID)) {
            MessageFactory.basicErrorEmbed(
                    event.getChannel(),
                    "Bot Owner Action",
                    "Only the Bot Owner can perform this action"
            );
            return true;
        }
        return false;
    }

    public static void deleteMessage(Message message) {
        LogUtils.logMessage(message, false);
        message.delete().queue();
    }

    public static boolean tryDeleteMessage(Message message) {
        LogUtils.logMessage(message, false);
        try {
            message.delete().queue();
            return true;
        } catch (InsufficientPermissionException e) {
            LogUtils.getMainLogger().error("Unable to delete Message " + message, e);
        }
        return false;
    }
}
