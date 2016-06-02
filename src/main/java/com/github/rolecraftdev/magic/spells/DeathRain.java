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
import com.github.rolecraftdev.util.GeneralUtil;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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

/**
 * A {@link Spell} implementation that spawns arrows in the same manor as
 * {@link ArrowShower} and additionally makes them explosive.
 *
 * @since 0.0.5
 */
@SuppressWarnings("deprecation")
public class DeathRain implements Spell {
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
    public DeathRain(final SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String getName() {
        return "Death Rain";
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateAttackMana(final Player caster,
            final LivingEntity target, final int modifier) {
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateLeftClickMana(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        return 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float estimateRightClickMana(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        return (800f - modifier / 200f > 0) ? 800f - modifier / 200f : 0;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float rightClick(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        Block target;
        if (block != null) {
            target = block;
        } else {
            final Block temp = caster.getTargetBlock(transparency, spellManager
                    .getRange());
            if (temp != null) {
                target = temp;
            } else {
                return CAST_FAILURE;
            }
        }

        final World world = target.getWorld();

        if (world.getHighestBlockYAt(target.getLocation()) > target.getY()) {
            caster.sendMessage(spellManager.getPlugin().getMessage(
                    Messages.ARROW_BELOW_GROUND_FAILURE));
            return CAST_FAILURE;
        }

        final Location center = new Location(target.getWorld(), target.getX(),
                target.getY() + 40, target.getZ());
        final Vector velocity = target.getLocation().toVector()
                .subtract(center.toVector()).normalize().multiply(0.5d);

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z < 5; z++) {
                final Arrow arrow = world.spawn(
                        new Location(world, center.getX() + x, center.getY(),
                                center.getZ() + z), Arrow.class);
                arrow.setMetadata("Multiplier", new FixedMetadataValue(
                        spellManager.getPlugin(), 6f));
                arrow.setMetadata("Explosion", new FixedMetadataValue(
                        spellManager.getPlugin(), true));
                arrow.setVelocity(GeneralUtil.velocityRandomiser(velocity));
            }
        }

        return estimateRightClickMana(caster, block, modifier, face);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float leftClick(final Player caster, final Block block,
            final int modifier, final BlockFace face) {
        return BAD_SITUATION;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public float attack(final Player caster, final LivingEntity target,
            final int modifier) {
        return BAD_SITUATION;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public Recipe getWandRecipe() {
        // same for each
        final ItemStack result = new ItemStack(Material.STICK);
        final ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        final String[] lore = {
                "A virtual airstrike, this wand is capabale of bringing an",
                "army to their knees with one cast" };
        meta.setLore(Arrays.asList(lore));
        result.setItemMeta(meta);
        final ShapedRecipe recipe = new ShapedRecipe(result);
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

    /**
     * @since 0.0.5
     */
    @Override
    public SoundWrapper getSound() {
        return new SoundWrapper(Sound.GHAST_SCREAM, 1.0f, -2.0f);
    }
}
