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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} that is called when a player is about to join a
 * {@link Guild}.
 *
 * @since 0.0.5
 */
public class GuildPlayerJoinEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link Player} joining the guild
     */
    private final Player player;
    /**
     * The {@link GuildRank} the new {@link Player} has been allocated to
     */
    private final GuildRank rank;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param guild the affected {@link Guild}
     * @param player the player who is about to join
     * @param rank the {@link GuildRank} of the player
     * @since 0.0.5
     */
    public GuildPlayerJoinEvent(final RolecraftCore plugin, final Guild guild,
            final Player player, final GuildRank rank) {
        super(plugin, guild);
        this.player = player;
        this.rank = rank;
    }

    /**
     * Get the player who is about to join the {@link Guild}.
     *
     * @return the joining player
     * @since 0.0.5
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the {@link GuildRank} of the player who is about to join the
     * {@link Guild}.
     *
     * @return the {@link GuildRank} of the player
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
