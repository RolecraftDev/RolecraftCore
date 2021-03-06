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
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.experience.RCExperienceChangeEvent;
import com.github.rolecraftdev.event.experience.RCExperienceEvent.ChangeReason;
import com.github.rolecraftdev.experience.ExperienceHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionRule;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Holds persistent Rolecraft data for a player.
 *
 * @since 0.0.5
 */
public final class PlayerData {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
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
     * Whether the data is wholly loaded.
     */
    private volatile boolean loaded;
    /**
     * Whether the data is currently in the phase of being unloaded.
     */
    private volatile boolean unloading;

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
     * Constructor.
     *
     * @param playerId the {@link UUID} of the owner of this data
     * @param name the username of the owner of this data
     * @since 0.0.5
     */
    public PlayerData(final RolecraftCore plugin, @Nonnull final UUID playerId,
            @Nonnull final String name) {
        this.playerId = playerId;
        this.name = name;
        this.plugin = plugin;

        settings = PlayerSettings.defaults();
    }

    /**
     * Get the {@link UUID} of the player who owns this data.
     *
     * @return the owner's {@link UUID}
     * @since 0.0.5
     */
    @Nonnull
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Get the username of the player who owns this data.
     *
     * @return the owner's username
     * @since 0.0.5
     */
    @Nonnull
    public String getPlayerName() {
        return name;
    }

    /**
     * Return the accompanied {@link PlayerSettings}.
     *
     * @return the owner's {@link PlayerSettings}
     * @since 0.0.5
     */
    @Nonnull
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
    @Nullable
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
    @Nullable
    public UUID getProfession() {
        if (loaded) {
            return profession;
        } else {
            return null;
        }
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
            return ExperienceHelper.getLevel(experience);
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
            return ExperienceHelper.expToNextLevel(experience);
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
    public void setUnloading(final boolean unload) {
        unloading = unload;
    }

    /**
     * Set the status of the loading phase. Calling this from anywhere save DAO
     * classes is ill-advised.
     *
     * @param loaded the new loading status
     * @since 0.0.5
     */
    public void setLoaded(final boolean loaded) {
        this.loaded = loaded;

        if (loaded) {
            RolecraftEventFactory.playerDataLoaded(this);
        }
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
     * @see #setInfluence(int)
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
     * @see #setInfluence(int)
     * @see #getInfluence()
     */
    public void subtractInfluence(final int influence) {
        setInfluence(getInfluence() - influence);
    }

    /**
     * Set the experience of the owner. When invoked, a new
     * {@link RCExperienceChangeEvent} will be called and used to decide upon further
     * execution. All of this will only happen when this is loaded and not in
     * the unloading phase.
     *
     * @param amount the owner's new experience amount
     * @param reason the reason for this change
     * @since 0.0.5
     */
    public void setExperience(final float amount, final ChangeReason reason) {
        if (loaded && !unloading) {
            final RCExperienceChangeEvent event = RolecraftEventFactory
                    .callRCExpEvent(plugin, Bukkit.getPlayer(playerId),
                            amount - experience, reason);
            if (!event.isCancelled()) {
                this.experience = event.getNewExperience();
            }
        }
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
     * @since 0.0.5
     */
    public void addExperience(final float amount,
            @Nonnull final ChangeReason reason) {
        setExperience(getExperience() + amount, reason);
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
    public void subtractExperience(final float amount,
            final ChangeReason reason) {
        setExperience(getExperience() - amount, reason);
    }

    /**
     * Set the karma of the owner. This action will only be completed when this
     * is loaded and not in the unloading phase.
     *
     * @param karma the owner's new karma level
     * @since 0.0.5
     */
    public void setKarma(final float karma) {
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
    public void addKarma(final float amount) {
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
    public void subtractKarma(final float amount) {
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
    public void setMana(final float newMana) {
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
    public void addMana(final float amount) {
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
    public void subtractMana(final float amount) {
        setMana(getMana() - amount);
    }

    /**
     * Get the rate at which mana should be regenerated per 2 seconds.
     *
     * @return the owner's mana regeneration rate
     * @since 0.0.5
     */
    public float getManaRegenRate() {
        float constant = plugin.getConfigValues().getManaRegenConstant();

        if (this.profession != null) {
            final Profession profession = plugin.getProfessionManager()
                    .getProfession(this.profession);
            final Float temp = profession
                    .getRuleValue(ProfessionRule.MANA_REGEN_CONSTANT);

            if (temp != null) {
                constant = temp;
            }
        }

        return (float) ((constant * 2) + (getLevel() * 0.5));
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
     * @param settings the owner's {@link PlayerSettings}
     * @since 0.0.5
     * @deprecated for internal use only
     */
    @Deprecated
    public void initialise(final UUID guild, final UUID profession,
            final UUID secondProfession, final int influence, final float exp,
            final float karma, final float mana,
            final PlayerSettings settings) {
        this.guild = guild;
        this.profession = profession;
        this.influence = influence;
        this.secondProfession = secondProfession;
        experience = exp;
        this.karma = karma;
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
    @Nullable
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
    public void setSecondProfession(final UUID secondProfession) {
        if (loaded && !unloading) {
            this.secondProfession = secondProfession;
        }
    }
}
