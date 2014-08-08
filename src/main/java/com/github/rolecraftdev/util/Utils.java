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

import java.util.List;

/**
 * General utility methods for Rolecraft
 */
public class Utils {
    /**
     * Gets the entity targeted by the given player. If there is no entity
     * being targeted by the player within the given range, null is returned
     *
     * @param player The Player to get the entity target for
     * @param range  The maximum distance the target can be from the player
     * @return The entity targeted by the given player, or null if there isn't
     *         one within the given range
     */
    public static Entity getTarget(Player player, int range) {
        Block[] bs = player.getLineOfSight(null, range).toArray(new Block[0]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block b : bs) {
            for (Entity e : near) {
                if (e.getLocation().distance(b.getLocation()) < 2) {
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
     *         isn't one within the given range
     */
    public static LivingEntity getLivingTarget(Player player, int range) {
        Block[] bs = player.getLineOfSight(null, range).toArray(new Block[0]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block b : bs) {
            for (Entity e : near) {
                if (e.getLocation().distance(b.getLocation()) < 2) {
                    if (e instanceof LivingEntity) {
                        return (LivingEntity) e;
                    }
                }
            }
        }
        return null;
    }

    public static Vector getUnitVectorFacing(Player ply) {
        double x = -Math.sin(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        double z = Math.cos(Math.toRadians(ply.getLocation().getYaw())) *
                Math.cos(Math.toRadians(ply.getLocation().getPitch()));
        double y = -Math.sin(Math.toRadians(ply.getLocation().getPitch()));
        return new Vector(x, y, z);
    }
}
