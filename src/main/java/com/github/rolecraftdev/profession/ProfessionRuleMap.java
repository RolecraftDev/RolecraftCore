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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProfessionRuleMap {
    private final String professionName;
    private final Map<ProfessionRule<?>, Object> rules = new HashMap<ProfessionRule<?>, Object>();

    public ProfessionRuleMap(final String professionName) {
        this.professionName = professionName;
    }

    public String getProfessionName() {
        return professionName;
    }

    public <T> T get(ProfessionRule<T> key) {
        return key.cast(rules.get(key));
    }

    public boolean set(ProfessionRule<?> key, Object value) {
        if (key == null || !key.validate(value))
            return false;

        rules.put(key, value);
        return true;
    }

    public static ProfessionRuleMap load(final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection rulesSection = config.getConfigurationSection("rules");

        if (rulesSection == null)
            rulesSection = config.createSection("rules");

        // Remove file name's extension
        final String name = file.getName().replaceAll("\\..*", "");
        final ProfessionRuleMap ruleMap = new ProfessionRuleMap(name);

        for (Entry<String, Object> element : rulesSection.getValues(true).entrySet()) {
            // TODO: If false -> inform user something is wrong (?)
            // Validation is done by #set
            ruleMap.set(ProfessionRule.getRule(element.getKey()), element.getValue());
        }
        return ruleMap;
    }
}
