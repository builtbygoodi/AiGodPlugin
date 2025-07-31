package me.goodi.ai.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemUtils {

    private static final List<Material> itemMaterials = Arrays.stream(Material.values())
            .filter(Material::isItem)
            .filter(m -> m != Material.AIR)
            .toList();

    private static final Random random = new Random();

    public static void giveRandomItem(Player player) {
        if (itemMaterials.isEmpty()) return;

        Material randomMaterial = itemMaterials.get(random.nextInt(itemMaterials.size()));

        ItemStack randomItem = new ItemStack(randomMaterial);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), randomItem);
        } else {
            player.getInventory().addItem(randomItem);
        }

        player.sendMessage("§a§lGod §f| " + randomMaterial.name() + " has been bestowed upon you.");
    }
}
