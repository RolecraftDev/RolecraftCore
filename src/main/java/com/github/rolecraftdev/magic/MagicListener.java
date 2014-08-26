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
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.spell.SpellCastEvent;
import com.github.rolecraftdev.util.SoundWrapper;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

/**
 * A {@link Listener} for {@link Event}s that can be used to perform
 * {@link Spell}s, when of course, met under certain conditions.
 *
 * @since 0.0.5
 */
public class MagicListener implements Listener {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    private final SpellManager spellManager;
    private final Map<UUID, Scoreboard> scoreboards;
    private final ScoreboardManager scoreboardMgr;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param manager the {@link SpellManager} of which the {@link Spell}s
     *        should be handled by this {@link Listener}
     * @since 0.0.5
     */
    MagicListener(final RolecraftCore plugin, final SpellManager manager) {
        this.plugin = plugin;
        spellManager = manager;
        scoreboards = new HashMap<UUID, Scoreboard>();
        scoreboardMgr = Bukkit.getScoreboardManager();
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleManaScoreboard(PlayerInteractEvent e) {
        ItemStack stack = e.getItem();
        boolean shown = false;
        if (stack != null && stack.getType() == Material.STICK) {
            Spell spell = getSpell(stack);
            if (spell != null) {
                PlayerData data = plugin.getDataManager().getPlayerData(
                        e.getPlayer().getUniqueId());
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

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        final ItemStack stack = e.getItem();
        if (stack == null || stack.getType() != Material.STICK) {
            return;
        }

        final Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK
                && action != Action.LEFT_CLICK_AIR
                && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Spell spell = getSpell(e.getItem());
        if (spell == null) {
            return;
        }

        final Player player = e.getPlayer();
        final Block clicked = e.getClickedBlock();
        if (!(spellManager.canCast(player, spell))) {
            player.sendMessage(plugin.getMessage(Messages.CANNOT_CAST_SPELL,
                    MsgVar.create("$spell", spell.getName())));
            return;
        }

        if (action == Action.LEFT_CLICK_AIR
                || action == Action.LEFT_CLICK_BLOCK) {
            final float estimate = spell.estimateLeftClickMana(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());
            if (estimate > spellManager.getMana(player)) {
                return;
            }

            final SpellCastEvent event = RolecraftEventFactory.spellCast(spell,
                    player, estimate);
            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
                return;
            }

            final float evtMana = event.getManaCost();
            float retVal = spell.leftClick(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());

            // MIN_VALUE = error, MIN_NORMAL = can't be cast right now
            if (retVal == Float.MIN_VALUE || retVal == Float.MIN_NORMAL) {
                return;
            }

            retVal = evtMana;
            spellManager.subtractMana(player, retVal);

            if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                    .getSettings().isSpellChatMessage()) {
                player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                        MsgVar.create("$spell", spell.getName())));
            }

            final SoundWrapper sound = spell.getSound();
            if (sound != null) {
                sound.play(player.getLocation());
            }
        } else {
            final float estimate = spell.estimateRightClickMana(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());
            if (estimate > spellManager.getMana(player)) {
                return;
            }

            final SpellCastEvent event = RolecraftEventFactory.spellCast(spell,
                    player, estimate);
            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
                return;
            }

            float retVal = spell.rightClick(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());

            // MIN_VALUE = error, MIN_NORMAL = can't be cast right now
            if (retVal == Float.MIN_VALUE || retVal == Float.MIN_NORMAL) {
                return;
            }

            retVal = event.getManaCost();
            spellManager.subtractMana(player, retVal);

            if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                    .getSettings().isSpellChatMessage()) {
                player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                        MsgVar.create("$spell", spell.getName())));
            }

            final SoundWrapper sound = spell.getSound();
            if (sound != null) {
                sound.play(player.getLocation());
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getCause() == DamageCause.MAGIC || !(e
                .getDamager() instanceof Player)) {
            return;
        }

        final Player player = (Player) e.getDamager();
        final Spell spell = getSpell(player.getItemInHand());
        if (spell == null) {
            return;
        }

        if (!(spellManager.canCast(player, spell))) {
            player.sendMessage(plugin.getMessage(Messages.CANNOT_CAST_SPELL,
                    MsgVar.create("$spell", spell.getName())));
            return;
        }

        final float estimate = spell.estimateAttackMana(player,
                (LivingEntity) e.getEntity(),
                spellManager.getMagicModifier(player));
        if (estimate > spellManager.getMana(player)) {
            return;
        }

        final SpellCastEvent event = RolecraftEventFactory.spellCast(
                spell, e.getDamager(), estimate);
        if (event.isCancelled()) {
            player.sendMessage(event.getCancelMessage());
            return;
        }

        float retVal = spell.attack(player, (LivingEntity) e.getEntity(),
                spellManager.getMagicModifier(player));

        // MIN_VALUE = error, MIN_NORMAL = can't be cast right now
        if (retVal == Float.MIN_VALUE
                || retVal == Float.MIN_NORMAL) {
            return;
        }

        retVal = event.getManaCost();
        spellManager.subtractMana(player, retVal);

        if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                .getSettings().isSpellChatMessage()) {
            player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                    MsgVar.create("$spell", spell.getName())));
        }

        final SoundWrapper sound = spell.getSound();
        if (sound != null) {
            sound.play(player.getLocation());
        }
    }

    /**
     * Retrieve the {@link Spell} associated to the given wand.
     *
     * @param stick the wand
     * @return the {@link Spell} that can be cast with the given wand
     */
    private Spell getSpell(ItemStack stick) {
        if (stick == null || stick.getType() != Material.STICK || !stick
                .hasItemMeta() || !stick.getItemMeta().hasDisplayName()) {
            return null;
        }

        final Spell temp = spellManager.getSpell(ChatColor
                .stripColor(stick.getItemMeta().getDisplayName()));
        if (!stick.getEnchantments().isEmpty()
                && stick.getEnchantments().get(Enchantment.LUCK) == 10) {
            return temp;
        }
        return null;
    }
}
