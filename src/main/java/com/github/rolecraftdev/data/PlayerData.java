package com.github.rolecraftdev.data;

import java.util.UUID;

public final class PlayerData {
    private final UUID playerId;
    private String name;
    private int influence;

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

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
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
