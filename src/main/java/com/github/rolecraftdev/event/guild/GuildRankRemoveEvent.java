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
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} called after a {@link GuildRank} is removed from a
 * {@link Guild}.
 *
 * @since 0.0.5
 */
public class GuildRankRemoveEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The removed {@link GuildRank}.
     */
    private final GuildRank rank;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param guild the affected {@link Guild}
     * @param rank the {@link GuildRank} that has been removed from its
     *        {@link Guild}
     * @since 0.0.5
     */
    public GuildRankRemoveEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank) {
        super(plugin, guild);
        this.rank = rank;
    }

    /**
     * Get the {@link GuildRank} that has been removed from its {@link Guild}.
     *
     * @return the removed {@link GuildRank}
     * @since 0.0.5
     */
    public GuildRank getRank() {
        return rank;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 0.0.5
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
