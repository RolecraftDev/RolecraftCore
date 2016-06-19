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
package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftCancellable;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * A {@link GuildEvent} called when a player teleports to their guild's home
 * location.
 *
 * @since 0.1.0
 */
public class GuildHomeTeleportEvent extends GuildEvent
        implements RolecraftCancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link Player} setting the guild home.
     */
    @Nonnull
    private final Player teleporter;
    /**
     * The new {@link Location} for the guild home.
     */
    @Nonnull
    private final Location homeLocation;

    /**
     * Whether the event is cancelled.
     */
    private boolean cancelled;
    /**
     * The message to send to the involved party(s) if he event is cancelled.
     */
    private String cancelMessage;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param guild the affected {@link Guild}
     * @param teleporter the teleporting {@link Player}
     * @param homeLocation the home {@link Location} being teleported to
     * @since 0.1.0
     */
    public GuildHomeTeleportEvent(final RolecraftCore plugin,
            @Nonnull final Guild guild, @Nonnull final Player teleporter,
            @Nonnull final Location homeLocation) {
        super(plugin, guild);
        this.teleporter = teleporter;
        this.homeLocation = homeLocation;

        this.cancelMessage = plugin.getMessage(Messages.NOT_ALLOWED);
    }

    /**
     * Gets the {@link Player} who is teleporting to their guild's home.
     *
     * @return the teleporting player
     * @since 0.1.0
     */
    @Nonnull
    public Player getPlayer() {
        return teleporter;
    }

    /**
     * Gets the {@link Location} of the guild home being teleported to.
     *
     * @return the new location of the teleporting player
     * @since 0.1.0
     */
    @Nonnull
    public Location getHomeLocation() {
        return homeLocation;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Nonnull @Override
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setCancelMessage(@Nonnull String message) {
        this.cancelMessage = message;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
