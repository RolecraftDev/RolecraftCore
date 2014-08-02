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

import com.github.rolecraftdev.data.Region2D;
import com.github.rolecraftdev.event.guild.GuildPlayerJoinEvent;
import com.github.rolecraftdev.event.guild.GuildPlayerLeaveEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a {@link Guild} in Rolecraft. {@link Guild}s are player-creatable
 * and provide lots of functionality.
 */
public final class Guild {
    /**
     * The Leader {@link GuildRank}, which is present whenever a new
     * {@link Guild} is created, and thus cannot be removed.
     */
    private final GuildRank LEADER = new GuildRank("Leader",
            EnumSet.allOf(GuildAction.class), new HashSet<UUID>());
    /**
     * The Default {@link GuildRank}, which is present whenever a new
     * {@link Guild} is created, and thus cannot be removed.
     */
    private final GuildRank DEFAULT = new GuildRank("Default",
            new HashSet<GuildAction>(), new HashSet<UUID>());

    /**
     * The {@link GuildManager} object this {@link Guild} is registered to.
     */
    private final GuildManager guildManager;
    /**
     * A unique identifier that refers to this {@link Guild}.
     */
    private final UUID guildId;
    /**
     * A {@link Set} containing all of this {@link Guild}'s members' unique
     * identifiers, which includes its leader.
     */
    private final Set<UUID> members;
    /**
     * A {@link Set} containing all of the {@link GuildRank}s available in this
     * {@link Guild}, as configured by the leader.
     */
    private final Set<GuildRank> ranks;

    /**
     * The unique name of this {@link Guild}.
     */
    private String name;
    /**
     * The unique identifier of the player who leads this {@link Guild}.
     */
    private UUID leader;
    /**
     * The home point of this {@link Guild}. Used for teleporting.
     */
    private Location home;
    /**
     * The current influence level of this {@link Guild}, which depends on the
     * influence levels of all the members combined.
     */
    private int influence;
    /**
     * A {@link Region2D} object containing the hall of this {@link Guild}.
     */
    private Region2D hallRegion;

    /**
     * Creates a new {@link Guild} object linked to the given
     * {@link GuildManager}.
     *
     * @param guildManager - The {@link GuildManager}, this {@link Guild} is
     *                     contained by.
     */
    public Guild(final GuildManager guildManager) {
        this.guildManager = guildManager;
        guildId = UUID.randomUUID();
        members = new HashSet<UUID>();
        ranks = new HashSet<GuildRank>();

        ranks.add(LEADER);
        ranks.add(DEFAULT);
    }

    /**
     * Creates a new {@link Guild} object linked to the given
     * {@link GuildManager}, which is based on the given data.
     *
     * @param guildManager - The {@link GuildManager} this {@link Guild} belongs
     *                     to
     * @param guildId      - The unique identifier
     * @param name         - The unique name
     * @param leader       - The unique identifier of the leader
     * @param members      - A {@link Set} of all of the members
     * @param ranks        - A {@link Set} of all of the available {@link GuildRank}s
     * @param home         - The {@link Location} the home point
     * @param influence    - The current influence
     * @param hallRegion   - The {@link Region2D} that resembles the hall
     */
    public Guild(final GuildManager guildManager, final UUID guildId,
            final String name, final UUID leader, final Set<UUID> members,
            final Set<GuildRank> ranks, final Location home,
            final int influence, final Region2D hallRegion) {
        this.guildManager = guildManager;
        this.guildId = guildId;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.ranks = ranks;
        this.home = home;
        this.influence = influence;
        this.hallRegion = hallRegion;
    }

