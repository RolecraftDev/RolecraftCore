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
package com.github.rolecraftdev.util;

import org.bukkit.Location;
import org.bukkit.Sound;

/**
 * A wrapper class for {@link Sound}s that simplifies certain tasks.
 *
 * @since 0.0.5
 */
public class SoundWrapper {
    /**
     * The default note.
     *
     * @since 0.0.5
     */
    public static final SoundWrapper DEFAULT = new SoundWrapper(
            Sound.ENDERMAN_TELEPORT, 0.8f, 0.0f);
    /**
     * The note that should be played when an arrow is shot.
     *
     * @since 0.0.5
     */
    public static final SoundWrapper BOW = new SoundWrapper(
            Sound.SHOOT_ARROW, 1.0f, 0.0f);
    /**
     * The note that should be played when an entity has successfully struck
     * another entity.
     *
     * @since 0.0.5
     */
    public static final SoundWrapper SWORD = new SoundWrapper(
            Sound.SUCCESSFUL_HIT, 1.0f, 0.0f);

    /**
     * The volume of the {@link Sound}.
     */
    private final float volume;
    /**
     * The pitch of the {@link Sound}.
     */
    private final float pitch;
    /**
     * The {@link Sound} type.
     */
    private final Sound sound;

    /**
     * Constructor.
     *
     * @param sound the {@link Sound} type
     * @param volume the volume of the {@link Sound}
     * @param pitch the pitch of the {@link Sound}
     * @since 0.0.5
     */
    public SoundWrapper(final Sound sound, final float volume,
            final float pitch) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    /**
     * Gets the Bukkit {@link Sound} object for this SoundWrapper
     *
     * @return this SoundWrappers Bukkit {@link Sound}
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Gets the volume for this SoundWrapper
     *
     * @return this SoundWrappers volume
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Gets the pitch of this SoundWrapper
     *
     * @return this SoundWrappers pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Play the embedded {@link Sound} at the predefined volume with the
     * predefined pitch at the given {@link Location}.
     *
     * @param location the {@link Location} at which the note should be played
     * @since 0.0.5
     */
    public void play(final Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }
}
