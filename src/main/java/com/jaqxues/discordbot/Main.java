package com.jaqxues.discordbot;

import com.jaqxues.discordbot.bot.EventDispatcher;
import com.jaqxues.discordbot.bot.utils.LifeCycleManager;
import com.jaqxues.discordbot.utils.Constants;
import com.jaqxues.discordbot.utils.FileUtils;
import com.jaqxues.discordbot.utils.LogUtils;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import org.jetbrains.annotations.Nullable;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import ch.qos.logback.classic.util.ContextInitializer;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 16.09.2018 - Time 20:48.
 */

public class Main {

    public static void main(String[] args) {
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, Constants.LOGBACK_CONFIG_PATH);
        // Get Configuration (Bot Token and Command Prefix)
        String token = getToken(args);

        LogUtils.getMainLogger().info("Building JDA instance");
        JDA jda;
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .build()
                    .awaitReady();
            LogUtils.getMainLogger().debug("Successfully created JDA instance");
        } catch (LoginException e) {
            LogUtils.getMainLogger().error("Could not login", e);
            return;
        } catch (InterruptedException e) {
            LogUtils.getMainLogger().error("Thread was interrupted", e);
            return;
        }

        LifeCycleManager.onStartUp(jda);

        LogUtils.getMainLogger().debug("Adding EventListener to JDA instance");
        jda.addEventListener(new EventDispatcher());
        LogUtils.getMainLogger().debug("Added EventListener to JDA instance");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(LifeCycleManager::refresh, 1, 30, TimeUnit.MINUTES);
    }

    /**
     * Takes care of getting the token for the bot. If the token could not be read from
     * {@link Constants#TOKEN_TXT_PATH}, it asks for user input.
     *
     * @param args The arguments with which the Bot was started.
     * @return The Bot Token that will be used to build the JDA instance.
     */
    private static String getToken(@Nullable String[] args) {
        boolean fromBat = args != null && args.length != 0;
        String token = FileUtils.readFile(
                (fromBat) ? args[1] : Constants.TOKEN_TXT_PATH
        );
        if (token == null || token.isEmpty()) {
            LogUtils.getMainLogger().error("Could not read token. Requesting User Input");
            if (fromBat) {
                LogUtils.getMainLogger().error("Started from bat. No User Input Possible");
                throw new IllegalStateException();
            }
            System.out.println("Bot Token:");
            token = new Scanner(System.in).nextLine();
        }
        return token;
    }
}
