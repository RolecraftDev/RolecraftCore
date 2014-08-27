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
package com.github.rolecraftdev.util;

import com.github.rolecraftdev.data.storage.YamlFile;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.profession.ProfessionRule;
import com.github.rolecraftdev.profession.ProfessionRuleMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map.Entry;
import java.util.UUID;

/**
 * A utility class for deserialising {@link Profession}s and their attributes.
 *
 * @since 0.0.5
 */
public class ProfessionDeserializer {
    /**
     * The path to the {@link Profession}'s {@link UUID}.
     */
    private static final String ID = "id";
    /**
     * The path to the {@link Profession}'s name
     */
    private static final String NAME = "name";
    /**
     * The path to the {@link Profession}'s {@link ProfessionRule}s.
     */
    private static final String RULES = "rules";

    /**
     * The {@link YamlFile} that contains the serialised {@link Profession}.
     */
    private final YamlFile professionConfig;

    /**
     * Constructor.
     *
     * @param professionConfig the {@link YamlFile} that contains the serialised
     *        {@link Profession}
     * @since 0.0.5
     */
    public ProfessionDeserializer(final YamlFile professionConfig) {
        this.professionConfig = professionConfig;
    }

    /**
     * Get the {@link YamlFile} that contains the serialised {@link Profession}.
     *
     * @return the {@link YamlFile} that contains the serialised
     *         {@link Profession}
     * @since 0.0.5
     */
    public YamlFile getConfig() {
        return professionConfig;
    }

    /**
     * Retrieve the {@link Profession} from the linked file.
     *
     * @param professionManager the {@link ProfessionManager} that is used to
     *        construct a new {@link Profession}
     * @return the deserialised {@link Profession}
     * @since 0.0.5
     */
    public Profession getProfession(final ProfessionManager professionManager) {
        return new Profession(professionManager, getProfessionId(),
                getProfessionName(), getProfessionRuleMap());
    }

    /**
     * Retrieve the {@link Profession}'s {@link UUID} from the linked file.
     *
     * @return the deserialised {@link UUID}
     * @since 0.0.5
     */
    private UUID getProfessionId() {
        String id = professionConfig.getString(ID);
        // So we don't have to catch NullPointerException
        if (id == null) {
            id = "";
        }

        try {
            return UUID.fromString(id);
        } catch (final IllegalArgumentException e) {
            Bukkit.getLogger()
                    .warning("[WARNING] [Rolecraft] Invalid ID for profession "
                            + getProfessionName() + ", generating a new one");
            professionConfig.set(ID, UUID.randomUUID().toString());
            // Update file
            professionConfig.save();
            return getProfessionId();
        }
    }

    /**
     * Retrieve the {@link Profession}'s name from the linked file.
     *
     * @return the deserialised name
     * @since 0.0.5
     */
    private String getProfessionName() {
        return professionConfig.getString(NAME, "unset");
    }

    /**
     * Retrieve the {@link Profession}'s {@link ProfessionRuleMap} from the
     * linked file.
     *
     * @return the deserialised {@link ProfessionRuleMap}
     * @since 0.0.5
     */
    private ProfessionRuleMap getProfessionRuleMap() {
        final ProfessionRuleMap ruleMap = new ProfessionRuleMap(
                getProfessionName());
        ConfigurationSection rulesSection =
                professionConfig.getConfigurationSection(RULES);

        if (rulesSection == null) {
            rulesSection = professionConfig.createSection(RULES);
        }

        for (final Entry<String, Object> pair : rulesSection.getValues(true)
                .entrySet()) {
            final String ruleName = pair.getKey();
            final Object value = pair.getValue();

            // Validation is done by ProfessionRuleMap#set
            if (!ruleMap.set(ProfessionRule.getRule(ruleName), value)) {
                Bukkit.getLogger()
                        .warning("[WARNING] [Rolecraft] Couldn't set rule "
                                + ruleName + " for profession "
                                + getProfessionName());
            }
        }
        return ruleMap;
    }
}
