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
 * A utility class for serialising and deserialising {@link Location}s.
 *
 * @since 0.0.5
 */
public class LocationSerializer {
    /**
     * @since 0.0.5
     */
    private LocationSerializer() {
    }

    /**
     * Deserialise the given string.
     *
     * @param serial the string that is to be deserialised
     * @return the constructed {@link Location}
     * @throws IllegalArgumentException when the given string can't be
     *         deserialised
     * @since 0.0.5
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

        final World world = Bukkit.getServer().getWorld(
                UUID.fromString(values[0]));
        if (world == null) {
            throw new IllegalArgumentException(
                    "ID:2 " + values[0] + " is not a world on this server!");
        }

        double x;
        double y;
        double z;
        float yaw;
        float pitch;

        try {
            x = Double.valueOf(values[1]);
            y = Double.valueOf(values[2]);
            z = Double.valueOf(values[3]);
            pitch = Float.valueOf(values[4]);
            yaw = Float.valueOf(values[5]);
        } catch (final NumberFormatException ex) {
            throw new IllegalArgumentException("ID:3 " +
                    "Could not cast double values: " + ex.getMessage());
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Serialise the given {@link Location}.
     *
     * @param loc the {@link Location} that is to be serialised
     * @return the constructed string
     * @since 0.0.5
     */
    public static String serialize(final Location loc) {
        if (loc == null) {
            return null;
        }

        return loc.getWorld().getUID().toString() + "," + loc.getX() + "," + loc
                .getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc
                .getPitch();
    }
}
