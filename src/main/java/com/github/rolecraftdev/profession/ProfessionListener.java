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
package com.github.rolecraftdev.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Listens for and manipulates various {@link Event}s affecting
 * {@link Profession}s, which in some cases depends on the configuration.
 *
 * @since 0.0.5
 */
public class ProfessionListener implements Listener {
    private final ProfessionManager professionManager;
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param professionManager the {@link ProfessionManager} of which the
     *        {@link Profession}s should be handled by this {@link Listener}
     * @since 0.0.5
     */
    public ProfessionListener(final ProfessionManager professionManager) {
        this.professionManager = professionManager;
        this.plugin = professionManager.getPlugin();
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        final ItemStack current = event.getCurrentItem();
        final Player clicker = (Player) event.getWhoClicked();
        final Profession profession = professionManager
                .getPlayerProfession(clicker.getUniqueId());

        if (current == null || profession == null) {
            return;
        }

        if (event.getSlotType() == SlotType.ARMOR
                && !isMaterialEquippable(clicker, current)) {
            clicker.sendMessage(professionManager.getPlugin().getMessage(
                    Messages.PROFESSION_DENY_ARMOR,
                    MessageVariable.PROFESSION.value(profession.getName())));
            event.setCancelled(true);
            return;
        }

        if (event.getSlotType() == SlotType.QUICKBAR
                && !isMaterialUsable(clicker, current)) {
            clicker.sendMessage(professionManager.getPlugin().getMessage(
                    Messages.PROFESSION_DENY_ITEM,
                    MessageVariable.PROFESSION.value(profession.getName())));
            event.setCancelled(true);
            return;
        }

        if (event instanceof CraftItemEvent && !isMaterialUsable(clicker,
                current)) {
            clicker.sendMessage(professionManager.getPlugin().getMessage(
                    Messages.PROFESSION_DENY_ITEM,
                    MessageVariable.PROFESSION.value(profession.getName())));
            event.setCancelled(true);
            return;
        }

        if (!canEnchant(clicker, current.getEnchantments())) {
            clicker.sendMessage(professionManager.getPlugin().getMessage(
                    Messages.PROFESSION_DENY_ENCHANTMENT,
                    MessageVariable.PROFESSION.value(profession.getName())));
            event.setCancelled(true);
            return;
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEnchantItem(final EnchantItemEvent event) {
        final ItemStack enchanted = event.getItem();
        if (enchanted == null) {
            return;
        }

        final Player enchanter = event.getEnchanter();
        final Profession profession = professionManager
                .getPlayerProfession(enchanter.getUniqueId());

        if (!canEnchant(enchanter, event.getEnchantsToAdd())) {
            enchanter.sendMessage(professionManager.getPlugin().getMessage(
                    Messages.PROFESSION_DENY_ENCHANTMENT,
                    MessageVariable.PROFESSION.value(profession.getName())));
            event.setCancelled(true);
        }
    }

    private boolean canEnchant(final Player player,
            final Map<Enchantment, Integer> enchantments) {
        final UUID playerId = player.getUniqueId();

        final Profession profession = professionManager
                .getPlayerProfession(playerId);
        if (profession == null) {
            return false; // rolecraft is not intended to handle players without professions
        }

        if (isEnchantable(enchantments, profession)) {
            return true;
        }

        if (!plugin.getConfigValues().allowSecondProfessions() || !plugin
                .getConfigValues().allowSecondProfessionEnchantments()) {
            return false;
        }

        final Profession secondProfession = professionManager
                .getPlayerSecondProfession(playerId);
        return secondProfession != null && isEnchantable(enchantments,
                secondProfession);
    }

    private boolean isEnchantable(final Map<Enchantment, Integer> enchantments,
            final Profession profession) {
        return isEnchantmentsAllowed(enchantments, profession
                .getRuleValue(ProfessionRule.USABLE_ENCHANTMENTS));
    }

    /**
     * Check whether it's allowed to use the given enchantments with respect to
     * the given rules.
     *
     * @param enchantments the enchantments that should be checked
     * @param rules the rules with which should be complied
     * @return {@code true} if the given player is allowed to use the given
     *         {@link ItemStack}; {@code false} otherwise
     */
    private boolean isEnchantmentsAllowed(
            final Map<Enchantment, Integer> enchantments, final List<?> rules) {
        if (enchantments == null || enchantments.isEmpty()) {
            return true;
        }
        if (rules == null || rules.isEmpty()) {
            // use config default value
            return professionManager.getPlugin().getConfig().getBoolean(
                    "professiondefaults.enchantments", true);
        }

        for (final Entry<Enchantment, Integer> enchantment : enchantments
                .entrySet()) {
            final String name = enchantment.getKey().getName().toLowerCase();
            final int level = enchantment.getValue();

            if (!rules.contains(name) && !rules.contains(name + ".*")
                    && !rules.contains(name + "." + level)
                    && !rules.contains("*") || rules.contains("-" + name)) {
                return false;
            }
        }
        return true;
    }

    private boolean isMaterialUsable(final Player player,
            final ItemStack stack) {
        final UUID playerId = player.getUniqueId();

        final Profession profession = professionManager
                .getPlayerProfession(playerId);
        if (profession == null) {
            return false; // rolecraft is not intended to handle players without professions
        }

        if (isMaterialAllowed(stack.getType(), profession
                .getRuleValue(ProfessionRule.USABLE_ITEMS))) {
            return true;
        }

        if (!plugin.getConfigValues().allowSecondProfessions() || !plugin
                .getConfigValues().allowSecondProfessionArmor()) {
            return false;
        }

        final Profession secondProfession = professionManager
                .getPlayerSecondProfession(playerId);
        return secondProfession != null && isMaterialAllowed(stack.getType(),
                secondProfession.getRuleValue(ProfessionRule.USABLE_ITEMS));
    }

    private boolean isMaterialEquippable(final Player player,
            final ItemStack stack) {
        final UUID playerId = player.getUniqueId();

        final Profession profession = professionManager
                .getPlayerProfession(playerId);
        if (profession == null) {
            return false; // rolecraft is not intended to handle players without professions
        }

        if (isMaterialAllowed(stack.getType(), profession
                .getRuleValue(ProfessionRule.USABLE_ARMOR))) {
            return true;
        }

        if (!plugin.getConfigValues().allowSecondProfessions() || !plugin
                .getConfigValues().allowSecondProfessionArmor()) {
            return false;
        }

        final Profession secondProfession = professionManager
                .getPlayerSecondProfession(playerId);
        return secondProfession != null && isMaterialAllowed(stack.getType(),
                secondProfession.getRuleValue(ProfessionRule.USABLE_ARMOR));
    }

    /**
     * Check whether it's allowed to use the given {@link Material} with respect
     * to the given rules.
     *
     * @param material the {@link Material} that should be checked
     * @param rules the rules with which should be complied
     * @return {@code true} if there are no restrictions set on the given
     *         {@link Material} in the given rules
     */
    private boolean isMaterialAllowed(final Material material,
            final List<?> rules) {
        if (material == null) {
            return true;
        }
        if (rules == null || rules.isEmpty()) {
            // use config default value
            return professionManager.getPlugin().getConfig().getBoolean(
                    "professiondefaults.armor", true);
        }

        final String matName = material.toString().toLowerCase();
        return rules.contains(matName)
                || (rules.contains("*") && !rules.contains("-" + matName));
    }
}
