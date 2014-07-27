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

import java.util.UUID;

/**
 * A representation of all of the {@link GuildAction}s which can be performed
 * and are related to {@link Guild}s in one way or another.
 */
public enum GuildAction {
    // TODO: More things might be added here
    KICK_MEMBER("kick"), INVITE("invite"), SET_HOME("set-home"), CHANGE_BLOCK(
            "modify-hall"), IGNITE_BLOCK("ignite-hall-block");

    /**
     * A player-readable version of the name of this {@link GuildAction}.
     */
    private final String playerReadable;

    GuildAction(final String playerReadable) {
        this.playerReadable = playerReadable;
        GuildManager.actionMap.put(playerReadable, this);
    }

    /**
     * Get a player-readable version of the name of this {@link GuildAction}.
     *
     * @return The player-readable version of this {@link GuildAction}'s name
     */
    public String getPlayerReadableName() {
        return playerReadable;
    }

    /**
     * Checks whether the given player is permitted to perform this
     * {@link GuildAction} within the given {@link Guild}. This invokes
     * {@link Guild#can(UUID, GuildAction)} and passes this {@link GuildAction}.
     *
     * @param player - The player to check the permissions of
     * @param guild  - The {@link Guild} to check the permissions of the given
     *               player in
     * @return True if the given player can perform this action and false in any
     * other case
     */
    public boolean can(final UUID player, final Guild guild) {
        return guild.can(player, this);
    }

    public static GuildAction fromHumanReadable(final String humanReadable) {
        return GuildManager.fromHumanReadable(humanReadable);
    }
}
