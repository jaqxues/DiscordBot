package com.jaqxues.discordbot.bot.commands;

import com.jaqxues.discordbot.bot.utils.BaseCommand;
import com.jaqxues.discordbot.bot.utils.MessageFactory;
import com.jaqxues.discordbot.bot.utils.Variables;
import com.jaqxues.discordbot.utils.Constants;
import com.jaqxues.discordbot.utils.FileUtils;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.Color;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 18.09.2018 - Time 01:18.
 */

public class CompileModPackCommand implements BaseCommand {

    private static String defaultParams;
    private static String example = "10.26.5.0 : Debug : Premium : 1.0.0.0 : Beta : Development : SignPack : Master : Adb Push";

    public CompileModPackCommand() {
        defaultParams = getDefaultParams();
    }

    public static String getDefaultParams() {
        if (defaultParams == null || defaultParams.isEmpty())
            defaultParams = example;
        return defaultParams;
    }

    public static void setDefaultParams(String str) {
        defaultParams = str;
    }

    private static String[] parseParams(String message) throws IllegalArgumentException {
        try {
            String[] params;
            String[] result = new String[12];
            params = message.split(" : ");

            // Snapchat Version
            result[0] = params[0];

            // Debug or Release
            if (params[1].startsWith("d"))
                result[1] = "1";
            else if (params[1].startsWith("r"))
                result[1] = "2";
            else
                throw new IllegalArgumentException();

            // Basic or Premium
            if (params[2].isEmpty() || params[2].startsWith("p"))
                result[2] = "2";
            else if (params[2].startsWith("b"))
                result[2] = "1";
            else throw new IllegalArgumentException();

            // Pack Version
            result[3] = params[3];

            // Beta or Release (prod)
            if (params[4].isEmpty() || params[4].startsWith("r") || params[4].startsWith("p"))
                result[4] = "1";
            else if (params[4].startsWith("b"))
                result[4] = "2";
            else
                throw new IllegalArgumentException();

            // Development
            if (params[5].startsWith("d") || params[5].startsWith("y"))
                result[5] = "y";
            else
                result[5] = "n";

            // Sign Pack
            if (params[6].startsWith("y") || params[6].startsWith("s"))
                result[6] = "2";
            else if (params[6].startsWith("n")) {
                result[6] = "1";
                if (params[8].startsWith("adb") || params[8].startsWith("y"))
                    result[7] = "y";
                else
                    result[7] = "n";
                return result;
            } else
                throw new IllegalArgumentException();

            // KeyStore Path
            result[7] = getKeyStoreInfo("Path");

            // Master or Debug Key?
            if (params[7].startsWith("m"))
                result[8] = "Master";
            else if (params[7].startsWith("d"))
                result[8] = "Debug";
            else throw new IllegalArgumentException();

            // KeyStore Password
            result[9] = getKeyStoreInfo("Password");

            // Key Password
            result[10] = getKeyPasswordFromAlias(result[8]);

            if (params[8].startsWith("a") || params[8].startsWith("y"))
                result[11] = "y";
            else
                result[11] = "n";
            return result;
        } catch (Exception e) {
            LogUtils.getMainLogger().error("Could not handle Input", e);
            throw new IllegalArgumentException();
        }
    }

