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
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.spell.SpellCastEvent;

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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicListener implements Listener {
    private final RolecraftCore plugin;
    private final SpellManager spellManager;
    private final Map<UUID, Scoreboard> scoreboards;
    private final ScoreboardManager scoreboardMgr;

    MagicListener(final RolecraftCore plugin, SpellManager manager) {
        this.plugin = plugin;
        spellManager = manager;
        scoreboards = new HashMap<UUID, Scoreboard>();
        scoreboardMgr = Bukkit.getScoreboardManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleManaScoreboard(PlayerInteractEvent e) {
        ItemStack stack = e.getItem();
        boolean shown = false;
        if (stack != null && stack.getType() == Material.STICK) {
            Spell spell = spellManager.getSpell(
                    ChatColor.stripColor(stack.getItemMeta().getDisplayName()));
            if (spell != null) {
                PlayerData data = plugin.getDataManager()
                        .getPlayerData(e.getPlayer().getUniqueId());
                if (scoreboards.containsKey(e.getPlayer().getUniqueId())) {
                    scoreboards.get(e.getPlayer().getUniqueId())
                            .getObjective("Mana").getScore("Mana")
                            .setScore((int) data.getMana());
                } else {
                    Scoreboard board = scoreboardMgr.getNewScoreboard();
                    Objective mana = board.registerNewObjective("Mana",
                            String.valueOf(data.getMana()));
                    mana.getScore("Mana").setScore((int) data.getMana());
                    mana.setDisplayName("Mana");
                    mana.getScore(String.valueOf(data.getMana()));
                    e.getPlayer().setScoreboard(board);
                    scoreboards.put(e.getPlayer().getUniqueId(), board);
                }
                shown = true;
            }
        }

        if (!shown) {
            e.getPlayer().setScoreboard(scoreboardMgr.getNewScoreboard());
        }
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

                                // no, 0 indicates a free cast for some spells
                                // in high level players, use MIN_NORMAL for 
                                // can't be cast
                                if (retVal == Float.MIN_VALUE
                                        || retVal == Float.MIN_NORMAL) {
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
                                // no, 0 indicates a free cast for some spells
                                // in high level players, use MIN_NORMAL for 
                                // can't be cast
                                if (retVal == Float.MIN_VALUE
                                        || retVal == Float.MIN_NORMAL) {
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
        if (e.getCause() == DamageCause.MAGIC) {
            return;
        }
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
                        if (retVal == Float.MIN_VALUE
                                || retVal == Float.MIN_NORMAL) {
                            return;
                        }
                        SpellCastEvent event = new SpellCastEvent(
                                plugin, spell, e.getDamager());
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            player.sendMessage(
                                    event.getCancelMessage());
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
