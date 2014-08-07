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

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Utils {

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
    
    public static LivingEntity getLivingTarget(Player player, int range) {
        Block[] bs = player.getLineOfSight(null, range).toArray(new Block[0]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block b : bs) {
            for (Entity e : near) {
                if (e.getLocation().distance(b.getLocation()) < 2) {
                    if(e instanceof LivingEntity)
                        return (LivingEntity) e;
                }
            }
        }
        return null;
    }
}
