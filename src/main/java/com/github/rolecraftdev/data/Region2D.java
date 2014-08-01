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

import org.bukkit.Location;

/**
 * Represents a 2D region, used for storing data in SQL
 */
public class Region2D {
    private final int minX, minZ;
    private final int maxX, maxZ;

    public Region2D(final int minX, final int minZ, final int maxX,
            final int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean containsLocation(final Location location) {
        final double x = location.getX();
        final double z = location.getZ();

        return x >= minX && z >= minZ && x <= maxX && z <= maxZ;
    }

    @Override
    public String toString() {
        return "R2D:" + minX + ";" + minZ + ";" + maxX + ";" + maxZ;
    }

    public static Region2D fromString(final String string) {
        final String[] split = string.split(":")[1].split(";");
        return new Region2D(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                Integer.parseInt(split[3]));
    }
}
