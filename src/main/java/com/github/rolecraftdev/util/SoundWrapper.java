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
 * A wrapper around a Bukkit {@link Sound} which contains a value for the volume
 * and pitch of the sound. Used for playing sounds when {@link
 * com.github.rolecraftdev.magic.Spell}s are cast in Rolecraft
 */
public class SoundWrapper {
    /**
     * The volume of the sound
     */
    private final float volume;
    /**
     * The pitch of the sound
     */
    private final float pitch;
    /**
     * The Bukkit {@link Sound} object this object is a wrapper around
     */
    private final Sound sound;

    /**
     * Constructs a new {@link SoundWrapper} around the given {@link Sound} with
     * the given volume and pitch
     *
     * @param sound the {@link Sound} to wrap
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public SoundWrapper(Sound sound, float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    /**
     * Plays the Bukkit {@link Sound} being wrapped at the volume and pitch
     * contained by this {@link SoundWrapper} at the given {@link Location}
     *
     * @param location the {@link Location} to play the sound at
     */
    public void play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    /**
     * The sound used for most {@link com.github.rolecraftdev.magic.Spell}s
     */
    public static final SoundWrapper defaultSound = new SoundWrapper(
            Sound.ENDERMAN_TELEPORT, 0.8f, 0.0f);
    /**
     * The sound used for {@link com.github.rolecraftdev.magic.Spell}s involving
     * bows
     */
    public static final SoundWrapper bowSound = new SoundWrapper(
            Sound.SHOOT_ARROW, 1.0f, 0.0f);
    /**
     * The sound used for {@link com.github.rolecraftdev.magic.Spell}s involving
     * swords
     */
    public static final SoundWrapper swordSound = new SoundWrapper(
            Sound.SUCCESSFUL_HIT, 1.0f, 0.0f);
}
