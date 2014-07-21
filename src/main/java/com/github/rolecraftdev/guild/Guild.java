package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.data.dataobjects.ChunkLocation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Guild {
    private UUID leader;
    private Set<UUID> officers;
    private Set<UUID> members;
    private Location home;
    private Set<ChunkLocation> claimedChunks;

    public Set<ChunkLocation> getClaimedChunks() {
        return new HashSet<ChunkLocation>(claimedChunks);
    }

    public boolean can(final UUID player, final GuildAction action) {
        return action.can(player, this);
    }

    public boolean isClaimed(final ChunkLocation location) {
        return claimedChunks.contains(location);
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

    public void teleportToHome(final Entity entity) {
        entity.teleport(home);
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
}
