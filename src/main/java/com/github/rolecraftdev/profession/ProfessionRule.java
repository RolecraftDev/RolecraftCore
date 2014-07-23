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
import java.util.Set;

public class ProfessionRule<T> {

    // Constant pool for easy lookups
    private static final Set<ProfessionRule<?>> pool = new HashSet<ProfessionRule<?>>();

    // Constants
    // here

    public static ProfessionRule<?> getRule(String name) {
        for (ProfessionRule<?> element : pool) {
            if (element.getName().equals(name))
                return element;
        }
        return null;
    }

    private final String name;
    private final Class<T> type;

    private ProfessionRule(String name, Class<T> type) {
        this.name = name;
        this.type = type;

        pool.add(this);
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public boolean validate(Object object) {
        return object != null && getType().isAssignableFrom(object.getClass());
    }

    public T cast(Object object) {
        return getType().cast(object);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    // ProfessionRules are equal when their names are
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProfessionRule))
            return false;

        ProfessionRule<?> other = (ProfessionRule<?>) object;
        return getName().equals(other.getName());
    }
}
