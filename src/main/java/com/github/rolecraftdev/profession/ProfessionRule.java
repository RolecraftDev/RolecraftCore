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
package com.github.rolecraftdev.profession;

import com.github.rolecraftdev.magic.Spell;

import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a configurable rule, which is used for {@link Profession}s.
 *
 * @since 0.0.5
 */
@SuppressWarnings("rawtypes")
public final class ProfessionRule<T> {
    /**
     * Constants pool for simplified look-up.
     */
    private static final Set<ProfessionRule<?>> pool = new HashSet<ProfessionRule<?>>();

    /**
     * Allowed {@link Spell}s.
     *
     * @since 0.0.5
     */
    public static final ProfessionRule<List> USABLE_SPELLS = new ProfessionRule<List>(
            "usable-spells", List.class);
    /**
     * The armor of which the use is permitted.
     *
     * @since 0.0.5
     */
    public static final ProfessionRule<List> USABLE_ARMOR = new ProfessionRule<List>(
            "usable-armor", List.class);
    /**
     * Allowed {@link Enchantment}s.
     *
     * @since 0.0.5
     */
    public static final ProfessionRule<List> USABLE_ENCHANTMENTS = new ProfessionRule<List>(
            "usable-enchantments", List.class);
    /**
     * Permitted items for crafting and equipping.
     *
     * @since 0.0.5
     */
    public static final ProfessionRule<List> USABLE_ITEMS = new ProfessionRule<List>(
            "usable-weapons", List.class);

    /**
     * The name of this {@link ProfessionRule}.
     */
    private final String name;
    /**
     * The type of this {@link ProfessionRule}.
     */
    private final Class<T> type;

    /**
     * Constructor.
     *
     * @param name the {@link ProfessionRule}'s name
     * @param type the {@link ProfessionRule}'s type
     */
    private ProfessionRule(final String name, final Class<T> type) {
        this.name = name;
        this.type = type;

        pool.add(this);
    }

    /**
     * Retrieve the {@link ProfessionRule} with the specified name.
     *
     * @param name the name of the wanted {@link ProfessionRule}
     * @return the {@link ProfessionRule} with the given name
     * @since 0.0.5
     */
    @Nullable
    public static ProfessionRule<?> getRule(final String name) {
        for (final ProfessionRule<?> element : pool) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Get the name of this {@link ProfessionRule}.
     *
     * @return the name
     * @since 0.0.5
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of this {@link ProfessionRule}.
     *
     * @return the type
     * @since 0.0.5
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Check whether the given {@link Object} is an instance of the declared
     * type of this {@link ProfessionRule}.
     *
     * @param object the {@link Object} that should be examined
     * @return {@code true} if the given {@link Object} is an instance of the
     *         declared type of this {@link ProfessionRule}
     * @since 0.0.5
     */
    public boolean validate(final Object object) {
        return object != null && getType().isAssignableFrom(object.getClass());
    }

    /**
     * Cast the given {@link Object} to the known type of this
     * {@link ProfessionRule} and obtain it as an instance of that type.
     *
     * @param object the {@link Object} which should be cast
     * @return the given {@link Object} with this {@link ProfessionRule}'s known
     *         type
     * @since 0.0.5
     */
    public T cast(final Object object) {
        return getType().cast(object);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @since 0.0.5
     */
    // ProfessionRules are equal when their names are
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof ProfessionRule)) {
            return false;
        }

        final ProfessionRule<?> other = (ProfessionRule<?>) object;
        return getName().equals(other.getName());
    }
}
