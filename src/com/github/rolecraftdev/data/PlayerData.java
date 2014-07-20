package com.github.rolecraftdev.data;

import java.util.UUID;

public final class PlayerData {
    private final UUID playerId;
    private String name;

    public PlayerData(final UUID playerId, final String name) {
        this.playerId = playerId;
        this.name = name;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Called when loaded in SQL
     * 
     * @deprecated DO NOT CALL
     */
    @Deprecated
    public void intialize() {

    }
}
