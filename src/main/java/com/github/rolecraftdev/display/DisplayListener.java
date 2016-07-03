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
package com.github.rolecraftdev.display;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.data.PlayerDataLoadedEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listens for logins and players leaving and creates and disposes scoreboards
 * accordingly.
 *
 * @since 0.0.5
 */
public class DisplayListener implements Listener {
    private final RolecraftCore plugin;

    /**
     * @since 0.0.5
     */
    public DisplayListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    // priority set as this MUST happen after DataListener.onPlayerJoin
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDataLoaded(final PlayerDataLoadedEvent event) {
        plugin.getDisplayUpdater().createDisplayFor(
                plugin.getServer().getPlayer(
                        event.getPlayerData().getPlayerId()));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    /**
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    /**
     * Runs display-related functions that should be ran when a player leaves
     * the Bukkit server.
     *
     * @param player the player who leaves
     */
    private void onPlayerLeave(final Player player) {
        plugin.getDisplayUpdater().disposeDisplayOf(player);
    }
}
