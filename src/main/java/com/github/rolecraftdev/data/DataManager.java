package com.github.rolecraftdev.data;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.DataStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DataManager {
    private final RolecraftCore plugin;
    private final DataStore store;
    private final Map<UUID, PlayerData> loadedPlayerData;

    public DataManager(final RolecraftCore plugin, final DataStore store) {
        this.plugin = plugin;
        this.store = store;

        loadedPlayerData = new HashMap<UUID, PlayerData>();
    }

    public void loadOrCreateData(final UUID player) {
        // TODO
    }

    public void unloadAndSaveData(final UUID player) {
        // TODO
    }

    public PlayerData getPlayerData(final UUID player) {
        return loadedPlayerData.get(player);
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public DataStore getStore() {
        return store;
    }
}
