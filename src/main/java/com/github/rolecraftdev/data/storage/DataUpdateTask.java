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
package com.github.rolecraftdev.data.storage;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;

import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

/**
 * A task that updates {@link PlayerData} entries in the database. Designed to
 * be run asynchronously and periodically.
 *
 * Scheduled in {@link DataManager}.
 *
 * @since 0.0.5
 */
public final class DataUpdateTask extends BukkitRunnable {
    private final RolecraftCore plugin;

    /**
     * @since 0.0.5
     */
    public DataUpdateTask(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void run() {
        final DataManager dataManager = this.plugin.getDataManager();
        dataManager.saveAllPlayerData(
                true); // sync param = true as task should be on a separate thread to the main thread
    }
}
