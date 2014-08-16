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
import com.github.rolecraftdev.util.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;

//Material.getId()
@SuppressWarnings("deprecation")
public class DeathRain implements Spell {

    private static HashSet<Byte> transparency;

    private SpellManager manager;

    static {
        transparency = new HashSet<Byte>();
        transparency.add((byte) Material.AIR.getId());
        transparency.add((byte) Material.GLASS.getId());
        transparency.add((byte) Material.STATIONARY_WATER.getId());
        transparency.add((byte) Material.WATER.getId());
    }

    public DeathRain(SpellManager spellManager) {
        manager = spellManager;
    }

    @Override
    public String getName() {
        return "Death Rain";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier, BlockFace face) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier, BlockFace face) {
        return (800f - modifier / 200f > 0) ? 800f - modifier / 200f : 0;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier, BlockFace face) {
        Block target = null;
        if (block != null) {
            target = block;
        } else {
            Block temp = ply.getTargetBlock(transparency, manager.getRange());
            if (temp != null) {
                target = temp;
            } else {
                return Float.MIN_VALUE;
            }
        }
        Block index = target;

        boolean isTop = true;

        loop:
        for (int i = 0; i < 40; i++) {
            index = index.getRelative(BlockFace.UP);
            switch (index.getType()) {
                case AIR:
                    continue;
                case LEAVES:
                    continue;
                case DEAD_BUSH:
                    continue;
                default:
                    isTop = false;
                    break loop;
            }
        }

        if (!isTop) {
            ply.sendMessage("You must aim above ground to rain arrows!");
            return Float.MIN_VALUE;
        }

        Location center = new Location(target.getWorld(), target.getX(),
                target.getY() + 40, target.getZ());

        Vector velocity = target.getLocation().toVector()
                .subtract(center.toVector()).normalize().multiply(0.5d);
        World world = target.getWorld();

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z < 5; z++) {
                Arrow arrow = world.spawn(
                        new Location(world, center.getX() + x, center.getY(),
                                center.getZ() + z), Arrow.class);
                arrow.setMetadata("Multiplier",
                        new FixedMetadataValue(manager.getPlugin(),
                                new Float(6)));
                arrow.setMetadata("Explosion",
                        new FixedMetadataValue(manager.getPlugin(),
                                new Boolean(true)));
                arrow.setVelocity(Utils.velocityRandomiser(velocity));
            }
        }

        return (800f - modifier / 200f > 0) ? 800f - modifier / 200f : 0;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier, BlockFace face) {
        return 0;
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
        String[] lore = {
                "A virtual airstrike, this wand is capabale of bringing an",
                "army to their knees with one cast" };
        meta.setLore(Arrays.asList(lore));
        result.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("WPB",
                "PEP",
                "BPW");
        recipe.setIngredient('W', Material.BOW);
        recipe.setIngredient('P', Material.EMERALD);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        return recipe;
    }

}
