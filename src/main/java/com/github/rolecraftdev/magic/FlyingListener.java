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

import com.github.rolecraftdev.RolecraftCore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class FlyingListener implements Listener {
    private RolecraftCore plugin;

    public FlyingListener(RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInHand();
        if (isFly(hand)) {
            event.getPlayer().setAllowFlight(true);
            event.getPlayer().setFlying(true);
            event.getPlayer().setMetadata("rolecraftfly",
                    new FixedMetadataValue(plugin, true));
            event.getPlayer().setFallDistance(0f);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemChange(PlayerItemHeldEvent event) {
        ItemStack stack = event.getPlayer().getInventory()
                .getItem(event.getNewSlot());
        if (isFly(stack)) {
            event.getPlayer().setAllowFlight(true);
            event.getPlayer().setFlying(true);
            event.getPlayer().setMetadata("rolecraftfly",
                    new FixedMetadataValue(plugin, true));
            event.getPlayer().setFallDistance(0f);

        } else {
            if (event.getPlayer().hasMetadata("rolecraftfly")) {
                event.getPlayer().removeMetadata("rolecraftfly", plugin);
                event.getPlayer().setFlying(false);
                event.getPlayer().setAllowFlight(false);
                event.getPlayer().setFallDistance(0f);
            }
        }
    }

    private boolean isFly(ItemStack stack) {
        if (stack == null || stack.getType() != Material.STICK) {
            return false;
        }
        if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
            if (ChatColor
                    .stripColor(stack.getItemMeta().getDisplayName())
                    .equalsIgnoreCase("fly")) {
                return !stack.getEnchantments().isEmpty()
                        && stack.getEnchantments().get(Enchantment.LUCK) == 10;
            }
        }

        return false;
    }
}
