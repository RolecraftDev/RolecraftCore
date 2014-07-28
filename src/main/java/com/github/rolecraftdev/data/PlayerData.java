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

import com.github.rolecraftdev.util.LevelUtil;

/**
 * Holds Rolecraft data for a specific player, which is stored in SQL.
 */
public final class PlayerData {
	private final UUID playerId;
	private final String name;

	private UUID guild;
	private UUID profession;
	private int influence;
	private float experience;

	private volatile boolean loaded;
	private volatile boolean unloading;

	/**
	 * @deprecated There is no reason to exclude the player's name
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

	public boolean isLoaded() {
		return loaded;
	}

	public boolean isUnloading() {
		return unloading;
	}

	public void setUnloading(boolean unload) {
		this.unloading = unload;
	}

	/**
	 * DO NOT CALL UNLESS UNLOADING VIA DATABASE
	 * 
	 * @param loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public String getPlayerName() {
		return name;
	}

	public UUID getGuild() {
		if (loaded)
			return guild;
		return null;
	}

	public UUID getProfession() {
		if (loaded)
			return profession;
		else
			return null;
	}

	public void setProfession(final UUID profession) {
		if (loaded)
			if (!unloading)
				this.profession = profession;
	}

	public int getInfluence() {
		if (loaded)
			return influence;
		else
			return -1;
	}

	/*
	 * public void setPlayerName(final String name) { this.name = name; }
	 */

	public void setGuild(final UUID guild) {
		if (loaded)
			if (!unloading)
				this.guild = guild;
	}

	public void setInfluence(final int influence) {
		if (loaded)
			if (!unloading)
				this.influence = influence;
	}

	public void addInfluence(final int influence) {
		if (loaded)
			if (!unloading)
				this.influence += influence;
	}

	public void subtractInfluence(final int influence) {
		if (loaded)
			if (!unloading)
				this.influence -= influence;
	}

	public void addExperience(final float amount) {
		if (loaded)
			if (!unloading)
				experience += amount;
	}

	public void subtractExperience(final float amount) {
		if (loaded)
			if (!unloading)
				experience -= amount;
	}

	public int getLevel() {
		if (loaded)
			return LevelUtil.getLevel(experience);
		else
			return -1;
	}

	public float getExpToNextLevel() {
		if (loaded)
			return LevelUtil.expToNextLevel(experience);
		else
			return -1;
	}
	
	public float getExp () {
		if(loaded) 
			return experience;
		else 
			return -1;
	}

	/**
	 * For internal use only - called when loaded in SQL.
	 * 
	 * @deprecated Do not call
	 */
	@Deprecated
	public void initialise(final UUID guild, final UUID profession,
			final int influence, final float exp) {
		this.guild = guild;
		this.profession = profession;
		this.influence = influence;
		this.experience = exp;

		loaded = true;
	}
	
	/**
	 * For internal use only - called when reset via SQL
	 */
	@Deprecated
	public void clear () {
		this.guild = null;
		this.profession = null;
		this.influence = 0;
		this.experience = 0;
		
		unloading = false;
	}
}
