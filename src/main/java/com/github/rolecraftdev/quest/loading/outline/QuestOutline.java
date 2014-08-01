package com.github.rolecraftdev.quest.loading.outline;

import java.util.ArrayList;
import java.util.List;

public final class QuestOutline {
    private final String name;
    private final List<String> description;
    private final List<QuestObjectiveOutline> objectives;
    private final int startingObjective;

    public QuestOutline(final String name, final List<String> description,
            final List<QuestObjectiveOutline> objectives,
            final int startingObjective) {
        this.name = name;
        this.description = description;
        this.objectives = objectives;
        this.startingObjective = startingObjective;
    }

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return new ArrayList<String>(description);
    }

    public List<QuestObjectiveOutline> getObjectives() {
        return new ArrayList<QuestObjectiveOutline>(objectives);
    }

    public int getStartingObjective() {
        return startingObjective;
    }
}
