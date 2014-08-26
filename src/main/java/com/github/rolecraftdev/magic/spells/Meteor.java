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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashSet;

/**
 * A {@link Spell} implementation that will spawn a meteor in the air.
 *
 * @since 0.0.5
 */
@SuppressWarnings("deprecation")
public class Meteor implements Spell {
    /**
     * All {@link Material} IDs that should be interpreted as transparent when
     * targeting {@link Block}s.
     */
    private static final HashSet<Byte> transparency;

    private final SpellManager spellManager;

    /**
     * @since 0.0.5
     */
    static {
        transparency = new HashSet<Byte>();
        transparency.add((byte) Material.AIR.getId());
        transparency.add((byte) Material.GLASS.getId());
        transparency.add((byte) Material.STATIONARY_WATER.getId());
        transparency.add((byte) Material.WATER.getId());
    }

    /**
     * Constructor.
     *
     * @param spellManager the {@link SpellManager} this {@link Spell}
     *        implementation will be registered to
     * @since 0.0.5
     */
    public Meteor(final SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getName() {
        return "Meteor";
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
        return (200f - modifier / 100f > 0) ? 200f - modifier / 100f : 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float rightClick(Player caster, Block block, int modifier,
            BlockFace face) {
        Block target;
        if (block != null) {
            target = block;
        } else {
            Block temp = caster.getTargetBlock(transparency, spellManager
                    .getRange());
            if (temp != null) {
                target = temp;
            } else {
                return CAST_FAILURE;
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
            caster.sendMessage("You must aim above ground to shoot a meteor!");
            return CAST_FAILURE;
        }
        Location center = new Location(target.getWorld(),
                target.getX(), target.getY() + 20, target.getZ());
        Vector velocity = target.getLocation().toVector()
                .subtract(center.toVector())
                .normalize().multiply(0.2d);
        Entity tnt = caster.getWorld().spawn(
                new Location(caster.getWorld(), target.getX(), center.getY(),
                        target.getZ()), TNTPrimed.class);
        tnt.setVelocity(velocity);
        return (200f - modifier / 100f > 0) ? 200f - modifier / 100f : 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float leftClick(Player caster, Block block, int modifier,
            BlockFace face) {
        return BAD_SITUATION;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float attack(Player caster, LivingEntity target, int modifier) {
        return BAD_SITUATION;
    }

    /**
     * @since 0.0.5
     */
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
        recipe.setIngredient('S', Material.TNT);
        recipe.setIngredient('I', Material.EMERALD);
        return recipe;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public SoundWrapper getSound() {
        return new SoundWrapper(Sound.FIZZ, 1.0f, 0f);
    }
}
