package com.github.rolecraftdev.event.spell;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftEvent;
import com.github.rolecraftdev.magic.spell.Spell;

import org.bukkit.entity.Entity;

public abstract class SpellEvent extends RolecraftEvent {
    private final Spell spell;
    private final Entity caster;

    public SpellEvent(final RolecraftCore plugin, final Spell spell,
            final Entity caster) {
        super(plugin);
        this.spell = spell;
        this.caster = caster;
    }
}
