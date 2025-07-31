package me.goodi.ai;

import me.goodi.ai.agent.AILoopTask;
import me.goodi.ai.agent.FavorManager;
import me.goodi.ai.agent.QTableManager;
import me.goodi.ai.commands.FavorCommand;
import me.goodi.ai.listeners.ContextualFavorListener;
import me.goodi.ai.listeners.PlayerAFKListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Ai extends JavaPlugin {
    private QTableManager qTableManager;

    @Override
    public void onEnable() {
        qTableManager = new QTableManager();

        getServer().getPluginManager().registerEvents(new ContextualFavorListener(), this);
        getCommand("favor").setExecutor(new FavorCommand());

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }


        File qFile = new File(getDataFolder(), "qtable.yml");
        qTableManager.loadFromFile(qFile);

        FavorManager.setup(getDataFolder());
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, FavorManager::saveFavor, 6000L, 6000L);



        getServer().getScheduler().runTaskTimer(this,
                new AILoopTask(qTableManager),
                20L,
                200L
        );

        PlayerAFKListener afkListener = new PlayerAFKListener();
        getServer().getPluginManager().registerEvents(afkListener, this);
        afkListener.startAfkChecker(this);

        getLogger().info("AI God is watching...");
    }

    @Override
    public void onDisable() {
        File qFile = new File(getDataFolder(), "qtable.yml");
        qTableManager.saveToFile(qFile);
        FavorManager.saveFavor();

    }
}