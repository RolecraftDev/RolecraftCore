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
package com.github.rolecraftdev.magic;

import org.apache.commons.lang.Validate;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.magic.listener.FlyingListener;
import com.github.rolecraftdev.magic.listener.MagicListener;
import com.github.rolecraftdev.magic.listener.ProjectileListener;
import com.github.rolecraftdev.magic.spells.*;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionRule;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A helper class for managing {@link Spell}s and global configurable options
 * affecting them.
 *
 * @since 0.0.5
 */
public class SpellManager {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    private final Map<String, Spell> spells;
    private final int maxRange;
    private final Map<String, Boolean> emptyMap;

    /**
     * Create a new {@link SpellManager} and load certain configurable options
     * from the given plugin's configuration file. Additionally this also
     * registers all Rolecraft {@link Spell} implementations and the required
     * {@link Listener}s along with scheduling the appropriate
     * {@link BukkitRunnable} implementations.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public SpellManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        spells = new HashMap<String, Spell>();
        maxRange = plugin.getConfig().getInt("magicrange", 100);
        emptyMap = new HashMap<String, Boolean>();

        // Tier 1 spells
        register("Freeze Block", new FreezeBlock(this));
        register("Burn Block", new BurnBlock(this));
        register("Destroy Block", new DestroyBlock(this));
        register("Lesser Sword", new LesserSword(this));
        register("Weak Bow", new WeakBow(this));

        // Tier 2 spells
        register("Freeze Ray", new FreezeRay(this));
        register("Fire Beam", new FireBeam(this));
        register("Strong Bow", new StrongerBow(this));
        register("Strong Sword", new StrongerSword(this));
        register("Break Block", new BreakBlock(this));

        // Tier 3 spells
        register("Silk Touch", new SilkTouch(this));
        register("Farbreak", new Farbreak(this));
        register("Excellent Bow", new ExcellentBow(this));
        register("Multi-Arrow", new MultiArrow(this));
        register("Arrow Shower", new ArrowShower(this));

        // Tier 4 spells
        register("Bomb", new Bomb(this));
        register("Meteor", new Meteor(this));
        register("Silky Farbreak", new FarbreakSilkTouch(this));
        register("Mining Hammer", new MiningHammer(this));
        register("Fly", new Fly(this));
        register("Hand Cannon", new HandCannon(this));

        // Tier 5 spells
        register("Avada Kedavra", new AvadaKedavra(this));
        register("Death Rain", new DeathRain(this));

        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new MagicListener(plugin), plugin);
        pluginManager.registerEvents(new ProjectileListener(), plugin);
        pluginManager.registerEvents(new FlyingListener(plugin), plugin);

        new ManaRegenTask(plugin).runTaskTimer(plugin, 20L, 40L);
    }

    /**
     * Returns the associated {@link RolecraftCore} instance.
     *
     * @return the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Register a new {@link Spell} along with an appropriate wand name to this
     * {@link SpellManager}. This automatically registers an the {@link Recipe}
     * returned by {@link Spell#getWandRecipe()} and a new {@link Permission} in
     * the following form:
     * <em>rolecraft.spell.(wandName to lowercase, no spaces)</em>.
     *
     * @param wandName the name of the wand that will execute the specified
     *        {@link Spell}
     * @param spell the {@link Spell} to register
     * @since 0.0.5
     */
    public void register(@Nonnull final String wandName,
            @Nonnull final Spell spell) {
        Validate.isTrue(!spells.containsKey(wandName));

        spells.put(wandName, spell);
        Bukkit.getPluginManager().addPermission(new Permission(
                "rolecraft.spell." + wandName.toLowerCase().replaceAll(" ", ""),
                "Allows access to the spell '" + wandName + "'",
                PermissionDefault.TRUE, emptyMap));
        Bukkit.addRecipe(spell.getWandRecipe());
    }

