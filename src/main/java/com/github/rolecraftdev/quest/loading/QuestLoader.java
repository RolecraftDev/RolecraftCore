package com.github.rolecraftdev.quest.loading;

import com.github.rolecraftdev.quest.loading.exception.InvalidObjectiveException;
import com.github.rolecraftdev.quest.loading.exception.InvalidQuestException;
import com.github.rolecraftdev.quest.loading.outline.QuestOutline;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class QuestLoader {
    protected final File directory;
    protected final Set<QuestOutline> questOutlines;

    public QuestLoader(final File directory) {
        if (directory == null) {
            throw new IllegalArgumentException();
        }

        this.directory = directory;
        questOutlines = new HashSet<QuestOutline>();
    }

    public abstract void loadQuestOutlines() throws InvalidQuestException,
            InvalidObjectiveException;

    public QuestOutline getQuestOutline(final String name) {
        for (final QuestOutline outline : questOutlines) {
            if (outline.getName().equalsIgnoreCase(name)) {
                return outline;
            }
        }
        return null;
    }
}
