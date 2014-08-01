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
package com.github.rolecraftdev.quest.objective;

import com.github.rolecraftdev.quest.Quest;
import com.github.rolecraftdev.quest.loading.outline.QuestObjectiveOutline;

public class QuestObjective {
    private final QuestObjectiveOutline outline;
    /**
     * The type of objective this objective is
     */
    private final ObjectiveType type;
    /**
     * The completion value for the quest objective
     */
    private final ObjectiveResult[] results;

    /**
     * This objective's quest
     */
    private Quest quest;
    /**
     * The current value for the progression of this quest objective
     */
    private Object value;

    public QuestObjective(final QuestObjectiveOutline outline,
            final ObjectiveType type, final ObjectiveResult[] results) {
        this.outline = outline;
        this.type = type;
        this.results = results;
    }

    public QuestObjective(final QuestObjectiveOutline outline,
            final ObjectiveType type, final ObjectiveResult[] results,
            final Object value) {
        this.outline = outline;
        this.type = type;
        this.results = results;
        this.value = value;
    }

    public QuestObjectiveOutline getOutline() {
        return outline;
    }

    public ObjectiveType getObjectiveType() {
        return type;
    }

    public ObjectiveResult[] getResults() {
        return results;
    }

    public Quest getQuest() {
        return quest;
    }

    public Object getValue() {
        return value;
    }

    public void setQuest(final Quest quest) {
        this.quest = quest;
    }

    public void setValue(final Object value) {
        this.value = value;

        final ObjectiveResult result = type.getCompleted(results, value);
        if (result != null) {
            quest.objectiveComplete(this, result);
        }
    }
}