    /**
     * Replaces a registered {@link Spell} with the given name with the given
     * {@link Spell}. There <em>must</em> already be a spell registered with the
     * given name for this to work (this can be checked with
     * {@link #spellExists(String)}).
     *
     * @param wandName the name of the spell to replace
     * @param spell the new {@link Spell}
     * @since 0.1.0
     */
    public void replaceSpell(@Nonnull final String wandName,
            @Nonnull final Spell spell) {
        Validate.isTrue(spells.containsKey(wandName));

        final Spell oldSpell = spells.remove(wandName);
        this.removeRecipe(oldSpell.getWandRecipe());

        spells.put(wandName, spell);
        Bukkit.addRecipe(spell.getWandRecipe());
    }

    /**
     * Checks whether a {@link Spell} is registered with the given name.
     *
     * @param spellName the name to check
     * @return whether a spell with the given name exists
     * @since 0.1.0
     */
    public boolean spellExists(@Nonnull final String spellName) {
        return this.spells.containsKey(spellName);
    }

    /**
     * Checks whether the given {@link Spell} is registered.
     *
     * @param spell the spell to check
     * @return whether the given spell is registered with this manager
     * @since 0.1.0
     */
    public boolean isRegistered(@Nonnull final Spell spell) {
        return this.spells.containsValue(spell);
    }

    private void removeRecipe(final ShapedRecipe recipe) {
        final Iterator<Recipe> it = plugin.getServer().recipeIterator();

        whileLoop: while (it.hasNext()) {
            final Recipe cur = it.next();
            if (!(cur instanceof ShapedRecipe)) {
                continue;
            }

            final ShapedRecipe curShaped = (ShapedRecipe) cur;
            final Map<Character, ItemStack> ingredients = recipe.getIngredientMap();
            final Map<Character, ItemStack> curIngredients = curShaped.getIngredientMap();

            // sadly, we have to compare ingredients as we have no way to compare the results of the crafting
            if (curIngredients.values().containsAll(ingredients.values())) {
                // and now we must compare the exact shapes... sigh...
                final String[] shape = recipe.getShape();
                final String str = shape[0] + shape[1] + shape[2];

                final String[] curShape = curShaped.getShape();
                final String curStr = curShape[0] + curShape[1] + curShape[2];

                for (int i = 0; i < curStr.length(); i++) {
                    if (!curIngredients.get(curStr.charAt(i))
                            .equals(ingredients.get(str.charAt(i)))) {
                        continue whileLoop; // why is there no easy way to remove a recipe ffs. making me use gotos in java. smh
                    }
                }

                it.remove();
            }
        }
    }

    /**
     * Checks whether the given player is allowed to perform spells. This
     * requires the specified player to have at least one spell permission node
     * and his {@link Profession} should also allow magic.
     *
     * @param player the player to investigate
     * @return {@code true} if the specified player somewhere has a
     *         magic-allowing property defined
     * @since 0.0.5
     */
    public boolean canCast(@Nonnull final Player player) {
        Validate.notNull(player);

        boolean hasPermission = false;

        for (final PermissionAttachmentInfo permission : player
                .getEffectivePermissions()) {
            if (permission.getPermission().startsWith("rolecraft.spell.")) {
                hasPermission = true;
            }
        }

        if (!hasPermission) {
            return false;
        }

        final Profession profession = plugin.getProfessionManager()
                .getPlayerProfession(player.getUniqueId());

        if (profession == null) {
            return false;
        }

        final List<?> usable = profession
                .getRuleValue(ProfessionRule.USABLE_SPELLS);

        // Use the default value when none is set for the player's profession
        if (usable == null) {
            return plugin.getConfig().getBoolean("professiondefaults.spells");
        }

        return true;
    }

