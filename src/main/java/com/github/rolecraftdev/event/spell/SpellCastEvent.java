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
import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.util.SoundWrapper;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link SpellEvent} that is called when a {@link Spell} is on the brink of
 * being cast.
 *
 * @since 0.0.5
 */
public class SpellCastEvent extends SpellEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Entity caster;
    private final SpellCastType type;

    private float manaCost;
    private boolean cancelled;
    private SoundWrapper sound;
    /**
     * The message sent to the caster when this event is cancelled.
     */
    private String cancelMessage = ChatColor.DARK_RED + "You can't do that!";

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
            @Nonnull final SpellCastType type,
            @Nullable final SoundWrapper sound) {
        super(plugin, spell);
        this.caster = caster;
        this.manaCost = manaCost;
        this.type = type;
        this.sound = sound;
    }

    public enum SpellCastType {
        LEFT_CLICK, RIGHT_CLICK, ATTACK
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
     */
    public void setSound(final SoundWrapper sound) {
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
    public void setCancelMessage(final String cancelMessage) {
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
