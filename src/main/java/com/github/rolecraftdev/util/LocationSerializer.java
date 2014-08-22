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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

/**
 * A class used for transforming a {@link Location} to a serialized
 * {@link String} and back again. Used for storing data
 */
public class LocationSerializer {
    /**
     * Transforms the given {@link String} into a {@link Location}, assuming
     * the given {@link String} is a valid serialized {@link Location}, as can
     * be obtained via the {@link #serialize(Location)} method
     *
     * @param serial The serialized {@link String} to deserialize
     * @return A {@link Location} deserialized from the given {@link String}
     * @throws IllegalArgumentException If the provided serialized
     *                                  {@link String} isn't valid
     */
    public static Location deserialize(final String serial)
            throws IllegalArgumentException {
        if (serial == null) {
            return null;
        }

        final String[] values = serial.split(",");
        if (values.length != 6) {
            throw new IllegalArgumentException(
                    "ID:1 " + serial + " is not a valid serialization!");
        }

        final World world = Bukkit.getServer()
                .getWorld(UUID.fromString(values[0]));
        if (world == null) {
            throw new IllegalArgumentException(
                    "ID:2 " + values[0] + " is not a world on this server!");
        }

        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;

        try {
            x = Double.valueOf(values[1]);
            y = Double.valueOf(values[2]);
            z = Double.valueOf(values[3]);
            pitch = Float.valueOf(values[4]);
            yaw = Float.valueOf(values[5]);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "ID:3 " + "Could not cast double values: " + ex
                            .getMessage());
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Transforms the given {@link Location} into a serialized {@link String}
     * for storage
     *
     * @param loc The {@link Location} to turn into a serialized {@link String}
     * @return A serialized String representing the given {@link Location}
     */
    public static String serialize(Location loc) {
        if (loc == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(loc.getWorld().getUID().toString());
        sb.append(",");
        sb.append(loc.getX());
        sb.append(",");
        sb.append(loc.getY());
        sb.append(",");
        sb.append(loc.getZ());
        sb.append(",");
        sb.append(loc.getYaw());
        sb.append(",");
        sb.append(loc.getPitch());
        return sb.toString();
    }

    /**
     * Should never be called
     */
    private LocationSerializer() {}
}
