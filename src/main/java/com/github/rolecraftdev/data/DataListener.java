package com.github.rolecraftdev.data;

import com.github.rolecraftdev.RolecraftCore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * The data listener for Rolecraft which saves and loads {@link PlayerData}
 * objects from storage when a player quits or joins, respectively.
 *
 * @since 0.0.5
 */
public final class DataListener implements Listener {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the linked {@link RolecraftCore} object
     * @since 0.0.5
     */
    public DataListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        plugin.getDataManager().loadOrCreateData(
                event.getPlayer().getUniqueId());
    }

    /**
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final UUID playerId = event.getPlayer().getUniqueId();
        plugin.getDataManager().unloadAndSaveData(playerId);
    }
}
