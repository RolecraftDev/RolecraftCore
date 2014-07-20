package com.github.rolecraftdev.data;

import java.util.UUID;

public final class PlayerData {
	private final UUID playerId;

	public PlayerData(final UUID playerId) {
		this.playerId = playerId;
	}

	public UUID getPlayerId() {
		return playerId;
	}
}
