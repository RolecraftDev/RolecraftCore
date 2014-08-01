package com.github.rolecraftdev.quest.objective.types;

import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.ObjectiveType;

/**
 * A type of quest objective which requires killing a certain amount of
 * entities
 */
public class KillEntityObjectiveType implements ObjectiveType {
    @Override
    public ObjectiveResult getCompleted(final ObjectiveResult[] results,
            final Object value) {
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException();
        }

        for (final ObjectiveResult result : results) {
            if (!(result.getRequirement() instanceof Number)) {
                throw new IllegalArgumentException();
            }
            if (((Number) value).intValue() >= ((Number) result
                    .getRequirement()).intValue()) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String getTypeName() {
        return "kill-entity";
    }
}
