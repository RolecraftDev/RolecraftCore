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

/**
 * Represents a rectangular 2-dimensional region on the horizontal plane.
 *
 * @since 0.0.5
 */
public class Region2D {
    /**
     * The lowest x value in the region
     */
    private final int minX;
    /**
     * The lowest z value in the region
     */
    private final int minZ;
    /**
     * The highest x value in the region
     */
    private final int maxX;
    /**
     * The highest z value in the region
     */
    private final int maxZ;

    /**
     * Constructor. Automatically checks which x and z values are smaller and
     * bigger to set minX / maxX and minZ / maxZ
     *
     * @param x1 the first x-coordinate
     * @param z1 the first z-coordinate
     * @param x2 the second x-coordinate
     * @param z2 the second z-coordinate
     * @since 0.0.5
     */
    public Region2D(final int x1, final int z1, final int x2, final int z2) {
        minX = Math.min(x1, x2);
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxZ = Math.max(z1, z2);
    }

    /**
     * Get the lowest x-coordinate.
     *
     * @return the lowest x-coordinate
     * @since 0.0.5
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Get the lowest z-coordinate.
     *
     * @return the lowest z-coordinate
     * @since 0.0.5
     */
    public int getMinZ() {
        return minZ;
    }

    /**
     * Get the highest x-coordinate.
     *
     * @return the highest x-coordinate
     * @since 0.0.5
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Get the highest z-coordinate.
     *
     * @return the highest z-coordinate
     * @since 0.0.5
     */
    public int getMaxZ() {
        return maxZ;
    }

    /**
     * Check whether a {@link Location} lies between the vertices of this plane,
     * and thus ignoring its height.
     *
     * @param location the location that should be inspected
     * @return {@code true} if the given location lies between the vertices of
     *         this plane, otherwise {@code false}
     * @since 0.0.5
     */
    public boolean containsLocation(final Location location) {
        final double x = location.getX();
        final double z = location.getZ();

        return x >= minX && z >= minZ && x <= maxX && z <= maxZ;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String toString() {
        return "R2D:" + minX + ";" + minZ + ";" + maxX + ";" + maxZ;
    }

    /**
     * Retrieve a new {@link Region2D} from a string that in the format used by
     * {@link #toString()}.
     *
     * @param string the string that should be parsed to construct a new
     *        {@link Region2D} object
     * @return the constructed {@link Region2D}
     * @since 0.0.5
     */
    public static Region2D fromString(final String string) {
        final String[] split = string.split(":")[1].split(";");
        return new Region2D(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                Integer.parseInt(split[3]));
    }
}