    private static void trackInput(Process p, String[] input, MessageChannel channel) {
        new Thread(() -> {
            try {
                StringBuilder msgBuilder = new StringBuilder();
                Scanner scanner = new Scanner(p.getInputStream());
                OutputStreamWriter writer = new OutputStreamWriter(p.getOutputStream());
                int i = 0;
                String line;
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.contains("------InputRequest------")) {
                        Thread.sleep(100);
                        writer.append(input[i++]).append("\n").flush();
                    } else if (line.contains("EXITING MODULEPACK COMPILER")) {
                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setTitle("Successfully executed ModulePack Compiler")
                                .setDescription("Provides additional Output Details")
                                .setColor(Color.GREEN);
                        while (scanner.hasNextLine() && !(line = scanner.nextLine()).contains("============")) {
                            System.out.println(line);
                            if (line.isEmpty())
                                continue;
                            String[] parts = line.split(": ", 2);
                            try {
                                embedBuilder.addField(parts[0], parts[1], parts[0].length() < 20 && parts[1].length() < 20);
                            } catch (Exception e) {
                                embedBuilder.addBlankField(false);
                                embedBuilder.addField("Error Retrieving Field", line, false);
                                embedBuilder.addBlankField(false);
                            }
                        }
                        for (String str : msgBuilder.toString().split("\n")) {
                            if (str == null || str.isEmpty())
                                continue;
                            embedBuilder.addBlankField(false);
                            String[] parts = str.split(":", 2);
                            try {
                                embedBuilder.addField(parts[0], parts[1], false);
                            } catch (Exception e) {
                                embedBuilder.addField("Error", str, false);
                            }
                        }
                        if (msgBuilder.toString().contains("error"))
                            embedBuilder.setColor(Color.RED);
                        channel.sendMessage(embedBuilder.build()).queue();
                        return;
                    } else if (line.contains("Signing") && !line.toLowerCase().contains("completed") && !line.toLowerCase().contains("skipped")) {
                        String output = input[i++] + "\n" + input[i++] + "\n";
                        writer.append(output).flush();
                    } else if (line.contains("adb: error: ")) {
                        msgBuilder.append("ADB Error:").append(line.substring(line.indexOf("adb: error: "))).append("\n");
                    }
                }
            } catch (Exception e) {
                LogUtils.getMainLogger().error("Error while tracking input request", e);
                MessageFactory.errorEmbed(
                        channel,
                        "Unknown Exception",
                        "An unknown exception has been thrown while running the ModulePack Compiler",
                        e
                );
            }
        }).start();
    }

    private static String getKeyStoreInfo(String key) {
        JSONObject json = FileUtils.fileToJSON(Constants.KEYSTORE_JSON);
        if (json == null)
            throw new IllegalStateException("Could not read the KeyStore.json File");
        return json.getJSONObject("KeyStore").getString(key);
    }

    private static String getKeyPasswordFromAlias(String alias) {
        JSONObject json = FileUtils.fileToJSON(Constants.KEYSTORE_JSON);
        if (json == null)
            throw new IllegalStateException("Could not read the KeyStore.json File");
        for (Object o : json.getJSONArray("Keys")) {
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.getString("Alias").equals(alias))
                return jsonObject.getString("Password");
        }
        throw new IllegalStateException("Could not find Key Password or Alias");
    }

    @Override
    public String getName() {
        return "Module Pack Compiler Command";
    }

    @Override
    public String getAlias() {
        return "mp";
    }

    @Override
    public String getDescription() {
        return "A command used to compile SnapTools' Module Packs";
    }

    @Override
    public List<String> getUsageInstructions() {
        return Arrays.asList(
                "=== Get Example----Gives an example with valid parameters for this command",
                "=== Set Default *parameters*----Sets the default parameters",
                "=== Get Default----Gives the current default parameters for this command",
                "=== Default----Compiles a ModulePack with the current default parameters",
                "=== *parameters*----Compiles a ModulePack with the given parameters");
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
        str = str.toLowerCase();
        if (str.startsWith("get example")) {
            MessageFactory.basicSuccessEmbed(
                    event.getChannel(),
                    "ModulePack Compiler Command Example",
                    "\n\n```" + Variables.commandPrefix + getAlias() + " " + example + "```"
            );
            return;
        } else if (str.startsWith("set default")) {
            str = str.substring(11).trim();
            try {
                parseParams(str);
            } catch (IllegalArgumentException e) {
                System.out.print("Executed");
                MessageFactory.errorEmbed(
                        event.getChannel(),
                        "IllegalArgumentException",
                        "Could not parse input. Not saving defaults",
                        e
                );
                return;
            }
            defaultParams = str;
            MessageFactory.basicSuccessEmbed(
                    event.getChannel(),
                    "Success",
                    "Successfully saved the default params"
            );
            return;
        } else if (str.startsWith("get default")) {
            MessageFactory.basicSuccessEmbed(
                    event.getChannel(),
                    "ModulePack Compile Command Default Parameters",
                    "\n\n\n\n```" + Variables.commandPrefix + getAlias() + " " + getDefaultParams() + "```"
            );
            return;
        } else if (str.startsWith("d")) {
            str = defaultParams;
        }
        String[] input;
        try {
            input = parseParams(str);
        } catch (IllegalArgumentException e) {
            LogUtils.getMainLogger().error("Error while parsing input \"" + str + "\"", e);
            MessageFactory.errorEmbed(
                    event.getChannel(),
                    "IllegalArgumentException",
                    "The provided input could not be parsed as Parameters",
                    e
            );
            return;
        }

        event.getChannel().sendTyping().queue();

        try {
            Process compileProcess = Runtime.getRuntime().exec(Constants.MODPACK_COMPILER_BAT);
            trackInput(compileProcess, input, event.getChannel());
        } catch (Throwable t) {
            LogUtils.getMainLogger().error("Could not execute the PackCompiler", t);
            MessageFactory.errorEmbed(
                    event.getChannel(),
                    "Could not execute ModulePack Compiler",
                    "An unknown exception has been thrown while starting the ModulePack Compiler script"
                    , t
            );
        }
    }
}
