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
import javax.annotation.Nullable;

/**
 * A {@link GuildEvent} called when the home for a guild is set to a new
 * location.
 *
 * @since 0.1.0
 */
public class GuildHomeSetEvent extends GuildEvent
        implements RolecraftCancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link Player} setting the guild home.
     */
    @Nullable
    private final Player setter;
    /**
     * The new {@link Location} for the guild home.
     */
    @Nonnull
    private final Location newLocation;

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
     * @param setter the {@link Player} setting the new home
     * @param newLocation the new {@link Location} of the guild home
     * @since 0.1.0
     */
    public GuildHomeSetEvent(final RolecraftCore plugin,
            @Nonnull final Guild guild, final Player setter,
            final Location newLocation) {
        super(plugin, guild);
        this.setter = setter;
        this.newLocation = newLocation;

        this.cancelMessage = plugin.getMessage(Messages.NOT_ALLOWED);
    }

    /**
     * Gets the {@link Player} who is responsible for setting the new home of
     * the {@link Guild}.
     *
     * @return the player setting the new guild home
     * @since 0.1.0
     */
    @Nullable
    public Player getSetter() {
        return setter;
    }

    /**
     * Gets the new {@link Location} for the guild home.
     *
     * @return the new guild home location
     * @since 0.1.0
     */
    @Nonnull
    public Location getNewLocation() {
        return newLocation;
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
