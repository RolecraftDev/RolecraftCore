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

import com.github.rolecraftdev.util.LevelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Holds Rolecraft data for a specific player, which is stored in SQL.
 */
public final class PlayerData {
    /**
     * The unique ID of the player this object holds data for
     */
    private final UUID playerId;
    /**
     * The username of the player this object holds data for
     */
    private final String name;

    /**
     * The unique ID of the {@link com.github.rolecraftdev.guild.Guild} the
     * player belongs to, or null if the player doesn't belong to a Guild
     */
    private UUID guild;
    /**
     * The unique ID of the {@link com.github.rolecraftdev.profession.Profession}
     * the player belongs to, or null if the player doesn't have a Profession
     */
    private UUID profession;
    /**
     * The quests the player is currently embarking on
     */
    private List<UUID> quests;
    /**
     * The current influence value for the player, calculated by his or her
     * actions
     */
    private int influence;
    /**
     * The player's current Rolecraft level of experience
     */
    private float experience;
    /**
     * The player's current Rolecraft karma level, used in the Afterlife addon
     * for Rolecraft
     */
    private float karma;

    /**
     * Whether the data is loaded
     */
    private volatile boolean loaded;
    /**
     * Whether the data is currently being unloaded
     */
    private volatile boolean unloading;

    /**
     * Constructs a new PlayerData object for a player
     *
     * @param playerId The ID of the player the data is for
     * @param name     The username of the player the data is for
     */
    public PlayerData(final UUID playerId, final String name) {
        this.playerId = playerId;
        this.name = name;
    }

    /**
     * Gets the unique ID of the player this object holds data for
     *
     * @return The unique ID of the player this object holds data for
     */
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Gets the username of the player this object holds data for
     *
     * @return The username of the player this object holds data for
     */
    public String getPlayerName() {
        return name;
    }

    /**
     * Returns whether this data has finished loading
     *
     * @return True if this data is loaded, otherwise false
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Returns whether this data is currently being unloaded
     *
     * @return Whether this data is currently being unloaded
     */
    public boolean isUnloading() {
        return unloading;
    }

    /**
     * Gets the unique ID of the player's guild, or null if he doesn't have one.
     * Null will also be returned if the data isn't loaded
     *
     * @return The unique ID of the player's guild - null if he doesn't have one
     * or if the data isn't loaded
     */
    public UUID getGuild() {
        if (loaded) {
            return guild;
        }
        return null;
    }

    /**
     * Gets the unique ID of the player's profession, or null if he doesn't have
     * one. Null will also be returned if the data isn't loaded
     *
     * @return The unique ID of the player's profession - null if he doesn't
     * have one or if the data isn't loaded
     */
    public UUID getProfession() {
        if (loaded) {
            return profession;
        } else {
            return null;
        }
    }

    public List<UUID> getQuests() {
        return new ArrayList<UUID>(quests);
    }

    /**
     * Gets the current influence value for the player
     *
     * @return The player's current influence level
     */
    public int getInfluence() {
        if (loaded) {
            return influence;
        } else {
            return -1;
        }
    }

    /**
     * Gets the player's current level
     *
     * @return The player's current level
     */
    public int getLevel() {
        if (loaded) {
            return LevelUtil.getLevel(experience);
        } else {
            return -1;
        }
    }

    /**
     * Gets the amount of experience the player requires before reaching the
     * next level
     *
     * @return The amount of experience the player needs to level up
     */
    public float getExpToNextLevel() {
        if (loaded) {
            return LevelUtil.expToNextLevel(experience);
        } else {
            return -1;
        }
    }

    /**
     * Gets the player's current level of experience
     *
     * @return The player's current experience value
     */
    public float getExperience() {
        if (loaded) {
            return experience;
        } else {
            return -1;
        }
    }

    /**
     * Gets the player's current karma value
     *
     * @return The player's current karma value
     */
    public float getKarma() {
        return karma;
    }

    /**
     * DO NOT CALL UNLESS UNLOADING VIA DATABASE
     *
     * @param unload Whether the data is currently being unloaded
     */
    public void setUnloading(boolean unload) {
        this.unloading = unload;
    }

    /**
     * DO NOT CALL UNLESS UNLOADING VIA DATABASE
     *
     * @param loaded Whether the data is loaded
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Sets the player's guild to the given guild
     *
     * @param guild The {@link com.github.rolecraftdev.guild.Guild} the player
     *              is joining
     */
    public void setGuild(final UUID guild) {
        if (loaded) {
            if (!unloading) {
                this.guild = guild;
            }
        }
    }

    /**
     * Sets the player's profession to the given profession
     *
     * @param profession The ID of the {@link com.github.rolecraftdev.profession.Profession}
     *                   the player is joining
     */
    public void setProfession(final UUID profession) {
        if (loaded) {
            if (!unloading) {
                this.profession = profession;
            }
        }
    }

    public void setQuests(final List<UUID> quests) {
        this.quests = quests;
    }

    /**
     * Sets the player's influence level to the given influence
     *
     * @param influence The new level of influence for the player
     */
    public void setInfluence(final int influence) {
        if (loaded) {
            if (!unloading) {
                this.influence = influence;
            }
        }
    }

    /**
     * Adds the given amount of influence to the player's influence value
     *
     * @param influence The amount of influence to add
     */
    public void addInfluence(final int influence) {
        setInfluence(getInfluence() + influence);
    }

    /**
     * Subtracts the given amount of influence from the player's influence value
     *
     * @param influence The amount of influence to subtract
     */
    public void subtractInfluence(final int influence) {
        setInfluence(getInfluence() - influence);
    }

    /**
     * Sets the player's experience level
     *
     * @param amount The new experience value for the player
     */
    public void setExperience(final float amount) {
        if (loaded) {
            if (!unloading) {
                experience = amount;
            }
        }
    }

    /**
     * Adds the given amount to the player's experience value
     *
     * @param amount The amount of experience to add
     */
    public void addExperience(final float amount) {
        setExperience(getExperience() + amount);
    }

    /**
     * Subtracts the given amount from the player's experience value
     *
     * @param amount The amount of experience to subtract
     */
    public void subtractExperience(final float amount) {
        setExperience(getExperience() - amount);
    }

    /**
     * Sets the player's karma value
     *
     * @param karma The new value for the player's karma
     */
    public void setKarma(float karma) {
        if (loaded) {
            if (!unloading) {
                this.karma = karma;
            }
        }
    }

    /**
     * Adds the given amount to the player's karma
     *
     * @param amount The amount to add to the player's karma
     */
    public void addKarma(float amount) {
        setKarma(getKarma() + amount);
    }

    /**
     * Subtracts the given amount from the player's karma
     *
     * @param amount The amount to subtract from the player's karma
     */
    public void subtractKarma(float amount) {
        setKarma(getKarma() - amount);
    }

    /**
     * For internal use only - called when reset via SQL
     *
     * @deprecated Do not call
     */
    @Deprecated
    public void clear() {
        this.guild = null;
        this.profession = null;
        this.influence = 0;
        this.experience = 0;
        this.karma = 0;

        unloading = false;
    }

    /**
     * For internal use only - called when loaded in SQL.
     *
     * @deprecated Do not call
     */
    @Deprecated
    public void initialise(final UUID guild, final UUID profession,
            final int influence, final float exp, final float karma) {
        this.guild = guild;
        this.profession = profession;
        this.influence = influence;
        this.experience = exp;
        this.karma = karma;

        loaded = true;
    }
}
