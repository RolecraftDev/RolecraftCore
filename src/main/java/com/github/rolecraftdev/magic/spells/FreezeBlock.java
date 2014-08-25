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
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;

/**
 * A {@link Spell} implementation that freezes, or recreates the event in which water
 * runs over, the pointed at block.
 */
@SuppressWarnings("deprecation")
public class FreezeBlock implements Spell {
    private static HashSet<Byte> transparency;

    static {
        // declare it so water isn't transparent

        transparency = new HashSet<Byte>();
        transparency.add((byte) Material.AIR.getId());
        transparency.add((byte) Material.GLASS.getId());
    }

    public FreezeBlock(SpellManager spellManager) {
    }

    @Override
    public String getName() {
        return "Freeze Block";
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier,
            BlockFace face) {
        Block targetBlock = ply.getTargetBlock(transparency, 5);
        if (targetBlock.getType() == Material.STATIONARY_LAVA) {
            return 50;
        }
        return 5;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier,
            BlockFace face) {
        Block targetBlock = ply.getTargetBlock(transparency, 5);
        if (targetBlock.getType() == Material.STATIONARY_LAVA) {
            return 50;
        }
        return 5;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier,
            BlockFace face) {
        return click(ply, block, modifier, face);
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier,
            BlockFace face) {
        return click(ply, block, modifier, face);
    }

    private float click(Player ply, Block block, int modifier, BlockFace face) {
        Block targetBlock = ply.getTargetBlock(transparency, 5);
        BlockBreakEvent bbe = new BlockBreakEvent(targetBlock, ply);
        Bukkit.getPluginManager().callEvent(bbe);
        if (bbe.isCancelled()) {
            return Float.MIN_VALUE;
        }
        float retVal = Float.MIN_VALUE;

        if (targetBlock != null) {
            BlockState state = targetBlock.getState();

            switch (targetBlock.getType()) {
                case STATIONARY_WATER:
                    targetBlock.setType(Material.ICE);
                    retVal = 5;
                    break;
                case WATER:
                    targetBlock.setType(Material.ICE);
                    retVal = 5;
                    break;
                case STATIONARY_LAVA:
                    targetBlock.setType(Material.OBSIDIAN);
                    retVal = 50;
                    break;
                case LAVA:
                    targetBlock.setType(Material.COBBLESTONE);
                    retVal = 5;
                    break;
                default:
                    targetBlock.setType(Material.ICE);
                    retVal = 5;
                    break;
            }
            BlockPlaceEvent bpe = new BlockPlaceEvent(targetBlock, state,
                    null, null, ply, true);
            Bukkit.getPluginManager().callEvent(bpe);
            if (bpe.isCancelled()) {
                state.update();
            }
        }
        return retVal;
    }

    @Override
    public float attack(Player ply, LivingEntity ent, int modifier) {
        return Float.MIN_VALUE;
    }

    @Override
    public Recipe getWandRecipe() {
        // same for each
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        result.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("SSI", "SIS", "ISS");
        recipe.setIngredient('S', Material.SNOW_BALL);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    @Override
    public SoundWrapper getSound() {
        return SoundWrapper.defaultSound;
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }
}
