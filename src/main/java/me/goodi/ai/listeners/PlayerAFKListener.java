package me.goodi.ai.listeners;

import me.goodi.ai.player.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerAFKListener implements Listener {

    private final Map<Player, Long> lastActive = new HashMap<>();
    private static final long AFK_TIMEOUT = 120000L;

    public void startAfkChecker(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    long last = lastActive.getOrDefault(player, System.currentTimeMillis());
                    long now = System.currentTimeMillis();
                    boolean afk = PlayerInfo.getAfk(player);

                    if (!afk && now - last >= AFK_TIMEOUT) {
                        PlayerInfo.setAfk(player, true);
                        player.sendMessage("§eYou are now AFK.");

                    } else if (afk && now - last < AFK_TIMEOUT) {
                        PlayerInfo.setAfk(player, false);
                        player.sendMessage("§aYou are no longer AFK.");
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    private void updateActivity(Player player) {
        lastActive.put(player, System.currentTimeMillis());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        updateActivity(e.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.getFrom().toVector().equals(e.getTo().toVector())) {
            updateActivity(e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        updateActivity(e.getPlayer());
    }
}