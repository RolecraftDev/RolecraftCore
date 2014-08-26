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

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player rank within a {@link Guild}. {@link GuildRank}s have
 * certain permissions as configured by the leader of the {@link Guild} or
 * anyone else allowed to edit them.
 *
 * @since 0.0.5
 */
public final class GuildRank {
    /**
     * The name of this {@link GuildRank}.
     */
    @Nonnull
    private final String name;
    /**
     * The {@link GuildAction}s the players with this {@link GuildRank} are
     * allowed to perform.
     */
    @Nonnull
    private final Set<GuildAction> permitted;
    /**
     * All players who are a part of this {@link GuildRank}.
     */
    @Nonnull
    private final Set<UUID> members;

    /**
     * Constructor.
     *
     * @param name the name of the {@link GuildRank}
     * @param permitted the predefined permitted {@link GuildAction}s
     * @param members the predefined members of this {@link GuildRank}
     * @since 0.0.5
     */
    public GuildRank(@Nonnull final String name,
            @Nonnull final Set<GuildAction> permitted,
            @Nonnull final Set<UUID> members) {
        this.name = name;
        this.permitted = permitted;
        this.members = members;
    }

    /**
     * Get the name of this {@link GuildRank}.
     *
     * @return the name
     * @since 0.0.5
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Obtain the {@link GuildAction}s members of this {@link GuildRank} may
     * perform.
     *
     * @return the permitted {@link GuildAction}s
     * @since 0.0.5
     */
    @Nonnull
    public Set<GuildAction> getPermittedActions() {
        return EnumSet.copyOf(permitted);
    }

    /**
     * Get the players who are a part of this {@link GuildRank}.
     *
     * @return the members
     * @since 0.0.5
     */
    @Nonnull
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    /**
     * Check whether the given player is a member of this {@link GuildRank}.
     *
     * @param player the {@link UUID} of the player whose membership will be
     *        checked
     * @return only {@code true} if the player is indeed a member of this
     *         {@link GuildRank}
     * @since 0.0.5
     */
    public boolean hasPlayer(@Nonnull final UUID player) {
        Validate.notNull(player);
        return members.contains(player);
    }

    /**
     * Check whether the given {@link GuildAction} can be performed by members
     * of this {@link GuildRank}.
     *
     * @param action the {@link GuildAction} that should be checked
     * @return {@code true} if the given {@link GuildAction} can be performed;
     *         {@code false} otherwise
     * @since 0.0.5
     */
    public boolean can(@Nonnull final GuildAction action) {
        Validate.notNull(action);
        return permitted.contains(action);
    }

    /**
     * Sends the given message to all online players who are a member of this
     * {@link GuildRank}.
     *
     * @param message the message that should be sent to all members
     * @since 0.0.5
     */
    public void broadcastMessage(@Nonnull final String message) {
        Validate.notNull(message);
        for (final UUID playerId : members) {
            final Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Adds the given member to this {@link GuildRank}.
     *
     * @param member the {@link UUID} of the player to add
     * @since 0.0.5
     */
    public void addMember(@Nonnull final UUID member) {
        Validate.notNull(member);
        members.add(member);
    }

    /**
     * Remove the given player from this {@link GuildRank}.
     *
     * @param member the {@link UUID} of the player to remove
     * @since 0.0.5
     */
    public void removeMember(@Nonnull final UUID member) {
        Validate.notNull(member);
        members.remove(member);
    }

    /**
     * Add the given {@link GuildAction} to this {@link GuildRank}'s permitted
     * actions.
     *
     * @param perm the {@link GuildAction} that should be granted permissions to
     * @since 0.0.5
     */
    public void allowAction(@Nonnull final GuildAction perm) {
        Validate.notNull(perm);
        permitted.add(perm);
    }

    /**
     * Remove the given {@link GuildAction} from this {@link GuildRank}'s
     * permitted actions and thus refusing permissions to the specified
     * {@link GuildAction}.
     *
     * @param perm the {@link GuildAction} that should be refused permissions to
     * @since 0.0.5
     */
    public void disallowAction(@Nonnull final GuildAction perm) {
        Validate.notNull(perm);
        permitted.remove(perm);
    }

    /**
     * Serialise this {@link GuildRank}.
     *
     * @return the serialised version
     * @since 0.0.5
     */
    @Nonnull
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
     * Attempt to deserialise the given string. It is strongly recommended to
     * not build these strings yourself, but instead use {@link #serialize()}.
     *
     * @param serialized the string to deserialise
     * @return the deserialised version
     * @since 0.0.5
     */
    @Nonnull
    public static GuildRank deserialize(@Nonnull final String serialized) {
        Validate.notNull(serialized);

        final String[] data = serialized.split(":");
        final Set<GuildAction> actions = new HashSet<GuildAction>();
        final Set<UUID> members = new HashSet<UUID>();

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
