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
import com.github.rolecraftdev.util.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A {@link Spell} implementation which can be used to kill the living entity
 * the executor points at.
 *
 * @since 0.0.5
 */
public class AvadaKedavra implements Spell {
    private final SpellManager spellManager;

    /**
     * Constructor.
     *
     * @param spellManager the {@link SpellManager} this {@link Spell}
     *        implementation will be registered to
     * @since 0.0.5
     */
    public AvadaKedavra(final SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getName() {
        return "Avada Kedavra";
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateAttackMana(Player caster, LivingEntity target,
            int modifier) {

        if (target != null) {
            if (target instanceof Player) {
                return 1000;
            } else {
                return 600 - modifier;
            }
        }
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
        LivingEntity toKill = Utils.getLivingTarget(caster, spellManager
                .getRange());
        if (toKill != null) {
            if (toKill instanceof Player) {
                return 1500;
            } else {
                return 800 - modifier;
            }
        }
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float rightClick(Player caster, Block block, int modifier,
            BlockFace face) {
        LivingEntity toKill = Utils.getLivingTarget(caster, spellManager
                .getRange());
        if (toKill == null) {
            return Float.MIN_VALUE;
        }

        EntityDamageByEntityEvent edbee = new EntityDamageByEntityEvent(caster,
                toKill,
                DamageCause.MAGIC, Double.MAX_VALUE);
        Bukkit.getPluginManager().callEvent(edbee);
        if (!edbee.isCancelled()) {
            toKill.setHealth(0D); // pwnt
            if (toKill instanceof Player) {
                spellManager.setMana(caster, 0f);
                caster.sendMessage("Your mana has been drained!");
            } else {
                return 800 - modifier;
            }
        }

        return 0;
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

        EntityDamageByEntityEvent edbee = new EntityDamageByEntityEvent(caster,
                target,
                DamageCause.MAGIC, Double.MAX_VALUE);
        Bukkit.getPluginManager().callEvent(edbee);
        if (!edbee.isCancelled()) {
            target.setHealth(0D); // pwnt
            if (target instanceof Player) {
                spellManager.setMana(caster, 0f);
                caster.sendMessage("Your mana has been drained!");
            } else {
                return 800 - modifier;
            }
        }

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
        recipe.shape("OOC", "OEO", "COO");
        recipe.setIngredient('O', Material.SKULL);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('C', Material.DIAMOND_BLOCK);

        return recipe;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public SoundWrapper getSound() {
        return new SoundWrapper(Sound.ENDERMAN_STARE, 1.0F, 2.0F);
    }
}
