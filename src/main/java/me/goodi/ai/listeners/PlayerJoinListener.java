package me.goodi.ai.listeners;

import me.goodi.ai.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e){
        Player player = e.getPlayer();
        PlayerInfo.setup(player);
    }

}
