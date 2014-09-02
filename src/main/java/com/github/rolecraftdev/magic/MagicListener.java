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
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.spell.SpellCastEvent;
import com.github.rolecraftdev.event.spell.SpellCastEvent.SpellCastType;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

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
    }

    /*
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        // Only create the mana-display if the player can perform magic
        if (spellManager.canCast(event.getPlayer())) {
            spellManager.getManaUpdater().createDisplayFor(event.getPlayer());
        }
    }

    /*
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    /*
     * @since 0.0.5
     */
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {
        onPlayerLeave(event.getPlayer());
    }

    /**
     * Runs functions that should be ran when a player leaves the Minecraft
     * server.
     *
     * @param player the player who leaves
     */
    private void onPlayerLeave(final Player player) {
        spellManager.getManaUpdater().disposeDisplayOf(player);
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR
                && action != Action.RIGHT_CLICK_BLOCK
                && action != Action.LEFT_CLICK_AIR
                && action != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Player caster = event.getPlayer();
        final Block clicked = event.getClickedBlock();
        final Spell cast = getAvailableSpell(caster, event.getItem());
        final int casterModifier = spellManager.getMagicModifier(caster);

        if (cast == null) {
            return;
        }

        if (action == Action.LEFT_CLICK_AIR
                || action == Action.LEFT_CLICK_BLOCK) {
            final float estimate = cast.estimateLeftClickMana(caster, clicked,
                    casterModifier, event.getBlockFace());
            final SpellCastEvent castEvent = initiateCasting(cast,
                    caster, estimate, SpellCastType.LEFT_CLICK);
            // Cast the spell with the most up-to-date values
            final float castCost = cast.leftClick(caster, clicked, spellManager
                    .getMagicModifier(caster), event.getBlockFace());

            finaliseCasting(castEvent, castCost, estimate);
        } else {
            final float estimate = cast.estimateRightClickMana(caster, clicked,
                    casterModifier, event.getBlockFace());
            final SpellCastEvent castEvent = initiateCasting(cast,
                    caster, estimate, SpellCastType.RIGHT_CLICK);
            // Cast the spell with the most up-to-date values
            final float castCost = cast
                    .rightClick(caster, clicked, spellManager
                            .getMagicModifier(caster), event.getBlockFace());

            finaliseCasting(castEvent, castCost, estimate);
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.MAGIC) {
            return;
        }
        if (!(event.getDamager() instanceof Player)
                || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        final Player caster = (Player) event.getDamager();
        final LivingEntity target = (LivingEntity) event.getEntity();
        final Spell cast = getAvailableSpell(caster, caster.getItemInHand());

        if (cast == null) {
            return;
        }

        final float estimate = cast.estimateAttackMana(caster, target,
                spellManager.getMagicModifier(caster));
        final SpellCastEvent castEvent = initiateCasting(cast, caster,
                estimate, SpellCastType.ATTACK);
        // Cast the spell with the most up-to-date values
        final float castCost = cast.attack(caster, target, spellManager
                .getMagicModifier(caster));

        finaliseCasting(castEvent, castCost, estimate);
    }

    /**
     * Gets the spell that is available for use given the specified wand. If
     * {@code player} cannot use the wand to cast a spell one way or another,
     * {@code null} is returned.
     *
     * @param player the player to investigate
     * @param stack the wand the player uses
     * @return the spell that is available to the player given the wand
     */
    private Spell getAvailableSpell(final Player player, final ItemStack stack) {
        if (stack == null) {
            return null;
        }

        final Spell spell = spellManager.getSpellFromItem(stack);

        if (spell == null) {
            return null;
        }
        if (!spellManager.canCast(player, spell)) {
            player.sendMessage(plugin.getMessage(Messages.CANNOT_CAST_SPELL,
                    MsgVar.SPELL.value(spell.getName())));
            return null;
        } else {
            return spell;
        }
    }

    /**
     * Calls a cast spell event and handle it. If the event shoudln't be built
     * on further, {@code null} will be returned. Note that this doesn't perform
     * the spell.
     *
     * @param spell the spell to cast in the event
     * @param caster the caster of the spell
     * @param manaCost the cost of performing this spell in its current context
     * @param type the manor in which the spell has been performed
     * @return the appropriate cast spell event if there's no reason to
     *         discontinue, {@code null} otherwise
     */
    private SpellCastEvent initiateCasting(final Spell spell,
            final Player caster, final float manaCost, final SpellCastType type) {
        final SpellCastEvent castEvent = RolecraftEventFactory.spellCast(spell,
                caster, manaCost, type);

        if (castEvent.isCancelled()) {
            caster.sendMessage(castEvent.getCancelMessage());
            return null;
        } else if (castEvent.getManaCost() > spellManager.getMana(caster)) {
            return null;
        } else {
            return castEvent;
        }
    }

    /**
     * Finish up the cast spell event after it has been performed.
     *
     * @param castEvent the appropriate cast spell event
     * @param castCost the cost of the spell when it is performed
     * @param estimate the predefined casting cast
     */
    private void finaliseCasting(final SpellCastEvent castEvent,
            float castCost, final float estimate) {
        if (!(castEvent.getCaster() instanceof Player)) {
            return;
        }
        if (castCost == Spell.CAST_FAILURE || castCost == Spell.BAD_SITUATION) {
            return;
        }
        // Use the estimate if it was incorrect, the event cost otherwise
        if (castCost == estimate) {
            castCost = castEvent.getManaCost();
        }

        final Player caster = (Player) castEvent.getCaster();

        spellManager.subtractMana(caster, castCost);

        if (plugin.getDataManager().getPlayerData(caster.getUniqueId())
                .getSettings().isSpellChatMessage()) {
            caster.sendMessage(plugin.getMessage(Messages.SPELL_CAST,
                    MsgVar.SPELL.value(castEvent.getSpell().getName())));
        }
        if (castEvent.getSound() != null) {
            castEvent.getSound().play(caster.getLocation());
        }
    }
}
