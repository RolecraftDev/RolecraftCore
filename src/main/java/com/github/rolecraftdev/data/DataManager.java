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

/**
 * Manages data objects, such as {@link PlayerData} objects, in Rolecraft
 */
public final class DataManager {
    /**
     * The Rolecraft plugin
     */
    private final RolecraftCore plugin;
    /**
     * The {@link DataStore} that data is being stored in and loaded from by
     * this DataManager
     */
    private final DataStore store;
    /**
     * A Map of player unique identifiers to loaded {@link PlayerData} objects -
     * {@link PlayerData} objects are loaded when a player joins the server and
     * unloaded when the player quits
     */
    private final Map<UUID, PlayerData> loadedPlayerData;

    /**
     * Creates a new DataManager object
     *
     * @param plugin The Rolecraft plugin this DataManager is storing data for
     */
    public DataManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        this.store = plugin.getDataStore();

        loadedPlayerData = new HashMap<UUID, PlayerData>();
    }

    /**
     * Loads data for the given player from the database. If no data exists for
     * the player, a new set of data is created
     *
     * @param player The unique identifier of the player to load data for
     */
    public void loadOrCreateData(final UUID player) {
        final PlayerData data = new PlayerData(player,
                plugin.getServer().getPlayer(player).getName());
        loadedPlayerData.put(player, data);
        store.requestPlayerData(data);
    }

    /**
     * Unloads data for the given player and saves it to the database
     *
     * @param player The unique identifier of the player to save data for
     */
    public void unloadAndSaveData(final UUID player) {
        final PlayerData data = loadedPlayerData.remove(player);
        if (data != null) {
            store.commitPlayerData(data);
        }
    }

    /**
     * Unloads all currently loaded {@link PlayerData} objects which are stored
     * by this DataManager
     */
    public void unloadAllPlayerData() {
        for (final UUID id : loadedPlayerData.keySet()) {
            unloadAndSaveData(id);
        }
    }

    /**
     * Gets the {@link PlayerData} object for the player with the given unique
     * identifier ({@link UUID})
     *
     * @param player The unique identifier of the player to get data for
     * @return The {@link PlayerData} for the player with the given identifier
     */
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

    /**
     * Gets the {@link DataStore} being used to store and retrieve data for
     * this DataManager
     *
     * @return This DataManager's {@link DataStore} object
     */
    public DataStore getStore() {
        return store;
    }
}
