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
import com.github.rolecraftdev.data.storage.DataUpdateTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
     * The {@link BukkitTask} used to periodically update the database.
     */
    private final BukkitTask automaticUpdaterTask;

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

        final DataUpdateTask updateTask = new DataUpdateTask(plugin);
        this.automaticUpdaterTask = updateTask
                .runTaskTimerAsynchronously(plugin, 6000L, 6000L); // 5min timer
    }

    /**
     * Only call from {@link RolecraftCore#onDisable()}.
     *
     * @since 0.0.5
     */
    public void cleanup() {
        this.automaticUpdaterTask.cancel();
        this.unloadAllPlayerData();
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
        this.unloadAndSaveData(player, false);
    }

    private void unloadAndSaveData(final UUID player, final boolean sync) {
        final PlayerData data = loadedPlayerData.remove(player);
        if (data == null) {
            return;
        }

        if (sync) {
            store.commitPlayerDataSync(data);
        } else {
            store.commitPlayerData(data, true);
        }
    }

    /**
     * Unload all currently loaded {@link PlayerData}. Should only be used when
     * the plugin is disabling as it saves data synchronously.
     *
     * @since 0.0.5
     */
    public void unloadAllPlayerData() {
        for (final UUID id : loadedPlayerData.keySet()) {
            unloadAndSaveData(id, true);
        }
    }

    /**
     * Updates the database with all currently loaded {@link PlayerData}. Called
     * periodically in async by {@link DataUpdateTask}.
     *
     * @param sync whether to save data on the current thread
     * @since 0.0.5
     */
    public void saveAllPlayerData(boolean sync) {
        for (final UUID id : loadedPlayerData.keySet()) {
            savePlayerData(id, sync);
        }
    }

    /**
     * Saves, but does not unload, the {@link PlayerData} for the player with
     * the given {@link UUID}.
     *
     * @param player the unique identifier of the player to save data for
     * @param sync whether to save the data on the current thread
     * @since 0.0.5
     */
    public void savePlayerData(final UUID player, boolean sync) {
        final PlayerData data = this.loadedPlayerData.get(player);
        if (data == null) {
            return;
        }

        if (sync) {
            store.commitPlayerDataSync(data);
        } else {
            store.commitPlayerData(data, false);
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
     * Get the {@link PlayerData} that is in correlation with the specified
     * player.
     *
     * @param player the player
     * @return the {@link PlayerData} of the specified player
     * @since 0.1.0
     */
    public PlayerData getPlayerData(final Player player) {
        return getPlayerData(player.getUniqueId());
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
     * Obtain all currently loaded {@link PlayerData} objects.
     *
     * @return all loaded {@link PlayerData}
     * @since 0.0.5
     */
    public Collection<PlayerData> getPlayerDatum() {
        return loadedPlayerData.values();
    }
}
