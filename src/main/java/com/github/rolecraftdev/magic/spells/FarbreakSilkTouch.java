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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Collection;

public class FarbreakSilkTouch implements Spell {

    private SpellManager manager;
    private ItemStack simulate;

    public FarbreakSilkTouch(SpellManager spellManager) {
        manager = spellManager;
        simulate = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        simulate.addEnchantment(Enchantment.SILK_TOUCH, 1);
    }

    @Override
    public String getName() {
        // TODO: IDK but Farbreak Silk Touch is too long
        return "Silky Farbreak";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        // 5 if the block is close, 10 for ranged
        if (block != null) {
            return 5;
        }
        return 10;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        if (block != null) {
            return 5;
        }
        return 10;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {

        float retVal = 0;
        Block toBreak = null;
        if (block == null) {
            toBreak = ply.getTargetBlock(null, manager.getRange());
            if (toBreak == null) {
                return Float.MIN_VALUE;
            }
            retVal = 10;
        } else {
            toBreak = block;
            retVal = 5;
        }

        if (RolecraftCore.isExtraEvents()) {
            BlockBreakEvent event = new BlockBreakEvent(block, ply);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return Float.MIN_VALUE;
            }
        }
        MaterialData data = block.getState().getData();
        Collection<ItemStack> drops = block.getDrops(simulate);

        block.setType(Material.AIR);
        for (ItemStack i : drops) {
            block.getWorld().dropItemNaturally(block.getLocation(), i);
        }

        return retVal;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier) {
        float retVal = 0;
        Block toBreak = null;
        if (block == null) {
            toBreak = ply.getTargetBlock(null, manager.getRange());
            if (toBreak == null) {
                return Float.MIN_VALUE;
            }
            retVal = 10;
        } else {
            toBreak = block;
            retVal = 5;
        }

        if (RolecraftCore.isExtraEvents()) {
            BlockBreakEvent event = new BlockBreakEvent(block, ply);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return Float.MIN_VALUE;
            }
        }
        MaterialData data = block.getState().getData();
        Collection<ItemStack> drops = block.getDrops(simulate);

        block.setType(Material.AIR);
        for (ItemStack i : drops) {
            block.getWorld().dropItemNaturally(block.getLocation(), i);
        }

        return retVal;
    }

    @Override
    public float attack(Player ply, LivingEntity ent, int modifier) {
        return 0;
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
        recipe.shape("WPB", "PBP", "BPW");
        recipe.setIngredient('W', Material.BOW);
        recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        recipe.setIngredient('B', Material.EMERALD);
        return recipe;
    }
}