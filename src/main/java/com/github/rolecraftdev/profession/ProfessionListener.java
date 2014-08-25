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

import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map.Entry;

/**
 * Used to enforce rules specified for professions
 */
public class ProfessionListener implements Listener {
    private final ProfessionManager parent;

    public ProfessionListener(ProfessionManager professionManager) {
        parent = professionManager;
    }

    // enforce armor wearing rules

    /**
     * In the profession file, these are determined by the tag usable-armor
     * followed by a list of tags that are the names of items as defined in
     * {@link org.bukkit.Material}
     *
     * @param event the {@link InventoryClickEvent} used to check that players
     *              aren't putting disallowed armour in their armour slots
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void enforceArmorRules(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            final Player player = (Player) event.getWhoClicked();
            if (parent.getPlayerProfession(player.getUniqueId()) == null) {
                if (!parent.getPlugin().getConfig()
                        .getBoolean("professiondefaults.enchantments")) {
                    player.sendMessage(parent.getPlugin().getMessage(
                            Messages.PROFESSION_DENY_ENCHANTMENT,
                            MsgVar.create("$profession", parent
                                    .getPlayerProfession(player.getUniqueId())
                                    .getName())));
                    event.setCancelled(true);
                    return;
                } else {
                    return;
                }
            }
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                if (!checkMaterial(
                        event.getCurrentItem().getType(),
                        player, parent.getPlayerProfession(player.getUniqueId())
                                .getRuleValue(ProfessionRule.USABLE_ARMOR))) {
                    player.sendMessage(parent.getPlugin().getMessage(
                            Messages.PROFESSION_DENY_ARMOR,
                            MsgVar.create("$profession", parent
                                    .getPlayerProfession(player.getUniqueId())
                                    .getName())));
                    event.setCancelled(true);
                }
            }

            if (!checkEnchantments(player, event.getCurrentItem())) {
                player.sendMessage(parent.getPlugin().getMessage(
                        Messages.PROFESSION_DENY_ENCHANTMENT,
                        MsgVar.create("$profession", parent.getPlayerProfession(
                                player.getUniqueId()).getName())));
                event.setCancelled(true);
            }
            if (event instanceof CraftItemEvent) {
                if (!checkMaterial(event.getCurrentItem().getType(), player,
                        parent.getPlayerProfession(player.getUniqueId())
                                .getRuleValue(ProfessionRule.USABLE_ITEMS))) {
                    player.sendMessage(parent.getPlugin().getMessage(Messages
                            .PROFESSION_DENY_ITEM, MsgVar.create(
                            "$profession", parent.getPlayerProfession(player
                                    .getUniqueId()).getName())));
                    event.setCancelled(true);
                }
            } else if (event.getSlotType() == SlotType.QUICKBAR) {
                if (!checkMaterial(
                        event.getCurrentItem().getType(),
                        player,
                        parent.getPlayerProfession(
                                player.getUniqueId())
                                .getRuleValue(ProfessionRule.USABLE_ITEMS))) {
                    player.sendMessage(parent.getPlugin().getMessage(
                            Messages.PROFESSION_DENY_ITEM, MsgVar.create(
                                    "$profession", parent.getPlayerProfession(
                                            player.getUniqueId()).getName())));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void enforceEnchantRules(EnchantItemEvent event) {
        if (parent.getPlayerProfession(event.getEnchanter().getUniqueId())
                == null) {
            if (!parent.getPlugin().getConfig()
                    .getBoolean("professiondefaults.enchantments")) {
                event.getEnchanter().sendMessage(
                        parent.getPlugin().getMessage(
                                Messages.PROFESSION_DENY_ENCHANTMENT,
                                MsgVar.create(
                                        "$profession",
                                        parent.getPlayerProfession(
                                                event.getEnchanter()
                                                        .getUniqueId())
                                                .getName())));
                event.setCancelled(true);
                return;
            } else {
                return;
            }
        }
        if (!checkEnchantments(event.getEnchanter(), event.getItem())) {
            event.getEnchanter().sendMessage(
                    parent.getPlugin().getMessage(
                            Messages.PROFESSION_DENY_ENCHANTMENT,
                            MsgVar.create(
                                    "$profession",
                                    parent.getPlayerProfession(
                                            event.getEnchanter().getUniqueId())
                                            .getName())));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void enforceItemChange(PlayerItemHeldEvent event) {
        ItemStack stack = event.getPlayer().getInventory()
                .getItem(event.getNewSlot());
        if (parent.getPlayerProfession(event.getPlayer().getUniqueId())
                == null) {
            if (!parent.getPlugin().getConfig()
                    .getBoolean("professiondefaults.enchantments")) {
                event.getPlayer().sendMessage(
                        parent.getPlugin()
                                .getMessage(
                                        Messages.PROFESSION_DENY_ITEM,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getPlayer()
                                                                .getUniqueId())
                                                        .getName())));
                event.setCancelled(true);
                return;
            } else {
                return;
            }
        }
        if (stack != null && stack.getType() != Material.AIR) {
            if (!checkMaterial(
                    event.getPlayer().getInventory()
                            .getItem(event.getNewSlot()).getType(),
                    event.getPlayer(),
                    parent.getPlayerProfession(event.getPlayer().getUniqueId())
                            .getRuleValue(ProfessionRule.USABLE_ITEMS))) {
                event.getPlayer().sendMessage(
                        parent.getPlugin()
                                .getMessage(
                                        Messages.PROFESSION_DENY_ITEM,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getPlayer()
                                                                .getUniqueId())
                                                        .getName())));
                event.setCancelled(true);
            }
            if (!checkEnchantments(event.getPlayer(), event.getPlayer()
                    .getInventory().getItem(event.getNewSlot()))) {
                event.getPlayer().sendMessage(
                        parent.getPlugin()
                                .getMessage(
                                        Messages.PROFESSION_DENY_ENCHANTMENT,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getPlayer()
                                                                .getUniqueId())
                                                        .getName())));
            }
        }
    }

    private boolean checkEnchantments(Player ply, ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            return true;
        }
        if (stack.getEnchantments() == null
                || stack.getEnchantments().isEmpty()) {
            return true;
        }
        Profession prof = parent.getPlayerProfession(ply.getUniqueId());

        List<?> rules = prof.getRuleValue(ProfessionRule.USABLE_ENCHANTMENTS);
        if (rules == null || rules.isEmpty()) {
            return parent.getPlugin().getConfig()
                    .getBoolean("professiondefaults.enchantments");
        }

        boolean allow;

        for (Entry<Enchantment, Integer> enchantment : stack.getEnchantments()
                .entrySet()) {
            boolean enchantmentAllowed = false;
            boolean levelAllowed = false;
            boolean levelOverride = false;

            String name = enchantment.getKey().toString()
                    .replace("Enchantment.", "").toLowerCase();
            name = name.substring(0, name.lastIndexOf('.'));
            if (rules.contains(name)
                    || (rules.contains("*") && !rules.contains("-" + name))) {
                enchantmentAllowed = true;
            }
            if (enchantmentAllowed) {
                // if it contains usable-enchantments.*
                if (rules.contains("*")) {
                    levelAllowed = true;
                }
                // if it contains usable-enchantments.enchantment.*
                if (rules.contains(name + ".*")) {
                    levelAllowed = true;
                }
                // if it contains usable-enchantments.enchantment.level
                // explicitly declaring this will override all other rules
                if (rules.contains(name + "."
                        + String.valueOf(enchantment.getValue()))) {
                    levelOverride = true;
                }
                // if it doesn't contain -usable-enchantments.enchantment.level
                if (rules.contains("-" + name + "."
                        + String.valueOf(enchantment.getValue()))) {
                    levelAllowed = false;
                    levelOverride = false;
                }
            }
            allow = levelOverride || (enchantmentAllowed && levelAllowed);
            if (!allow) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMaterial(Material mat, Player ply, List<?> rules) {
        if (mat == null) {
            return true;
        }
        String material = mat.toString().replace("Material.", "").toLowerCase();
        if (rules == null || rules.isEmpty()) {
            // ifndef return default
            return parent.getPlugin().getConfig()
                    .getBoolean("professiondefaults.armor");
        }
        if (rules.contains(material)
                || (rules.contains("*") && !rules.contains("-" + material))) {
            return true;
        }

        return false;
    }
}
