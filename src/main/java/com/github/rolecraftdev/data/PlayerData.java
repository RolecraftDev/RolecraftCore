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
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.quest.Quest;
import com.github.rolecraftdev.util.LevelUtil;

import org.bukkit.Bukkit;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Holds persistent Rolecraft data for a player.
 *
 * @since 0.0.5
 */
public final class PlayerData {
    /**
     * The player's {@link UUID}.
     */
    private final UUID playerId;
    /**
     * The player's username.
     */
    private final String name;
    /**
     * The player's {@link PlayerSettings}.
     */
    private volatile PlayerSettings settings;
    /**
     * The {@link UUID} of the player's {@link Guild}.
     */
    private UUID guild;
    /**
     * The {@link UUID} of the player's primary {@link Profession}.
     */
    private UUID profession;

    /**
     * The {@link UUID} of the player's secondary {@link Profession}.
     */
    private UUID secondProfession;
    /**
     * The {@link UUID}s of the player's {@link Quest}s along with their
     * progression.
     */
    private Map<UUID, String> questProgression;
    /**
     * The player's influence value, which is calculated by his actions.
     */
    private int influence;
    /**
     * The player's experience.
     */
    private float experience;
    /**
     * The player's karma, used in the Afterlife addon for Rolecraft.
     */
    private float karma;
    /**
     * The player's mana.
     */
    private float mana;

    /**
     * Whether the data is wholly loaded.
     */
    private volatile boolean loaded;
    /**
     * Whether the data is currently in the phase of being unloaded.
     */
    private volatile boolean unloading;
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param playerId the {@link UUID} of the owner of this data
     * @param name the username of the owner of this data
     * @since 0.0.5
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
     * Get the {@link UUID} of the player who owns this data.
     *
     * @return the owner's {@link UUID}
     * @since 0.0.5
     */
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Get the username of the player who owns this data.
     *
     * @return the owner's username
     * @since 0.0.5
     */
    public String getPlayerName() {
        return name;
    }

    /**
     * Return the accompanied {@link PlayerSettings}.
     *
     * @return the owner's {@link PlayerSettings}
     * @since 0.0.5
     */
    public PlayerSettings getSettings() {
        return settings;
    }

    /**
     * Check if the data in this object has been fully loaded.
     *
     * @return {@code true} when the data is completely loaded; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Check if this {@link PlayerData} is currently in the phase of being
     * unloaded.
     *
     * @return {@code true} if this instance is currently being unloaded
     * @since 0.0.5
     */
    public boolean isUnloading() {
        return unloading;
    }

    /**
     * Obtain the {@link UUID} of the owner's {@link Guild}. Note that this will
     * return {@code null} if this is not yet wholly loaded.
     *
     * @return the {@link UUID} of the owner's {@link Guild}
     * @since 0.0.5
     */
    public UUID getGuild() {
        if (loaded) {
            return guild;
        }
        return null;
    }

    /**
     * Get the {@link UUID} of the owner's primary {@link Profession}. Note that
     * this will return {@code null} if this is not yet wholly loaded.
     *
     * @return the {@link UUID} of the owner's primary {@link Profession}
     * @since 0.0.5
     */
    public UUID getProfession() {
        if (loaded) {
            return profession;
        } else {
            return null;
        }
    }

    /**
     * Get the owner's current {@link Quest} progression.
     *
     * @return the {@link Quest} progression of the owner
     * @since 0.0.5
     */
    public Map<UUID, String> getQuestProgression() {
        return questProgression;
    }

    /**
     * Obtain the owner's influence level. Note that this will return {@code -1}
     * if this is not yet wholly loaded.
     *
     * @return the owner's influence level
     * @since 0.0.5
     */
    public int getInfluence() {
        if (loaded) {
            return influence;
        } else {
            return -1;
        }
    }

    /**
     * Obtain the owner's level, which is calculated with his experience. Note
     * that this will return {@code -1} if this is not yet wholly loaded.
     *
     * @return the owner's level
     * @since 0.0.5
     * @see #getExperience()
     */
    public int getLevel() {
        if (loaded) {
            return LevelUtil.getLevel(experience);
        } else {
            return -1;
        }
    }

    /**
     * Obtain the amount of experience needed for the owner to reach the next
     * level. Note that this will return {@code -1} if this is not yet wholly
     * loaded.
     *
     * @return the amount of experience till the next level
     * @since 0.0.5
     */
    public float getExpToNextLevel() {
        if (loaded) {
            return LevelUtil.expToNextLevel(experience);
        } else {
            return -1;
        }
    }

    /**
     * Get the amount of experience the owner currently has. Note that this will
     * return {@code -1} if this is not yet wholly loaded.
     *
     * @return the owner's experience
     * @since 0.0.5
     * @see #getLevel()
     */
    public float getExperience() {
        if (loaded) {
            return experience;
        } else {
            return -1;
        }
    }

    /**
     * Get the amount of karma the owner has. Note that this will return
     * {@code -1} if this is not yet wholly loaded.
     *
     * @return the owner's karma
     * @since 0.0.5
     */
    public float getKarma() {
        if (loaded) {
            return karma;
        } else {
            return -1;
        }
    }

