# GodAI – Adaptive Minecraft AI Plugin

**GodAI** is an experimental Minecraft plugin that uses Q-learning to interact with players based on their in-game behavior and state.

> This repository contains the full **source code** (`src/` folder). No `.jar` is included.

---

## Features

- Reinforcement learning with Q-table memory  
- Tracks player states (health, favor, AFK status, time of day)  
- Applies random actions: good effects, bad effects, damage, healing, or nothing  
- Saves/loads learned Q-values to YAML config  
- Constantly learns from in-game feedback  

---

## How to Build

To turn this into a `.jar`:

1. Clone the repo or download the code  
2. Create a Java project (Maven/Gradle or your IDE)  
3. Add Spigot or Paper as a dependency  
4. Add a `plugin.yml` (see below)  
5. Compile to a `.jar` file  

---

## Example `plugin.yml`

```yaml
name: GodiAI
main: me.goodi.ai.Main
version: 1.0
api-version: 1.16
commands: {}
```

## Notes

This repo is for development and learning purposes.

## Contact

Created by Goodi
Email - builtbygoodi@gmail.com
Discord - builtbygoodi