    /**
     * Check whether the given player can cast the specified {@link Spell}
     * according to his {@link Profession} and the default rules.
     *
     * @param player the player to check the permissions of
     * @param spell the {@link Spell} which should be scrutinised
     * @return {@code true} if the given player can perform the given
     *         {@link Spell}; {@code false} otherwise
     * @since 0.0.5
     */
    public boolean canCast(@Nonnull final Player player,
            @Nonnull final Spell spell) {
        Validate.notNull(player);
        Validate.notNull(spell);

        if (!player.hasPermission(
                "rolecraft.spell." + spell.getName().toLowerCase())) {
            return false;
        }

        final Profession profession = plugin.getProfessionManager()
                .getPlayerProfession(player.getUniqueId());

        if (profession == null) {
            return false;
        }

        final List<?> usable = profession
                .getRuleValue(ProfessionRule.USABLE_SPELLS);

        // Use the default value when none is set for the player's profession
        if (usable == null) {
            return plugin.getConfig().getBoolean("professiondefaults.spells");
        }
        return usable.contains(spell.getName()) || (usable.contains("*") &&
                !usable.contains("-" + spell.getName()));
    }

    /**
     * Obtain the {@link Spell} that is linked to the given wand name.
     *
     * @param wandName the wand name to get the corresponding {@link Spell} of
     * @return the corresponding {@link Spell}
     * @since 0.0.5
     */
    @Nullable
    public Spell getSpell(@Nonnull final String wandName) {
        return spells.get(wandName);
    }

    /**
     * Obtain the {@link Spell} that's wand is the given {@link ItemStack}
     *
     * @param stack the wand {@link ItemStack} to get the {@link Spell} from
     * @return the given {@link ItemStack}s {@link Spell}, or {@code null}
     * @since 0.0.5
     */
    @Nullable
    public Spell getSpellFromItem(@Nonnull final ItemStack stack) {
        Validate.notNull(stack);

        // Item meta is only null if the material is air -> no item meta check
        if (stack.getType() != Material.STICK
                || !stack.getItemMeta().hasDisplayName()) {
            return null;
        }
        if (stack.getEnchantmentLevel(Enchantment.LUCK) != 10) {
            return null;
        }
        return getSpell(ChatColor.stripColor(stack.getItemMeta()
                .getDisplayName()));
    }

    /**
     * Get the amount of mana a player has, which traces back to
     * {@link PlayerData#getMana()}, but returns {@code 0} when the
     * {@link PlayerData} for the given player hasn't wholly loaded yet.
     *
     * @param ply the player to get the mana of
     * @return the player's current amount of mana
     * @since 0.0.5
     */
    public float getMana(final Player ply) {
        final float mana = plugin.getDataManager().getPlayerData(
                ply.getUniqueId()).getMana();

        if (mana == -1) {
            return 0;
        } else {
            return mana;
        }
    }

    /**
     * Draw the specified amount of mana from the given player. Directly traces
     * back to {@link PlayerData#subtractMana(float)}.
     *
     * @param ply the player of whom to draw mana
     * @param amount the amount of mana that should be removed
     * @since 0.0.5
     */
    public void subtractMana(final Player ply, final float amount) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId()).subtractMana(
                amount);
    }

    /**
     * Set the mana of the given player. Directly traces back to
     * {@link PlayerData#setMana(float)}
     *
     * @param ply the player of whom the mana should be changed
     * @param mana the new amount of mana
     * @since 0.0.5
     */
    public void setMana(final Player ply, final float mana) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId()).setMana(mana);
    }

    /**
     * Retrieve a number that relies on a player's {@link Profession} and his
     * current level, which can give them an edge in magic.
     *
     * @param ply the player to get the current special modifier of
     * @return the {@link Profession}- and level-based number
     * @since 0.0.5
     */
    public int getMagicModifier(final Player ply) {
        // TODO: make this work
        return 0;
    }

    /**
     * Get all {@link Spell}s that are registered to this {@link SpellManager}.
     *
     * @return all registered {@link Spell}s
     * @since 0.0.5
     */
    public Collection<Spell> getSpells() {
        return spells.values();
    }

    /**
     * Get the configured maximum range, which is used for {@link Spell}s that
     * affect something distantly.
     *
     * @return the maximum range
     * @since 0.0.5
     */
    public int getRange() {
        return maxRange;
    }
}
