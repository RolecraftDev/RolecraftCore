package com.github.rolecraftdev.quest.objective;

public interface ObjectiveType {
    ObjectiveResult getCompleted(ObjectiveResult[] results, Object value);

    String getTypeName();
}
