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
