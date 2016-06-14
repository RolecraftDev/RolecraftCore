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
package com.github.rolecraftdev.magic.spells;

import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;
import com.github.rolecraftdev.util.SoundWrapper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A {@link Spell} implementation which is equivalent to {@link SilkTouch} with
 * an increased distance.
 *
 * @since 0.0.5
 */
public class FarbreakSilkTouch implements Spell {
    private final SpellManager spellManager;

    /**
     * Constructor.
     *
     * @param spellManager the {@link SpellManager} this {@link Spell}
     *        implementation will be registered to
     * @since 0.0.5
     */
    public FarbreakSilkTouch(final SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getName() {
        return this.spellManager.getConfiguredName(this.getDefaultName());
    }

    /**
     * @since 0.1.0
     */
    @Override
    public String getDefaultName() {
        return "Silky Farbreak";
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateAttackMana(final Player caster,
            final LivingEntity target, final int modifier) {
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateLeftClickMana(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        // 5 if the block is close, 10 for ranged
        if (block != null) {
            return 5;
        }
        return 10;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateRightClickMana(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        if (block != null) {
            return 5;
        }
        return 10;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float rightClick(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        return click(caster, block, modifier, face);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float leftClick(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        return click(caster, block, modifier, face);
    }

    @SuppressWarnings("deprecation")
    private float click(final Player ply, final Block block,
            final int modifier, final BlockFace face) {
        float retVal;
        Block toBreak;
        if (block == null) {
            toBreak = ply.getTargetBlock(null, spellManager.getRange());
            if (toBreak == null) {
                return CAST_FAILURE;
            }
            retVal = 10;
        } else {
            toBreak = block;
            retVal = 5;
        }

        if (spellManager.getPlugin().isExtraEvents()) {
            final BlockBreakEvent event = new BlockBreakEvent(toBreak, ply);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return CAST_FAILURE;
            }
        }

        final ItemStack itemStack =
                new ItemStack(toBreak.getType(), 1, (short) 1,
                        toBreak.getData());
        toBreak.setType(Material.AIR);
        toBreak.getWorld().dropItemNaturally(toBreak.getLocation(), itemStack);
        return retVal;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float attack(final Player caster, final LivingEntity target,
            final int modifier) {
        return BAD_SITUATION;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public ShapedRecipe getWandRecipe() {
        // same for each
        final ItemStack result = new ItemStack(Material.STICK);
        final ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        result.setItemMeta(meta);
        final ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("WPB", "PBP", "BPW");
        recipe.setIngredient('W', Material.BOW);
        recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        recipe.setIngredient('B', Material.EMERALD);
        return recipe;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public SoundWrapper getSound() {
        return SoundWrapper.DEFAULT;
    }
}
