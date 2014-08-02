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
package com.github.rolecraftdev.quest.loading;

import com.github.rolecraftdev.data.storage.YamlFile;
import com.github.rolecraftdev.quest.loading.exception.InvalidObjectiveException;
import com.github.rolecraftdev.quest.loading.exception.InvalidQuestException;
import com.github.rolecraftdev.quest.loading.outline.ObjectiveResultOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestObjectiveOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestOutline;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RCQQuestLoader extends QuestLoader {
    public RCQQuestLoader(final File directory) {
        super(directory);
    }

    @Override
    public void loadQuestOutlines()
            throws InvalidQuestException, InvalidObjectiveException {
        for (final File questFile : directory.listFiles()) {
            if (!questFile.getName().endsWith(".rcq")) {
                continue;
            }

            final YamlFile yaml = new YamlFile(questFile);

            // Options
            final String name = yaml.getString("name", null);
            final List<String> description = yaml.getStringList("description");
            final int startingObjective = yaml.getInt("first-objective", -1);

            if (name == null) {
                throw new InvalidQuestException("Must specify quest name!");
            }
            if (description == null) {
                throw new InvalidQuestException(
                        "Must give description for quest: " + name + "!");
            }
            if (startingObjective == -1) {
                throw new InvalidQuestException(
                        "Must specify first objective for quest" + name + "!");
            }

            // Objectives
            final ConfigurationSection objectives = yaml
                    .getConfigurationSection("objectives");
            final Set<String> objectiveKeys = objectives.getKeys(false);
            if (objectiveKeys == null || objectiveKeys.isEmpty()) {
                throw new InvalidObjectiveException(
                        "No objectives specified for quest: " + name + "!");
            }

            final List<QuestObjectiveOutline> objectiveOutlines = new ArrayList<QuestObjectiveOutline>();
            for (final String objectiveKey : objectiveKeys) {
                final ConfigurationSection objective = objectives
                        .getConfigurationSection(objectiveKey);

                final int objectiveId = objective.getInt("id", -1);
                final String type = objective.getString("type", null);
                final boolean optional = objective.getBoolean("optional");
                final List<String> objectiveDescription = objective
                        .getStringList("description");

                final ConfigurationSection results = objective
                        .getConfigurationSection("results");
                final Set<String> resultKeys = results.getKeys(false);
                if (resultKeys == null || resultKeys.isEmpty()) {
                    throw new InvalidObjectiveException(
                            "No results specified for objective with id: "
                                    + objectiveId + " in quest: " + name);
                }

                final List<ObjectiveResultOutline> resultOutlines = new ArrayList<ObjectiveResultOutline>();
                for (final String resultKey : resultKeys) {
                    final ConfigurationSection result = results
                            .getConfigurationSection(resultKey);

                    final int resultId = result.getInt("id", -1);
                    final String resultType = result.getString("type", null);
                    final int outcome = result.getInt("outcome", -1);
                    final String requirementS = result.getString("requirement");

                    Object requirement = null;
                    try {
                        requirement = Integer.parseInt(requirementS);
                    } catch (NumberFormatException e) {
                        try {
                            requirement = Double.parseDouble(requirementS);
                        } catch (NumberFormatException ex) {
                            requirement = requirementS;
                        }
                    }

                    resultOutlines.add(new ObjectiveResultOutline(resultId,
                            resultType, outcome, requirement, objectiveId,
                            name));
                }

                objectiveOutlines
                        .add(new QuestObjectiveOutline(objectiveId, type,
                                optional, objectiveDescription,
                                resultOutlines));
            }

            questOutlines
                    .add(new QuestOutline(name, description, objectiveOutlines,
                            startingObjective));
        }
    }
}
