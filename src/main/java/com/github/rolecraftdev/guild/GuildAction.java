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

import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * A representation of all of the {@link GuildAction}s which can be performed
 * and are related to {@link Guild}s in one way or another.
 *
 * @since 0.0.5
 */
public enum GuildAction {
    /**
     * Permission to kick a player from his {@link Guild}.
     *
     * @since 0.0.5
     */
    KICK_MEMBER("kick"),
    /**
     * Permission to invite a player to his {@link Guild}.
     *
     * @since 0.0.5
     */
    INVITE("invite"),
    /**
     * Set the home {@link Location} of his {@link Guild}.
     *
     * @since 0.0.5
     */
    SET_HOME("set-home"),
    /**
     * Modify his guild-hall.
     *
     * @since 0.0.5
     */
    CHANGE_BLOCK("modify-hall"),
    /**
     * Ignite blocks in his guild-hall.
     *
     * @since 0.0.5
     */
    IGNITE_BLOCK("ignite-hall-block"),
    /**
     * Assign a member a {@link GuildRank}.
     *
     * @since 0.1.0
     */
    ASSIGN_RANKS("assign-ranks"),
    /**
     * Send messages to all members of his {@link Guild}.
     *
     * @since 0.0.5
     */
    BROADCAST_MESSAGE("broadcast-message"),
    /**
     * Set whether or not the guild is an open guild.
     *
     * @since 0.1.0
     */
    SET_OPEN("set-open");

    /**
     * The human-readable version of the name of this {@link GuildAction}.
     */
    @Nonnull
    private final String humanReadable;

    /**
     * @since 0.0.5
     */
    GuildAction(@Nonnull final String humanReadable) {
        this.humanReadable = humanReadable;
        GuildManager.actionMap.put(humanReadable, this);
    }

    /**
     * Get the human-readable version of the name of this {@link GuildAction}.
     *
     * @return the human-readable name
     * @since 0.0.5
     */
    @Nonnull
    public String getHumanReadableName() {
        return humanReadable;
    }

    /**
     * Check whether the given player is allowed to perform this
     * {@link GuildAction} in the specified {@link Guild}.
     *
     * @param player the {@link UUID} of the player to investigate
     * @param guild the {@link Guild} to check the permissions in
     * @return {@code true} if the player is able to perform this
     *         {@link GuildAction}
     * @since 0.0.5
     * @see Guild#can(UUID, GuildAction)
     */
    public boolean can(@Nonnull final UUID player, @Nonnull final Guild guild) {
        return guild.can(player, this);
    }

    /**
     * Retrieve a {@link GuildAction} from a human-readable name.
     *
     * @param humanReadable the human-readable name to retrieve the
     *        {@link GuildAction} from
     * @return the associated {@link GuildAction}
     * @since 0.0.5
     * @see GuildManager#fromHumanReadable(String)
     */
    public static GuildAction fromHumanReadable(final String humanReadable) {
        return GuildManager.fromHumanReadable(humanReadable);
    }
}