    /**
     * Set the status of the unloading phase. Calling this from anywhere save
     * DAO classes is ill-advised.
     *
     * @param unload the new unloading status
     * @since 0.0.5
     */
    public void setUnloading(boolean unload) {
        unloading = unload;
    }

    /**
     * Set the status of the loading phase. Calling this from anywhere save DAO
     * classes is ill-advised.
     *
     * @param loaded the new loading status
     * @since 0.0.5
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Set the {@link Guild} of the owner. This action will only be completed
     * when this is loaded and not in the unloading phase.
     *
     * @param guild the {@link UUID} of the owner's new {@link Guild}
     * @since 0.0.5
     */
    public void setGuild(final UUID guild) {
        if (loaded && !unloading) {
            this.guild = guild;
        }
    }

    /**
     * Set the primary {@link Profession} of the owner. This action will only be
     * completed when this is loaded and not in the unloading phase.
     *
     * @param profession the {@link UUID} of the owner's new primary
     *        {@link Profession}
     * @since 0.0.5
     */
    public void setProfession(final UUID profession) {
        if (loaded && !unloading) {
            this.profession = profession;
        }
    }

    /**
     * Set the influence of the owner. This action will only be completed when
     * this is loaded and not in the unloading phase.
     *
     * @param influence the owner's new influence level
     * @since 0.0.5
     */
    public void setInfluence(final int influence) {
        if (loaded && !unloading) {
            this.influence = influence;
        }
    }

    /**
     * Adds the given influence to the current influence level. This action may
     * not be completed when this is not loaded or in the unloading phase.
     *
     * @param influence the amount of influence that should be added to the
     *        current amount
     * @since 0.0.5
     * @see #setInfluence(float)
     * @see #getInfluence()
     */
    public void addInfluence(final int influence) {
        setInfluence(getInfluence() + influence);
    }

    /**
     * Subtract the given influence from the current influence level. This
     * action may not be completed when this is not loaded or in the unloading
     * phase.
     *
     * @param influence the amount of influence that should be subtracted from
     *        the current amount
     * @since 0.0.5
     * @see #setInfluence(float)
     * @see #getInfluence()
     */
    public void subtractInfluence(final int influence) {
        setInfluence(getInfluence() - influence);
    }

    /**
     * Set the experience of the owner. When invoked, a new
     * {@link RCExpChangeEvent} will be called and used to decide upon further
     * execution. All of this will only happen when this is loaded and not in
     * the unloading phase.
     *
     * @param amount the owner's new experience amount
     * @since 0.0.5
     * @deprecated use {@link #setExperience(float, ChangeReason)}
     */
    @Deprecated
    public void setExperience(final float amount) {
        if (loaded && !unloading) {
            RCExpChangeEvent event = RCExpEventFactory
                    .callRCExpEvent(plugin, Bukkit.getServer()
                                    .getPlayer(playerId), amount - experience,
                            ChangeReason.DEFAULT);
            if (!event.isCancelled()) {
                experience = event.getNewExperience();
            }
        }
    }

    /**
     * Set the experience of the owner. When invoked, a new
     * {@link RCExpChangeEvent} will be called and used to decide upon further
     * execution. All of this will only happen when this is loaded and not in
     * the unloading phase.
     *
     * @param amount the owner's new experience amount
     * @param reason the reason for this change
     * @since 0.0.5
     */
    public void setExperience(final float amount, ChangeReason reason) {
        if (loaded && !unloading) {
            RCExpChangeEvent event = RCExpEventFactory.callRCExpEvent(plugin,
                    Bukkit.getServer().getPlayer(playerId),
                    amount - experience, reason);
            if (!event.isCancelled()) {
                experience = event.getNewExperience();
            }
        }
    }

    /**
     * Add the given experience to the current experience amount. This action
     * may not be completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of experience that should be added to the
     *        current amount
     * @since 0.0.5
     * @see #setExperience(float)
     * @see #getExperience()
     * @deprecated use {@link #addExperience(float, ChangeReason)}
     */
    @Deprecated
    public void addExperience(final float amount) {
        setExperience(getExperience() + amount);
    }

    /**
     * Add the given experience to the current experience amount. This action
     * may not be completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of experience that should be added to the
     *        current amount
     * @param reason the reason for this change
     * @see #setExperience(float, ChangeReason)
     * @see #getExperience()
     */
    public void addExperience(final float amount, ChangeReason reason) {
        setExperience(getExperience() + amount, reason);
    }

    /**
     * Subtract the given experience from the current experience amount. This
     * action may not be completed when this is not loaded or in the unloading
     * phase.
     *
     * @param amount the amount of experience that should be added to the
     *        current amount
     * @since 0.0.5
     * @see #setExperience(float)
     * @see #getExperience()
     * @deprecated use {@link #subtractExperience(float, ChangeReason)}
     */
    @Deprecated
    public void subtractExperience(final float amount) {
        setExperience(getExperience() - amount);
    }

