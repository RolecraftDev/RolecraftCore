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
package com.github.rolecraftdev.quest;

import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.QuestObjective;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a quest in Rolecraft.
 */
public final class Quest implements Serializable {
    private final UUID questId;
    private final UUID quester;
    private final String name;
    private final List<QuestObjective> objectives;
    private final int startingObjective;
    private final List<String> description;
    private final List<Integer> currentObjectiveIds;

    public Quest(final UUID quester, final String name,
            final List<QuestObjective> objectives, final int startingObjective,
            final List<String> description) {
        this.quester = quester;
        this.name = name;
        this.objectives = objectives;
        this.startingObjective = startingObjective;
        this.description = description;

        questId = UUID.randomUUID();
        currentObjectiveIds = new ArrayList<Integer>();
    }

    public Quest(final UUID questId, final UUID quester, final String name,
            final List<QuestObjective> objectives, final int startingObjective,
            final List<String> description,
            final List<Integer> currentObjectiveIds) {
        this.questId = questId;
        this.quester = quester;
        this.name = name;
        this.objectives = objectives;
        this.startingObjective = startingObjective;
        this.description = description;
        this.currentObjectiveIds = currentObjectiveIds;
    }

    public void objectiveComplete(final QuestObjective objective,
            final ObjectiveResult result) {
        // TODO: Here, we need to update the current objectives based on the
        // result, as well as check whether the quest has finished (if so, we
        // need to end the quest and reward the player) and notify the player
        // that they finished the objective. We also need to tell them their
        // next objective, if applicable
    }

    public UUID getQuestId() {
        return questId;
    }

    public UUID getQuester() {
        return quester;
    }

    public String getName() {
        return name;
    }

    public List<QuestObjective> getObjectives() {
        return new ArrayList<QuestObjective>(objectives);
    }

    public int getStartingObjective() {
        return startingObjective;
    }

    public List<String> getDescription() {
        return new ArrayList<String>(description);
    }

    public List<Integer> getCurrentObjectiveIds() {
        return new ArrayList<Integer>(currentObjectiveIds);
    }

    public List<QuestObjective> getCurrentObjectives() {
        final List<QuestObjective> results = new ArrayList<QuestObjective>();
        for (final QuestObjective objective : objectives) {
            if (currentObjectiveIds.contains(objective.getId())) {
                results.add(objective);
            }
        }
        return results;
    }
}
