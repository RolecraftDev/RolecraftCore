package com.github.rolecraftdev.magic;

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

import com.github.rolecraftdev.RolecraftCore;

public class FlyingListener implements Listener {

    private RolecraftCore plugin;

    public FlyingListener(RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        ItemStack hand = event.getPlayer().getInventory().getItemInHand();
        if (isFly(hand)) {
            event.getPlayer().setFlying(true);
            event.getPlayer().setMetadata(
                    "rolecraftfly",
                    new FixedMetadataValue(plugin,
                            new Boolean(true)));

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemChange(PlayerItemHeldEvent event) {
        ItemStack stack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(isFly(stack)) {
            event.getPlayer().setFlying(true);
            event.getPlayer().setMetadata(
                    "rolecraftfly",
                    new FixedMetadataValue(plugin,
                            new Boolean(true)));

        }
        else {
            if(event.getPlayer().hasMetadata("rolecraftfly")) {
                event.getPlayer().removeMetadata("rolecraftfly", plugin);
                event.getPlayer().setFlying(false);
            }
        }
    }

    private boolean isFly(ItemStack stack) {
        if(stack.getType() == Material.STICK) {
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasDisplayName()) {
                    if(ChatColor.stripColor(stack.getItemMeta().getDisplayName()).equalsIgnoreCase("fly")) {
                        if(stack.getEnchantments().size() > 0) {
                            if(stack.getEnchantments().get(Enchantment.LUCK) == 10) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
