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

import javax.annotation.Nullable;

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
     * Gets the amount of mana it would cost for the given {@link Player} to
     * cast the spell on the given {@link LivingEntity} with the given magic
     * modifier value, without actually casting the spell.
     *
     * @param ply the {@link Player} to check the cast cost for
     * @param entity the {@link LivingEntity} the spell is being cast on
     * @param modifier the magic modifier of the player
     * @return the same as {@link #attack(Player, LivingEntity, int)}, given the
     *         same values, without actually performing the action
     */
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier);

    /**
     * Gets the amount of mana it would cost for the given {@link Player} to
     * cast the spell using left click on the given {@link Block} with the given
     * magic modifier value, without actually casting the spell.
     *
     * @param ply the {@link Player} to check the cast cost for
     * @param block the {@link Block} which the spell would be cast on
     * @param modifier the magic modifier value for the player
     * @param face the {@link BlockFace} that is being faced
     * @return the same as {@link #leftClick(Player, Block, int, BlockFace)},
     *         given the same values, without performing the action
     */
    public float estimateLeftClickMana(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * Gets the amount of mana it would cost for the given {@link Player} to
     * cast the spell using right click on the given {@link Block} with the
     * given magic modifier value, without actually casting the spell.
     *
     * @param ply the {@link Player} to check the cast cost for
     * @param block the {@link Block} which the spell would be cast on
     * @param modifier the magic modifier value for the player
     * @param face the {@link BlockFace} that is being faced
     * @return the same as {@link #rightClick(Player, Block, int, BlockFace)},
     *         given the same values, without performing the action
     */
    public float estimateRightClickMana(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * Casts the spell in the form of right clicking on the given {@link Block}
     * on the given {@link BlockFace} and the given magic modifier for the given
     * {@link Player}
     *
     * @param ply the player that cast the spell
     * @param block the block that was interacted with, if air, null
     * @param modifier a modifier based on a player's profession & level
     * @param face the {@link BlockFace} being right clicked on
     * @return the mana cost of the spell
     */
    public float rightClick(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * Casts the spell in the form of left clicking on the given {@link Block}
     * on the given {@link BlockFace} and the given magic modifier for the given
     * {@link Player}
     *
     * @param ply the player that cast the spell
     * @param block the block that was interacted with, if air, null
     * @param modifier a modifier based on a player's profession & level
     * @param face the {@link BlockFace} being left clicked on
     * @return the mana cost of the spell
     */
    public float leftClick(Player ply, Block block, int modifier,
            BlockFace face);

    /**
     * Casts the spell in the form of attacking the given {@link LivingEntity}
     * with the given magic modifier for the given {@link Player}
     *
     * @param ply the {@link Player} that cast the spell
     * @param ent the {@link LivingEntity} that was interacted with
     * @param modifier a modifier based on a player's profession & level
     * @return the mana cost of the spell
     */
    public float attack(Player ply, LivingEntity ent, int modifier);

    /**
     * Used when creating wands to cast this spell
     *
     * @return a recipe for crafting the wand used for the casting of this
     * {@link Spell}
     */
    public Recipe getWandRecipe();

    /**
     * Gets the {@link SoundWrapper} around the Bukkit Sound which is played
     * when this {@link Spell} is cast. This may return {@code null} if there is
     * no specific sound
     *
     * @return the sound to play when this spell is cast, or {@code null}
     */
    @Nullable
    public SoundWrapper getSound();
}
