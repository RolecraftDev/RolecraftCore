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
package com.github.rolecraftdev.magic.listener;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.magic.spells.Fly;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Listens for {@link Event}s to provide a handler for the {@link Fly} spell.
 *
 * @since 0.0.5
 */
public class FlyingListener implements Listener {
    /**
     * The metadata tag used for a flying player
     */
    public static final String FLY_METADATA = "rolecraftfly";

    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public FlyingListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ItemStack hand = player.getInventory().getItemInHand();

        if (isFly(hand)) {
            setFlight(player, true);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final ItemStack stack = player.getInventory().getItem(
                event.getNewSlot());

        if (isFly(stack)) {
            setFlight(player, true);
        } else if (event.getPlayer().hasMetadata(FLY_METADATA)) {
            setFlight(player, false);
        }
    }

    /**
     * Allow the given player to fly or not.
     *
     * @param player the player of which flight should be enabled or disabled
     * @param fly enable flight or disable it
     */
    private void setFlight(final Player player, final boolean fly) {
        if (fly) {
            player.setMetadata(FLY_METADATA, new FixedMetadataValue(plugin,
                    true));
        } else {
            player.removeMetadata(FLY_METADATA, plugin);
        }

        player.setAllowFlight(fly);
        player.setFlying(fly);
        player.setFallDistance(0f);
    }

    /**
     * Check whether the given {@link ItemStack} is a wand that holds the
     * {@link Fly} spell.
     *
     * @param stack the {@link ItemStack} to investigate
     * @return {@code true} if the given {@link ItemStack} can indeed be used
     *         for the {@link Fly} spell
     */
    private boolean isFly(final ItemStack stack) {
        if (stack == null || stack.getType() != Material.STICK) {
            return false;
        }

        final String name = stack.getItemMeta().getDisplayName();

        if (name == null || !ChatColor.stripColor(name)
                .equalsIgnoreCase("fly")) {
            return false;
        }
        return !stack.getEnchantments().isEmpty()
                && stack.getEnchantments().get(Enchantment.LUCK) == 10;
    }
}
