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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Listens for and manipulates various events depending on the guild config
 */
public final class GuildListener implements Listener {
    private final GuildManager guildManager;

    GuildListener(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event
                .getEntity() instanceof Player) {
            if (guildManager.disallowHallPvp()
                    && getGuildFromHall(event.getEntity().getLocation())
                    != null) {
                event.setCancelled(true);
                return;
            }

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
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        event.setCancelled(cancel(event.getBlock().getLocation(),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (event.getBlockClicked() == null) {
            return;
        }
        final BlockFace face = event.getBlockFace();
        event.setCancelled(cancel(event.getBlockClicked().getLocation().add(
                        face.getModX(), face.getModY(), face.getModZ()),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        if (event.getBlockClicked() == null) {
            return;
        }
        event.setCancelled(cancel(event.getBlockClicked().getLocation(),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        event.setCancelled(cancel(event.getClickedBlock().getLocation(),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(cancel(event.getBlock().getLocation(),
                    ((Player) event.getEntity()).getUniqueId(),
                    event.isCancelled(), GuildAction.CHANGE_BLOCK));
        } else {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }

            final Guild guild = getGuildFromHall(
                    event.getBlock().getLocation());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBlockForm(final EntityBlockFormEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }
            final Guild guild = getGuildFromHall(
                    event.getBlock().getLocation());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockSpread(final BlockSpreadEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }

        final Guild guild = getGuildFromHall(event.getBlock().getLocation());
        if (guild != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockForm(final BlockFormEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }

        final Guild guild = getGuildFromHall(event.getBlock().getLocation());
        if (guild != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(final BlockFromToEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }

        final Guild guild = getGuildFromHall(event.getToBlock().getLocation());
        if (guild != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        for (final Block block : event.getBlocks()) {
            final Guild guild = getGuildFromHall(block.getLocation());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (getGuildFromHall(event.getRetractLocation()) != null
                || getGuildFromHall(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStructureGrow(final StructureGrowEvent event) {
        final List<BlockState> blocks = event.getBlocks();
        for (final BlockState block : blocks) {
            final Guild guild = getGuildFromHall(block.getLocation());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }

        final Guild guild = getGuildFromHall(event.getLocation());
        if (guild != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        final Player player = event.getPlayer();
        if (player != null) {
            event.setCancelled(
                    cancel(event.getBlock().getLocation(), player.getUniqueId(),
                            event.isCancelled(), GuildAction.IGNITE_BLOCK));
        } else {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }

            final Guild guild = getGuildFromHall(
                    event.getIgnitingBlock().getLocation());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEggThrow(final PlayerEggThrowEvent event) {
        if (cancel(event.getPlayer().getLocation(),
                event.getPlayer().getUniqueId(), false,
                GuildAction.CHANGE_BLOCK)) {
            event.setHatching(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace(final HangingPlaceEvent event) {
        event.setCancelled(cancel(event.getEntity().getLocation(),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player)) {
            if (guildManager.protectFromEnvironment()) {
                if (getGuildFromHall(event.getEntity().getLocation()) != null) {
                    event.setCancelled(true);
                }
            }
            return;
        }

        final Player player = (Player) event.getRemover();
        event.setCancelled(
                cancel(event.getEntity().getLocation(), player.getUniqueId(),
                        event.isCancelled(), GuildAction.CHANGE_BLOCK));
    }

    /**
     * Checks whether an event at the given location by the given location for
     * the given action should be cancelled, returning the value of def if we
     * have no preference
     *
     * @param loc    The {@link Location} at which the event is occurring
     * @param player The {@link UUID} of the player causing the event
     * @param def    The value to return if we have no preference
     * @param action The {@link GuildAction} which is taking place
     * @return
     */
    private boolean cancel(final Location loc, final UUID player,
            final boolean def, final GuildAction action) {
        final Guild guild;
        guild = getGuildFromHall(loc);
        if (guild == nullGuild) {
            return true;
        }
        if (guild != null) {
            return !(guild.isMember(player) && guild
                    .can(player, action));
        }
        return def;
    }

    /**
     * Gets the {@link Guild} Hall at the given location, returning null if
     * there isn't one or nullGuild if SQL hasn't loaded
     *
     * @param loc The {@link Location} to get the guild hall at
     * @return null if no guild is found, nullGuild if SQL isn't loaded
     */
    private Guild getGuildFromHall(final Location loc) {
        final Set<Guild> guilds = guildManager.getGuilds();
        if (guilds == null) {
            return nullGuild;
        }
        for (final Guild guild : guilds) {
            if (guild.getGuildHallRegion() != null) {
                if (guild.getGuildHallRegion().containsLocation(loc)) {
                    return guild;
                }
            }
        }
        return null;
    }

    private final Guild nullGuild = new Guild(null);
}
