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

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player rank within a specific {@link Guild}. {@link GuildRank}s
 * can be created by the {@link Guild}'s leader and can have different
 * permissions.
 */
public final class GuildRank {
    /**
     * The name of this {@link GuildRank}.
     */
    private final String name;
    /**
     * The actions within the {@link Guild}, which members of this
     * {@link GuildRank} are allowed to perform.
     */
    private final Set<GuildAction> permitted;
    /**
     * All of the members of the {@link Guild} this {@link GuildRank} is
     * included in who are in this {@link GuildRank}.
     */
    private final Set<UUID> members;

    /**
     * Creates a new {@link GuildRank} using the given parameters.
     *
     * @param name the name of the new {@link GuildRank}
     * @param permitted the {@link GuildAction}s, the {@link GuildRank}'s
     *                  members can perform
     * @param members the members of the new {@link GuildRank}
     */
    public GuildRank(final String name, final Set<GuildAction> permitted,
            final Set<UUID> members) {
        this.name = name;
        this.permitted = permitted;
        this.members = members;
    }

    /**
     * Get the name of this {@link GuildRank}.
     *
     * @return the name of this {@link GuildRank}
     */
    public String getName() {
        return name;
    }

    /**
     * Get a copy of the {@link Set} used to hold the {@link GuildAction}s, that
     * members of this {@link GuildRank} are allowed to perform.
     *
     * @return a copy of the {@link Set} containing {@link GuildAction}s, that
     * members of this {@link GuildRank} can perform
     */
    public Set<GuildAction> getPermittedActions() {
        return EnumSet.copyOf(permitted);
    }

    /**
     * Get a copy of the {@link Set} used to hold the members of the
     * {@link Guild} who are a part of this {@link GuildRank}.
     *
     * @return a copy of the {@link Set} of members of the {@link Guild} who are
     * part of this {@link GuildRank}
     */
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    /**
     * Checks whether the given player is a part of this {@link GuildRank}.
     *
     * @param player the player whose membership of this {@link GuildRank} is
     *               in question
     * @return {@code true} if this {@link GuildRank} contains the given player,
     *         otherwise {@code false}
     */
    public boolean hasPlayer(final UUID player) {
        Validate.notNull(player);
        return members.contains(player);
    }

    /**
     * Checks whether the given {@link GuildAction} can be performed by a member
     * of this {@link GuildRank} - i.e whether it is included in the {@link Set}
     * returned by {@link #getPermittedActions()}.
     *
     * @param action the {@link GuildAction} whose inclusion in the permitted
     *               {@link GuildAction}s of this {@link GuildRank} is being
     *               checked
     * @return {@code true} if it is included, else {@code false}
     */
    public boolean can(final GuildAction action) {
        Validate.notNull(action);
        return permitted.contains(action);
    }

    /**
     * Sends the given message to every currently online {@link Player} who is
     * part of this GuildRank
     *
     * @param message the message to broadcast within this GuildRank
     */
    public void broadcastMessage(final String message) {
        Validate.notNull(message);
        for (final UUID playerId : members) {
            final Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Adds the given player as a member of this {@link GuildRank}.
     *
     * @param member the unique identifier of the player to add to this {@link
     *               GuildRank}
     */
    public void addMember(final UUID member) {
        Validate.notNull(member);
        members.add(member);
    }

    /**
     * Removes the given player from being a member of this {@link GuildRank}.
     *
     * @param member the unique identifier of the player to remove from this
     *               {@link GuildRank}
     */
    public void removeMember(final UUID member) {
        Validate.notNull(member);
        members.remove(member);
    }

    /**
     * Adds the given {@link GuildAction} to this {@link GuildRank}'s permitted
     * actions
     *
     * @param perm the permission to grant this {@link GuildRank}
     */
    public void allowAction(final GuildAction perm) {
        Validate.notNull(perm);
        permitted.add(perm);
    }

    /**
     * Removes the given {@link GuildAction} from this {@link GuildRank}'s
     * permitted actions
     *
     * @param perm the permission to disallow this {@link GuildRank}
     */
    public void disallowAction(final GuildAction perm) {
        Validate.notNull(perm);
        permitted.remove(perm);
    }

    /**
     * Creates a {@link String} for storage in SQL, which does not use commas.
     *
     * @return a serialized version of this {@link GuildRank}
     */
    public String serialize() {
        StringBuilder res = new StringBuilder();
        res.append(name);
        res.append(":");
        for (GuildAction action : permitted) {
            res.append(action.ordinal());
            res.append("#");
        }
        res = new StringBuilder(res.substring(0, res.length() - 1));
        res.append(":");
        for (UUID id : members) {
            res.append(id.toString());
            res.append("#");
        }
        return res.substring(0, res.length() - 1);
    }

    /**
     * Deserializes the given {@link String} into a {@link GuildRank} object,
     * assuming it is a valid serialized {@link GuildRank}. It can be built in a
     * separate thread and passed into the main one, assuming the other thread
     * destroys all references.
     *
     * @param serialized the String to deserialize into a {@link GuildRank} object
     * @return the final deserialized {@link GuildRank}
     */
    public static GuildRank deserialize(String serialized) {
        String[] data = serialized.split(":");
        Set<GuildAction> actions = new HashSet<GuildAction>();
        Set<UUID> members = new HashSet<UUID>();
        for (String action : data[1].split("#")) {
            int actionValue = Integer.parseInt(action);
            actions.add(GuildAction.values()[actionValue]);
        }
        for (String member : data[2].split("#")) {
            if (!member.equals("")) {
                members.add(UUID.fromString(member));
            }
        }

        return new GuildRank(data[0], actions, members);
    }
}
