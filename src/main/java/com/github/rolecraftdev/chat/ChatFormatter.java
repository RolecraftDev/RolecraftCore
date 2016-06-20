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
package com.github.rolecraftdev.chat;

import com.github.rolecraftdev.chat.channel.ChatChannel;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Formats chat messages based on a configurable format.
 *
 * @since 0.1.0
 */
public final class ChatFormatter {
    /**
     * The chat format to be used by {@link #formatMessage(Player, String, String, ChatChannel, String)}
     */
    @Nonnull
    private String format;

    /**
     * Constructor.
     *
     * @param format the chat format for this formatter
     * @since 0.1.0
     */
    public ChatFormatter(@Nonnull final String format) {
        this.format = format;
    }

    /**
     * Formats the given message, using the given parameters.
     *
     * @param sender the sender of the message
     * @param prefix the prefix of the sender
     * @param suffix the suffix of the sender
     * @param channel the channel the message is to be sent to
     * @param message the raw message itself
     * @return the formatted message
     * @since 0.1.0
     */
    public String formatMessage(@Nonnull final Player sender,
            @Nullable final String prefix, @Nullable final String suffix,
            @Nonnull final ChatChannel channel, @Nonnull final String message) {
        String inProg = format;
        if (prefix != null && format.contains("prefix")) {
            inProg = inProg.replaceAll("prefix", prefix);
        }
        if (suffix != null && format.contains("suffix")) {
            inProg = inProg.replaceAll("suffix", suffix);
        }
        return inProg.replaceAll("player", sender.getDisplayName())
                .replaceAll("channel", channel.getColor() + channel.getName())
                .replaceAll("msg", channel.getColor() + message);
    }

    /**
     * Gets the chat format being used by this formatter.
     *
     * @return this formatters chat format
     * @since 0.1.0
     */
    @Nonnull
    public String getFormat() {
        return format;
    }

    /**
     * Sets the chat format being used by this formatter.
     *
     * @param format the new format for this formatter
     * @since 0.1.0
     */
    public void setFormat(@Nonnull final String format) {
        this.format = format;
    }
}
