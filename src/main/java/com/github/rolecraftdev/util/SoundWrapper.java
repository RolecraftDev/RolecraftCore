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

public class SoundWrapper {
    private final float volume;
    private final float pitch;
    private final Sound sound;


    public SoundWrapper(Sound sound, float volume, float pitch) {
        this.volume = volume;
        this.pitch = pitch;
        this.sound = sound;
    }

    public void play (Location location) {
        location.getWorld().playSound(location,sound,volume,pitch);
    }

    public static final SoundWrapper defaultSound = new SoundWrapper(Sound.ENDERMAN_TELEPORT,0.8f,0.0f);
    public static final SoundWrapper bowSound = new SoundWrapper(Sound.SHOOT_ARROW,1.0f,0.0f);
    public static final SoundWrapper swordSound = new SoundWrapper(Sound.SUCCESSFUL_HIT, 1.0f, 0.0f);
}
