package com.multiminer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.multiminer.MultiMiner;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MultiMinerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("multiminer.json");

    // Vein Mining
    public boolean veinMiningEnabled = true;
    public int maxVeinSize = 64;
    public boolean veinRequiresTool = true;
    public float veinExhaustionMultiplier = 1.0f;

    // Tree Felling
    public boolean treeFellingEnabled = true;
    public int maxTreeSize = 128;
    public boolean treeRequiresTool = true;
    public float treeExhaustionMultiplier = 1.0f;

    // Activation
    public ActivationMode activationMode = ActivationMode.SNEAK;

    public enum ActivationMode {
        SNEAK,
        KEYBIND,
        ALWAYS_ON
    }

    public static MultiMinerConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                MultiMinerConfig config = GSON.fromJson(json, MultiMinerConfig.class);
                if (config != null) return config;
            } catch (IOException e) {
                MultiMiner.LOGGER.error("Failed to load config, using defaults", e);
            }
        }
        MultiMinerConfig config = new MultiMinerConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            MultiMiner.LOGGER.error("Failed to save config", e);
        }
    }
}
