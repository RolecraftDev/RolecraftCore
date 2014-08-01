package com.github.rolecraftdev.quest.objective;

import java.io.Serializable;

public interface ObjectiveType extends Serializable {
    ObjectiveResult getCompleted(ObjectiveResult[] results, Serializable value);

    String getTypeName();
}
