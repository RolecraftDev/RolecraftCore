package com.github.rolecraftdev.guild;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player rank within a specific guild. Ranks can be created by the
 * guild leader and can have different permissions
 */
public final class GuildRank {
    /**
     * The name of this guild rank
     */
    private final String name;
    /**
     * The actions within the guild which members of this guild rank are allowed
     * to perform
     */
    private final Set<GuildAction> permitted;
    /**
     * All of the members of the guild this guild rank is included in who are
     * in this rank
     */
    private final Set<UUID> members;

    /**
     * Creates a new guild rank using the given parameters
     *
     * @param name      The name of the new guild rank
     * @param permitted The actions the new rank's members can perform
     * @param members   The members of the new guild rank
     */
    public GuildRank(final String name, final Set<GuildAction> permitted,
            final Set<UUID> members) {
        this.name = name;
        this.permitted = permitted;
        this.members = members;
    }

    /**
     * Gets the name of this guild rank
     *
     * @return The name of this guild rank
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a copy of the set used to hold the guild actions members of this
     * guild rank are allowed to perform
     *
     * @return A copy of the Set containing guild actions members of this rank
     * can perform
     */
    public Set<GuildAction> getPermittedActions() {
        return new HashSet<GuildAction>(permitted);
    }

    /**
     * Gets a copy of the set used to hold the members of the guild who are a
     * part of this guild rank
     *
     * @return A copy of the Set of members of the guild who are part of this
     * guild rank
     */
    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }
}
