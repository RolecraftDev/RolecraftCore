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
import java.util.UUID;

/**
 * Represents a player-selectable profession.
 *
 * @since 0.0.5
 */
public class Profession {
    /**
     * The {@link ProfessionManager} this {@link Profession} is registered to.
     */
    private final ProfessionManager professionManager;
    /**
     * The {@link UUID} of this {@link Profession}.
     */
    private final UUID professionId;

    /**
     * The name of this {@link Profession}.
     */
    private String name;
    /**
     * The {@link ProfessionRuleMap} of this {@link Profession}.
     */
    private ProfessionRuleMap rules;

    /**
     * Constructor.
     *
     * @param professionManager the {@link ProfessionManager} this
     *        {@link Profession} will be registered to
     * @param professionId the {@link Profession}'s {@link UUID}
     * @param name the {@link Profession}'s name
     * @param rules the {@link Profession}'s {@link ProfessionRuleMap}
     * @since 0.0.5
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
     * Get the {@link ProfessionManager} this {@link Profession} is supposed to
     * be registered to.
     *
     * @return the {@link ProfessionManager} this {@link Profession} is
     *         registered to
     * @since 0.0.5
     */
    public ProfessionManager getManager() {
        return professionManager;
    }

    /**
     * Get the {@link UUID} of this {@link Profession}.
     *
     * @return the {@link UUID}
     * @since 0.0.5
     */
    public UUID getId() {
        return professionId;
    }

    /**
     * Get the name of this {@link Profession}.
     *
     * @return the name
     * @since 0.0.5
     */
    public String getName() {
        return name;
    }

    /**
     * Get the {@link ProfessionRuleMap} of this {@link Profession}.
     *
     * @return the {@link ProfessionRuleMap}
     * @since 0.0.5
     */
    @Nonnull
    public ProfessionRuleMap getRuleMap() {
        return rules;
    }

    /**
     * Get the value that is referred to by the given {@link ProfessionRule} key
     * in the {@link ProfessionRuleMap} of this {@link Profession}.
     *
     * @param rule the key of which the value is wanted
     * @param <T> the type of the given {@link ProfessionRule}
     * @return the value of the given {@link ProfessionRule} key
     * @since 0.0.5
     */
    @Nullable
    public <T> T getRuleValue(final ProfessionRule<T> rule) {
        return getRuleMap().get(rule);
    }

    /**
     * Set the name of this {@link Profession}.
     *
     * @param name the new name for the profession
     * @since 0.0.5
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the {@link ProfessionRuleMap} of this {@link Profession}.
     *
     * @param rules the new {@link ProfessionRuleMap}
     * @since 0.0.5
     */
    public void setRuleMap(final ProfessionRuleMap rules) {
        this.rules = rules;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public int hashCode() {
        return professionId.hashCode();
    }

    /**
     * @since 0.0.5
     */
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Profession)) {
            return false;
        }

        final Profession other = (Profession) object;
        return professionId.equals(other.getId());
    }
}
