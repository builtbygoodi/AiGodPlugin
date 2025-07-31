package me.goodi.ai.player;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerInfo {

    private static YamlConfiguration getPlayerConfig(Player player) {
        File playerFolder = new File("plugins/ai/player_data/" + player.getUniqueId().toString());
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }

        File playerInfoFile = new File(playerFolder, "player_info.yml");
        if (!playerInfoFile.exists()) {
            try {
                playerInfoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(playerInfoFile);
    }

    public static void setup(Player player) {
        YamlConfiguration playerInfoConfig = getPlayerConfig(player);

        playerInfoConfig.addDefault("afk", false);
        playerInfoConfig.addDefault("in_combat", false);
        playerInfoConfig.options().copyDefaults(true);

        // Save the file to persist defaults
        saveConfig(player, playerInfoConfig);
    }

    public static void setAfk(Player player, boolean afk) {
        YamlConfiguration playerInfoConfig = getPlayerConfig(player);
        playerInfoConfig.set("afk", afk);

        File playerFolder = new File("plugins/ai/player_data/" + player.getUniqueId().toString());
        File playerInfoFile = new File(playerFolder, "player_info.yml");

        try {
            playerInfoConfig.save(playerInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setInCombat(Player player, boolean inCombat) {
        YamlConfiguration playerInfoConfig = getPlayerConfig(player);
        playerInfoConfig.set("in_combat", inCombat);
        saveConfig(player, playerInfoConfig);
    }

    public static boolean getAfk(Player player) {
        YamlConfiguration playerInfoConfig = getPlayerConfig(player);
        return playerInfoConfig.getBoolean("afk", false);
    }

    public static boolean getInCombat(Player player) {
        YamlConfiguration playerInfoConfig = getPlayerConfig(player);
        return playerInfoConfig.getBoolean("in_combat", false);
    }

    private static void saveConfig(Player player, YamlConfiguration config) {
        File playerFolder = new File("plugins/ai/player_data/" + player.getUniqueId().toString());
        File playerInfoFile = new File(playerFolder, "player_info.yml");

        try {
            config.save(playerInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
