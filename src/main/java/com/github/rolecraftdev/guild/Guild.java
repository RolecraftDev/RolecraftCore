package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.data.dataobjects.ChunkLocation;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Guild {
    private final GuildManager guildManager;

    private String name;
    private UUID leader;
    private Set<UUID> officers;
    private Set<UUID> members;
    private Location home;
    private Set<ChunkLocation> claimedChunks;
    private int influence;

    public Guild(final GuildManager guildManager) {
        this.guildManager = guildManager;
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

    public void claimChunk(final ChunkLocation chunkLocation) {
        claimedChunks.add(chunkLocation);
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
}
