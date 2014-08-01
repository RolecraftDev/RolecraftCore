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
