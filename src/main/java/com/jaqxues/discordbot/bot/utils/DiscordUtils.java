package com.jaqxues.discordbot.bot.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
            event.getChannel().sendMessage("Only the Bot Owner is able to perform this action.").queue();
            return true;
        }
        return false;
    }

    /**
     * Method to check if there are no parameters for this command. If so, send a small message to help the user
     *
     * @param params Parameters to check if they exist
     * @param event The corresponding MessageReceivedEvent
     * @return True if the event has been consumed.
     */
    public static boolean requiresParams(String params, MessageReceivedEvent event) {
        if (params == null || params.isEmpty()) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + "This method is usually called with parameters. If you need help, you can just append \"help\" to this command").queue();
            return true;
        }
        return false;
    }
}
