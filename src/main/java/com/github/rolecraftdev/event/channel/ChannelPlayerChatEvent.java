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
package com.github.rolecraftdev.event.channel;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.chat.channel.ChatChannel;
import com.github.rolecraftdev.event.RolecraftCancellable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called when a {@link Player} sends a message in a {@link ChatChannel}.
 *
 * @since 0.1.0
 */
public class ChannelPlayerChatEvent extends ChannelPlayerEvent
        implements RolecraftCancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The sent chat message.
     */
    private String message;
    /**
     * Whether the event is cancelled.
     */
    private boolean cancelled = false;
    /**
     * The message to be sent to the involved player if the event is cancelled.
     */
    private String cancelMessage;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param channel the involved {@link ChatChannel}
     * @param player the involed {@link Player}
     * @since 0.1.0
     */
    public ChannelPlayerChatEvent(@Nonnull final RolecraftCore plugin,
            @Nonnull final ChatChannel channel, @Nonnull final Player player,
            @Nonnull final String message) {
        super(plugin, channel, player);

        this.message = message;
        this.cancelMessage = plugin.getMessage(Messages.NOT_ALLOWED);
    }

    /**
     * Gets the chat message being sent.
     *
     * @return the involved chat message
     * @since 0.1.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the chat message being sent.
     *
     * @param message the new message to be sent
     * @since 0.1.0
     */
    public void setMessage(final String message) {
        this.message = message;
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

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 0.1.0
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
