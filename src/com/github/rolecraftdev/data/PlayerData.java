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

    public String getPlayerName() {
        return name;
    }

    /**
     * Called when loaded in SQL
     * 
     * @deprecated DO NOT CALL
     */
    @Deprecated
<<<<<<< HEAD
    public void initialise() {
=======
    public void initialize() {
>>>>>>> e974701262d7bc31076c506c8e9169981b8ff10d

    }
}
