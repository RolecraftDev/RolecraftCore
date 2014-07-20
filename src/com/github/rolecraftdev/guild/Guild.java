package com.github.rolecraftdev.guild;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.github.rolecraftdev.data.dataobjects.ChunkLocation;

public final class Guild {
    private UUID leader;
    private Set<UUID> officers;
    private Set<UUID> members;
    private Location home;
    private Set<ChunkLocation> claimedChunks;

    public UUID getLeader() {
        return leader;
    }

    public Set<UUID> getOfficers() {
        return new HashSet<UUID>(officers);
    }

    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    public Set<ChunkLocation> getClaimedChunks() {
        return new HashSet<ChunkLocation>(claimedChunks);
    }

    public boolean can(final UUID player, final GuildAction action) {
        return action.can(player, this);
    }

    public boolean isClaimed(final ChunkLocation location) {
        return claimedChunks.contains(location);
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

    public Location getHomeLocation() {
        return home;
    }

    public void teleportToHome(final Entity entity) {
        entity.teleport(home);
    }
}
