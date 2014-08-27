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
import com.github.rolecraftdev.event.spell.SpellCastEvent.SpellCastType;
import com.github.rolecraftdev.util.SoundWrapper;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
     * @param spellManager the {@link SpellManager} of which the {@link Spell}s
     *        should be handled by this {@link Listener}
     * @since 0.0.5
     */
    MagicListener(final RolecraftCore plugin, final SpellManager spellManager) {
        this.plugin = plugin;
        this.spellManager = spellManager;
        scoreboards = new HashMap<UUID, Scoreboard>();
        scoreboardMgr = Bukkit.getScoreboardManager();
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleManaScoreboard(final PlayerInteractEvent e) {
        final ItemStack stack = e.getItem();
        boolean shown = false;
        if (stack != null && stack.getType() == Material.STICK) {
            final Spell spell = spellManager.getSpellFromItem(stack);
            if (spell != null) {
                final PlayerData data = plugin.getDataManager().getPlayerData(
                        e.getPlayer().getUniqueId());
                if (scoreboards.containsKey(e.getPlayer().getUniqueId())) {
                    scoreboards.get(e.getPlayer().getUniqueId())
                            .getObjective("Mana").getScore("Mana")
                            .setScore((int) data.getMana());
                } else {
                    final Scoreboard board = scoreboardMgr.getNewScoreboard();
                    final Objective mana = board.registerNewObjective("Mana",
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
    public void onInteract(final PlayerInteractEvent e) {
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

        final Spell spell = spellManager.getSpellFromItem(e.getItem());
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
            final SpellCastEvent event = RolecraftEventFactory.spellCast(spell,
                    player, estimate, SpellCastType.LEFT_CLICK,
                    spell.getSound());

            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
                return;
            } else if (event.getManaCost() > spellManager.getMana(player)) {
                return;
            }

            float manaCost = spell.leftClick(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());

            if (manaCost == Spell.CAST_FAILURE
                    || manaCost == Spell.BAD_SITUATION) {
                return;
            }

            // Only take into account event changes if the estimate was accurate
            // otherwise there could be unintended consequences. The estimate should
            // pretty much always be accurate anyway without a failure (which is
            // already accounted for - we return in that event)
            if (manaCost == estimate) {
                manaCost = event.getManaCost();
            }

            spellManager.subtractMana(player, manaCost);

            if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                    .getSettings().isSpellChatMessage()) {
                player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                        MsgVar.create("$spell", spell.getName())));
            }

            final SoundWrapper sound = event.getSound();
            if (sound != null) {
                sound.play(player.getLocation());
            }
        } else {
            final float estimate = spell.estimateRightClickMana(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());
            final SpellCastEvent event = RolecraftEventFactory.spellCast(spell,
                    player, estimate, SpellCastType.RIGHT_CLICK,
                    spell.getSound());

            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
                return;
            } else if (event.getManaCost() > spellManager.getMana(player)) {
                return;
            }

            float manaCost = spell.rightClick(player, clicked,
                    spellManager.getMagicModifier(player), e.getBlockFace());

            if (manaCost == Spell.CAST_FAILURE
                    || manaCost == Spell.BAD_SITUATION) {
                return;
            }

            // Only take into account event changes if the estimate was accurate
            // otherwise there could be unintended consequences. The estimate should
            // pretty much always be accurate anyway without a failure (which is
            // already accounted for - we return in that event)
            if (manaCost == estimate) {
                manaCost = event.getManaCost();
            }

            spellManager.subtractMana(player, manaCost);

            if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                    .getSettings().isSpellChatMessage()) {
                player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                        MsgVar.create("$spell", spell.getName())));
            }

            final SoundWrapper sound = event.getSound();
            if (sound != null) {
                sound.play(player.getLocation());
            }
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (e.getCause() == DamageCause.MAGIC || !(e
                .getDamager() instanceof Player)) {
            return;
        }

        final Player player = (Player) e.getDamager();
        final Spell spell = spellManager.getSpellFromItem(
                player.getItemInHand());
        if (spell == null) {
            return;
        }

        if (!(spellManager.canCast(player, spell))) {
            if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                    .getSettings().isSpellChatMessage()) {
                player.sendMessage(plugin.getMessage(Messages.CANNOT_CAST_SPELL,
                        MsgVar.create("$spell", spell.getName())));
            }
            return;
        }

        final float estimate = spell.estimateAttackMana(player,
                (LivingEntity) e.getEntity(),
                spellManager.getMagicModifier(player));
        final SpellCastEvent event = RolecraftEventFactory.spellCast(spell,
                e.getDamager(), estimate, SpellCastType.ATTACK,
                spell.getSound());

        if (event.isCancelled()) {
            player.sendMessage(event.getCancelMessage());
            return;
        } else if (event.getManaCost() > spellManager.getMana(player)) {
            return;
        }

        float manaCost = spell.attack(player, (LivingEntity) e.getEntity(),
                spellManager.getMagicModifier(player));

        if (manaCost == Spell.CAST_FAILURE
                || manaCost == Spell.BAD_SITUATION) {
            return;
        }

        // Only take into account event changes if the estimate was accurate
        // otherwise there could be unintended consequences. The estimate should
        // pretty much always be accurate anyway without a failure (which is
        // already accounted for - we return in that event)
        if (manaCost == estimate) {
            manaCost = event.getManaCost();
        }

        spellManager.subtractMana(player, manaCost);

        if (plugin.getDataManager().getPlayerData(player.getUniqueId())
                .getSettings().isSpellChatMessage()) {
            player.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                    MsgVar.create("$spell", spell.getName())));
        }

        final SoundWrapper sound = event.getSound();
        if (sound != null) {
            sound.play(player.getLocation());
        }
    }
}
