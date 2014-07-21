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
        final PlayerData data = new PlayerData(player);
        loadedPlayerData.put(player, data);
        store.requestPlayerData(data);
    }

    public void unloadAndSaveData(final UUID player) {
        final PlayerData data = loadedPlayerData.remove(player);
        if (data != null) {
            store.commitPlayerData(data);
        }
    }

    public PlayerData getPlayerData(final UUID player) {
        PlayerData result = loadedPlayerData.get(player);
        if (result != null) {
            return result;
        }
        loadOrCreateData(player);
        return getPlayerData(player);
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public DataStore getStore() {
        return store;
    }
}
