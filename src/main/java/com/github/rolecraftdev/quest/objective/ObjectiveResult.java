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

import java.io.Serializable;

public class ObjectiveResult implements Serializable {
    /**
     * The quest this result's objective is part of
     */
    private final Quest quest;
    /**
     * The type of objective this result relates to
     */
    private final ObjectiveType type;
    /**
     * The value required to obtain this result
     */
    private final Object requirement;
    /**
     * The objective in the quest which will be triggered if this result occurs
     */
    private final int outcome;
    /**
     * The result identifier
     */
    private final int id;

    /**
     * The objective this result is for
     */
    private QuestObjective objective;

    public ObjectiveResult(final Quest quest, final ObjectiveType type,
            final Object requirement, final int outcome, final int id) {
        this.quest = quest;
        this.type = type;
        this.requirement = requirement;
        this.outcome = outcome;
        this.id = id;
    }

    public Quest getQuest() {
        return quest;
    }

    public ObjectiveType getObjectiveType() {
        return type;
    }

    public Object getRequirement() {
        return requirement;
    }

    public int getOutcome() {
        return outcome;
    }

    public int getId() {
        return id;
    }

    public QuestObjective getObjective() {
        return objective;
    }

    public void setObjective(final QuestObjective objective) {
        if (this.objective != null) {
            throw new IllegalStateException();
        }
        if (objective == null) {
            throw new IllegalArgumentException();
        }
        this.objective = objective;
    }
}
