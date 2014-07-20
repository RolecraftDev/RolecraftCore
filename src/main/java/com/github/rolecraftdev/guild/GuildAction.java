package com.github.rolecraftdev.guild;

import java.util.UUID;

public enum GuildAction {
    KICK_MEMBER(2), KICK_OFFICER(3), INVITE(2), LEAVE(1), SET_LEADER(3), SET_HOME(
            3), BUY_LAND(2);

    private final int accessLevel;

    private GuildAction(final int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean can(final UUID player, final Guild guild) {
        final GuildRole role = guild.getRole(player);
        if (role != null) {
            return accessLevel >= role.getAccessLevel();
        }
        return false;
    }
}
