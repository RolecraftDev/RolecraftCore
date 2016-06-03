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

    public DataUpdateTask(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        final DataManager dataManager = this.plugin.getDataManager();
        dataManager.saveAllPlayerData(true); // sync param = true as task should be on a separate thread to the main thread
    }
}
