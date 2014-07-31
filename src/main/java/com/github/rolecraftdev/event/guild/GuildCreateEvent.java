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
import com.github.rolecraftdev.guild.Guild;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * An event called upon the creation of a new {@link Guild} in Rolecraft
 */
public class GuildCreateEvent extends GuildEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Whether the creation of the guild is cancelled
     */
    private boolean cancelled;
    /**
     * The message to send to the founder if the event is cancelled
     */
    private String cancelMessage = "You can't create a guild right now!";

    public GuildCreateEvent(final RolecraftCore plugin, final Guild guild) {
        super(plugin, guild);
    }

    /**
     * Gets the {@link Player} who founded the new {@link Guild}
     *
     * @return The {@link Player} who founded the new {@link Guild}
     */
    public Player getFounder() {
        return getPlugin().getServer().getPlayer(getGuild().getLeader());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Gets the message which will be sent to the founder if the event is
     * cancelled
     *
     * @return The message to send to the founder if the event is cancelled
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Sets the message to send to the founder of the new {@link Guild} if the
     * event is cancelled
     *
     * @param cancelMessage The new message to send to the founder if the event
     *                      is cancelled
     */
    public void setCancelMessage(final String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
