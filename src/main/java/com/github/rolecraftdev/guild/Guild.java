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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.guild.GuildPlayerJoinEvent;
import com.github.rolecraftdev.util.Region2D;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player-creatable guild.
 *
 * @since 0.0.5
 */
public final class Guild {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * The {@link GuildManager} this {@link Guild} is registered to.
     */
    private final GuildManager guildManager;
    /**
     * The {@link UUID} of this {@link Guild}.
     */
    private final UUID guildId;
    /**
     * A {@link Set} containing all of this {@link Guild}'s members'
     * {@link UUID}s, including the one of its leader.
     */
    private final Set<UUID> members;
    /**
     * A {@link Set} containing all of the {@link GuildRank}s available in this
     * {@link Guild}, as configured by the leader.
     */
    private final Set<GuildRank> ranks;

    /**
     * The name of this {@link Guild}.
     */
    private String name;
    /**
     * The {@link UUID} of this {@link Guild}'s leader
     */
    private UUID leader;
    /**
     * The home point of this {@link Guild}, which is used for teleportation.
     * This may be {@code null} during normal execution if the guild doesn't
     * have a home {@link Location}.
     */
    private Location home;
    /**
     * The influence level of this {@link Guild}, which is fully dependent on
     * the influence levels of all its members combined.
     */
    private int influence;
    /**
     * The guild hall of this {@link Guild}. This may be {@code null} during
     * normal execution if this {@link Guild} doesn't have a guild hall.
     */
    private Region2D hallRegion;
    /**
     * Whether or not this {@link Guild} is open. If this is {@code true}, the
     * {@link Guild} is open, and can be joined <em>without invitation</em>. If
     * {@code false}, invitation is required to join the {@link Guild}.
     */
    private boolean open;

    /**
     * Create a new {@link Guild}, automatically generating a semi-random
     * {@link UUID}; and the leader and default {@link GuildRank}s. When the
     * given {@link GuildManager} is {@code null}, all fields will be assigned
     * {@code null}.
     *
     * @param guildManager the {@link GuildManager} this {@link Guild} will be
     *        registered to
     * @since 0.0.5
     */
    public Guild(final GuildManager guildManager) {
        if (guildManager == null) {
            this.guildManager = null;
            plugin = null;
            members = null;
            ranks = null;
            guildId = null;
            return;
        }

        plugin = guildManager.getPlugin();
        this.guildManager = guildManager;
        guildId = UUID.randomUUID();
        members = new HashSet<UUID>();
        ranks = new HashSet<GuildRank>();

        ranks.add(new GuildRank(plugin.getMessage(Messages.GUILD_LEADER_RANK),
                EnumSet.allOf(GuildAction.class), new HashSet<UUID>()));
        ranks.add(new GuildRank(plugin.getMessage(Messages.GUILD_DEFAULT_RANK),
                EnumSet.noneOf(GuildAction.class), new HashSet<UUID>()));
    }

    /**
     * Constructor.
     *
     * @param guildManager the {@link GuildManager} this {@link Guild} will be
     *        registered to
     * @param guildId the {@link Guild}'s {@link UUID}
     * @param name the {@link Guild}'s name
     * @param leader the {@link Guild}'s leader
     * @param members the {@link Guild}'s members
     * @param ranks the {@link Guild}'s ranks
     * @param home the {@link Guild}'s home {@link Location}
     * @param influence the members' combined influence
     * @param hallRegion the {@link Guild}'s hall
     * @param open whether this {@link Guild} is joinable
     * @since 0.0.5
     */
    public Guild(final GuildManager guildManager, final UUID guildId,
            final String name, final UUID leader, final Set<UUID> members,
            final Set<GuildRank> ranks, final Location home,
            final int influence, final Region2D hallRegion,
            final boolean open) {
        plugin = guildManager.getPlugin();
        this.guildManager = guildManager;
        this.guildId = guildId;
        this.name = name;
        this.leader = leader;
        this.members = members;
        this.ranks = ranks;
        this.home = home;
        this.influence = influence;
        this.hallRegion = hallRegion;
        this.open = open;
    }

    /**
     * Get the {@link GuildManager} this {@link Guild} is supposed to be
     * registered to.
     *
     * @return the {@link GuildManager} this {@link Guild} is registered to
     * @since 0.0.5
     */
    @Nullable
    public GuildManager getManager() {
        return guildManager;
    }

