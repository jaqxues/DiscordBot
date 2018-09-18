package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.bot.utils.Variables;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 15:42.
 */

public class StringUtilsCommand implements BaseCommand {
    @Override
    public String getName() {
        return "String Utils Command";
    }

    @Override
    public String getAlias() {
        return "string";
    }

    @Override
    public String getDescription() {
        return "A collection of (probably useless) functions I needed once";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(
                "=== from Unicode *<unicode_text>*----Generates a \"normal\" String from <unicode_text>. Unicode needs to be in this format: \"\\u0011\"",
                "=== to Unicode *<normal_text>*----Generates the Unicode values from <normal_text>",
                "=== from Bytes *<byte_array>*----Generates a String from <byte_array>",
                "=== to Bytes *<normal_text>*----Generates a ByteArray from <normal_text>"
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
        str = str.toLowerCase();
        if (str.startsWith("from unicode")) {
            str = str.substring(12).trim();
            StringBuilder builder = new StringBuilder("Converted content: \"");
            StringBuilder errorBuilder = new StringBuilder("Errors:");
            for (String code : str.substring(2).split("\\\\u")) {
                if (code.startsWith(" "))
                    builder.append(" ");
                try {
                    String trimmed = code.trim();
                    builder.append((char) Integer.parseInt(trimmed, 16));
                } catch (NumberFormatException | ClassCastException e) {
                    LogUtils.getMainLogger().error("Could not recognise character \"" + code + "\"", e);
                    errorBuilder.append("\n\t *(Not recognised character: \\u").append(code).append(")* ");
                }
                if (code.endsWith(" ")) {
                    builder.append(" ");
                }
                String msg = builder.toString() +
                        (errorBuilder.toString().equals("Errors:") ? "" : "\n\n" + errorBuilder.toString());
                event.getChannel().sendMessage(msg).queue();
            }
        } else if (str.startsWith("to unicode")) {
            str = str.substring(10).trim();
            StringBuilder builder = new StringBuilder();
            for (char character : (str = str.trim()).toCharArray()) {
                builder.append("\\u").append(Integer.toHexString((int) character).toUpperCase());
            }
            event.getChannel().sendMessage(str).append(": ").append(builder.toString()).queue();
        } else if (str.startsWith("from bytes")) {
            str = str.substring(10).trim();
            StringBuilder builder = new StringBuilder("Converted Content: \"");
            StringBuilder errorBuilder = new StringBuilder("Error:");
            List<Byte> bytes = new ArrayList<>();
            for (String inputByte : str.split(",")) {
                inputByte = inputByte.trim();
                try {
                    bytes.add(Byte.parseByte(inputByte));
                } catch (NumberFormatException e) {
                    errorBuilder.append("\n\t").append("Could not parse byte ").append(inputByte);
                }
            }
            byte[] array = new byte[bytes.size()];
            for (int i = 0; i < array.length; i++)
                array[i] = bytes.get(i);
            builder.append(new String(array));
            String msg = builder.toString() +
                    (errorBuilder.toString().equals("Error:") ? "" : "\n\n" + errorBuilder.toString());
            event.getChannel().sendMessage(msg).queue();
        } else if (str.startsWith("to bytes")) {
            str = str.substring(8).trim();
            event.getChannel().sendMessage("Converted Content: " + Arrays.toString(str.getBytes())).queue();
        } else {
            MessageFactory.basicErrorEmbed(
                    event.getChannel(),
                    "Unknown Request",
                    "Could not process your input, type `" + Variables.commandPrefix + getAlias() + " help` to get a small List of what this command does"
            );
        }
    }
}
