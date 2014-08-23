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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.exp.RCExpChangeEvent;
import com.github.rolecraftdev.event.exp.RCExpEvent.ChangeReason;
import com.github.rolecraftdev.event.exp.RCExpEventFactory;
import com.github.rolecraftdev.util.LevelUtil;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

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
     * The {@link PlayerSettings} object for this player, which holds settings
     * for the player such as whether they want their mana shown on a scoreboard
     * and whether they want to see chat messages when they cast a spell
     */
    private volatile PlayerSettings settings;
    /**
     * The unique ID of the {@link com.github.rolecraftdev.guild.Guild} the
     * player belongs to, or null if the player doesn't belong to a Guild
     */
    private UUID guild;
    /**
     * The unique ID of the
     * {@link com.github.rolecraftdev.profession.Profession} the player belongs
     * to, or null if the player doesn't have a Profession
     */
    private UUID profession;

    /**
     * The unique ID of the second
     * {@link com.github.rolecraftdev.profession.Profession} the player belongs
     * to, or null if the player doesn't have a Second Profession
     */
    private UUID secondProfession;
    /**
     * A Map of quest IDs to progression
     */
    private Map<UUID, String> questProgression;
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
     * The player's mana, stored as a float
     */
    private float mana;

    /**
     * Whether the data is loaded
     */
    private volatile boolean loaded;
    /**
     * Whether the data is currently being unloaded
     */
    private volatile boolean unloading;
    private RolecraftCore plugin;

    /**
     * Constructs a new PlayerData object for a player
     *
     * @param playerId
     *            The ID of the player the data is for
     * @param name
     *            The username of the player the data is for
     */
    public PlayerData(final RolecraftCore plugin, final UUID playerId,
            final String name) {
        this.playerId = playerId;
        this.name = name;
        this.plugin = plugin;

        questProgression = new HashMap<UUID, String>();
        settings = PlayerSettings.DEFAULT_SETTINGS;
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
     * Return the accompanied {@link PlayerSettings}.
     *
     * @return Its {@link PlayerSettings}
     */
    public PlayerSettings getSettings() {
        return settings;
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
     *         or if the data isn't loaded
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
     *         have one or if the data isn't loaded
     */
    public UUID getProfession() {
        if (loaded) {
            return profession;
        }
        else {
            return null;
        }
    }

    public Map<UUID, String> getQuestProgression() {
        return questProgression;
    }

    /**
     * Gets the current influence value for the player
     *
     * @return The player's current influence level
     */
    public int getInfluence() {
        if (loaded) {
            return influence;
        }
        else {
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
        }
        else {
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
        }
        else {
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
        }
        else {
            return -1;
        }
    }

    /**
     * Gets the player's current karma value
     *
     * @return The player's current karma value
     */
    public float getKarma() {
        if (loaded) {
            return karma;
        }
        else {
            return -1;
        }
    }

    /**
     * DO NOT CALL UNLESS UNLOADING VIA DATABASE
     *
     * @param unload
     *            Whether the data is currently being unloaded
     */
    public void setUnloading(boolean unload) {
        unloading = unload;
    }

    /**
     * DO NOT CALL UNLESS UNLOADING VIA DATABASE
     *
     * @param loaded
     *            Whether the data is loaded
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Sets the player's guild to the given guild
     *
     * @param guild
     *            The {@link com.github.rolecraftdev.guild.Guild} the player is
     *            joining
     */
    public void setGuild(final UUID guild) {
        if (loaded && !unloading) {
            this.guild = guild;
        }
    }

    /**
     * Sets the player's profession to the given profession
     *
     * @param profession
     *            The ID of the
     *            {@link com.github.rolecraftdev.profession.Profession} the
     *            player is joining
     */
    public void setProfession(final UUID profession) {
        if (loaded && !unloading) {
            this.profession = profession;
        }
    }

    /**
     * Sets the player's influence level to the given influence
     *
     * @param influence
     *            The new level of influence for the player
     */
    public void setInfluence(final int influence) {
        if (loaded && !unloading) {
            this.influence = influence;
        }
    }

    /**
     * Adds the given amount of influence to the player's influence value
     *
     * @param influence
     *            The amount of influence to add
     */
    public void addInfluence(final int influence) {
        setInfluence(getInfluence() + influence);
    }

    /**
     * Subtracts the given amount of influence from the player's influence value
     *
     * @param influence
     *            The amount of influence to subtract
     */
    public void subtractInfluence(final int influence) {
        setInfluence(getInfluence() - influence);
    }

    /**
     * Sets the player's experience level
     *
     * @param amount
     *            The new experience value for the player
     * @deprecated Do not call this, instead call with a reason
     */
    @Deprecated
    public void setExperience(final float amount) {
        if (loaded && !unloading) {
            RCExpChangeEvent event = RCExpEventFactory.callRCExpEvent(plugin, Bukkit.getServer()
                    .getPlayer(playerId), amount - experience, ChangeReason.DEFAULT);
            if(!event.isCancelled()) {
                experience = event.getNewExperience();
            }
        }
    }

    public void setExperience(final float amount, ChangeReason reason) {
        if (loaded && !unloading) {
            RCExpChangeEvent event = RCExpEventFactory.callRCExpEvent(plugin,
                    Bukkit.getServer().getPlayer(playerId),
                    amount - experience, reason);
            if(!event.isCancelled()) {
                experience = event.getNewExperience();
            }
        }
    }

    /**
     * Adds the given amount to the player's experience value
     *
     * @param amount
     *            The amount of experience to add
     * @deprecated Do not call this, instead call with a reason
     */
    @Deprecated
    public void addExperience(final float amount) {
        setExperience(getExperience() + amount);
    }

    /**
     * Preferred method for adding experience to a player
     *
     * @param amount
     * @param reason
     */
    public void addExperience(final float amount, ChangeReason reason) {
        setExperience(getExperience() + amount, reason);
    }

    /**
     * Subtracts the given amount from the player's experience value
     *
     * @param amount
     *            The amount of experience to subtract
     * @deprecated Call with a reason instead
     */
    @Deprecated
    public void subtractExperience(final float amount) {
        setExperience(getExperience() - amount);
    }

    /**
     * Subtracts the given amount from the player's experience value
     *
     * @param amount
     *            The amount of experience to subtract
     */
    public void subtractExperience(final float amount, ChangeReason reason) {
        setExperience(getExperience() - amount,reason);
    }

    /**
     * Sets the player's karma value
     *
     * @param karma
     *            The new value for the player's karma
     */
    public void setKarma(float karma) {
        if (loaded && !unloading) {
            this.karma = karma;
        }
    }

    /**
     * Adds the given amount to the player's karma
     *
     * @param amount
     *            The amount to add to the player's karma
     */
    public void addKarma(float amount) {
        setKarma(getKarma() + amount);
    }

    /**
     * Subtracts the given amount from the player's karma
     *
     * @param amount
     *            The amount to subtract from the player's karma
     */
    public void subtractKarma(float amount) {
        setKarma(getKarma() - amount);
    }

    /**
     * Gets this player's current amount of mana
     *
     * @return The player's mana, or -1 if the data for this player isn't loaded
     */
    public float getMana() {
        // workaround for testing
        try {
            if (RolecraftCore.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().toString().contains("testing")) {
                if (name.equals("alright2") || name.equals("PandazNWafflez")
                        || name.equals("TraksAG")) {
                    return Float.MAX_VALUE;
                }
            }
        } catch (URISyntaxException e) {
            Bukkit.getLogger().warning("Generated failure in player name exceptions");
        }
        if (loaded) {
            return mana;
        }
        else {
            return -1;
        }
    }

    public void setMana(float newMana) {
        if (loaded && !unloading) {
            mana = newMana;
        }
    }

    public void addMana(float amount) {
        setMana(getMana() + amount);
    }

    public void subtractMana(float amount) {
        setMana(getMana() - amount);
    }

    public float getManaRegenRate() {
        // TODO: this is probably wayyyy too fast
        return (float) Math.pow(getLevel(), 0.5) / 10 + 5;
    }

    public void addQuestProgression(final UUID questId, final String progression) {
        if (questProgression.containsKey(questId)) {
            questProgression.remove(questId);
        }
        questProgression.put(questId, progression);
    }

    /**
     * For internal use only - called when reset via SQL
     *
     * @deprecated Do not call
     */
    @Deprecated
    public void clear() {
        guild = null;
        profession = null;
        influence = 0;
        experience = 0;
        karma = 0;
        mana = 0;

        unloading = false;
    }

    /**
     * For internal use only - called when loaded in SQL.
     *
     * @deprecated Do not call
     */
    @Deprecated
    public void initialise(final UUID guild, final UUID profession,
            final UUID secondProfession, final int influence, final float exp,
            final float karma, float mana, final Map<UUID, String> progression,
            final PlayerSettings settings) {
        this.guild = guild;
        this.profession = profession;
        this.influence = influence;
        this.secondProfession = secondProfession;
        experience = exp;
        this.karma = karma;
        questProgression = progression;
        this.mana = mana;
        this.settings = settings;

        loaded = true;
    }

    public UUID getSecondProfession() {
        if (loaded) {
            return secondProfession;
        } else {
            return null;
        }
    }

    public void setSecondProfession(UUID secondProfession) {
        if (loaded && !unloading) {
            this.secondProfession = secondProfession;
        }
    }
}
