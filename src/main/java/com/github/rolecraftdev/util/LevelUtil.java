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
public class LevelUtil {
    /**
     * Gets the level which a player with the given amount of experience would
     * have
     *
     * @param experience - The amount of experience to get the level for
     * @return The level correlating to the given amount of experience
     */
    public static final int getLevel(float experience) {
        // TODO: make this return a level given experience
        return -1;
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
        // TODO: make this return exp gain from killing the given entity type
        return 0;
    }
}
