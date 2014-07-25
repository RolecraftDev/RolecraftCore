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
package com.github.rolecraftdev.data.serialization;

import com.github.rolecraftdev.data.storage.YamlFile;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.profession.ProfessionRule;
import com.github.rolecraftdev.profession.ProfessionRuleMap;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map.Entry;
import java.util.UUID;

/**
 * A class which can be used to extract a {@link Profession} and all its related
 * classes (except for {@link ProfessionManager}) from a YAML file by using
 * appropriate deserialization.
 */
public class ProfessionDeserializer {
    /**
     * The path to the {@link Profession}'s unique identifier.
     */
    private static final String ID = "id";
    /**
     * The path to the {@link Profession}'s unique name.
     */
    private static final String NAME = "name";
    /**
     * The path to the {@link Profession}'s {@link ProfessionRule}s.
     */
    private static final String RULES = "rules";

    /**
     * The configuration file that contains a serialized {@link Profession}.
     */
    private final YamlFile professionConfig;

    /**
     * Create a new {@link ProfessionDeserializer}.
     * 
     * @param professionConfig - Serialized {@link Profession} file
     */
    public ProfessionDeserializer(final YamlFile professionConfig) {
        this.professionConfig = professionConfig;
    }

    /**
     * Get the configuration file that contains the serialized
     * {@link Profession}.
     * 
     * @return The serialized {@link Profession} file
     */
    public YamlFile getConfig() {
        return professionConfig;
    }

    /**
     * Deserialize the contents of the file, to return a {@link Profession}
     * object. Note that this simply uses most other methods in this
     * {@link ProfessionDeserializer} to construct a {@link Profession}.
     * 
     * @param professionManager - The {@link ProfessionManager}, the
     *            {@link Profession} will be assigned to
     * @return The deserialized {@link Profession}
     */
    public Profession getProfession(final ProfessionManager professionManager) {
        return new Profession(professionManager, getProfessionId(),
                getProfessionName(), getProfessionRuleMap());
    }

    /**
     * Deserialize the unique identifier in the file, which is defined at the
     * path {@link #ID}.
     * 
     * @return The deserialized unique identifier
     */
    public UUID getProfessionId() {
        String id = professionConfig.getString(ID);
        // So we don't have to catch NullPointerException
        if (id == null)
            id = "";

        try {
            return UUID.fromString(professionConfig.getString(ID));
        } catch (IllegalArgumentException e) {
            System.out
            .println("[WARNING] [Rolecraft] Invalid ID for profession "
                    + getProfessionName() + ", generating a new one");
            professionConfig.set(ID, UUID.randomUUID().toString());
            // Update file
            professionConfig.save();
            return getProfessionId();
        }
    }

    /**
     * Deserialize the unique name in the file, which is defined at the path
     * {@link #NAME}.
     * 
     * @return The deserialized unique name
     */
    public String getProfessionName() {
        return professionConfig.getString(NAME, "unset");
    }

    /**
     * Deserialize the {@link ProfessionRule}s in the file, which are defined at
     * the path {@link #RULES}.
     * 
     * @return The deserialized {@link ProfessionRule}s.
     */
    public ProfessionRuleMap getProfessionRuleMap() {
        final ProfessionRuleMap ruleMap = new ProfessionRuleMap(
                getProfessionName());
        ConfigurationSection rulesSection =
                professionConfig.getConfigurationSection(RULES);

        if (rulesSection == null)
            rulesSection = professionConfig.createSection(RULES);

        for (final Entry<String, Object> pair : rulesSection.getValues(true)
                .entrySet()) {
            final String ruleName = pair.getKey();
            final Object value = pair.getValue();

            // Validation is done by ProfessionRuleMap#set
            if (!ruleMap.set(ProfessionRule.getRule(ruleName), value)) {
                System.out.println("[WARNING] [Rolecraft] Couldn't set rule "
                        + ruleName + " for profession " + getProfessionName());
            }
        }
        return ruleMap;
    }
}
