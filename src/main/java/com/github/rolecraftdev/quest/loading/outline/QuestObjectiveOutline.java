package com.github.rolecraftdev.quest.loading.outline;

import java.util.ArrayList;
import java.util.List;

public class QuestObjectiveOutline {
    private final int id;
    private final String typeName;
    private final boolean optional;
    private final List<String> description;
    private final List<ObjectiveResultOutline> results;

    public QuestObjectiveOutline(final int id, final String typeName,
            final boolean optional, final List<String> description,
            final List<ObjectiveResultOutline> results) {
        this.id = id;
        this.typeName = typeName;
        this.optional = optional;
        this.description = description;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<String> getDescription() {
        return new ArrayList<String>(description);
    }

    public List<ObjectiveResultOutline> getResults() {
        return new ArrayList<ObjectiveResultOutline>(results);
    }
}
