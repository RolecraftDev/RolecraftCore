package com.github.rolecraftdev.quest.loading.outline;

public class ObjectiveResultOutline {
    private final int id;
    private final String typeName;
    private final int outcome;
    private final Object requirement;
    private final int objectiveId;
    private final String questName;

    public ObjectiveResultOutline(final int id, final String typeName,
            final int outcome, final Object requirement, final int objectiveId,
            final String questName) {
        this.id = id;
        this.typeName = typeName;
        this.outcome = outcome;
        this.requirement = requirement;
        this.objectiveId = objectiveId;
        this.questName = questName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getOutcome() {
        return outcome;
    }

    public Object getRequirement() {
        return requirement;
    }

    public int getObjectiveId() {
        return objectiveId;
    }

    public String getQuestName() {
        return questName;
    }
}
