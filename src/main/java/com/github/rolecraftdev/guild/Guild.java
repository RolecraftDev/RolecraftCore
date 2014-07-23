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

import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.data.Region2D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a guild in Rolecraft
 */
public final class Guild {
    /**
     * The GuildManager object this guild is registered to
     */
    private final GuildManager guildManager;
    
    /**
     * A UUID that refers to this guild
     */
    private final UUID guildId;

    /**
     * The name of this guild
     */
    private String name;
    /**
     * The unique identifier of the player who leads this guild
     */
    private UUID leader;
    /**
     * A Set containing all of this guild's officers' unique identifiers. This
     * does not contain the guild leader's unique identifier
     */
    private Set<UUID> officers;
    /**
     * A Set containing all of this guild's members' unique identifiers,
     * including officers and the leader
     */
    private Set<UUID> members;
    /**
     * The home point of this guild, used for teleporting to the guild home
     */
    private Location home;
    /**
     * The current influence level of the guild, which depends on the individual
     * influence levels of the members of the guild
     */
    private int influence;
    /**
     * The 2D region containing this guild's guild hall
     */
    private Region2D hallRegion;

    /**
     * Creates a new Guild object linked to the given GuildManager object, which
     * is used for interaction with the rest of the plugin
     * 
     * 
     *
     * @param guildManager The GuildManager object this Guild belongs to
     */
    public Guild(final GuildManager guildManager) {
        this.guildManager = guildManager;
        guildId = UUID.randomUUID();
    }

    /**
     * Checks whether the player with the given unique identifier is allowed to
     * perform the given action within this guild
     *
     * @param player The player to check the ability to perform the given action
     * @param action The action to check whether the given player can perform
     * @return Whether the given player is allowed to perform the given action
     */
    public boolean can(final UUID player, final GuildAction action) {
        return action.can(player, this);
    }

    /**
     * Checks whether the player with the given unique identifier is a member of
     * this guild
     *
     * @param player The player whose membership of this guild will be checked
     * @return Whether the player with the given id is a member of this guild
     */
    public boolean isMember(final UUID player) {
        return members.contains(player);
    }

    /**
     * Gets the role within the guild of the player with the given unique
     * identifier, or null if the player with the given identifier isn't a
     * member of this guild
     *
     * @param player The player to check the guild role in this guild for
     * @return The GuildRole object representing the given player's role in this
     * guild
     */
    public GuildRole getRole(final UUID player) {
        if (!members.contains(player)) {
            return null;
        }
        if (leader.equals(player)) {
            return GuildRole.LEADER;
        }
        if (officers.contains(player)) {
            return GuildRole.OFFICER;
        }
        return GuildRole.MEMBER;
    }

    /**
     * Gets the current influence of this guild without calculating a new value.
     * If an up-to-date influence value is required, use calculateInfluence()
     * instead (which is slower but always accurate)
     *
     * @return The guild's last calculated influence value
     */
    public int getInfluence() {
        return influence;
    }

    /**
     * Calculates and returns a new influence value for this guild based on the
     * influence values of the individual players within this guild
     *
     * @return The newly calculated influence value for this guild
     */
    public int calculateInfluence() {
        int influence = 0;
        for (final UUID playerId : members) {
            final PlayerData data = guildManager.getPlugin().getDataManager()
                    .getPlayerData(playerId);
            influence += data.getInfluence();
        }
        return this.influence = influence;
    }

    /**
     * Teleports the given entity to the home location of this guild
     *
     * @param entity The Entity to teleport to this guild's home
     */
    public void teleportToHome(final Entity entity) {
        entity.teleport(home);
    }

    /**
     * Gets the name of this guild, which should be unique
     *
     * @return The unique name of this guild
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the home location for this guild, or null if the guild doesn't have
     * a home location set
     *
     * @return This guild's home location, or null if it doesn't exist
     */
    public Location getHomeLocation() {
        return home;
    }

    /**
     * Gets the unique identifier of the player who is the leader of this guild
     *
     * @return The leader of this guild's unique identifier
     */
    public UUID getLeader() {
        return leader;
    }

    /**
     * Gets a copy of the set used to hold the officers of this guild
     *
     * @return A copy of the Set of this guild's officers
     */
    public Set<UUID> getOfficers() {
        return new HashSet<UUID>(officers);
    }

    /**
     * Gets a copy of the set used to hold the members of this guild
     *
     * @return A copy of the Set of this guild's members
     */
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    /**
     * Gets the 2D region containing the guild hall this guild owns, or null if
     * this guild doesn't currently own a guild hall
     *
     * @return The Region2D object representing the region of this guild's hall
     */
    public Region2D getGuildHallRegion() {
        return hallRegion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Guild)) {
            return false;
        }
        final Guild other = (Guild) o;
        return other.name.equals(name) && other.leader.equals(leader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (leader != null ? leader.hashCode() : 0);
        return result;
    }

    /**
     * Sets the given Region2D object as this guild's guild hall region. Should
     * only ever be called from GuildManager when a guild hall is claimed, hence
     * the method is package private
     *
     * @param hallRegion The Region2D to become this guild's new guild hall
     */
    void claimAsGuildHall(final Region2D hallRegion) {
        this.hallRegion = hallRegion;
    }
}
