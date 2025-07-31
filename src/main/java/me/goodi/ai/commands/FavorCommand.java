package me.goodi.ai.commands;

import me.goodi.ai.agent.FavorManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FavorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 2) {
            String sub = args[0];
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid number.");
                return true;
            }

            switch (sub.toLowerCase()) {
                case "add" -> {
                    FavorManager.addFavor(player, amount);
                    player.sendMessage("§aFavor increased. Now: " + FavorManager.getFavor(player));
                }
                case "set" -> {
                    FavorManager.setFavor(player, amount);
                    player.sendMessage("§aFavor set to: " + FavorManager.getFavor(player));
                }
                default -> player.sendMessage("§cUse /favor add <amount> or /favor set <amount>");
            }
        } else {
            player.sendMessage("§eYour favor: " + FavorManager.getFavor(player));
        }

        return true;
    }
}
