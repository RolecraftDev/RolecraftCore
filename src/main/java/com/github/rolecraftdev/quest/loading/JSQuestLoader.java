package com.github.rolecraftdev.quest.loading;

import com.github.rolecraftdev.quest.loading.QuestLoader;
import com.github.rolecraftdev.quest.loading.exception.InvalidObjectiveException;
import com.github.rolecraftdev.quest.loading.exception.InvalidQuestException;

import java.io.File;

public class JSQuestLoader extends QuestLoader {
    public JSQuestLoader(final File directory) {
        super(directory);
    }

    @Override
    public void loadQuestOutlines()
            throws InvalidQuestException, InvalidObjectiveException {
        // TODO
    }
}
