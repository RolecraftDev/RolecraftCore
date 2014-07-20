package com.github.rolecraftdev;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RCListener implements Listener {
    private final RolecraftCore plugin;

    public RCListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        plugin.getDataManager().loadOrCreateData(
                event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        plugin.getDataManager().unloadAndSaveData(
                event.getPlayer().getUniqueId());
    }
}
