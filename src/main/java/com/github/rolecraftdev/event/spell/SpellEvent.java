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
import com.github.rolecraftdev.event.RolecraftEvent;
import com.github.rolecraftdev.magic.Spell;

import javax.annotation.Nonnull;

/**
 * A {@link RolecraftEvent} called in coherence with a {@link Spell}.
 *
 * @since 0.0.5
 */
public abstract class SpellEvent extends RolecraftEvent {
    /**
     * The affected {@link Spell}.
     */
    @Nonnull
    private final Spell spell;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param spell the affected {@link Spell}
     * @since 0.0.5
     */
    SpellEvent(final RolecraftCore plugin, @Nonnull final Spell spell) {
        super(plugin);
        this.spell = spell;
    }

    /**
     * Returns the affected {@link Spell}.
     *
     * @return the affected {@link Spell}
     * @since 0.0.5
     */
    @Nonnull
    public Spell getSpell() {
        return spell;
    }
}
