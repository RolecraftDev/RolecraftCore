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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper around a {@link Map} of {@link ProfessionRule}s and their
 * appropriate values, which allows for type maintainability.
 *
 * @since 0.0.5
 */
public final class ProfessionRuleMap {
    /**
     * The name of the associated {@link Profession}.
     */
    @Nonnull
    private final String professionName;
    /**
     * The {@link Map} for {@link ProfessionRule}s and their configured value.
     */
    private final Map<ProfessionRule<?>, Object> rules = new HashMap<ProfessionRule<?>, Object>();

    /**
     * Constructor.
     *
     * @param professionName the name of the associated {@link Profession}
     * @since 0.0.5
     */
    public ProfessionRuleMap(@Nonnull final String professionName) {
        this.professionName = professionName;
    }

    /**
     * Get the name of the associated {@link Profession}.
     *
     * @return the name of the associated {@link Profession}
     * @since 0.0.5
     */
    @Nonnull
    public String getProfessionName() {
        return professionName;
    }

    /**
     * Obtain the configured value of the given {@link ProfessionRule}.
     *
     * @param key the {@link ProfessionRule} of which the value should be
     *        returned
     * @param <T> the type the given {@link ProfessionRule} is defined as
     * @return the configured value of the given {@link ProfessionRule}
     * @since 0.0.5
     */
    @Nullable
    public <T> T get(final ProfessionRule<T> key) {
        if (!rules.containsKey(key)) {
            return null;
        }
        return key.cast(rules.get(key));
    }

    /**
     * Obtain a {@link Set} of keys for this rule map's {@link ProfessionRule}s.
     *
     * @return a {@link Set} of keys for this rule map's {@link ProfessionRule}s
     */
    public Set<ProfessionRule<?>> getRuleKeys() {
        return new HashSet<ProfessionRule<?>>(rules.keySet());
    }

    /**
     * Set the value of given {@link ProfessionRule} to the specified
     * {@link Object}. The given value should be an instance of the
     * {@link ProfessionRule}'s type.
     *
     * @param key the {@link ProfessionRule} the value should be set of
     * @param value the new value
     * @return {@code true} if setting the key to the given value has been
     *         successful; {@code false} otherwise
     * @since 0.0.5
     */
    public boolean set(final ProfessionRule<?> key, final Object value) {
        if (key == null || !key.validate(value)) {
            return false;
        }

        rules.put(key, value);
        return true;
    }
}
