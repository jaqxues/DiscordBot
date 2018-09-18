package com.jaqxues.discordbot.utils;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 21:26.
 */

public class LogUtils {

    private static final ColoredPrinter coloredPrinter = new ColoredPrinter.Builder(1, false).build();
    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void logMessage(Message message, boolean inRoleColor) {
        StringBuilder stringBuilder = new StringBuilder(String.format("Logging Message: [%s][%s][%s]: %s", message.getGuild().getName(), message.getChannel().getName(), message.getAuthor().getName(), message.getContentRaw()));
        for (MessageEmbed embed : message.getEmbeds()) {
            stringBuilder.append("\n      Embed, Title: ").append(embed.getTitle())
                    .append(", Description: ").append(embed.getDescription());
            int i = 1;
            for (MessageEmbed.Field field : embed.getFields()) {
                stringBuilder.append("\n            Field ").append(i++)
                        .append(", Name: ").append(field.getName())
                        .append(", Value: ").append(field.getValue());
            }
        }
        String str = stringBuilder.toString();
        if (inRoleColor)
            println(str, getRoleColorAnsi(message.getMember()));
        else
            System.out.println(str);
    }

    private static Ansi.FColor getRoleColorAnsi(Member member) {
        Color color = member.getColor();
        if (color == null)
            color = Color.WHITE;
        int lowest = Integer.MAX_VALUE;
        Colors colorLowest = Colors.White;
        for (Colors enumColor : Colors.values()) {
            int distance = (color.getRed() - enumColor.getColor().getRed()) ^ 2 +
                    (color.getGreen() - enumColor.getColor().getGreen()) ^ 2 +
                    (color.getBlue() - enumColor.getColor().getBlue()) ^ 2;
            if (distance < lowest) {
                lowest = distance;
                colorLowest = enumColor;
            }
        }
        return colorLowest.getAnsiColor();
    }

    public static void println(Object object, Ansi.FColor color) {
        coloredPrinter.println(object, Ansi.Attribute.NONE, color, Ansi.BColor.NONE);
        System.out.println(object);
    }

    public static void println(Object object) {
        System.out.println(object);
    }

    public static Logger getMainLogger() {
        return logger;
    }

    public static Logger newInstance(Class aClass) {
        return LoggerFactory.getLogger(aClass);
    }

    public static Logger newInstance(String str) {
        return LoggerFactory.getLogger(str);
    }

    private void logBotState(String message) {
        System.out.println(message);
    }

    public enum Colors {
        Black(Color.BLACK, Ansi.FColor.BLACK), White(Color.WHITE, Ansi.FColor.WHITE), Yellow(Color.YELLOW, Ansi.FColor.YELLOW), Magenta(Color.MAGENTA, Ansi.FColor.MAGENTA), Green(Color.GREEN, Ansi.FColor.GREEN), Blue(Color.BLUE, Ansi.FColor.BLUE), Red(Color.RED, Ansi.FColor.RED), Cyan(Color.CYAN, Ansi.FColor.CYAN);
        Color color;
        Ansi.FColor ansiColor;

        Colors(Color color, Ansi.FColor ansiColor) {
            this.color = color;
            this.ansiColor = ansiColor;
        }

        Color getColor() {
            return this.color;
        }

        public Ansi.FColor getAnsiColor() {
            return this.ansiColor;
        }
    }
}
