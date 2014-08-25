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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * A {@link Spell} implementation which fires an arrow that deals less damage than
 * one shot with a bow.
 */
public class WeakBow implements Spell {
    private SpellManager parent;

    public WeakBow(SpellManager spellManager) {
        parent = spellManager;
    }

    @Override
    public String getName() {
        return "Weak Bow";
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
        return (40f - modifier / 10f > 0) ? (40f - modifier / 10f) : 0f;
    }

    @Override
    public float rightClick(Player caster, Block block, int modifier,
            BlockFace face) {
        Entity ent = caster.launchProjectile(Arrow.class);
        ent.setVelocity(ent.getVelocity().multiply(0.5f));
        ent.setMetadata("Multiplier",
                new FixedMetadataValue(parent.getPlugin(), 0.5f));
        return (40f - modifier / 10f > 0) ? (40f - modifier / 10f) : 0f;
    }

    @Override
    public float leftClick(Player caster, Block block, int modifier,
            BlockFace face) {
        return Float.MIN_VALUE;
    }

    @Override
    public float attack(Player caster, LivingEntity target, int modifier) {
        return Float.MIN_VALUE;
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
        recipe.shape("SSI", "SIS", "ISS");
        recipe.setIngredient('S', Material.ARROW);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    @Override
    public SoundWrapper getSound() {
        return SoundWrapper.bowSound;
    }
}
