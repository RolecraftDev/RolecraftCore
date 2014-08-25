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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

/**
 * A {@link Spell} implementation which shoots an arrow that deals an increased
 * amount of damage and applies a greater amount of knockback.
 */
public class HandCannon implements Spell {
    private final SpellManager manager;

    public HandCannon(final SpellManager manager) {
        this.manager = manager;
    }

    @Override
    public String getName() {
        return "Hand Cannon";
    }

    @Override
    public float estimateAttackMana(Player caster, LivingEntity target,
            int modifier) {
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player caster, Block block, int modifier,
            BlockFace face) {
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player caster, Block block, int modifier,
            BlockFace face) {
        return (300f - modifier / 100f > 0) ? 300f - modifier / 100f : 0;
    }

    @Override
    public float rightClick(Player caster, Block block, int modifier,
            BlockFace face) {
        if (block != null) {
            return Float.MIN_VALUE;
        }

        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(4));
        arrow.setMetadata("Multiplier",
                new FixedMetadataValue(manager.getPlugin(), (float) 6));
        arrow.setMetadata("Knockback",
                new FixedMetadataValue(manager.getPlugin(), new Float(1.0)));
        // Make a loud bang when used

        return (300f - modifier / 100f > 0) ? 300f - modifier / 100f : 0;
    }

    @Override
    public float leftClick(Player caster, Block block, int modifier,
            BlockFace face) {
        return 0;
    }

    @Override
    public float attack(Player caster, LivingEntity target, int modifier) {
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
        recipe.shape("WPB",
                "PBP",
                "BPW");
        recipe.setIngredient('W', Material.EMERALD);
        recipe.setIngredient('P', Material.BOW);
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        return recipe;
    }

    @Override
    public SoundWrapper getSound() {
        return new SoundWrapper(Sound.EXPLODE, 0.8f, 1.0f);
    }
}
