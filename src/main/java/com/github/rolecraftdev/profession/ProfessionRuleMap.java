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

/**
 * A wrapper around a Map of ProfessionRules to Objects, used for storing rules
 * about a profession and their values
 */
public final class ProfessionRuleMap {
    /**
     * The name of the profession this rule map contains rules for
     */
    private final String professionName;
    /**
     * A Map of ProfessionRules to Objects holding all of the loaded rules for
     * the profession this rule map holds rules for
     */
    private final Map<ProfessionRule<?>, Object> rules = new HashMap<ProfessionRule<?>, Object>();

    public ProfessionRuleMap(final String professionName) {
        this.professionName = professionName;
    }

    public String getProfessionName() {
        return professionName;
    }

    /**
     * Gets the value of the given ProfessionRule in this rulemap, or null if
     * the given rule isn't set
     *
     * @param key The rule to get the value of in this rule map
     * @param <T> The type which will be returned as the rule's value
     * @return The value for the given rule, cast to T
     */
    public <T> T get(final ProfessionRule<T> key) {
        return key.cast(rules.get(key));
    }

    /**
     * Sets the given rule to the given value
     *
     * @param key   The rule to set
     * @param value The value to set the given rule to
     * @return Whether the value was successfully set to the rule
     */
    public boolean set(final ProfessionRule<?> key, final Object value) {
        if (key == null || !key.validate(value)) {
            return false;
        }

        rules.put(key, value);
        return true;
    }

    /**
     * Loads a new ProfessionRuleMap from the given file
     *
     * @param file The file to load the map of rules to values from
     * @return The ProfessionRuleMap object created from the file's contents
     */
    public static ProfessionRuleMap load(final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final YamlConfiguration config = YamlConfiguration
                .loadConfiguration(file);
        ConfigurationSection rulesSection = config
                .getConfigurationSection("rules");

        if (rulesSection == null) {
            rulesSection = config.createSection("rules");
        }

        // Remove file name's extension
        final String name = file.getName().replaceAll("\\..*", "");
        final ProfessionRuleMap ruleMap = new ProfessionRuleMap(name);

        for (final Entry<String, Object> element : rulesSection.getValues(true)
                .entrySet()) {
            // Validation is done by #set
            if (!ruleMap.set(ProfessionRule.getRule(element.getKey()),
                    element.getValue())) {
                System.out.println(
                        "[WARNING] [Rolecraft] Couldn't set rule " + element
                                .getKey() + " for profession " + name);
            }
        }
        return ruleMap;
    }
}
