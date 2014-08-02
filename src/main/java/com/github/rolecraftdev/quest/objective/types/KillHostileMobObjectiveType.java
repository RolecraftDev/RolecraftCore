package com.github.rolecraftdev.quest.objective.types;

import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.ObjectiveType;

/**
 * An objective type which requires killing a specified amount of hostile mobs
 */
public class KillHostileMobObjectiveType implements ObjectiveType {
    public static final String NAME = "kill-hostile-mob";

    @Override
    public ObjectiveResult getCompleted(final ObjectiveResult[] results,
            final Object value) {
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException();
        }

        for (final ObjectiveResult result : results) {
            if (!(result.getOutline().getRequirement() instanceof Number)) {
                throw new IllegalArgumentException();
            }
            if (((Number) value).intValue() >= ((Number) result.getOutline()
                    .getRequirement()).intValue()) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String getTypeName() {
        return NAME;
    }
}
