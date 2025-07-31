package me.goodi.ai.agent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FavorManager {
    private static final Map<UUID, Integer> favorMap = new HashMap<>();
    private static File favorFile;
    private static YamlConfiguration favorConfig;

    public static void setup(File dataFolder) {
        favorFile = new File(dataFolder, "favor.yml");

        if (!favorFile.exists()) {
            try {
                favorFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        favorConfig = YamlConfiguration.loadConfiguration(favorFile);
        loadFavor();
    }

    public static void saveFavor() {
        if (favorConfig == null) {
            Bukkit.getLogger().warning("Favor config is not loaded!");
            return;
        }

        for (Map.Entry<UUID, Integer> entry : favorMap.entrySet()) {
            favorConfig.set("favor." + entry.getKey().toString(), entry.getValue());
        }

        try {
            favorConfig.save(favorFile);
            Bukkit.getLogger().info("Favor data saved successfully!");
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save favor data!");
            e.printStackTrace();
        }
    }

    private static void loadFavor() {
        if (favorConfig.contains("favor")) {
            for (String key : favorConfig.getConfigurationSection("favor").getKeys(false)) {
                try {
                    UUID playerUUID = UUID.fromString(key);
                    int favor = favorConfig.getInt("favor." + key);
                    favorMap.put(playerUUID, favor);
                } catch (IllegalArgumentException e) {
                    Bukkit.getLogger().warning("Invalid UUID found in favor.yml: " + key);
                }
            }
        }
    }

    public static int getFavor(Player player) {
        return favorMap.getOrDefault(player.getUniqueId(), 0);
    }

    public static void addFavor(Player player, int amount) {
        favorMap.put(player.getUniqueId(), getFavor(player) + amount);
    }

    public static void setFavor(Player player, int value) {
        favorMap.put(player.getUniqueId(), value);
    }
}
