package me.goodi.ai.agent;

import me.goodi.ai.player.PlayerInfo;
import me.goodi.ai.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class AILoopTask implements Runnable {
    private final QTableManager ai;

    public AILoopTask(QTableManager ai) {
        this.ai = ai;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerState state = buildStateFromPlayer(player);
            Action action = ai.chooseAction(state, 0.1); // 10% explore

            //player.sendMessage("§7[AI] State: " + state + ", Action: " + action); //AI decision

            applyActionToPlayer(action, player);

            double rewardSignal = evaluateActionFeedback(player, action);
            PlayerState nextState = buildStateFromPlayer(player);

            ai.updateQ(state, action, rewardSignal, nextState);
        }
    }


    private PlayerState buildStateFromPlayer(Player player) {
        String world = player.getWorld().getName();
        long time = player.getWorld().getTime();
        int favor = getFavor(player);
        double health = player.getHealth();

        health = Math.max(0, Math.min(player.getMaxHealth(), health));

        int timeBucket = (int)(time / 12000);

        favor = Math.max(-100, Math.min(100, favor));

        boolean afk = PlayerInfo.getAfk(player);

        return new PlayerState(world, timeBucket, favor, health, afk);
    }





    private void applyActionToPlayer(Action action, Player player) {
        switch (action) {
            case GIVE_ITEM -> {
                ItemUtils.giveRandomItem(player);
            }
            case DAMAGE -> {
                player.sendMessage("§c§lGod §f| Pain has been inflicted apon you, do better.");
                player.damage(2);
            }
            case GIVE_GOOD_EFFECT -> {
                player.sendMessage("§a§lGod §f| A good feeling washes over you.");
                giveRandomPositivePotionEffect(player);
            }
            case GIVE_BAD_EFFECT -> {
                player.sendMessage("§c§lGod §f| Something feels wrong...");
                giveRandomNegativePotionEffect(player);
            }
            case HEAL -> {
                double healAmount = 4;
                player.setHealth(Math.min(player.getHealth() + healAmount, player.getMaxHealth()));
                player.sendMessage("§a§lGod §f| You feel a little healthier.");
            }
            case NOTHING -> player.sendMessage("§lGod §7| Nothing happens...");
        }
    }


    private double evaluateActionFeedback(Player player, Action action) {
        int favor = getFavor(player);
        double health = player.getHealth();
        double maxHealth = player.getMaxHealth();
        boolean afk = PlayerInfo.getAfk(player);

        int healthBucket = (int) (health / (maxHealth / 5));

        return switch (action) {
            case HEAL -> {
                if (healthBucket <= 1) {
                    yield favor >= 1 ? 15 : -1;
                } else if (healthBucket == 2) {
                    yield favor >= 1 ? 10 : -2;
                } else {
                    yield favor >= 1 ? 5 : -5;
                }
            }

            case GIVE_ITEM, GIVE_GOOD_EFFECT -> {
                if (afk) {
                    yield favor >= 1 ? 2 : -12;
                } else {
                    yield favor >= 1 ? 10 : -10;
                }
            }

            case GIVE_BAD_EFFECT, DAMAGE -> {
                if (afk) {
                    yield favor <= 0 ? -2 : -12;
                } else {
                    yield favor <= 0 ? 10 : -10;
                }
            }

            case NOTHING -> afk ? 1 : -1;
        };
    }



    public static void giveRandomPositivePotionEffect(Player player) {
        PotionEffectType[] positiveEffects = {
                PotionEffectType.SPEED,
                PotionEffectType.REGENERATION,
                PotionEffectType.STRENGTH,
                PotionEffectType.FIRE_RESISTANCE,
                PotionEffectType.JUMP_BOOST,
                PotionEffectType.STRENGTH,
                PotionEffectType.ABSORPTION,
                PotionEffectType.HASTE,
                PotionEffectType.INVISIBILITY,
                PotionEffectType.LUCK,
                PotionEffectType.SATURATION
        };

        Random random = new Random();
        int randomIndex = random.nextInt(positiveEffects.length);
        PotionEffectType randomPotionEffectType = positiveEffects[randomIndex];

        int duration = random.nextInt(100) + 200;
        int amplifier = random.nextInt(2);

        PotionEffect randomPotionEffect = new PotionEffect(randomPotionEffectType, duration, amplifier);
        player.addPotionEffect(randomPotionEffect);

    }

    public static void giveRandomNegativePotionEffect(Player player) {
        PotionEffectType[] negativeEffects = {
                PotionEffectType.POISON,
                PotionEffectType.WEAKNESS,
                PotionEffectType.SLOWNESS,
                PotionEffectType.BLINDNESS,
                PotionEffectType.NAUSEA,
                PotionEffectType.WITHER,
                PotionEffectType.DARKNESS,
                PotionEffectType.UNLUCK,
                PotionEffectType.HUNGER
        };

        Random random = new Random();
        int randomIndex = random.nextInt(negativeEffects.length);
        PotionEffectType randomPotionEffectType = negativeEffects[randomIndex];

        int duration = random.nextInt(200) + 100;
        int amplifier = random.nextInt(2);

        PotionEffect randomPotionEffect = new PotionEffect(randomPotionEffectType, duration, amplifier);
        player.addPotionEffect(randomPotionEffect);

    }



    private int getFavor(Player player) {
        return FavorManager.getFavor(player);
    }
}