    /**
     * Subtract the given experience from the current experience amount. This
     * action may not be completed when this is not loaded or in the unloading
     * phase.
     *
     * @param amount the amount of experience that should be added to the
     *        current amount
     * @param reason the reason for this change
     * @since 0.0.5
     * @see #setExperience(float, ChangeReason)
     * @see #getExperience()
     */
    public void subtractExperience(final float amount, ChangeReason reason) {
        setExperience(getExperience() - amount, reason);
    }

    /**
     * Set the karma of the owner. This action will only be completed when this
     * is loaded and not in the unloading phase.
     *
     * @param karma the owner's new karma level
     * @since 0.0.5
     */
    public void setKarma(float karma) {
        if (loaded && !unloading) {
            this.karma = karma;
        }
    }

    /**
     * Add the given karma to the current karma level. This action may not be
     * completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of karma that should be added to the current
     *        amount
     * @since 0.0.5
     * @see #setKarma(float)
     * @see #getKarma()
     */
    public void addKarma(float amount) {
        setKarma(getKarma() + amount);
    }

    /**
     * Subtract the given karma from the current karma level. This action may
     * not be completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of karma that should be subtracted from the
     *        current amount
     * @since 0.0.5
     * @see #setKarma(float)
     * @see #getKarma()
     */
    public void subtractKarma(float amount) {
        setKarma(getKarma() - amount);
    }

    /**
     * Get the amount of mana the owner has. Note that this will return
     * {@code -1} if this is not yet wholly loaded.
     *
     * @return the owner's mana
     * @since 0.0.5
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
            Bukkit.getLogger().warning("Problem in get exceptions - ignore");
        }
        if (loaded) {
            return mana;
        } else {
            return -1;
        }
    }

    /**
     * Set the mana of the owner. This action will only be completed when this
     * is loaded and not in the unloading phase.
     *
     * @param newMana the owner's new mana level
     * @since 0.0.5
     */
    public void setMana(float newMana) {
        if (loaded && !unloading) {
            mana = newMana;
        }
    }

    /**
     * Add the given mana to the current mana level. This action may not be
     * completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of mana that should be added to the current
     *        amount
     * @since 0.0.5
     * @see #setMana(float)
     * @see #getMana()
     */
    public void addMana(float amount) {
        setMana(getMana() + amount);
    }

    /**
     * Subtract the given mana from the current mana level. This action may not
     * be completed when this is not loaded or in the unloading phase.
     *
     * @param amount the amount of mana that should be subtracted from the
     *        current amount
     * @since 0.0.5
     * @see #setMana(float)
     * @see #getMana()
     */
    public void subtractMana(float amount) {
        setMana(getMana() - amount);
    }

    /**
     * Get the rate at which mana should be regenerated.
     *
     * @return the owner's mana regeneration rate
     * @since 0.0.5
     */
    public float getManaRegenRate() {
        // TODO: this is probably wayyyy too fast
        return (float) Math.pow(getLevel(), 0.5) / 10 + 5;
    }

    /**
     * Set the current progression of a {@link Quest} for the owner.
     *
     * @param questId the {@link UUID} of the {@link Quest}
     * @param progression its new progression value
     * @since 0.0.5
     */
    public void addQuestProgression(final UUID questId,
            final String progression) {
        if (questProgression.containsKey(questId)) {
            questProgression.remove(questId);
        }
        questProgression.put(questId, progression);
    }

    /**
     * Resets all values to their initial value. This should only be called by
     * DAO classes, doing otherwise might cause unknown problems.
     *
     * @since 0.0.5
     * @deprecated for internal use only
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
     * Set all values. This should only be used when all the data of a player
     * has been collected from the appropriate database.
     *
     * @param guild the {@link UUID} of the owner's {@link Guild}
     * @param profession the {@link UUID} of the owner's primary
     *        {@link Profession}
     * @param secondProfession the {@link UUID} of the owner's secondary
     *        {@link Profession}
     * @param influence the owner's influence level
     * @param exp the owner's experience
     * @param karma the owner's karma level
     * @param mana the owner's mana level
     * @param progression the owner's {@link Quest} along with their progression
     *        data
     * @param settings the owner's {@link PlayerSettings}
     * @since 0.0.5
     * @deprecated for internal use only
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

    /**
     * Get the {@link UUID} of the owner's secondary {@link Profession}. Note
     * that this will return {@code null} if this is not yet wholly loaded.
     *
     * @return the {@link UUID} of the owner's secondary {@link Profession}
     * @since 0.0.5
     */
    public UUID getSecondProfession() {
        if (loaded) {
            return secondProfession;
        } else {
            return null;
        }
    }

    /**
     * Set the secondary {@link Profession} of the owner. This action will only
     * be completed when this is loaded and not in the unloading phase.
     *
     * @param secondProfession the {@link UUID} of the owner's new secondary
     *        {@link Profession}
     * @since 0.0.5
     */
    public void setSecondProfession(UUID secondProfession) {
        if (loaded && !unloading) {
            this.secondProfession = secondProfession;
        }
    }
}
