package me.goodi.ai.agent;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QTableManager {
    private final Map<PlayerState, Map<Action, Double>> qTable = new HashMap<>();
    private final List<Action> actions = List.of(Action.values());

    private final double learningRate = 0.1;
    private final double discountFactor = 0.9;

    public void initializeState(PlayerState state) {
        qTable.putIfAbsent(state, new HashMap<>());
        for (Action action : actions) {
            qTable.get(state).putIfAbsent(action, 0.0);
        }
    }

    public Action chooseAction(PlayerState state, double epsilon) {
        initializeState(state);

        if (Math.random() < epsilon) {
            return actions.get(new Random().nextInt(actions.size()));
        }

        return qTable.get(state).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Action.GIVE_ITEM);
    }

    public void updateQ(PlayerState state, Action action, double reward, PlayerState nextState) {
        initializeState(state);
        initializeState(nextState);

        double currentQ = qTable.get(state).getOrDefault(action, 0.0);
        double maxNextQ = qTable.get(nextState).values().stream().max(Double::compare).orElse(0.0);

        double newQ = currentQ + learningRate * (reward + discountFactor * maxNextQ - currentQ);
        qTable.get(state).put(action, newQ);
    }

    public void saveToFile(File file) {
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<PlayerState, Map<Action, Double>> entry : qTable.entrySet()) {
            PlayerState state = entry.getKey();
            String stateKey = state.world + "," + state.timeBucket + "," + state.favorBucket + "," + state.healthBucket + "," + state.afk;

            for (Map.Entry<Action, Double> qEntry : entry.getValue().entrySet()) {
                String path = "qtable." + stateKey + "." + qEntry.getKey().name();
                config.set(path, qEntry.getValue());
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(File file) {
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        qTable.clear();

        if (config.isConfigurationSection("qtable")) {
            for (String stateKey : config.getConfigurationSection("qtable").getKeys(false)) {
                String[] parts = stateKey.split(",");
                if (parts.length != 5) continue;

                String world = parts[0];
                int timeBucket = Integer.parseInt(parts[1]);
                int favorBucket = Integer.parseInt(parts[2]);
                int healthBucket = Integer.parseInt(parts[3]);
                boolean afk = Boolean.getBoolean(parts[4]);



                PlayerState state = new PlayerState(world, timeBucket, favorBucket, healthBucket, afk);
                Map<Action, Double> actionMap = new HashMap<>();

                for (String actionName : config.getConfigurationSection("qtable." + stateKey).getKeys(false)) {

                    Action action = Action.valueOf(actionName);
                    double value = config.getDouble("qtable." + stateKey + "." + actionName);
                    actionMap.put(action, value);

                }

                qTable.put(state, actionMap);
            }
        }
    }
}
