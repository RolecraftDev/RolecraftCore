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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * A {@link Spell} implementation that shoots an arrow which will deal increased
 * damage on hit.
 *
 * @since 0.0.5
 */
public class ExcellentBow implements Spell {
    private final SpellManager manager;

    /**
     * Constructor.
     *
     * @param manager the {@link SpellManager} this {@link Spell} implementation
     *        will be registered to
     * @since 0.0.5
     */
    public ExcellentBow(final SpellManager manager) {
        this.manager = manager;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getName() {
        return "Excellent Bow";
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateAttackMana(Player caster, LivingEntity target,
            int modifier) {
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateLeftClickMana(Player caster, Block block, int modifier,
            BlockFace face) {
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateRightClickMana(Player caster, Block block, int modifier,
            BlockFace face) {
        return (100f - modifier / 100f > 0) ? 100f - modifier / 100f : 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float rightClick(Player caster, Block block, int modifier,
            BlockFace face) {
        if (block != null) {
            return Float.MIN_VALUE;
        }

        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(2d));
        arrow.setMetadata("Multiplier", new FixedMetadataValue(
                manager.getPlugin(), 2f));

        return (100f - modifier / 100f > 0) ? 100f - modifier / 100f : 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float leftClick(Player caster, Block block, int modifier,
            BlockFace face) {
        return Float.MIN_VALUE;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float attack(Player caster, LivingEntity target, int modifier) {
        return Float.MIN_VALUE;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public Recipe getWandRecipe() {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        result.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("PPB",
                "PBP",
                "BPP");
        recipe.setIngredient('P', Material.BOW);
        recipe.setIngredient('B', Material.EMERALD);
        return recipe;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public SoundWrapper getSound() {
        return SoundWrapper.bowSound;
    }
}
