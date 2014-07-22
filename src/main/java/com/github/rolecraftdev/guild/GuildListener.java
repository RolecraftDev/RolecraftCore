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
package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.data.Region2D;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Set;
import java.util.UUID;

public final class GuildListener implements Listener {
    private final GuildManager guildManager;

    GuildListener(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event
                .getEntity() instanceof Player) {
            final UUID playerId = ((Player) event.getEntity()).getUniqueId();
            final UUID damagerId = ((Player) event.getDamager()).getUniqueId();

            if (guildManager.getPlayerGuild(playerId)
                    .equals(guildManager.getPlayerGuild(damagerId))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        event.setCancelled(cancel(event.getBlock().getLocation(),
                event.getPlayer().getUniqueId()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        event.setCancelled(cancel(event.getBlock().getLocation(),
                event.getPlayer().getUniqueId()));
    }

    private boolean cancel(final Location loc, final UUID player) {
        final Set<Guild> guilds = guildManager.getGuilds();
        for (final Guild guild : guilds) {
            final Region2D hall = guild.getGuildHallRegion();
            if (hall.containsLocation(loc)) {
                return !(guild.isMember(player) && guild
                        .can(player, GuildAction.CHANGE_BLOCK));
            }
        }

        return false;
    }
}