    /**
     * Check whether the player is allowed to perform the given
     * {@link GuildAction} within this {@link Guild}.
     *
     * @param player the {@link UUID} of the player to check the permissions of
     * @param action the {@link GuildAction} that should be tested
     * @return {@code true} if the given player has permissions to execute the
     *         specified {@link GuildAction}; {@code false} otherwise
     * @since 0.0.5
     */
    public boolean can(@Nonnull final UUID player,
            @Nonnull final GuildAction action) {
        Validate.notNull(player);
        Validate.notNull(action);

        for (final GuildRank rank : ranks) {
            if (rank.hasPlayer(player) && rank.can(action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given player is a member of this {@link Guild}.
     *
     * @param player the {@link UUID} of the player whose membership will be
     *        checked
     * @return only {@code true} if the player is indeed a member of this
     *         {@link Guild}
     * @since 0.0.5
     */
    public boolean isMember(@Nonnull final UUID player) {
        Validate.notNull(player);
        return members.contains(player);
    }

    /**
     * Get all the {@link GuildRank}s the given player has within this
     * {@link Guild}. Note that this method will return {@code null} if the
     * specified player has zero {@link GuildRank}s in this {@link Guild}.
     *
     * @param player the {@link UUID} of the player the {@link GuildRank}s
     *        should be returned of
     * @return all {@link GuildRank}s the given player has
     * @since 0.0.5
     */
    @Nullable
    public Set<GuildRank> getPlayerRanks(@Nonnull final UUID player) {
        final Set<GuildRank> result = new HashSet<GuildRank>();
        for (final GuildRank rank : ranks) {
            if (rank.hasPlayer(player)) {
                result.add(rank);
            }
        }
        return result.isEmpty() ? null : result;
    }

    /**
     * Obtain the influence level of this {@link Guild} without calculating its
     * most up-to-date value.
     *
     * @return the influence value
     * @since 0.0.5
     */
    public int getInfluence() {
        return influence;
    }

    /**
     * Teleport the given {@link Entity} to the home {@link Location} of this
     * {@link Guild}.
     *
     * @param entity the {@link Entity} to teleport
     * @since 0.0.5
     */
    public void teleportToHome(@Nonnull final Entity entity) {
        Validate.notNull(entity);

        if (getHomeLocation() != null) {
            entity.teleport(getHomeLocation());
        }
    }

    /**
     * Get the {@link UUID} of this {@link Guild}.
     *
     * @return the {@link UUID}
     * @since 0.0.5
     */
    @Nullable
    public UUID getId() {
        return guildId;
    }

    /**
     * Get the name of this {@link Guild}.
     *
     * @return the name
     * @since 0.0.5
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Get the home {@link Location} for this {@link Guild}, or null if it
     * hasn't been set yet.
     *
     * @return the home {@link Location}
     * @since 0.0.5
     */
    @Nullable
    public Location getHomeLocation() {
        return home;
    }

    /**
     * Get the {@link UUID} of the leader of this {@link Guild}.
     *
     * @return the leader's {@link UUID}
     * @since 0.0.5
     */
    @Nullable
    public UUID getLeader() {
        return leader;
    }

    /**
     * Retrieve a copy of this {@link Guild}'s members.
     *
     * @return the members
     * @since 0.0.5
     */
    @Nullable
    public Set<UUID> getMembers() {
        if (members == null) {
            return null;
        }
        return new HashSet<UUID>(members);
    }

    /**
     * Retrieve a copy of this {@link Guild}'s {@link GuildRank}s.
     *
     * @return the {@link GuildRank}s
     * @since 0.0.5
     */
    @Nullable
    public Set<GuildRank> getRanks() {
        if (ranks == null) {
            return null;
        }
        return new HashSet<GuildRank>(ranks);
    }

    /**
     * Retrieve the {@link GuildRank} in this {@link Guild} with the specified
     * name.
     *
     * @param name the name of the wanted {@link GuildRank}
     * @return the applicable {@link GuildRank}
     * @since 0.0.5
     */
    @Nullable
    public GuildRank getRank(@Nonnull final String name) {
        for (final GuildRank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    /**
     * Get the hall of this {@link Guild}, which could also be {@code null} when
     * it remains unregistered.
     *
     * @return the guild-hall
     * @since 0.0.5
     */
    @Nullable
    public Region2D getGuildHallRegion() {
        return hallRegion;
    }

    /**
     * Get the leader {@link GuildRank} within this {@link Guild}.
     *
     * @return the leader {@link GuildRank}
     * @since 0.0.5
     */
    @Nonnull
    public GuildRank getLeaderRank() {
        return getRank(plugin.getMessage(Messages.GUILD_LEADER_RANK));
    }

    /**
     * Get the default {@link GuildRank} within this {@link Guild}.
     *
     * @return the default {@link GuildRank}
     * @since 0.0.5
     */
    @Nonnull
    public GuildRank getDefaultRank() {
        return getRank(plugin.getMessage(Messages.GUILD_DEFAULT_RANK));
    }

    /**
     * Check whether this {@link Guild} is joinable to anyone.
     *
     * @return {@code true} if this {@link Guild} is open; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sends the given message to all online players who are a member of this
     * {@link Guild}.
     *
     * @param message the message that should be sent to all members
     * @since 0.0.5
     */
    public void broadcastMessage(@Nonnull final String message) {
        Validate.notNull(message);

        final Set<UUID> members = getMembers();
        if (members == null || members.isEmpty()) {
            return;
        }

        for (final UUID uuid : members) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Sends the given message to all online players who are part of the given
     * {@link GuildRank}(s).
     *
     * @param message the message that should be sent to all members of the
     *        specified {@link GuildRank}(s)
     * @param ranks the {@link GuildRank}s the message should be broadcast for
     * @since 0.0.5
     */
    public void broadcastMessage(@Nonnull final String message,
            @Nonnull final GuildRank... ranks) {
        Validate.notNull(message);
        Validate.notNull(ranks);

        for (final GuildRank rank : ranks) {
            rank.broadcastMessage(message);
        }
    }

    /**
     * Set the name of this {@link Guild}.
     *
     * @param name the new name. Cannot be null
     * @since 0.0.5
     */
    public void setName(@Nonnull final String name) {
        Validate.notNull(name);

        this.name = name;
        plugin.getDataStore().updateGuildData(this);
    }

    /**
     * Set the leader of this {@link Guild}. If this {@link Guild} already has a
     * leader, the current leader is demoted from his position and the given
     * leader is raised to the leader {@link GuildRank}. When the specified
     * leader isn't a member of this {@link Guild}, he will automatically join.
     *
     * @param leader the {@link UUID} of the leader
     * @since 0.0.5
     */
    public void setLeader(@Nonnull final UUID leader) {
        Validate.notNull(leader);

        if (this.leader != null) {
            getLeaderRank().removeMember(this.leader);
            getDefaultRank().addMember(this.leader);
        }
        if (!members.contains(leader)) {
            members.add(leader);
        }

        this.leader = leader;
        getLeaderRank().addMember(leader);
        plugin.getDataStore().updateGuildData(this);
    }

    /**
     * Called to create a guild by setting the name and leader for the new guild
     * but should never be called outside of these circumstances, i.e if the
     * guild already has a leader. This method MUST be called upon creation of a
     * guild - if {@link Guild#setName(String)} and {@link Guild#setLeader(UUID)}
     * are used instead there WILL be an exception thrown.
     *
     * @param name the new name of the guild. Not {@code null}
     * @param leader the UUID of the new leader of the guild. Not {@code null}
     * @since 0.0.5
     */
    public void create(@Nonnull final String name, @Nonnull final UUID leader) {
        Validate.notNull(name);
        Validate.notNull(leader);

        this.name = name;
        this.leader = leader;
        if (!members.contains(leader)) {
            members.add(leader);
        }
        getLeaderRank().addMember(leader);

        plugin.getDataStore().updateGuildData(this);
    }

    /**
     * Adds the given member to this {@link Guild} along with the specified
     * {@link GuildRank}.
     *
     * @param member the {@link UUID} of the player to add
     * @param rank the start {@link GuildRank} of the player
     * @since 0.0.5
     */
    public GuildPlayerJoinEvent addMember(@Nonnull final UUID member,
            @Nonnull final GuildRank rank) {
        Validate.notNull(member);
        Validate.notNull(rank);

        final GuildPlayerJoinEvent event = RolecraftEventFactory
                .guildPlayerJoined(this, Bukkit.getPlayer(member), rank);
        if (event.isCancelled()) {
            return event;
        }

        members.add(member);
        rank.addMember(member);
        plugin.getDataStore().addPlayerToGuild(member, this);
        plugin.getDataStore().updateGuildRanks(this);
        return event;
    }

    /**
     * Remove the given player from this {@link Guild}. This method assumes that
     * the removal isn't a kick - if you want to remove someone as if it was a
     * kick, use {@link #removeMember(UUID, boolean)}
     *
     * @param member the {@link UUID} of the player to remove
     * @since 0.0.5
     * @see {@link #removeMember(UUID, boolean}
     */
    public void removeMember(@Nonnull final UUID member) {
        removeMember(member, false);
    }

    /**
     * Remove the given player from this {@link Guild}.
     *
     * @param member the {@link UUID} of the player to remove
     * @param kicked whether the leave is due to a kick
     * @since 0.0.5
     */
    public void removeMember(@Nonnull final UUID member, final boolean kicked) {
        Validate.notNull(member);
        Validate.isTrue(getMembers().contains(member));

        if (kicked) {
            RolecraftEventFactory.guildPlayerKicked(this,
                    Bukkit.getPlayer(member));
        } else {
            RolecraftEventFactory.guildPlayerLeave(this,
                    Bukkit.getPlayer(member));
        }

        final boolean removed = members.remove(member);
        if (!removed) {
            throw new IllegalArgumentException(
                    "The given member isn't part of this Guild!");
        }
        for (final GuildRank rank : getPlayerRanks(member)) {
            rank.removeMember(member);
        }
        plugin.getDataStore().removePlayerFromGuild(member, this);
        plugin.getDataStore().updateGuildRanks(this);
    }

    /**
     * Add the given {@link GuildRank} to this {@link Guild}.
     *
     * @param rank the {@link GuildRank} that should be added
     * @since 0.0.5
     */
    public boolean addRank(final GuildRank rank) {
        Validate.notNull(rank);
        Validate.isTrue(getRank(rank.getName()) == null);

        final boolean retVal = ranks.add(rank);
        plugin.getDataStore().updateGuildRanks(this);
        return retVal;
    }

    /**
     * Attempt to remove the specified {@link GuildRank} from this {@link Guild}
     * . The leader and default {@link GuildRank}s may not be removed.
     *
     * @param rank the {@link GuildRank} to remove
     * @return {@code true} only if the given {@link GuildRank} has been removed
     *         from this {@link Guild}
     * @since 0.0.5
     */
    public boolean removeRank(final GuildRank rank) {
        Validate.notNull(rank);

        final String name = rank.getName().toLowerCase();
        final boolean retVal = !(name.equals("leader") || name
                .equals("default")) && ranks.remove(rank);
        plugin.getDataStore().updateGuildRanks(this);
        return retVal;
    }

    /**
     * Set the home {@link Location} of this {@link Guild} to the given
     * {@link Location}.
     *
     * @param home the new home {@link Location}
     * @since 0.0.5
     */
    public void setHomeLocation(@Nullable final Location home) {
        this.home = home;
        plugin.getDataStore().updateGuildData(this);
    }

    /**
     * Set this {@link Guild} to be open or closed.
     *
     * @param open the new open status
     * @since 0.0.5
     */
    public void setOpen(final boolean open) {
        this.open = open;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Guild)) {
            return false;
        }
        final Guild other = (Guild) o;
        return !(guildId == null || other.guildId == null) && guildId
                .equals(other.getId());
    }

    /**
     * @since 0.0.5
     */
    @Override
    public int hashCode() {
        return guildId == null ? -1 : guildId.hashCode();
    }

    /**
     * Set the given {@link Region2D} as the hall of this {@link Guild}. This
     * method should only ever be called from its {@link GuildManager} when the
     * hall is claimed, hence the package-private access modifier is utilised.
     *
     * @param hallRegion the new guild-hall
     * @since 0.0.5
     */
    void claimAsGuildHall(@Nullable final Region2D hallRegion) {
        this.hallRegion = hallRegion;
        plugin.getDataStore().updateGuildData(this);
    }
}
