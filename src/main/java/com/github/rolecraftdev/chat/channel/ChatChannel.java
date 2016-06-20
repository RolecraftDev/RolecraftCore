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
package com.github.rolecraftdev.chat.channel;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a Rolecraft chat channel. Rolecraft chat channels are almost
 * entirely configurable and can be used to create all sorts of RPG-style chat
 * systems.
 *
 * @since 0.1.0
 */
public class ChatChannel {
    /**
     * The internal id of the chat channel.
     */
    private final int id;
    /**
     * The user-readable name of the chat channel.
     */
    @Nonnull
    private String name;
    /**
     * Whether players joining the server are added to the channel by default.
     */
    private boolean def;
    /**
     * Whether the channel is a moderator channel (a moderator channel requires
     * the rolecraft.channels.mod permission to enter).
     */
    private boolean mod;
    /**
     * Whether the channel is an administrator channel (an administrator channel
     * requires the rolecraft.channels.admin permission to enter).
     */
    private boolean admin;
    /**
     * The name of the guild which owns this chat channel, or {@code null} if
     * this channel is not guild-owned.
     */
    @Nullable
    private String guild;
    /**
     * The color in which chat in this channel should be displayed.
     */
    @Nullable
    private ChatColor color;
    /**
     * The range (in blocks) of this channel.
     */
    private int range;

    /**
     * Constructs a new Rolecraft chat channel with the given parameters.
     *
     * @param id the internal id of the chat channel
     * @param name the user-readable name of the chat channel
     * @param mod whether the channel is a moderator channel
     * @param admin whether the channel is an administrator channel
     * @param guild the name of the guild which owns this channel, if applicable
     * @param color the color in which chat in this channel should be displayed
     * @param range the range (in blocks) of this channel
     * @since 0.1.0
     */
    public ChatChannel(final int id, @Nonnull final String name,
            final boolean def, final boolean mod, final boolean admin,
            @Nullable final String guild, @Nullable final ChatColor color,
            final int range) {
        this.id = id;
        this.name = name;
        this.def = def;
        this.mod = mod;
        this.admin = admin;
        this.guild = guild;
        this.color = color;
        this.range = range;
    }

    /**
     * Gets the internal id of the chat channel.
     *
     * @return the channel's internal id
     * @since 0.1.0
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the chat channel.
     *
     * @return the channel's name
     * @since 0.1.0
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Checks if the chat channel is a default channel.
     *
     * @return whether the channel is a default channel
     * @since 0.1.0
     */
    public boolean isDefault() {
        return def;
    }

    /**
     * Checks if the chat channel is a moderator-only channel.
     *
     * @return if the chat channel is moderator-only
     * @since 0.1.0
     */
    public boolean isMod() {
        return mod;
    }

    /**
     * Checks if the chat channel is an administrator-only channel.
     *
     * @return if the chat channel is administrator-only
     * @since 0.1.0
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Gets the name of the guild which owns the chat channel, if this chat
     * channel is owned by a guild. If it is not owned by a guild, this method
     * returns {@code null}.
     *
     * @return the name of the guild which owns the channel
     * @since 0.1.0
     */
    @Nullable
    public String getGuild() {
        return guild;
    }

    /**
     * Gets the color in which message in this channel are displayed.
     *
     * @return the color for this channel's messages
     * @since 0.1.0
     */
    @Nullable
    public ChatColor getColor() {
        return color;
    }

    /**
     * Gets the range (in blocks) of messages in this channel.
     *
     * @return this channel's chat range (in blocks)
     * @since 0.1.0
     */
    public int getRange() {
        return range;
    }

    /**
     * Sets the name of the chat channel.
     *
     * @param name the channel's new name
     * @since 0.1.0
     */
    public void setName(@Nonnull final String name) {
        this.name = name;
    }

    /**
     * Sets whether the channel is a default channel.
     *
     * @param def whether the channel is default
     * @since 0.1.0
     */
    public void setDefault(final boolean def) {
        this.def = def;
    }

    /**
     * Sets whether the chat channel is a moderator-only channel.
     *
     * @param mod whether the chat channel should be moderator-only
     * @since 0.1.0
     */
    public void setMod(final boolean mod) {
        this.mod = mod;
    }

    /**
     * Sets whether the chat channel is an administrator-only channel.
     *
     * @param admin whether the chat channel should be administrator-only
     * @since 0.1.0
     */
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    /**
     * Sets the color to display messages in in this chat channel.
     *
     * @param color the new color for this channel
     * @since 0.1.0
     */
    public void setColor(@Nullable final ChatColor color) {
        this.color = color;
    }

    /**
     * Sets the range of this chat channel's messages (in blocks).
     *
     * @param range the new range of messages in this channel
     * @since 0.1.0
     */
    public void setRange(final int range) {
        this.range = range;
    }
}
