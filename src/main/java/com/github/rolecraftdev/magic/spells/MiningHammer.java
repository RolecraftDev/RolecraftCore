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
import org.bukkit.Sound;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Spell} implementation which will make the held item function like a
 * pickaxe, although this will affect more blocks when used.
 */
public class MiningHammer implements Spell {

    private enum Orientation {
        NORTHSOUTH,
        EASTWEST,
        FLAT;
    }

    private SpellManager manager;

    public MiningHammer(SpellManager spellManager) {
        manager = spellManager;
    }

    @Override
    public String getName() {
        return "Mining Hammer";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier,
            BlockFace face) {
        return (30f - modifier / 100f > 0) ? 30f - modifier / 100f : 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier,
            BlockFace face) {
        return (30f - modifier / 100f > 0) ? 30f - modifier / 100f : 0;
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
        if (block == null) {
            return Float.MIN_VALUE;
        }
        List<Block> blocks = null;
        if (face == BlockFace.DOWN || face == BlockFace.UP) {
            blocks = getBlocksAround(block, Orientation.FLAT);
        } else if (face == BlockFace.EAST || face == BlockFace.WEST) {
            blocks = getBlocksAround(block, Orientation.EASTWEST);
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            blocks = getBlocksAround(block, Orientation.NORTHSOUTH);
        } else {
            return Float.MIN_VALUE;
        }

        for (Block toBreak : blocks) {
            if (manager.getPlugin().isExtraEvents()) {
                BlockBreakEvent event = new BlockBreakEvent(toBreak, ply);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    continue;
                }
            }
            toBreak.breakNaturally();
        }
        return (30f - modifier / 100f > 0) ? 30f - modifier / 100f : 0;
    }

    @Override
    public float attack(Player ply, LivingEntity ent, int modifier) {
        return Float.MIN_VALUE;
    }

    private List<Block> getBlocksAround(Block center, Orientation orientation) {
        if (center == null) {
            return null;
        }

        List<Block> temp = new ArrayList<Block>(9);
        if (orientation == Orientation.NORTHSOUTH) {
            int z = center.getZ();
            for (int y = center.getY() - 1; y <= center.getY() + 1; y++) {
                for (int x = center.getX() - 1; x <= center.getX() + 1; x++) {
                    temp.add(center.getWorld().getBlockAt(x, y, z));
                }
            }
        } else if (orientation == Orientation.EASTWEST) {
            int x = center.getX();
            for (int y = center.getY() - 1; y <= center.getY() + 1; y++) {
                for (int z = center.getZ() - 1; z <= center.getZ() + 1; z++) {
                    temp.add(center.getWorld().getBlockAt(x, y, z));
                }
            }
        } else if (orientation == Orientation.FLAT) {
            int y = center.getY();
            for (int z = center.getZ() - 1; z <= center.getZ() + 1; z++) {
                for (int x = center.getX() - 1; x <= center.getX() + 1; x++) {
                    temp.add(center.getWorld().getBlockAt(x, y, z));
                }
            }
        } else {
            return null;
        }

        return temp;
    }

    @Override
    public Recipe getWandRecipe() {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        result.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("APB",
                "PBP",
                "BPA");
        recipe.setIngredient('A', Material.BOW);
        recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        recipe.setIngredient('B', Material.IRON_BLOCK);
        return recipe;
    }

    @Override
    public SoundWrapper getSound() {
        return new SoundWrapper(Sound.FIREWORK_LARGE_BLAST, 1.0f, 0f);
    }

}
