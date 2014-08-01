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
     * @param name      - The name of the new {@link GuildRank}
     * @param permitted - The {@link GuildAction}s, the {@link GuildRank}'s
     *                  members can perform
     * @param members   - The members of the new {@link GuildRank}
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
     * @return The name of this {@link GuildRank}
     */
    public String getName() {
        return name;
    }

    /**
     * Get a copy of the {@link Set} used to hold the {@link GuildAction}s, that
     * members of this {@link GuildRank} are allowed to perform.
     *
     * @return A copy of the {@link Set} containing {@link GuildAction}s, that
     * members of this {@link GuildRank} can perform
     */
    public Set<GuildAction> getPermittedActions() {
        return new HashSet<GuildAction>(permitted);
    }

    /**
     * Get a copy of the {@link Set} used to hold the members of the
     * {@link Guild} who are a part of this {@link GuildRank}.
     *
     * @return A copy of the {@link Set} of members of the {@link Guild} who are
     * part of this {@link GuildRank}
     */
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    /**
     * Checks whether the given player is a part of this {@link GuildRank}.
     *
     * @param player - The player whose membership of this {@link GuildRank} is
     *               in question
     * @return True if this {@link GuildRank} contains the given player,
     * otherwise false
     */
    public boolean hasPlayer(final UUID player) {
        return members.contains(player);
    }

    /**
     * Checks whether the given {@link GuildAction} can be performed by a member
     * of this {@link GuildRank} - i.e whether it is included in the {@link Set}
     * returned by {@link #getPermittedActions()}.
     *
     * @param action - The {@link GuildAction} whose inclusion in the permitted
     *               {@link GuildAction}s of this {@link GuildRank} is being
     *               checked
     * @return True if it is included, else false
     */
    public boolean can(final GuildAction action) {
        return permitted.contains(action);
    }

    /**
     * Adds the given player as a member of this {@link GuildRank}.
     *
     * @param member - The unique identifier of the player to add to this
     *               {@link GuildRank}
     */
    public void addMember(final UUID member) {
        members.add(member);
    }

    /**
     * Removes the given player from being a member of this {@link GuildRank}.
     *
     * @param member - The unique identifier of the player to remove from this
     *               {@link GuildRank}
     */
    public void removeMember(final UUID member) {
        members.remove(member);
    }

    /**
     * Adds the given {@link GuildAction} to this {@link GuildRank}'s permitted
     * actions
     *
     * @param perm - The permission to grant this {@link GuildRank}
     */
    public void allowAction(final GuildAction perm) {
        permitted.add(perm);
    }

    /**
     * Removes the given {@link GuildAction} from this {@link GuildRank}'s
     * permitted actions
     *
     * @param perm - The permission to disallow this {@link GuildRank}
     */
    public void disallowAction(final GuildAction perm) {
        permitted.remove(perm);
    }

    /**
     * Creates a {@link String} for storage in SQL, which does not use commas.
     *
     * @return A serialized version of this {@link GuildRank}
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":");
        for (GuildAction action : permitted) {
            sb.append(action.ordinal());
            sb.append("#");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        sb.append(":");
        for (UUID id : members) {
            sb.append(id.toString());
            sb.append("#");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Deserializes the given {@link String} into a {@link GuildRank} object,
     * assuming it is a valid serialized {@link GuildRank}. It can be built in a
     * separate thread and passed into the main one, assuming the otherold thread
     * destroys all references.
     *
     * @param s - The String to deserialize into a {@link GuildRank} object
     * @return The final deserialized {@link GuildRank}
     */
    public static GuildRank deserialize(String s) {
        String[] data = s.split(":");
        Set<GuildAction> actions = new HashSet<GuildAction>();
        Set<UUID> members = new HashSet<UUID>();
        for (String action : data[1].split("#")) {
            int actionValue = Integer.parseInt(action);
            actions.add(GuildAction.values()[actionValue]);
        }
        for (String member : data[2].split("#")) {
            if (!member.equals("") && member != null) {
                members.add(UUID.fromString(member));
            }
        }

        return new GuildRank(data[0], actions, members);
    }
}
