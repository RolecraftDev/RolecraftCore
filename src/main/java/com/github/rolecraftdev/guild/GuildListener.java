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

import com.github.rolecraftdev.util.ChunkLocation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Set;
import java.util.UUID;

/**
 * Listens for and manipulates various {@link Event}s affecting {@link Guild}s,
 * which in some cases depends on the configuration.
 *
 * @since 0.0.5
 */
public final class GuildListener implements Listener {
    private final GuildManager guildManager;
    /**
     * The special constant {@link Guild} instance used when the
     * {@link GuildManager} hasn't finished loading.
     */
    private final Guild nullGuild = new Guild(null);
    /**
     * The special constant {@link Guild} instance used to indicate a safezone.
     */
    private final Guild safeZone = new Guild(null);

    /**
     * Constructor.
     *
     * @param guildManager the {@link GuildManager} of which the {@link Guild}s
     *        should be handled by this {@link Listener}
     * @since 0.0.5
     */
    public GuildListener(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity damagee = event.getEntity();
        final Entity damager = event.getDamager();

        // Only handle if a player is damaged
        if (!(damagee instanceof Player)) {
            return;
        }
        if (guildManager.disallowHallPvp()
                && inGuildLand(damagee.getLocation())) {
            event.setCancelled(true);
            return;
        }

        // Cancel if original damager is in the same guild as the damagee
        UUID damagerId = null;

        if (damager instanceof Player) {
            damagerId = damager.getUniqueId();
        } else if (damager instanceof Projectile) {
            final Projectile projectile = (Projectile) damager;

            if (projectile.getShooter() instanceof Player) {
                damagerId = ((Player) projectile.getShooter()).getUniqueId();
            }
        }
        // No damager that should be handled
        if (damagerId == null) {
            return;
        }

        final Guild damageeGuild = guildManager.getPlayerGuild(damagee
                .getUniqueId());
        final Guild damagerGuild = guildManager.getPlayerGuild(damagerId);

        if (damageeGuild != null && damageeGuild.equals(damagerGuild)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (guildManager.hallAntiGrief() && cancel(
                event.getBlock().getLocation(), event.getPlayer().getUniqueId(),
                GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (guildManager.hallAntiGrief() && cancel(
                event.getBlock().getLocation(), event.getPlayer().getUniqueId(),
                GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (event.getBlockClicked() == null) {
            return;
        }

        final BlockFace face = event.getBlockFace();

        if (guildManager.hallAntiGrief() && cancel(
                event.getBlockClicked().getLocation().add(face.getModX(),
                        face.getModY(), face.getModZ()),
                event.getPlayer().getUniqueId(), GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        if (event.getBlockClicked() == null) {
            return;
        }
        if (guildManager.hallAntiGrief() && cancel(
                event.getBlockClicked().getLocation(), event.getPlayer()
                        .getUniqueId(), GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (guildManager.hallAntiGrief() && cancel(
                event.getClickedBlock().getLocation(), event.getPlayer()
                        .getUniqueId(), GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            if (guildManager.hallAntiGrief() && cancel(
                    event.getBlock().getLocation(), event.getEntity()
                            .getUniqueId(), GuildAction.CHANGE_BLOCK)) {
                event.setCancelled(true);
            }
            return;
        }
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBlockForm(final EntityBlockFormEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockSpread(final BlockSpreadEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockForm(final BlockFormEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(final BlockFromToEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getToBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        if (guildManager.disallowHallPistons()) {
            for (final Block block : event.getBlocks()) {
                if (inGuildLand(block.getLocation())) {
                    event.setCancelled(true);
                }
            }
            return;
        }

        if (!guildManager.hallAntiGrief()) {
            return;
        }
        // Cancel when a non-air block is pushed into/out of a guild-hall
        if (event.getLength() <= 0) {
            return;
        }

        // Check all blocks that are being pushed
        final Block first = event.getBlocks().get(0);
        Guild previous = getGuildFromLocation(first);

        for (int i = 0; event.getLength() > i; i++) {
            // Use the positions of the blocks after they moved
            final Guild current = getGuildFromLocation(first.getRelative(event
                    .getDirection(), i + 1));

            if (!areGuildsEqual(previous, current)) {
                event.setCancelled(true);
                return;
            } else {
                previous = current;
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (guildManager.disallowHallPistons()) {
            if (inGuildLand(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
            return;
        }

        if (!guildManager.hallAntiGrief()) {
            return;
        }

        // Cancel when a non-air block is pulled into/out of a guild-hall
        final Block retractee = event.getRetractLocation().getBlock();

        if (retractee == null || retractee.getType() == Material.AIR) {
            return;
        }

        final Guild from = getGuildFromLocation(retractee.getLocation());
        final Guild to = getGuildFromLocation(event.getBlock().getRelative(
                event.getDirection()));

        if (!areGuildsEqual(from, to)) {
            event.setCancelled(true);
        }
    }

    /**
     * Check whether two guild are equal. Meaning they can either share the same
     * referenced object or are logically equal.
     *
     * @param guild1 the first guild
     * @param guild2 the second guild
     * @return {@code true} if the specified guilds share common references or
     *         are logically equal
     */
    private boolean areGuildsEqual(final Guild guild1, final Guild guild2) {
        return guild1 == guild2 || guild1 != null && guild1.equals(guild2);
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStructureGrow(final StructureGrowEvent event) {
        if (event.getPlayer() != null) {
            for (final BlockState block : event.getBlocks()) {
                if (cancel(block.getLocation(),
                        event.getPlayer().getUniqueId(),
                        GuildAction.CHANGE_BLOCK)) {
                    event.setCancelled(true);
                    // We don't need to check any more blocks
                    return;
                }
            }
        } else if (guildManager.protectFromEnvironment()) {
            for (final BlockState block : event.getBlocks()) {
                if (getGuildFromLocation(block) != null) {
                    event.setCancelled(true);
                    // We don't need to check any more blocks
                    return;
                }
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getPlayer() != null && guildManager.hallAntiGrief()) {
            if (cancel(event.getBlock().getLocation(), event.getPlayer()
                    .getUniqueId(), GuildAction.IGNITE_BLOCK)) {
                event.setCancelled(true);
            }
            return;
        }
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (guildManager.hallAntiGrief() && cancel(
                event.getEntity().getLocation(), event.getPlayer()
                        .getUniqueId(), GuildAction.CHANGE_BLOCK)) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (guildManager.hallAntiGrief() && event
                .getRemover() instanceof Player) {
            final Player player = (Player) event.getRemover();

            if (cancel(event.getEntity().getLocation(), player.getUniqueId(),
                    GuildAction.CHANGE_BLOCK)) {
                event.setCancelled(true);
            }
            return;
        }
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (inGuildLand(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.NORMAL)
    @SuppressWarnings("deprecation")
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (!guildManager.protectFromEnvironment()) {
            return;
        }
        if (!inGuildLand(event.getLocation())) {
            return;
        }
        if (!guildManager.isLoaded()) {
            event.setCancelled(true);
        } else {
            switch (event.getSpawnReason()) {
                case CURED:
                case BED:
                case EGG:
                case SPAWNER_EGG:
                case BUILD_SNOWMAN:
                case BUILD_IRONGOLEM:
                case BREEDING:
                case CUSTOM:
                case LIGHTNING:
                    // Don't cancel these reasons
                    return;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    /**
     * Check whether the given {@link GuildAction} at the specified
     * {@link Location}, executed by the given player, should be cancelled.
     *
     * @param loc the {@link Location} the {@link GuildAction} occured at
     * @param player the performer of the {@link GuildAction}
     * @param action the executed {@link GuildAction}
     * @return {@code true} if the event should be cancelled; {@code false}
     *         otherwise
     */
    private boolean cancel(final Location loc, final UUID player,
            final GuildAction action) {
        final Guild guild;
        guild = getGuildFromLocation(loc);

        if (guild == nullGuild) {
            return true;
        } else if (guild != null) {
            if (guild == safeZone) {
                return true;
            }
            return !(guild.isMember(player) && guild.can(player, action));
        }
        return false;
    }

    /**
     * Check whether the specified location lies in a guild-hall.
     *
     * @param loc the location to check
     * @return {@code true} if the specified location lies in a guild-hall or if
     *         the {@link GuildManager} hasn't been fully loaded yet
     */
    private boolean inGuildLand(final Location loc) {
        final Set<Guild> guilds = guildManager.getGuilds();

        // Not loaded yet, assume this is guild land just to be safe
        if (guilds == null) {
            return true;
        }

        final ChunkLocation cLoc = new ChunkLocation(loc);
        return guildManager.getTerritoryManager().hasOwner(cLoc)
                || guildManager.getTerritoryManager().isSafeZone(cLoc);
    }

    /**
     * Retrieve the {@link Guild} from the location of its land. A special
     * constant instance of {@link Guild} is returned when the
     * {@link GuildManager} registered to this {@link Listener} is not yet
     * wholly loaded, that is, when {@link GuildManager#isLoaded()} returns
     * {@code false}.
     *
     * @param loc the {@link Location} that should be investigated
     * @return the appropriate {@link Guild} of which the hall is at the given
     *         {@link Location} or a special constant {@link Guild} instance.
     * @see {@link #nullGuild}
     */
    private Guild getGuildFromLocation(final Location loc) {
        final Set<Guild> guilds = guildManager.getGuilds();
        if (guilds == null) {
            return nullGuild;
        }

        final ChunkLocation cLoc = new ChunkLocation(loc);
        if (guildManager.getTerritoryManager().isSafeZone(cLoc)) {
            return safeZone;
        }

        return guildManager.getTerritoryManager()
                .getTerritoryOwner(new ChunkLocation(loc));
    }

    /**
     * Retrieve the {@link Guild} from the location of its land. A special
     * constant instance of {@link Guild} is returned when the
     * {@link GuildManager} registered to this {@link Listener} is not yet
     * wholly loaded, that is, when {@link GuildManager#isLoaded()} returns
     * {@code false}.
     *
     * @param block the {@link Block} whose {@link Location} should be
     *        investigated
     * @return the appropriate {@link Guild} of which the hall is at the given
     *         {@link Location} or a special constant {@link Guild} instance.
     * @see {@link #nullGuild}
     */
    private Guild getGuildFromLocation(final Block block) {
        return getGuildFromLocation(block.getLocation());
    }

    /**
     * Retrieve the {@link Guild} from the location of its land. A special
     * constant instance of {@link Guild} is returned when the
     * {@link GuildManager} registered to this {@link Listener} is not yet
     * wholly loaded, that is, when {@link GuildManager#isLoaded()} returns
     * {@code false}.
     *
     * @param block the {@link BlockState} whose {@link Location} should be
     *        investigated
     * @return the appropriate {@link Guild} of which the hall is at the given
     *         {@link Location} or a special constant {@link Guild} instance.
     * @see {@link #nullGuild}
     */
    private Guild getGuildFromLocation(final BlockState block) {
        return getGuildFromLocation(block.getLocation());
    }
}
