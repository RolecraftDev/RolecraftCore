package com.github.rolecraftdev.util;

import com.github.rolecraftdev.quest.Quest;
import com.github.rolecraftdev.quest.objective.QuestObjective;

public class QuestProgressionSerializer {
    public static String serializeProgression(final Quest quest) {
        final StringBuilder builder = new StringBuilder();
        for (final QuestObjective obj : quest.getObjectives()) {
            builder.append(obj.getOutline().getId()).append(":")
                    .append(obj.getValue()).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
