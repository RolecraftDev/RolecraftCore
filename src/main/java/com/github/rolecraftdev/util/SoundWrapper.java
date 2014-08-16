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
