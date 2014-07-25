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
import java.util.UUID;

public class Profession {
    private final ProfessionManager professionManager;
    private final UUID professionId;
    private final Set<UUID> members;

    private String name;
    private ProfessionRuleMap rules;

    public Profession(final ProfessionManager professionManager) {
        this.professionManager = professionManager;
        professionId = UUID.randomUUID();
        members = new HashSet<UUID>();
    }

    public Profession(final ProfessionManager professionManager,
            final UUID professionId, final Set<UUID> members,
            final String name, final ProfessionRuleMap rules) {
        this.professionManager = professionManager;
        this.professionId = professionId;
        this.members = members;
        this.name = name;
        this.rules = rules;
    }

    public ProfessionManager getManager() {
        return professionManager;
    }

    public Set<UUID> getMembers() {
        return new HashSet<UUID>(members);
    }

    public boolean isMember(final UUID member) {
        return members.contains(member);
    }

    public void addMember(final UUID member) {
        members.add(member);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ProfessionRuleMap getRuleMap() {
        return rules;
    }

    public void setRuleMap(final ProfessionRuleMap rules) {
        this.rules = rules;
    }

    public UUID getId() {
        return professionId;
    }

    @Override
    public int hashCode() {
        return professionId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Profession))
            return false;

        final Profession other = (Profession) object;
        return professionId.equals(other.getId());
    }
}
