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

import java.util.List;
import java.util.Random;

/**
 * General utility methods for Rolecraft
 */
public final class Utils {
    private static Random rand = Rand.r;
    private static final float VELOCITY_FACTOR = 0.2f;
    private static final float SMALL_VELOCITY_FACTOR = 0.05f;

    /**
     * Gets the entity targeted by the given player. If there is no entity
     * being targeted by the player within the given range, null is returned
     *
     * @param player The Player to get the entity target for
     * @param range  The maximum distance the target can be from the player
     * @return The entity targeted by the given player, or null if there isn't
     * one within the given range
     */
    public static Entity getTarget(final Player player, final int range) {
        @SuppressWarnings("deprecation")
        List<Block> blocks = player.getLineOfSight(null, range);
        Block[] blockArray = blocks.toArray(new Block[blocks.size()]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block block : blockArray) {
            for (Entity e : near) {
                if (e.getLocation().distance(block.getLocation()) < 2) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Gets the living entity targeted by the given player. If there is no
     * living entity being targeted by the player within the given range, null
     * is returned
     *
     * @param player The Player to get the living entity target for
     * @param range  The maximum distance the target can be from the player
     * @return The living entity targeted by the given player, or null if there
     * isn't one within the given range
     */
    public static LivingEntity getLivingTarget(final Player player,
            final int range) {
        @SuppressWarnings("deprecation")
        List<Block> blocks = player.getLineOfSight(null, range);
        Block[] blockArray = blocks.toArray(new Block[blocks.size()]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block block : blockArray) {
            for (Entity e : near) {
                if (e.getLocation().distance(block.getLocation()) < 2) {
                    if (e instanceof LivingEntity) {
                        return (LivingEntity) e;
                    }
                }
            }
        }
        return null;
    }

    public static Vector getUnitVectorFacing(final Player ply) {
        double x = -Math.sin(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        double z = Math.cos(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        double y = -Math.sin(Math.toRadians(ply.getLocation().getPitch()));
        return new Vector(x, y, z);
    }

    /**
     * Changes float values slightly to create the effect of randomness, based
     * on VELOCITY_FACTOR
     *
     * @param original The original velocity
     * @return A randomly modified version of the given float
     */
    public static float velocityRandomiser(final float original) {
        float velocity = original - VELOCITY_FACTOR;
        velocity += (2 * VELOCITY_FACTOR) * rand.nextFloat();
        return velocity;
    }

    /**
     * Convenience method for {@link Utils#velocityRandomiser(float)}, applies
     * to X, Y, and Z
     *
     * @param original The original velocity to randomise
     * @return A randomly modified version of the given {@link Vector} velocity
     */
    public static Vector velocityRandomiser(final Vector original) {
        return new Vector(velocityRandomiser((float) original.getX()),
                velocityRandomiser((float) original.getY()),
                velocityRandomiser((float) original.getZ()));
    }

    /**
     * Same as {@link Utils#velocityRandomiser(float)}, except with 1/4 of the
     * effect
     *
     * @param original The original velocity to slightly randomise
     * @return A slightly randomly modified version of the given velocity float
     */
    public static float smallVelocityRandomiser(final float original) {
        float velocity = original - SMALL_VELOCITY_FACTOR;
        velocity += (2 * SMALL_VELOCITY_FACTOR) * rand.nextFloat();
        return velocity;
    }

    /**
     * Same as {@link Utils#velocityRandomiser(Vector)}, except with 1/4 of the
     * effect
     *
     * @param original The {@link Vector to randomise}
     * @return A slightly randomised version of the given {@link Vector}
     */
    public static Vector smallVelocityRandomiser(final Vector original) {
        return new Vector(smallVelocityRandomiser((float) original.getX()),
                smallVelocityRandomiser((float) original.getY()),
                smallVelocityRandomiser((float) original.getZ()));
    }

    // prevent construction
    private Utils() {
    }
}
