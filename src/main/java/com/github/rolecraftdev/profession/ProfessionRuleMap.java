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

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper around a {@link Map} of a {@link ProfessionRule}s - {@link Object}s
 * pair. This is used for storing rules that govern events, that affect a
 * {@link Profession}.
 */
public final class ProfessionRuleMap {
    /**
     * The name of the {@link Profession} it regulates.
     */
    private final String professionName;
    /**
     * A {@link Map} that links values to {@link ProfessionRule}s, to dictate
     * its linked {@link Profession} accordingly.
     */
    private final Map<ProfessionRule<?>, Object> rules = new HashMap<ProfessionRule<?>, Object>();

    /**
     * Create a new {@link ProfessionRuleMap}.
     * 
     * @param professionName - The name of its linked {@link Profession}
     */
    public ProfessionRuleMap(final String professionName) {
        this.professionName = professionName;
    }

    /**
     * Get the name of the {@link Profession} that is linked to this
     * {@link ProfessionRuleMap}.
     * 
     * @return Its linked {@link Profession}'s name
     */
    public String getProfessionName() {
        return professionName;
    }

    /**
     * Gets the value of the given {@link ProfessionRule} in this
     * {@link ProfessionRuleMap}.
     * 
     * @param key - The {@link ProfessionRule} the value should be returned of
     * @param <T> - The return type
     * @return The value of the specified {@link ProfessionRule}, which could be
     *         null
     */
    public <T> T get(final ProfessionRule<T> key) {
        return key.cast(rules.get(key));
    }

    /**
     * Sets the given {@link ProfessionRule} to the given value.
     * 
     * @param key - The {@link ProfessionRule} to set
     * @param value - The value to set the given {@link ProfessionRule} to
     * @return True if the value has been set, else false
     */
    public boolean set(final ProfessionRule<?> key, final Object value) {
        if (key == null || !key.validate(value)) {
            return false;
        }

        rules.put(key, value);
        return true;
    }
}
