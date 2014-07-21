/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
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
