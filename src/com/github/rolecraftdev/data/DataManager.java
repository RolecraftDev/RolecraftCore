package com.github.rolecraftdev.data;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.DataStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DataManager {
    private final RolecraftCore plugin;
    private final DataStore store;

    private Map<UUID, PlayerData> loadedPlayerData = new HashMap<UUID, PlayerData>();

    public DataManager(final RolecraftCore plugin, final DataStore store) {
        this.plugin = plugin;
        this.store = store;
    }

    public void loadOrCreateData(final UUID player) {
        // TODO
    }

    public void unloadAndSaveData(final UUID player) {
        // TODO
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public DataStore getStore() {
        return store;
    }
}
