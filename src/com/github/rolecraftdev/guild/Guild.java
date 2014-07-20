package com.github.rolecraftdev.guild;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class Guild {
    private UUID leader;
    private List<UUID> officers;
    private List<UUID> members;
    private Location home;

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getOfficers() {
        return Collections.unmodifiableList(officers);
    }

    public List<UUID> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public boolean can(final UUID player, final GuildAction action) {
        return action.can(player, this);
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
