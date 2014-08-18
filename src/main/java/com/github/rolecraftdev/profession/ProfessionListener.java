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

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

public class ProfessionListener implements Listener {
    private final ProfessionManager parent;

    public ProfessionListener(ProfessionManager professionManager) {
        this.parent = professionManager;
    }

    // enforce armor wearing rules
    /**
     * In the profession file, these are determined by the tag usable-armor
     * followed by a list of tags that are the names of items as defined in
     * {@link org.bukkit.Material}
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void enforceArmorRules(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                Material type = event.getCurrentItem().getType();
                final List<?> usable = parent.getPlayerProfession(
                        event.getWhoClicked().getUniqueId()).getRuleValue(
                        ProfessionRule.USABLE_ARMOR);
                String material = type.toString().replace("Material.", "").toLowerCase();
                if (usable == null || usable.size() == 0) {
                    if (!parent.getPlugin().getConfig()
                            .getBoolean("professiondefaults.armor")) {
                        ((Player) event.getWhoClicked()).sendMessage(parent
                                .getPlugin().getMessage(
                                        Messages.PROFESSION_DENY_ARMOR,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getWhoClicked()
                                                                .getUniqueId())
                                                        .getName())));
                        event.setCancelled(true);
                    }
                }
                else {
                    // if it explicitly states they can use it, or has a wildcard and
                    // does not negate it
                    if (!(usable.contains(material) ||
                            (usable.contains("*") && !usable.contains("-"+material)))) {
                        ((Player) event.getWhoClicked()).sendMessage(parent
                                .getPlugin().getMessage(
                                        Messages.PROFESSION_DENY_ARMOR,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getWhoClicked()
                                                                .getUniqueId())
                                                        .getName())));
                        event.setCancelled(true);
                    }
                }
                
                if(!checkEnchantments((Player) event.getWhoClicked(), event.getCurrentItem())) {
                    ((Player) event.getWhoClicked()).sendMessage(parent
                            .getPlugin().getMessage(
                                    Messages.PROFESSION_DENY_ENCHANTMENT,
                                    MsgVar.create(
                                            "$profession",
                                            parent.getPlayerProfession(
                                                    event.getWhoClicked()
                                                            .getUniqueId())
                                                    .getName())));
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onEnchant(EnchantItemEvent event) {
        if(!checkEnchantments(event.getEnchanter(),event.getItem())) {
            event.setCancelled(true);
            ((Player) event.getEnchanter()).sendMessage(parent
                    .getPlugin().getMessage(
                            Messages.PROFESSION_DENY_ENCHANTMENT,
                            MsgVar.create(
                                    "$profession",
                                    parent.getPlayerProfession(
                                            event.getEnchanter()
                                                    .getUniqueId())
                                            .getName())));
        }
    }
    
    private boolean checkEnchantments(Player ply, ItemStack stack) {
        if(stack.getEnchantments() == null || stack.getEnchantments().size() ==0) {
            return true;
        }
        Profession prof = parent.getPlayerProfession(ply.getUniqueId());
        
        List<?> rules = prof.getRuleValue(ProfessionRule.USABLE_ENCHANTMENTS);
        if(rules == null || rules.size() == 0) {
            return parent.getPlugin().getConfig().getBoolean("professiondefaults.enchantments");
        }
        
        boolean allow = true;
        
        for(Entry<Enchantment,Integer> ench: stack.getEnchantments().entrySet()) {
            boolean enchantmentAllowed = false;
            boolean levelAllowed = false;
            boolean levelOverride = false;
            
            String name = ench.getKey().toString().replace("Enchantment.", "").toLowerCase();
            name = name.substring(0, name.lastIndexOf('.'));
            if(rules.contains(name) || (rules.contains("*") && !rules.contains("-" + name))) {
                enchantmentAllowed = true;
            }
            if(enchantmentAllowed) {
                // if it contains usable-enchantments.*
                if(rules.contains("*")) {
                    levelAllowed = true;
                }
                // if it contains usable-enchantments.enchantment.*
                if(rules.contains(name+".*")) {
                    levelAllowed = true;
                }
                // if it contains usable-enchantments.enchantment.level
                // explicitly declaring this will override all other rules
                if(rules.contains(name + "." + String.valueOf(ench.getValue()))) {
                    levelOverride = true;
                }
                // if it doesn't contain -usable-enchantments.enchantment.level
                if (rules.contains("-" + name +"."+ String.valueOf(ench.getValue()))) {
                    levelAllowed = false;
                    levelOverride = false;
                }
            }
            allow = levelOverride || (enchantmentAllowed && levelAllowed);
            if(!allow) {
                return false;
            } 
        }
        return allow;
    }
}
