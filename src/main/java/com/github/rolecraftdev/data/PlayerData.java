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
package com.github.rolecraftdev.data;

import java.util.UUID;

/**
 * Holds Rolecraft data for a specific player. Stored in SQL
 */
public final class PlayerData {
    private final UUID playerId;
    private final String name;

    private UUID guild;
    private UUID profession;
    private int influence;

    /**
     * @param playerId
     * @deprecated There is no reason not to have the player's name
     */
    @Deprecated
    public PlayerData(final UUID playerId) {
        this(playerId, null);
    }

    public PlayerData(final UUID playerId, final String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return name;
    }

    public UUID getGuild() {
        return guild;
    }

    public UUID getProfession() {
        return profession;
    }

    public int getInfluence() {
        return influence;
    }

    /*public void setPlayerName(final String name) {
        this.name = name;
    }*/

    public void setGuild(final UUID guild) {
        this.guild = guild;
    }

    public void setInfluence(final int influence) {
        this.influence = influence;
    }

    public void addInfluence(final int influence) {
        this.influence += influence;
    }

    public void subtractInfluence(final int influence) {
        this.influence -= influence;
    }

    /**
     * For internal use only - called when loaded in SQL
     *
     * @deprecated DO NOT CALL
     */
    @Deprecated
    public void initialise() {
    }
}
