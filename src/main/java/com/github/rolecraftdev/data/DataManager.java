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

import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper class for managing persistent data associated to Rolecraft.
 *
 * @since 0.0.5
 */
public final class DataManager {
    /**
     * The key used for metadata for when something requires to be confirmed.
     *
     * @since 0.0.5
     */
    public static final String CONFIRM_COMMAND_METADATA = "rolecraft-confirm";

    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * The {@link DataStore} implementation this manager uses for CRUD
     * operations.
     */
    private final DataStore store;
    /**
     * Contains the correlation between a player's {@link UUID} and his
     * {@link PlayerData}.
     */
    private final Map<UUID, PlayerData> loadedPlayerData;

    /**
     * Constructor.
     *
     * @param plugin the linked {@link RolecraftCore} object
     * @param store the used {@link DataStore} implementation
     * @since 0.0.5
     */
    public DataManager(final RolecraftCore plugin, final DataStore store) {
        this.plugin = plugin;
        this.store = store;
        // Thread safe operations!
        loadedPlayerData = new ConcurrentHashMap<UUID, PlayerData>();
    }

    /**
     * Loads {@link PlayerData} for the given player from the linked database.
     * If no data exists for the specified player, a new set of data is created
     * and loaded afterwards.
     *
     * @param player the {@link UUID} of the player
     * @since 0.0.5
     */
    public void loadOrCreateData(final UUID player) {
        final PlayerData data = new PlayerData(plugin, player, Bukkit
                .getPlayer(player).getName());
        loadedPlayerData.put(player, data);
        store.requestPlayerData(data, false);
    }

    /**
     * Unloads the {@link PlayerData} of the player from memory and saves it to
     * the used database.
     *
     * @param player the {@link UUID} of the player
     * @since 0.0.5
     */
    public void unloadAndSaveData(final UUID player) {
        final PlayerData data = loadedPlayerData.remove(player);
        if (data != null) {
            store.commitPlayerData(data);
        }
    }

    /**
     * Unload all currently loaded {@link PlayerData}.
     *
     * @since 0.0.5
     */
    public void unloadAllPlayerData() {
        for (final UUID id : loadedPlayerData.keySet()) {
            unloadAndSaveData(id);
        }
    }

    /**
     * Get the {@link PlayerData} that is in correlation with the specified
     * player.
     *
     * @param player the {@link UUID} of the player
     * @return the {@link PlayerData} of the specified player
     * @since 0.0.5
     */
    public PlayerData getPlayerData(final UUID player) {
        final PlayerData result = loadedPlayerData.get(player);
        if (result != null) {
            return result;
        }
        loadOrCreateData(player);
        return getPlayerData(player);
    }

    /**
     * Returns the associated {@link RolecraftCore} instance.
     *
     * @return the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Get the {@link DataStore} implementation that is used for CRUD
     * operations.
     *
     * @return the used {@link DataStore}
     * @since 0.0.5
     */
    public DataStore getStore() {
        return store;
    }

    /**
     * Obtain all currently loaded {@link PlayerData}.
     *
     * @return all loaded {@link PlayerData}
     * @since 0.0.5
     */
    public Collection<PlayerData> getPlayerDatum() {
        return loadedPlayerData.values();
    }
}
