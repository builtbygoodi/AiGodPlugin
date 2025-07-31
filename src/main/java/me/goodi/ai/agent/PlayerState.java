package me.goodi.ai.agent;

import java.util.Objects;

public class PlayerState {
    public String world;
    public int timeBucket;
    public int favorBucket;
    public int healthBucket;
    public boolean afk;
    //add in combat
    //is safe?

    public PlayerState(String world, int time, int favor, double health, boolean afk) {
        this.world = world;
        this.timeBucket = bucketTime(time);
        this.favorBucket = bucketFavor(favor, 5);
        this.healthBucket = bucketHealth(health);
        this.afk = afk;
    }

    private int bucketFavor(int favorValue, int bucketCount) {
        favorValue = Math.max(-100, Math.min(100, favorValue));
        return (favorValue + 100) / (200 / bucketCount);
    }

    private int bucketHealth(double health) {
        health = Math.max(0, Math.min(20, health));
        return (int)(health / 4);
    }

    private int bucketTime(long time) {
        time = Math.max(0, Math.min(23999, time));

        return (time < 12000) ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlayerState)) return false;
        PlayerState other = (PlayerState) o;
        return timeBucket == other.timeBucket &&
                favorBucket == other.favorBucket &&
                healthBucket == other.healthBucket &&
                afk == other.afk &&
                world.equals(other.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, timeBucket, favorBucket, healthBucket, afk);
    }

    @Override
    public String toString() {
        return String.format("PlayerState[%s,%d,%d,%d,%b]", world, timeBucket, favorBucket, healthBucket, afk);
    }
}
