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

import org.bukkit.entity.EntityType;

/**
 * Utilities related to player levelling and experience
 */
public final class LevelUtil {
    /**
     * Gets the level which a player with the given amount of experience would
     * have
     * <p>
     * Algorithm for levels is: y=ln(0.8(x+1.25))*((x+1.25)^0.4)*0.1+1
     *
     * @param experience - The amount of experience to get the level for
     * @return The level correlating to the given amount of experience
     */
    public static final int getLevel(float experience) {
        // TODO: make this a function that can, within reason, be solved for X
        // short circut to prevent "bottoming out"
        if(experience < 0.025) return 1;
        float temp = (float) Math.log(0.8*(experience + 1.25f));
        temp *= Math.pow(experience + 1.25f, 0.4f);
        temp *= 0.1f;
        temp += 1;
        return (int) Math.floor(temp);
    }

    /**
     * Gets the amount of experience required to reach the next level from the
     * given amount of experience
     *
     * @param experience The current amount of experience
     * @return The amount of experience required for a player with the given
     * amount of experience to level up
     */
    public static final float expToNextLevel(float experience) {
        // TODO: make this return the experience to the next level
        return -1;
    }

    /**
     * Gets the amount of experience which is gained from killing an entity of
     * the given type
     *
     * @param entityType The type of entity to get the kill exp for
     * @return The amount of experience gained from killing the given entity
     */
    public static final float expFromKill(final EntityType entityType) {
        switch(entityType) {
        case ZOMBIE:
            return 20;
        case CREEPER:
        case SKELETON:
            return 30;
        case WITHER:
            return 5000;
        case ENDER_DRAGON:
            return 10000;
        case SLIME:
            return 50;
        case PLAYER:
            return 1000;
        case BAT:
            return 5;
        case BLAZE:
            return 50;
        case CAVE_SPIDER:
            return 100;
        case CHICKEN:
        case COW:
            return 5;
        case ENDERMAN:
            return 50;
        case ENDER_CRYSTAL:
            return 150;
        case GHAST:
            return 100;
        case GIANT:
            return 40;
        case HORSE:
            return 10;
        case IRON_GOLEM:
            return 120;
        case MAGMA_CUBE:
            return 50;
        case MUSHROOM_COW:
            return 100;
        case OCELOT:
            return 50;
        case PIG:
            return 5;
        case PIG_ZOMBIE:
            return 40;
        case SHEEP:
            return 5;
        case SILVERFISH:
            return 100;
        case SNOWMAN:
            return 5;
        case SPIDER:
            return 30;
        case SQUID:
            return 5;
        case VILLAGER:
            return 30;
        case WITCH:
            return 50;
        case WOLF:
            return 10;
        default:
            return 0;
        }
    }

    /**
     * Do not call.
     */
    private LevelUtil() {}
}
