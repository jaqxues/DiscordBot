package com.jaqxues.discordbot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This file was created by Jacques (jaqxues) in the Project DiscordBot.<br>
 * Date: 17.09.2018 - Time 12:57.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtils {
    public static JsonElement fileToJson(String path) {
        return fileToJson(new File(path));
    }

    @Nullable
    public static JsonElement fileToJson(File file) {
        try (FileReader reader = new FileReader(file)) {
            return JsonSingletons.getParser().parse(reader);
        } catch (FileNotFoundException e) {
            LogUtils.getMainLogger().error("Could not find file", e);
        } catch (IOException e) {
            LogUtils.getMainLogger().error("Could not read file " + file, e);
        } catch (JsonParseException e) {
            LogUtils.getMainLogger().error("Could not parse Json" + e);
        }
        return null;
    }

    public static JSONObject fileToJSON(String path) {
        try (FileReader reader = new FileReader(path)) {
            StringBuilder builder = new StringBuilder();
            int code;
            while ((code = reader.read()) != -1) {
                builder.append((char) code);
            }
            return new JSONObject(builder.toString());
        } catch (FileNotFoundException e) {
            LogUtils.getMainLogger().error("Could not find file " + path, e);
        } catch (IOException e) {
            LogUtils.getMainLogger().error("IO Exception", e);
        } catch (JSONException e) {
            LogUtils.getMainLogger().error("Could not parse JSON", e);
        }
        return null;
    }

    public static void jsonToFile(String path, JsonObject json) {
        writeFile(path, json.toString());
    }

    public static void writeFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            LogUtils.getMainLogger().error("Could not save \"" + content + "\"", e);
        }
    }

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        if (!file.exists()) {
            LogUtils.getMainLogger().error("File " + file + " does not exist.");
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            StringBuilder builder = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                builder.append((char) i);
            }
            return builder.toString();
        } catch (IOException e) {
            return null;
        }
    }
}