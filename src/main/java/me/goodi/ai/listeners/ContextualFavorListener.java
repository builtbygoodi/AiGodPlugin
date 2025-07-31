package me.goodi.ai.listeners;

import me.goodi.ai.agent.FavorManager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class ContextualFavorListener implements Listener {



    @EventHandler
    public void killEntity(EntityDeathEvent e) {

        Player player = e.getEntity().getKiller();
        Entity entity = e.getEntity();

        if(player == null) return;

        if (isHostileMob(entity)){
            FavorManager.addFavor(player, 1);
        }

        if (isFriendly(entity)){
            FavorManager.addFavor(player, -1);
        }


    }

    public boolean isHostileMob(Entity entity) {
        return entity instanceof Monster;
    }

    public boolean isFriendly(Entity entity) {
        return (entity instanceof Player) || (entity instanceof Animals);
    }

}
