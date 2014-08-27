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
import org.bukkit.entity.Player;

/**
 * A utility class for player experience and levels.
 *
 * @since 0.0.5
 */
public final class LevelUtil {
    /**
     * @since 0.0.5
     */
    private LevelUtil() {
    }

    /**
     * Retrieve the level that the given amount of experience represents. The
     * algorithm for levels is:
     * <em>y = ln(0.8(x + 1.25)) * ((x + 1.25)^0.4) * 0.1 + 1</em>
     *
     * @param experience the amount of experience from which the appropriate
     *        level should be calculated
     * @return the level that corresponds to the given experience
     * @since 0.0.5
     */
    public static int getLevel(final float experience) {
        // TODO: make this a function that can, within reason, be solved for X

        // shortcut to prevent "bottoming out"
        if (experience < 0.025) {
            return 1;
        }

        float temp = (float) Math.log(0.8 * (experience + 1.25f));
        temp *= Math.pow(experience + 1.25f, 0.4f);
        temp *= 0.1f;
        temp += 1;
        return (int) Math.floor(temp);
    }

    /**
     * Retrieve the amount of experience that is needed to reach the next level
     * from the already obtained experience.
     *
     * @param experience the current amount of experience
     * @return the experience that is required to level up
     * @since 0.0.5
     */
    public static float expToNextLevel(final float experience) {
        // TODO: make this return the experience to the next level
        return -1;
    }

    public static float expFromPlayerKill(final Player killer,
            final Player killed) {
        // TODO: make this compare the levels of the two players and get an exp
        // value from that comparison
        return 1000;
    }

    /**
     * Get the amount of experience that is acquired when killing a mob of the
     * specified {@link EntityType}.
     *
     * @param entityType the {@link EntityType} that has been killed
     * @return the amount of experience gained when killing a mob of the
     *         specified {@link EntityType}
     * @since 0.0.5
     */
    public static float expFromKill(final EntityType entityType) {
        switch (entityType) {
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
            case BLAZE:
                return 50;
            case CAVE_SPIDER:
                return 100;
            case CHICKEN:
            case COW:
            case PIG:
            case SHEEP:
            case BAT:
            case SQUID:
            case SNOWMAN:
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
            case PIG_ZOMBIE:
                return 40;
            case SILVERFISH:
                return 100;
            case SPIDER:
                return 30;
            case VILLAGER:
                return 30;
            case WITCH:
                return 50;
            case WOLF:
                return 10;
            case SNOWBALL:
            case EGG:
            case DROPPED_ITEM:
                return 0;
            default:
                // account for other added EntityTypes, or hacks to add entity
                // type values to the enum (this is possible)
                return 20;
        }
    }
}
