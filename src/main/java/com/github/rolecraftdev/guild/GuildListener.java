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

import com.github.rolecraftdev.data.PlayerData;

import com.traksag.channels.Channel;
import com.traksag.channels.event.AsyncChannelChatEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
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
     * Constructor.
     *
     * @param guildManager the {@link GuildManager} of which the {@link Guild}s
     *        should be handled by this {@link Listener}
     * @since 0.0.5
     */
    GuildListener(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        // All function calls in here should be thread safe
        if (event.isCancelled() || !guildManager.getChannelBatch().isOpen()) {
            return;
        }

        final Channel channel = guildManager.getChannelBatch().getChannel(
                event.getPlayer());

        if (channel != null && channel.isOpen()) {
            // Send in channel
            event.setCancelled(true);
            channel.chat(event.isAsynchronous(), event.getPlayer(),
                    event.getFormat(), event.getMessage());
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChannelChat(final AsyncChannelChatEvent event) {
        // All function calls in here should be thread safe
        if (event.isCancelled()) {
            return;
        }

        for (final PlayerData data : guildManager.getPlugin().getDataManager()
                .getPlayerDatum()) {
            // Null becomes non-null, never the other way around -> this is safe
            if (data.getSettings() != null && data.getSettings()
                    .isGuildChatSpy()) {
                event.getRecipients().add(Bukkit.getPlayer(data.getPlayerId()));
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player
                && event.getEntity() instanceof Player) {
            if (guildManager.disallowHallPvp() && getGuildFromHall(
                    event.getEntity()) != null) {
                event.setCancelled(true);
                return;
            }

            final Guild damagerGuild = guildManager.getPlayerGuild(
                    event.getDamager().getUniqueId());
            if (damagerGuild != null && damagerGuild.equals(guildManager
                    .getPlayerGuild(event.getDamager().getUniqueId()))) {
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Arrow
                && event.getEntity() instanceof Player) {
            if (guildManager.disallowHallPvp() && getGuildFromHall(
                    event.getEntity()) != null) {
                event.setCancelled(true);
                return;
            }

            if (((Projectile) event.getDamager()).getShooter()
                    instanceof Player) {
                final UUID damagerId = ((AnimalTamer) ((Projectile) event
                        .getDamager()).getShooter()).getUniqueId();
                final Guild damagerGuild = guildManager
                        .getPlayerGuild(damagerId);
                if (damagerGuild != null && damagerGuild.equals(guildManager
                        .getPlayerGuild(event.getEntity().getUniqueId()))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        event.setCancelled(cancel(event.getBlock().getLocation(), event
                        .getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        event.setCancelled(cancel(event.getBlock().getLocation(), event
                        .getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
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
        event.setCancelled(cancel(
                event.getBlockClicked().getLocation()
                        .add(face.getModX(), face.getModY(), face.getModZ()),
                event.getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        if (event.getBlockClicked() == null) {
            return;
        }

        event.setCancelled(cancel(event.getBlockClicked().getLocation(), event
                        .getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        event.setCancelled(cancel(event.getClickedBlock().getLocation(), event
                        .getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(cancel(event.getBlock().getLocation(),
                    event.getEntity().getUniqueId(),
                    event.isCancelled(), GuildAction.CHANGE_BLOCK));
        } else {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }

            final Guild guild = getGuildFromHall(event.getBlock());
            if (guild != null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityBlockForm(final EntityBlockFormEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }
            final Guild guild = getGuildFromHall(event.getBlock());
            if (guild != null) {
                event.setCancelled(true);
            }
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

        if (getGuildFromHall(event.getBlock()) != null) {
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

        if (getGuildFromHall(event.getBlock()) != null) {
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

        if (getGuildFromHall(event.getToBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        for (final Block block : event.getBlocks()) {
            if (getGuildFromHall(block) != null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (getGuildFromHall(event.getRetractLocation()) != null
                || getGuildFromHall(event.getBlock()) != null) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStructureGrow(final StructureGrowEvent event) {
        for (final BlockState block : event.getBlocks()) {
            if (getGuildFromHall(block) != null) {
                event.setCancelled(true);
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

        if (getGuildFromHall(event.getLocation()) != null) {
            event.setCancelled(true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        final Player player = event.getPlayer();
        if (player != null) {
            event.setCancelled(cancel(event.getBlock().getLocation(),
                    player.getUniqueId(), event.isCancelled(),
                    GuildAction.IGNITE_BLOCK));
        } else {
            if (!guildManager.protectFromEnvironment()) {
                return;
            }

            if (getGuildFromHall(event.getIgnitingBlock()) != null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEggThrow(final PlayerEggThrowEvent event) {
        if (cancel(event.getPlayer().getLocation(), event.getPlayer()
                .getUniqueId(), false, GuildAction.CHANGE_BLOCK)) {
            event.setHatching(false);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace(final HangingPlaceEvent event) {
        event.setCancelled(cancel(event.getEntity().getLocation(), event
                        .getPlayer().getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player) && guildManager
                .protectFromEnvironment()) {
            if (getGuildFromHall(event.getEntity()) != null) {
                event.setCancelled(true);
            }
            return;
        }

        final Player player = (Player) event.getRemover();
        event.setCancelled(cancel(event.getEntity().getLocation(),
                player.getUniqueId(), event.isCancelled(),
                GuildAction.CHANGE_BLOCK));
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.NORMAL)
    @SuppressWarnings("deprecation")
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        final Guild guild = getGuildFromHall(event.getLocation());
        if (guild != null) {
            if (guild == nullGuild) {
                event.setCancelled(true);
                return;
            }
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
     * @param def the default return value, in the case of no justified
     *        preference
     * @param action the executed {@link GuildAction}
     * @return {@code true} if the event should be cancelled; {@code false}
     *         otherwise
     */
    private boolean cancel(final Location loc, final UUID player,
            final boolean def, final GuildAction action) {
        final Guild guild;
        guild = getGuildFromHall(loc);
        if (guild == nullGuild) {
            return true;
        }
        if (guild != null) {
            return !(guild.isMember(player) && guild.can(player, action));
        }
        return def;
    }

    /**
     * Retrieve the {@link Guild} from the location of its hall. A special
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

    /**
     * Retrieve the {@link Guild} from the location of its hall. A special
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
    private Guild getGuildFromHall(final Block block) {
        return getGuildFromHall(block.getLocation());
    }

    /**
     * Retrieve the {@link Guild} from the location of its hall. A special
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
    private Guild getGuildFromHall(final BlockState block) {
        return getGuildFromHall(block.getLocation());
    }

    /**
     * Retrieve the {@link Guild} from the location of its hall. A special
     * constant instance of {@link Guild} is returned when the
     * {@link GuildManager} registered to this {@link Listener} is not yet
     * wholly loaded, that is, when {@link GuildManager#isLoaded()} returns
     * {@code false}.
     *
     * @param entity the {@link Entity} whose {@link Location} should be
     *        investigated
     * @return the appropriate {@link Guild} of which the hall is at the given
     *         {@link Location} or a special constant {@link Guild} instance.
     * @see {@link #nullGuild}
     */
    private Guild getGuildFromHall(final Entity entity) {
        return getGuildFromHall(entity.getLocation());
    }
}
