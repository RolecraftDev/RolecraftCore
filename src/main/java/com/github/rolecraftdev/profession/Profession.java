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

import java.util.UUID;

/**
 * Represents a profession that can be chosen by players. After a player has
 * chosen a profession, events affecting that player will be handled according
 * to the profession's {@link ProfessionRule}s.
 */
public class Profession {
    /**
     * The {@link ProfessionManager} object this {@link Profession} is
     * registered to.
     */
    private final ProfessionManager professionManager;
    /**
     * A {@link UUID} that refers to this {@link Profession}.
     */
    private final UUID professionId;

    /**
     * The name of this {@link Profession}.
     */
    private String name;
    /**
     * A container for the {@link ProfessionRule}s this {@link Profession}
     * should be regulated by.
     */
    private ProfessionRuleMap rules;

    /**
     * Create a {@link Profession} from predefined settings. Note that some of
     * the fields are {@code final} and can thus not be modified later on.
     *
     * @param professionManager - The {@link ProfessionManager} this
     *                          {@link Profession} belongs to
     * @param professionId      - The unique identifier
     * @param name              - The unique name
     * @param rules             - A map of {@link ProfessionRule}s that handles events in
     *                          this {@link Profession}.
     */
    public Profession(final ProfessionManager professionManager,
            final UUID professionId, final String name,
            final ProfessionRuleMap rules) {
        this.professionManager = professionManager;
        this.professionId = professionId;
        this.name = name;
        this.rules = rules;
    }

    /**
     * Get the {@link ProfessionManager} this {@link Profession} belongs to.
     *
     * @return Its manager
     */
    public ProfessionManager getManager() {
        return professionManager;
    }

    /**
     * Get the unique identifier of this {@link Profession}.
     *
     * @return Its unique identifier
     */
    public UUID getId() {
        return professionId;
    }

    /**
     * Returns the unique name of this {@link Profession}.
     *
     * @return Its unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the map of {@link ProfessionRule}s this {@link Profession} is
     * dictated by.
     *
     * @return Its {@link ProfessionRuleMap}
     */
    public ProfessionRuleMap getRuleMap() {
        return rules;
    }

    /**
     * Gets the value of the given {@link ProfessionRule} in this Profession's
     * {@link ProfessionRuleMap} object
     *
     * @param rule The rule to get the value for
     * @param <T>  The type of the rule to get the value for
     * @return The value of the given rule for this Profession
     */
    public <T> T getRuleValue(final ProfessionRule<T> rule) {
        return getRuleMap().get(rule);
    }

    /**
     * Set the unique name of this {@link Profession}.
     *
     * @param name - The new unique name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the map of {@link ProfessionRule}s that will regulate events that
     * affect this {@link Profession}.
     *
     * @param rules - The new {@link ProfessionRuleMap}
     */
    public void setRuleMap(final ProfessionRuleMap rules) {
        this.rules = rules;
    }

    @Override
    public int hashCode() {
        return professionId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Profession)) {
            return false;
        }

        final Profession other = (Profession) object;
        return professionId.equals(other.getId());
    }
}
