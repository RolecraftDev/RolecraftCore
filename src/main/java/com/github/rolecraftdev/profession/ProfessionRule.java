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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A rule that a {@link Profession} should be dominated by. These are contained
 * by a {@link ProfessionRuleMap}.
 *
 * @param <T> - Type of its values in a {@link ProfessionRuleMap}
 */
@SuppressWarnings("rawtypes")
public final class ProfessionRule<T> {
    /**
     * Constants pool for easy lookups.
     */
    private static final Set<ProfessionRule<?>> pool = new HashSet<ProfessionRule<?>>();

    /**
     * A rule which defines the spells usable by a profession
     */
    public static final ProfessionRule<List> USABLE_SPELLS = new ProfessionRule<List>(
            "usable-spells", List.class);
    public static final ProfessionRule<List> USABLE_ARMOR = new ProfessionRule<List>(
            "usable-armor", List.class);
    public static final ProfessionRule<List> USABLE_ENCHANTMENTS = new ProfessionRule<List>(
            "usable-enchantments", List.class);
    /**
     * Denies the equipping and crafting of items, STRONGLY recommended to put an *
     * and deny specific ones in this field
     */
    public static final ProfessionRule<List> USABLE_ITEMS = new ProfessionRule<List>(
            "usable-weapons", List.class);

    /**
     * Get a unique {@link ProfessionRule} by its name.
     *
     * @param name the unique name of the rule
     * @return the {@link ProfessionRule} if it can be found in {@link #pool},
     *         else {@code null}
     */
    public static ProfessionRule<?> getRule(final String name) {
        for (final ProfessionRule<?> element : pool) {
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    /**
     * The unique name of this rule.
     */
    private final String name;
    /**
     * The type of object this rule should be set to.
     */
    private final Class<T> type;

    /**
     * Create a new, unique {@link ProfessionRule}.
     *
     * @param name the unique name of the rule
     * @param type the type of the values the rule takes
     */
    private ProfessionRule(final String name, final Class<T> type) {
        this.name = name;
        this.type = type;

        pool.add(this);
    }

    /**
     * Get the unique name of this {@link ProfessionRule}.
     *
     * @return the unique name of this {@link ProfessionRule}
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type, the values of this {@link ProfessionRule} should be in a
     * {@link ProfessionRuleMap}.
     *
     * @return the value type for this rule
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Checks whether the given object is valid to be cast to this
     * {@link ProfessionRule}'s value type.
     *
     * @param object the object to check the validity to cast of
     * @return {@code true} if the given object is of the correct type, {@code
     *         false } otherwise
     */
    public boolean validate(final Object object) {
        return object != null && getType().isAssignableFrom(object.getClass());
    }

    /**
     * Casts the given object to the type used for this {@link ProfessionRule}'s
     * values.
     *
     * @param object the object to cast to this rule's value type
     * @return the instance of the given object cast to this rule's value type
     */
    public T cast(final Object object) {
        return getType().cast(object);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

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