    /**
     * Checks whether the player with the given unique identifier is allowed to
     * perform the given {@link GuildAction} within this {@link Guild}.
     *
     * @param player - The player to check the permissions of
     * @param action - The {@link GuildAction} to check whether the given player
     *               can perform
     * @return True if the specified player can perform the given
     * {@link GuildAction} and false otherwise
     */
    public boolean can(final UUID player, final GuildAction action) {
        for (final GuildRank rank : ranks) {
            if (rank.hasPlayer(player) && rank.can(action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the player with the given unique identifier is a member of
     * this {@link Guild}.
     *
     * @param player - The player whose membership of this {@link Guild} will be
     *               checked
     * @return True if {@link #getMembers()} contains the given unique
     * identifier, else false
     */
    public boolean isMember(final UUID player) {
        return members.contains(player);
    }

    /**
     * Get a {@link Set} of all of the {@link GuildRank}s the given player has
     * within this {@link Guild}. Note that this method will return null if the
     * specified player has no {@link GuildRank}s in this {@link Guild}.
     *
     * @param player - The player to get the {@link GuildRank}s within this
     *               {@link Guild} for
     * @return A {@link Set} of ranks the player has in this {@link Guild}, or
     * null if no {@link GuildRank}s are found
     */
    public Set<GuildRank> getPlayerRanks(final UUID player) {
        final Set<GuildRank> result = new HashSet<GuildRank>();
        for (final GuildRank rank : ranks) {
            if (rank.hasPlayer(player)) {
                result.add(rank);
            }
        }
        return result.isEmpty() ? null : result;
    }

    /**
     * Get the current influence of this {@link Guild} without calculating the
     * up-to-date value.
     *
     * @return The {@link Guild}'s last calculated influence value
     */
    public int getInfluence() {
        return influence;
    }

    /**
     * Teleport the given {@link Entity} to the home {@link Location} of this
     * {@link Guild}.
     *
     * @param entity - The {@link Entity} to teleport
     */
    public void teleportToHome(final Entity entity) {
        entity.teleport(home);
    }

    /**
     * Get the unique identifier for this {@link Guild}.
     *
     * @return Its unique identifier
     */
    public UUID getId() {
        return guildId;
    }

    /**
     * Get the name of this {@link Guild}, which should be unique. Despite the
     * fact
     * that guild names should be unique, they can change, and thus should not
     * be used for comparisons of guilds. For correct equivalence checks, simply
     * use the overridden {@link #equals(Object)} method.
     *
     * @return Its unique name
     * @see #equals(Object)
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the home {@link Location} for this {@link Guild}, or null if it
     * hasn't been set.
     *
     * @return This {@link Guild}'s home {@link Location}, which can be unset
     * and thus null
     */
    public Location getHomeLocation() {
        return home;
    }

    /**
     * Get the unique identifier of the leader of this {@link Guild}.
     *
     * @return The leader's unique identifier
     */
    public UUID getLeader() {
        return leader;
    }

    /**
     * Get a copy of the {@link Set} used to hold the members of this
     * {@link Guild}.
     *
     * @return A copy of the original {@link Set} that contains the members of
     * this {@link Guild}
     */
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    /**
     * Gets a copy of the {@link Set} used to hold the {@link GuildRank}s
     * available in this {@link Guild}.
     *
     * @return A copy of the original {@link Set} that contains the obtainable
     * {@link GuildRank}s in this {@link Guild}
     */
    public Set<GuildRank> getRanks() {
        return new HashSet<GuildRank>(ranks);
    }

    /**
     * Gets the {@link GuildRank} within this {@link Guild} with the provided
     * name.
     *
     * @param name - The name of the wanted {@link GuildRank}
     * @return The applicable {@link GuildRank}, or null if none is found
     */
    public GuildRank getRank(final String name) {
        for (final GuildRank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    /**
     * Gets the {@link Region2D} containing the hall of this {@link Guild}, or
     * null if this {@link Guild} doesn't have one registered.
     *
     * @return The {@link Region2D} that represents its hall, which could be
     * null
     */
    public Region2D getGuildHallRegion() {
        return hallRegion;
    }

    /**
     * Set the unique name of this {@link Guild}.
     *
     * @param name - The new unique name
     */
    public void setName(final String name) {
        this.name = name;
        guildManager.getPlugin().getDataStore().updateGuildData(this);
    }

    /**
     * Set the leader of this {@link Guild}. If this {@link Guild} already has a
     * leader, the current leader is demoted to a member. If the given new
     * leader isn't already a member of this {@link Guild}, (s)he is added
     * automatically.
     *
     * @param leader - The unique identifier of the new leader
     */
    public void setLeader(final UUID leader) {
        if (this.leader != null) {
            LEADER.removeMember(this.leader);
            LEADER.addMember(this.leader);
        }
        if (!members.contains(leader)) {
            members.add(leader);
        }

        this.leader = leader;
        LEADER.addMember(leader);
        guildManager.getPlugin().getDataStore().updateGuildData(this);
    }

    /**
     * Adds the given member to this {@link Guild}, with the specified
     * {@link GuildRank}.
     *
     * @param member - The unique identifier of the player to add
     * @param rank   - The rank the specified player will have
     */
    public void addMember(final UUID member, final GuildRank rank) {
        Bukkit.getPluginManager().callEvent(new GuildPlayerJoinEvent(
                guildManager.getPlugin(), this, Bukkit.getPlayer(member),
                rank));

        members.add(member);
        rank.addMember(member);
        guildManager.getPlugin().getDataStore().addPlayerToGuild(member, this);
        guildManager.getPlugin().getDataStore().updateGuildRanks(this);
    }

    /**
     * Removes the given member from this {@link Guild}
     *
     * @param member - The unique identifier of the player to remove
     */
    public void removeMember(final UUID member) {
        Bukkit.getPluginManager().callEvent(new GuildPlayerLeaveEvent(
                guildManager.getPlugin(), this, Bukkit.getPlayer(member)));

        boolean removed = members.remove(member);
        if (!removed) {
            throw new IllegalArgumentException();
        }
        for (final GuildRank rank : getPlayerRanks(member)) {
            rank.removeMember(member);
        }
        guildManager.getPlugin().getDataStore()
                .removePlayerFromGuild(member, this);
        guildManager.getPlugin().getDataStore().updateGuildRanks(this);
    }

    /**
     * Adds the given {@link GuildRank} to this {@link Guild}.
     *
     * @param rank - The {@link GuildRank} to add
     */
    public boolean addRank(final GuildRank rank) {
        boolean retVal = getRank(rank.getName()) == null && ranks.add(rank);
        guildManager.getPlugin().getDataStore().updateGuildRanks(this);
        return retVal;
    }

    /**
     * Attempts to remove the given {@link GuildRank} from this {@link Guild}
     *
     * @param rank - The {@link GuildRank} to remove
     * @return True if the rank was removed, false otherwise
     */
    public boolean removeRank(final GuildRank rank) {
        final String name = rank.getName().toLowerCase();
        boolean retVal =
                !(name.equals("leader") || name.equals("default")) && ranks
                        .remove(rank);
        guildManager.getPlugin().getDataStore().updateGuildRanks(this);
        return retVal;
    }

    /**
     * Set the home {@link Location} of this {@link Guild} to the stated
     * {@link Location}.
     *
     * @param home - The new home {@link Location}
     */
    public void setHomeLocation(final Location home) {
        this.home = home;
        guildManager.getPlugin().getDataStore().updateGuildData(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Guild)) {
            return false;
        }
        final Guild other = (Guild) o;
        return guildId.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return guildId.hashCode();
    }

    /**
     * Set the given {@link Region2D} as the hall of this {@link Guild}. This
     * method should only ever be called from {@link GuildManager} when hall is
     * claimed, hence the package-private access modifier is utilised.
     *
     * @param hallRegion - The new hall
     */
    void claimAsGuildHall(final Region2D hallRegion) {
        this.hallRegion = hallRegion;
        guildManager.getPlugin().getDataStore().updateGuildData(this);
    }
}
