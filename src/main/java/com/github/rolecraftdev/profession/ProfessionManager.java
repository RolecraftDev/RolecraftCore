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

import com.github.rolecraftdev.RolecraftCore;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ProfessionManager {
    private final RolecraftCore plugin;
    private final Set<Profession> professions;

    public ProfessionManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        professions = new HashSet<Profession>();
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public Set<Profession> getProfessions() {
        return new HashSet<Profession>(professions);
    }

    public Profession getProfession(final String name) {
        for (final Profession profession : professions) {
            if (profession.getName().equalsIgnoreCase(name))
                return profession;
        }
        return null;
    }

    public Profession getPlayerProfession(final UUID player) {
        for (final Profession profession : professions) {
            if (profession.isMember(player))
                return profession;
        }
        return null;
    }

    public boolean addProfession(final Profession profession) {
        return professions.add(profession);
    }

    public void loadProfessions() {
        final File directory = new File(plugin.getDataFolder(), "professions");

        if (directory.isFile())
            directory.delete();
        if (!directory.exists())
            directory.mkdirs();

        for (final File professionFile : directory.listFiles()) {
            final ProfessionRuleMap rules = ProfessionRuleMap
                    .load(professionFile);
            // TODO: Create Profession and add it to professions
        }
    }
}
