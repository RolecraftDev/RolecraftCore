package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Guild {
    private final GuildManager guildManager;

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

    public Guild(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    public boolean can(final UUID player, final GuildAction action) {
        return action.can(player, this);
    }

    public boolean isMember(final UUID player) {
        return members.contains(player);
    }

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

    public int getInfluence() {
        return influence;
    }

    public int calculateInfluence() {
        int influence = 0;
        for (final UUID playerId : members) {
            final PlayerData data = guildManager.getPlugin().getDataManager()
                    .getPlayerData(playerId);
            influence += data.getInfluence();
        }
        return this.influence = influence;
    }

    public void teleportToHome(final Entity entity) {
        entity.teleport(home);
    }

    public String getName() {
        return name;
    }

    public Location getHomeLocation() {
        return home;
    }

    public UUID getLeader() {
        return leader;
    }

    public Set<UUID> getOfficers() {
        return new HashSet<UUID>(officers);
    }

    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Guild)) {
            return false;
        }
        final Guild other = (Guild) o;
        return other.name.equals(name) && other.leader.equals(leader);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (leader != null ? leader.hashCode() : 0);
        return result;
    }
}
