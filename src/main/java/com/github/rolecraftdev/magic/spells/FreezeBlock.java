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

import java.util.HashSet;

import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

@SuppressWarnings("deprecation")
public class FreezeBlock implements Spell {
    
    private static HashSet<Byte> transparency;

    private SpellManager manager;

    static {
        // declare it so water isn't transparent
        
        transparency = new HashSet<Byte>();
        transparency.add((byte) Material.AIR.getId());
        transparency.add((byte) Material.GLASS.getId());
    }
    
    public FreezeBlock(SpellManager spellManager) {
        manager = spellManager;
    }

    @Override
    public String getName() {
        return "Freeze Block";
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        Block targetBlock =ply.getTargetBlock(transparency, 5);
        if(targetBlock.getType() == Material.STATIONARY_LAVA) {
            return 50;
        }
        return 5;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        if (block != null) {
            BlockBreakEvent bbe = new BlockBreakEvent(block, ply);
            Bukkit.getPluginManager().callEvent(bbe);
            if (!bbe.isCancelled()) {
                BlockState state = block.getState();
                block.setType(Material.ICE);
                BlockPlaceEvent bpe = new BlockPlaceEvent(block, state, null,
                        null, ply, true);
                Bukkit.getPluginManager().callEvent(bpe);
                if (bpe.isCancelled()) {
                    state.update();
                }
            }
        }
        else {
            Block targetBlock =ply.getTargetBlock(transparency, 5);
            if(targetBlock != null){
                switch (targetBlock.getType()) {
                case STATIONARY_WATER:
                    targetBlock.setType(Material.ICE);
                    return 5;
                case WATER:
                    targetBlock.setType(Material.ICE);
                    return 5;
                case STATIONARY_LAVA:
                    targetBlock.setType(Material.OBSIDIAN);
                    return 50;
                case LAVA:
                    targetBlock.setType(Material.COBBLESTONE);
                    return 5;

                default:
                    break;
                }
            }
            return Float.MIN_VALUE;
        }
        return 5;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier) {
        return Float.MIN_VALUE;
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
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

}
