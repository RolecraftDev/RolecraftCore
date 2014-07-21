package com.github.rolecraftdev.data;

import java.util.UUID;

/**
 * Holds Rolecraft data for a specific player. Stored in SQL
 */
public final class PlayerData {
    private final UUID playerId;

    private String name;
    private String guild;
    private int influence;

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

    public String getGuild() {
        return guild;
    }

    public int getInfluence() {
        return influence;
    }

    public void setPlayerName(final String name) {
        this.name = name;
    }

    public void setGuild(final String guild) {
        this.guild = guild;
    }

    public void setInfluence(final int influence) {
        this.influence = influence;
    }

    /**
     * Called when loaded in SQL
     *
     * @deprecated DO NOT CALL
     */
    @Deprecated
    public void initialise() {
    }
}
