package ru.tempelstudio.WMVE.custom.Debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Debug {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("wmve.json");

    private static boolean Dungeon = false;
    private static boolean Classes = false;
    private static boolean Messages = false;
    private static boolean loaded = false;

    public static void load() {
        if (loaded) return;
        loaded = true;

        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
            Config config = GSON.fromJson(reader, Config.class);

            if (config != null) {
                Dungeon = config.Dungeon;
                Classes = config.Classes;
                Messages = config.Messages;
            }
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            System.err.println("[WMVE] Failed to load debug config: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());

            Config config = new Config(
                    Dungeon,
                    Classes,
                    Messages
            );

            try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException | JsonIOException e) {
            System.err.println("[WMVE] Failed to save debug config: " + e.getMessage());
        }
    }

    protected static void debugDungeon(boolean b) {
        load();
        Dungeon = b;
        save();
    }

    protected static void debugClasses(boolean b) {
        load();
        Classes = b;
        save();
    }

    protected static void debugMessages(boolean b) {
        load();
        Messages = b;
        save();
    }

    public static boolean Classes() {
        load();
        return Classes;
    }

    public static boolean Dungeon() {
        load();
        return Dungeon;
    }

    public static boolean Messages() {
        load();
        return Messages;
    }

    private record Config(
            boolean Dungeon,
            boolean Classes,
            boolean Messages
    ) {}
}
