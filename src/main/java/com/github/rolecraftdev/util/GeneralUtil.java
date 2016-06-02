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

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import pw.ian.albkit.util.Rand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

/**
 * A utility class for general use.
 *
 * @since 0.0.5
 */
public final class GeneralUtil {
    private static final Random rand = Rand.r;
    private static final float VELOCITY_FACTOR = 0.2f;
    private static final float SMALL_VELOCITY_FACTOR = 0.05f;

    public static void copyInputStreamToFile(InputStream stream, File dest)
            throws IOException {
        if (stream == null || dest == null) {
            throw new IllegalArgumentException();
        }

        final byte[] buffer = new byte[1024 * 4];

        try {
            final FileOutputStream output = new FileOutputStream(dest);
            int n;
            while ((n = stream.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            output.close();
        } finally {
            stream.close();
        }
    }

    /**
     * Retrieve the {@link Entity} that is targeted by the given player within
     * the given range.
     *
     * @param player the player who targets
     * @param range the maximum distance to the {@link Entity}
     * @return the targeted {@link Entity}
     * @since 0.0.5
     * @see #getLivingTarget(Player, int)
     */
    public static Entity getTarget(final Player player, final int range) {
        @SuppressWarnings("deprecation")
        final List<Block> blocks = player.getLineOfSight(null, range);
        final Block[] blockArray = blocks.toArray(new Block[blocks.size()]);
        final List<Entity> near = player.getNearbyEntities(range, range, range);
        for (final Block block : blockArray) {
            for (final Entity e : near) {
                if (e.getLocation().distance(block.getLocation()) < 2) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the {@link LivingEntity} that is targeted by the given player
     * within the given range.
     *
     * @param player the player who targets
     * @param range the maximum distance to the {@link LivingEntity}
     * @return the targeted {@link LivingEntity}
     * @since 0.0.5
     * @see #getTarget(Player, int)
     */
    public static LivingEntity getLivingTarget(final Player player,
            final int range) {
        @SuppressWarnings("deprecation")
        final
        List<Block> blocks = player.getLineOfSight(null, range);
        final Block[] blockArray = blocks.toArray(new Block[blocks.size()]);
        final List<Entity> near = player.getNearbyEntities(range, range, range);
        for (final Block block : blockArray) {
            for (final Entity e : near) {
                if (e.getLocation().distance(block.getLocation()) < 2) {
                    if (e instanceof LivingEntity) {
                        return (LivingEntity) e;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Acquire the direction the given player is facing towards as unit vector.
     *
     * @param ply the player of whom the facing unit vector is wanted
     * @return the given player's facing unit vector
     * @since 0.0.5
     */
    public static Vector getUnitVectorFacing(final Player ply) {
        final double x = -Math.sin(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        final double z = Math.cos(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        final double y = -Math
                .sin(Math.toRadians(ply.getLocation().getPitch()));
        return new Vector(x, y, z);
    }

    /**
     * Modify the given speed slightly and semi-randomly by using
     * {@link #VELOCITY_FACTOR}.
     *
     * @param original the speed that should be modified ever so slightly
     * @return the slightly modified given speed
     * @since 0.0.5
     * @see #smallVelocityRandomiser(float)
     */
    public static float velocityRandomiser(final float original) {
        float velocity = original - VELOCITY_FACTOR;
        velocity += (2 * VELOCITY_FACTOR) * rand.nextFloat();
        return velocity;
    }

    /**
     * Modify the given {@link Vector} slightly and semi-randomly by using
     * {@link #velocityRandomiser(float)}.
     *
     * @param original the {@link Vector} that should be modified ever so
     *        slightly
     * @return the slightly modified given {@link Vector}
     * @since 0.0.5
     * @see #smallVelocityRandomiser(Vector)
     */
    public static Vector velocityRandomiser(final Vector original) {
        return new Vector(velocityRandomiser((float) original.getX()),
                velocityRandomiser((float) original.getY()),
                velocityRandomiser((float) original.getZ()));
    }

    /**
     * Modify the given speed slightly and semi-randomly by using
     * {@link #SMALL_VELOCITY_FACTOR}.
     *
     * @param original the speed that should be modified ever so slightly
     * @return the slightly modified given speed
     * @since 0.0.5
     * @see #velocityRandomiser(float)
     */
    public static float smallVelocityRandomiser(final float original) {
        float velocity = original - SMALL_VELOCITY_FACTOR;
        velocity += (2 * SMALL_VELOCITY_FACTOR) * rand.nextFloat();
        return velocity;
    }

    /**
     * Modify the given {@link Vector} slightly and semi-randomly by using
     * {@link #smallVelocityRandomiser(float)}.
     *
     * @param original the {@link Vector} that should be modified ever so
     *        slightly
     * @return the slightly modified given {@link Vector}
     * @since 0.0.5
     * @see #velocityRandomiser(Vector)
     */
    public static Vector smallVelocityRandomiser(final Vector original) {
        return new Vector(smallVelocityRandomiser((float) original.getX()),
                smallVelocityRandomiser((float) original.getY()),
                smallVelocityRandomiser((float) original.getZ()));
    }

    /**
     * @since 0.0.5
     */
    private GeneralUtil() {
        throw new UnsupportedOperationException();
    }
}
