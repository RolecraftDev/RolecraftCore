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

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SpellCastEvent extends SpellEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Entity caster;

    private boolean cancelled;
    private String cancelMessage = ChatColor.DARK_RED + "You can't do that!";

    public SpellCastEvent(final RolecraftCore plugin, final Spell spell,
            final Entity caster) {
        super(plugin, spell);
        this.caster = caster;
    }

    public Entity getCaster() {
        return caster;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelMessage(final String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
