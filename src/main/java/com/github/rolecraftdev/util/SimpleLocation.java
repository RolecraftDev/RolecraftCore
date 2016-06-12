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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * Represents a location in a specific world, in a simple, easily serializable,
 * format.
 *
 * @since 0.1.0
 */
public class SimpleLocation {
    /**
     * The x-co-ordinate of the location.
     */
    private final int x;
    /**
     * The y-co-ordinate of the location.
     */
    private final int y;
    /**
     * The z-co-ordinate of the location.
     */
    private final int z;
    /**
     * The name of the world the location is within.
     */
    @Nonnull
    private final String world;

    /**
     * Constructs a new SimpleLocation from a set of three co-ordinates plus the
     * name of the world the location is within.
     *
     * @param x the x co-ordinate of the location
     * @param y the y co-ordinate of the location
     * @param z the z co-ordinate of the location
     * @param world the name of the world the location is within. Not null
     */
    public SimpleLocation(final int x, final int y, final int z,
            @Nonnull final String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    /**
     * Constructs a new SimpleLocation at the same physical location as the
     * given Bukkit {@link Location}.
     *
     * @param location the Bukkit {@link Location} to use data from
     */
    public SimpleLocation(@Nonnull final Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                location.getWorld().getName());
    }

    /**
     * Gets the x-co-ordinate of the location.
     *
     * @return the x-co-ordinate of the location
     * @since 0.1.0
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-co-ordinate of the location.
     *
     * @return the y-co-ordinate of the location
     * @since 0.1.0
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z-co-ordinate of the location.
     *
     * @return the z-co-ordinate of the location
     * @since 0.1.0
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the name of the world the location is within. This should never
     * return {@code null}.
     *
     * @return the name of the world the location is within
     * @since 0.1.0
     */
    @Nonnull
    public String getWorldName() {
        return world;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z + "," + this.world;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SimpleLocation other = (SimpleLocation) o;
        return x == other.x && y == other.y && z == other.z
                && world.equals(other.world);

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + world.hashCode();
        return result;
    }

    private static final String splitPattern = Pattern.quote(",");

    /**
     * Gets a SimpleLocation object from a serialized string consisting of x, y
     * and z co-ordinates as well as the world name, all separated by a single
     * comma.
     *
     * @param string the serial string
     * @return a SimpleLocation from the data in the given string
     */
    @Nullable
    public static SimpleLocation fromString(@Nonnull final String string) {
        final String[] split = string.split(splitPattern);
        if (split.length < 4) {
            return null;
        }

        int x, y, z;
        try {
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            return null; // bad string
        }

        final String world = split[3];
        return new SimpleLocation(x, y, z, world);
    }
}
