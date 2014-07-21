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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfessionRuleSet {
    private final String professionName;

    public ProfessionRuleSet(final String professionName) {
        this.professionName = professionName;
    }

    public void loadRules(final File directory) {
        final File file = new File(directory, professionName + ".yml");
        if (!file.exists()) {
            writeDefaults(professionName, file);
        }

        // TODO: Load settings from file
    }

    public void saveRules(final File file) {
        // TODO
    }

    public static void writeDefaults(final String profession, final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        final ProfessionRuleSet rules = defaultRuleSets.get(profession);
        if (rules != null) {
            rules.saveRules(file);
        }
    }

    private static final Map<String, ProfessionRuleSet> defaultRuleSets = new HashMap<String, ProfessionRuleSet>();

    static {
        final ProfessionRuleSet warriorDefault = new ProfessionRuleSet(
                "Warrior");
        final ProfessionRuleSet archerDefault = new ProfessionRuleSet("Archer");
        // TODO: Set settings
        defaultRuleSets.put("Warrior", warriorDefault);
        defaultRuleSets.put("Archer", archerDefault);
    }
}
