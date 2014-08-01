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
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} called when a {@link GuildRank} is modified - i.e when
 * a {@link GuildAction} is allowed or disallowed within the {@link GuildRank}
 */
public class GuildRankModifyEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link GuildRank} which has been modified
     */
    private final GuildRank rank;
    /**
     * The {@link GuildAction} being allowed or disallowed in this event
     */
    private final GuildAction perm;
    /**
     * Whether the {@link GuildAction} is being allowed or disallowed (true for
     * allowed, false for disallowed)
     */
    private final boolean value;

    public GuildRankModifyEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank, final GuildAction perm, final boolean value) {
        super(plugin, guild);
        this.rank = rank;
        this.perm = perm;
        this.value = value;
    }

    /**
     * Gets the {@link GuildRank} which has been modified
     *
     * @return The {@link GuildRank} which has been modified
     */
    public GuildRank getRank() {
        return rank;
    }

    /**
     * Gets the permissible {@link GuildAction} for which the permission value
     * is being set
     *
     * @return The {@link GuildAction} being allowed or disallowed
     */
    public GuildAction getPermission() {
        return perm;
    }

    /**
     * Gets whether the {@link GuildAction} is being allowed or disallowed
     *
     * @return True if the action is being allowed, false if it is being
     * disallowed
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
