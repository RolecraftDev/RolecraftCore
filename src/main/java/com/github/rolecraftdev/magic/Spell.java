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

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a spell which can be cast in Rolecraft.
 *
 * @since 0.0.5
 */
public interface Spell {
    /**
     * It isn't the right type of action for the spell to be cast.
     *
     * @since 0.0.5
     */
    float BAD_SITUATION = Float.MIN_NORMAL;
    /**
     * There is a failure to cast the spell.
     *
     * @since 0.0.5
     */
    float CAST_FAILURE = Float.MIN_VALUE;

    /**
     * Get the name of this {@link Spell}.
     *
     * @return the name
     * @since 0.0.5
     */
    String getName();

    /**
     * Retrieve the amount of mana that is required when performing the
     * {@link Spell} implementation in attack.
     *
     * @param caster the executor of the {@link Spell}
     * @param target the attacked {@link Entity}
     * @param modifier the magic modifier for the given player
     * @return the mana cost
     * @since 0.0.5
     * @see #attack(Player, LivingEntity, int)
     */
    float estimateAttackMana(Player caster, LivingEntity target, int modifier);

    /**
     * Retrieve the amount of mana that is required when performing the
     * {@link Spell} implementation on left-click.
     *
     * @param caster the executor of the {@link Spell}
     * @param block the {@link Block} that has been clicked
     * @param modifier the magic modifier for the given player
     * @param face the {@link BlockFace} that has been clicked
     * @return the mana cost
     * @since 0.0.5
     * @see #leftClick(Player, Block, int, BlockFace)
     */
    float estimateLeftClickMana(Player caster, Block block, int modifier,
            BlockFace face);

    /**
     * Retrieve the amount of mana that is required when performing the
     * {@link Spell} implementation on right-click.
     *
     * @param caster the executor of the {@link Spell}
     * @param block the {@link Block} that has been clicked
     * @param modifier the magic modifier for the given player
     * @param face the {@link BlockFace} that has been clicked
     * @return the mana cost
     * @since 0.0.5
     * @see #rightClick(Player, Block, int, BlockFace)
     */
    float estimateRightClickMana(Player caster, Block block, int modifier,
            BlockFace face);

    /**
     * Perform the {@link Spell} implementation on right-click.
     *
     * @param caster the executor of the {@link Spell}
     * @param block the {@link Block} that has been clicked
     * @param modifier the magic modifier for the given player
     * @param face the {@link BlockFace} that has been clicked
     * @return the mana cost, {@link #BAD_SITUATION} or {@link #CAST_FAILURE}
     * @since 0.0.5
     * @see #estimateRightClickMana(Player, Block, int, BlockFace)
     */
    float rightClick(Player caster, Block block, int modifier, BlockFace face);

    /**
     * Perform the {@link Spell} implementation on left-click.
     *
     * @param caster the executor of the {@link Spell}
     * @param block the {@link Block} that has been clicked
     * @param modifier the magic modifier for the given player
     * @param face the {@link BlockFace} that has been clicked
     * @return the mana cost, {@link #BAD_SITUATION} or {@link #CAST_FAILURE}
     * @since 0.0.5
     * @see #estimateLeftClickMana(Player, Block, int, BlockFace)
     */
    float leftClick(Player caster, Block block, int modifier, BlockFace face);

    /**
     * Perform the {@link Spell} implementation in attack.
     *
     * @param caster the executor of the {@link Spell}
     * @param target the attacked {@link Entity}
     * @param modifier the magic modifier for the given player
     * @return the mana cost, {@link #BAD_SITUATION} or {@link #CAST_FAILURE}
     * @since 0.0.5
     * @see #estimateAttackMana(Player, LivingEntity, int)
     */
    float attack(Player caster, LivingEntity target, int modifier);

    /**
     * Get the {@link Recipe} used for creating the representing wand.
     *
     * @return the wand {@link Recipe}
     * @since 0.0.5
     */
    @Nonnull Recipe getWandRecipe();

    /**
     * Get the {@link Sound}, indirectly, that should be played whenever the
     * {@link Spell} implementation is performed.
     *
     * @return the {@link Sound}, indirectly, that should be played on complete
     * @since 0.0.5
     */
    @Nullable SoundWrapper getSound();
}
