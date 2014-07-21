package com.github.rolecraftdev.guild;

import java.util.UUID;

public enum GuildAction {
    KICK_MEMBER(2), KICK_OFFICER(3), INVITE(2), LEAVE(1), SET_LEADER(
            3), SET_HOME(3);

    private final int defaultAccessLevel;

    private int accessLevel;

    private GuildAction(final int defaultAccessLevel) {
        this.defaultAccessLevel = defaultAccessLevel;
    }

    public int getDefaultAccessLevel() {
        return defaultAccessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean can(final UUID player, final Guild guild) {
        final GuildRole role = guild.getRole(player);
        if (role != null) {
            return role.getAccessLevel() >= accessLevel;
        }
        return false;
    }

    public String getConfigPath() {
        return name().toLowerCase().replace('_', '-');
    }
}
