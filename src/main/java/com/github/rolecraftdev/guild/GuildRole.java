package com.github.rolecraftdev.guild;

public enum GuildRole {
    LEADER(3), OFFICER(2), MEMBER(1);

    private final int accessLevel;

    private GuildRole(final int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
