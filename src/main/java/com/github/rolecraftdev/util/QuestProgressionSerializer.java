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

import com.github.rolecraftdev.quest.Quest;
import com.github.rolecraftdev.quest.QuestManager;
import com.github.rolecraftdev.quest.loading.outline.ObjectiveResultOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestObjectiveOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestOutline;
import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.ObjectiveType;
import com.github.rolecraftdev.quest.objective.QuestObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestProgressionSerializer {
    public static String serializeProgression(final Quest quest) {
        final StringBuilder builder = new StringBuilder();
        builder.append(quest.getOutline().getName()).append(",");
        for (final QuestObjective obj : quest.getObjectives()) {
            builder.append(obj.getOutline().getId()).append(":")
                    .append(obj.getValue()).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public static Quest getQuest(final QuestManager questMgr, final UUID id,
            final String serializedProgression, final UUID player) {
        final String[] split = serializedProgression.split(",");
        final String name = split[0];
        final int[][] data = new int[split.length][2];
        if (data.length < 1) {
            return null;
        }

        for (int i = 1; i < split.length; i++) {
            final String objective = split[i];
            final String[] split2 = objective.split(":");
            data[i][0] = Integer.parseInt(split2[0]);
            data[i][1] = Integer.parseInt(split2[1]);
        }

        final QuestOutline outline = questMgr.getLoader()
                .getQuestOutline(name);
        final List<QuestObjective> objectives = new ArrayList<QuestObjective>();
        for (int i = 0; i < outline.getObjectives().size(); i++) {
            final QuestObjectiveOutline objectiveOutline = outline
                    .getObjectives().get(i);
            final ObjectiveType objectiveType = questMgr.getObjectiveType(
                    objectiveOutline.getTypeName());
            final List<ObjectiveResult> results = new ArrayList<ObjectiveResult>();
            for (final ObjectiveResultOutline resultOutline : objectiveOutline
                    .getResults()) {
                results.add(new ObjectiveResult(resultOutline, objectiveType));
            }
            objectives.add(new QuestObjective(objectiveOutline, objectiveType,
                    results.toArray(new ObjectiveResult[results.size()]),
                    data[i][1]));
        }

        final List<Integer> curObjectives = new ArrayList<Integer>();
        for (final int[] curObjective : data) {
            curObjectives.add(curObjective[0]);
        }

        final Quest retVal = new Quest(outline, id, player, objectives,
                curObjectives);
        for (final QuestObjective objective : objectives) {
            for (final ObjectiveResult result : objective.getResults()) {
                result.setQuest(retVal);
            }
        }

        return retVal;
    }
}
