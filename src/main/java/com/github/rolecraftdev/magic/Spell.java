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
package com.github.rolecraftdev.magic;

import com.github.rolecraftdev.util.SoundWrapper;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

/**
 * Represents a spell in Rolecraft
 */
public interface Spell {
    /**
     * Gets the name of this spell
     *
     * @return The name of this spell
     */
    public String getName();

    /**
     * Used to test if a player has enough mana to perform this spell
     *
     * @param ply      The
     * @param entity
     * @param modifier
     * @return Should return the same as attack(Player, LivingEntity, int),
     * given the same values, without preforming the action
     */
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier);

    /**
     * Used to test if a player has enough mana to perform this spell
     *
     * @param ply      The
     * @param block
     * @param modifier
     * @param face     The BlockFace that is being faced
     * @return Should return the same as leftClick(Player, Block, int),
     * given the same values, without preforming the action
     */
    public float estimateLeftClickMana(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * Used to test if a player has enough mana to perform this spell
     *
     * @param ply      The
     * @param block
     * @param modifier
     * @param face
     * @return Should return the same as rightClick(Player, Block, int),
     * given the same values, without preforming the action
     */
    public float estimateRightClickMana(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * @param ply      The player that cast the spell
     * @param block    The block that was interacted with, if air, null
     * @param modifier A modifier based on a player's profession
     * @param face
     * @return the cost in mana
     */
    public float rightClick(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * @param ply      The player that cast the spell
     * @param block    The block that was interacted with, if air, null
     * @param modifier A modifier based on a player's profession
     * @param face
     * @return the cost in mana
     */
    public float leftClick(Player ply, Block block, int modifier,
            BlockFace face);

    public float attack(Player ply, LivingEntity ent, int modifier);

    /**
     * Used when creating wands to cast this spell
     *
     * @return a recipe for crafting
     */
    public Recipe getWandRecipe();

    public SoundWrapper getSound();
}
