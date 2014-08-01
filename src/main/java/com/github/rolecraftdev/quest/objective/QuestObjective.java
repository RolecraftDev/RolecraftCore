package com.github.rolecraftdev.quest.objective;

import com.github.rolecraftdev.quest.Quest;

import java.util.Arrays;
import java.util.List;

public class QuestObjective {
    /**
     * The identifier of this objective. Should be unique within the quest the
     * objective is a part of
     */
    private final int id;
    /**
     * The type of objective this objective is
     */
    private final ObjectiveType type;
    /**
     * The completion value for the quest objective
     */
    private final ObjectiveResult[] results;
    /**
     * Whether this objective is optional
     */
    private final boolean optional;
    /**
     * A description of the objective, where each element is a new line
     */
    private final List<String> description;

    /**
     * This objective's quest
     */
    private Quest quest;
    /**
     * The current value for the progression of this quest objective
     */
    private Object value;

    public QuestObjective(final int id, final ObjectiveType type,
            final ObjectiveResult[] results, final boolean optional,
            final String... description) {
        this.id = id;
        this.type = type;
        this.results = results;
        this.optional = optional;
        this.description = Arrays.asList(description);
    }

    public QuestObjective(final int id, final ObjectiveType type,
            final ObjectiveResult[] results, final boolean optional,
            final Object value, final String... description) {
        this.id = id;
        this.type = type;
        this.results = results;
        this.optional = optional;
        this.value = value;
        this.description = Arrays.asList(description);
    }

    public int getId() {
        return id;
    }

    public ObjectiveType getObjectiveType() {
        return type;
    }

    public ObjectiveResult[] getResults() {
        return results;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<String> getDescription() {
        return description;
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
