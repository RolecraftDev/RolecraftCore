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
public final class ProfessionRule<T> {
    /**
     * Constants pool for easy lookups.
     */
    private static final Set<ProfessionRule<?>> pool = new HashSet<ProfessionRule<?>>();

    /**
     * A rule which defines the spells usable by a profession
     */
    @SuppressWarnings("rawtypes")
    public static final ProfessionRule<List> USABLE_SPELLS = new ProfessionRule<List>(
            "usable-spells", List.class);
    @SuppressWarnings("rawtypes")
    public static final ProfessionRule<List> USABLE_ARMOR = new ProfessionRule<List>(
            "usable-armor", List.class);
    @SuppressWarnings("rawtypes")
    public static final ProfessionRule<List> USABLE_ENCHANTMENTS = new ProfessionRule<List>(
            "usable-enchantments",List.class);

    /**
     * Get a unique {@link ProfessionRule} by its name.
     *
     * @param name - Its unique name
     * @return The {@link ProfessionRule} if it can be found in {@link #pool},
     * else null
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
     * The name of this rule.
     */
    private final String name;
    /**
     * The type of object this rule should be set to.
     */
    private final Class<T> type;

    /**
     * Create a new, unique {@link ProfessionRule}.
     *
     * @param name - The unique name
     * @param type - The type of the values it will hold in a
     *             {@link ProfessionRuleMap}.
     */
    private ProfessionRule(final String name, final Class<T> type) {
        this.name = name;
        this.type = type;

        pool.add(this);
    }

    /**
     * Get the unique name of this {@link ProfessionRule}.
     *
     * @return Its unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type, the values of this {@link ProfessionRule} should be in a
     * {@link ProfessionRuleMap}.
     *
     * @return Its value type
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Checks whether the given object is valid to be cast to this
     * {@link ProfessionRule}'s value type.
     *
     * @param object - The object to check the validity to cast of
     * @return True if the given object is of the correct type, false otherwise
     */
    public boolean validate(final Object object) {
        return object != null && getType().isAssignableFrom(object.getClass());
    }

    /**
     * Casts the given object to the type used for this {@link ProfessionRule}'s
     * values.
     *
     * @param object - The object to cast to this rule's value type
     * @return The instance of the given object cast to this rule's value type
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
