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
import com.github.rolecraftdev.event.spell.SpellCastEvent;
import com.github.rolecraftdev.magic.spell.Spell;
import com.github.rolecraftdev.magic.spell.SpellManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicListener implements Listener {
    private RolecraftCore plugin;
    private SpellManager spellManager;

    public MagicListener(RolecraftCore plugin) {
        this.plugin = plugin;
        spellManager = plugin.getSpellManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().getType() == Material.STICK) {
                Action action = e.getAction();
                if (action == Action.RIGHT_CLICK_AIR
                        || action == Action.RIGHT_CLICK_BLOCK
                        || action == Action.LEFT_CLICK_AIR
                        || action == Action.LEFT_CLICK_BLOCK) {
                    Spell spell = spellManager.getSpell(
                            ChatColor.stripColor(e.getItem().getItemMeta()
                                    .getDisplayName()));
                    if (spell != null) {
                        Player player = e.getPlayer();
                        Block clicked = e.getClickedBlock();
                        if (!(spellManager.canCast(player, spell))) {
                            player.sendMessage(ChatColor.DARK_RED
                                    + "You can't cast that spell!");
                            return;
                        }
                        if (action == Action.LEFT_CLICK_AIR
                                || action == Action.LEFT_CLICK_BLOCK) {
                            if (spell.estimateLeftClickMana(player,
                                    clicked,
                                    spellManager.getMagicModfier(player))
                                    <
                                    spellManager.getMana(e.getPlayer())) {
                                SpellCastEvent event = new SpellCastEvent(
                                        plugin, spell, player);
                                Bukkit.getPluginManager().callEvent(event);
                                if (event.isCancelled()) {
                                    player.sendMessage(
                                            event.getCancelMessage());
                                    return;
                                }

                                float retVal = spell.leftClick(player,
                                        clicked, spellManager
                                                .getMagicModfier(player));
                                // MIN_VALUE indicates error, 0 indicates that
                                // the spell can't be cast in the current
                                // situation
                                if (retVal == Float.MIN_VALUE || retVal == 0) {
                                    return;
                                }
                                spellManager.subtractMana(player, retVal);
                                player.sendMessage(
                                        "You have cast " + spell.getName());
                            }
                        } else {
                            if (spell.estimateRightClickMana(player,
                                    clicked,
                                    spellManager.getMagicModfier(player))
                                    <
                                    spellManager.getMana(player)) {
                                SpellCastEvent event = new SpellCastEvent(
                                        plugin, spell, player);
                                Bukkit.getPluginManager().callEvent(event);
                                if (event.isCancelled()) {
                                    player.sendMessage(
                                            event.getCancelMessage());
                                    return;
                                }

                                float retVal = spell.rightClick(player,
                                        clicked, spellManager
                                                .getMagicModfier(player));
                                // MIN_VALUE indicates error, 0 indicates that
                                // the spell can't be cast in the current
                                // situation
                                if (retVal == Float.MIN_VALUE || retVal == 0) {
                                    return;
                                }
                                spellManager.subtractMana(player, retVal);
                                player.sendMessage(
                                        "You have cast " + spell.getName());
                            }
                        }
                    }
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (player.getItemInHand().getType() == Material.STICK) {
                Spell spell = spellManager.getSpell(
                        player.getItemInHand().getItemMeta().getDisplayName());
                if (spell != null) {
                    if (spellManager.getMana(player) >
                            spell.estimateAttackMana(player,
                                    (LivingEntity) e.getEntity(),
                                    spellManager.getMagicModfier(player))) {
                        float retVal = spell
                                .attack(player, (LivingEntity) e.getEntity(),
                                        spellManager.getMagicModfier(player));
                        if (retVal == Float.MIN_VALUE) {
                            return;
                        }
                        spellManager.subtractMana(player, retVal);
                        player.sendMessage("You have cast " + spell.getName());
                    }
                }
            }
        }
    }

}
