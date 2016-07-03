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
package com.github.rolecraftdev.event.spell;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftCancellable;
import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.util.SoundWrapper;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link SpellEvent} that is called when a {@link Spell} is on the brink of
 * being cast.
 *
 * @since 0.0.5
 */
public class SpellCastEvent extends SpellEvent implements RolecraftCancellable {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link Entity} who cast the {@link Spell}.
     */
    private final Entity caster;
    /**
     * The manner of {@link Spell} being cast, which may be left click, right
     * click, or attack.
     */
    private final SpellCastType type;

    /**
     * The quantity of mana to be consumed in the {@link Spell}'s usage.
     */
    private float manaCost;
    /**
     * Whether or not this event is cancelled; if this is true, the {@link Spell}
     * will not be cast.
     */
    private boolean cancelled;
    /**
     * The {@link SoundWrapper} object associated with the sound produced when
     * the spell is cast.
     */
    private SoundWrapper sound;
    /**
     * The message sent to the caster when this event is cancelled.
     */
    private String cancelMessage;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param spell the affected {@link Spell}
     * @param caster the executor of the {@link Spell}
     * @param manaCost the amount of mana the {@link Spell} will cost
     * @since 0.0.5
     */
    public SpellCastEvent(final RolecraftCore plugin, final Spell spell,
            @Nonnull final Entity caster, final float manaCost,
            @Nonnull final SpellCastType type) {
        super(plugin, spell);
        this.caster = caster;
        this.manaCost = manaCost;
        this.type = type;
        sound = spell.getSound();

        this.cancelMessage = plugin.getMessage(Messages.NOT_ALLOWED);
    }

    /**
     * The different categories of {@link Spell} which may be cast.
     *
     * @since 0.0.5
     */
    public enum SpellCastType {
        /**
         * A spell cast by a left click.
         */
        LEFT_CLICK,
        /**
         * A spell cast by a right click.
         */
        RIGHT_CLICK,
        /**
         * A spell cast by attacking.
         */
        ATTACK;
    }

    /**
     * Get the executor of the {@link Spell}.
     *
     * @return the executor of the {@link Spell}
     * @since 0.0.5
     */
    @Nonnull
    public Entity getCaster() {
        return caster;
    }

    /**
     * Gets the type of spell cast for this {@link SpellCastEvent}. Returns one
     * of RIGHT_CLICK, LEFT_CLICK or ATTACK.
     *
     * @return this event's {@link SpellCastType}
     * @since 0.0.5
     */
    @Nonnull
    public SpellCastType getCastType() {
        return type;
    }

    /**
     * Get the amount of mana it will cost the caster to perform the
     * {@link Spell}.
     *
     * @return the mana cost
     * @since 0.0.5
     */
    public float getManaCost() {
        return manaCost;
    }

    /**
     * Gets the {@link SoundWrapper} sound to be played when the spell is cast
     *
     * @return the {@link Spell}s sound
     * @since 0.0.5
     */
    @Nullable
    public SoundWrapper getSound() {
        return sound;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Obtain the message that will be sent to caster on the occasion of this
     * event being cancelled.
     *
     * @return the cancel message
     * @since 0.0.5
     */
    @Override @Nonnull
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * Set the amount of mana it will cost to perform the {@link Spell}
     *
     * @param manaCost the new mana cost
     * @since 0.0.5
     */
    public void setManaCost(final float manaCost) {
        this.manaCost = manaCost;
    }

    /**
     * Sets the {@link SoundWrapper} sound to be played when the spell is cast
     *
     * @param sound the {@link Spell}s sound for this cast
     * @since 0.0.5
     */
    public void setSound(@Nullable final SoundWrapper sound) {
        this.sound = sound;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Set the message that will be sent to the caster when this event is
     * cancelled.
     *
     * @param cancelMessage the new cancel message
     * @since 0.0.5
     */
    @Override
    public void setCancelMessage(@Nonnull final String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 0.0.5
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
