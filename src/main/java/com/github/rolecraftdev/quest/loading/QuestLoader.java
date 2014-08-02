/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
package com.github.rolecraftdev.quest.loading;

import com.github.rolecraftdev.quest.loading.exception.InvalidObjectiveException;
import com.github.rolecraftdev.quest.loading.exception.InvalidQuestException;
import com.github.rolecraftdev.quest.loading.outline.QuestOutline;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Loads Rolecraft quests
 */
public abstract class QuestLoader {
    /**
     * The directory to load quests from
     */
    protected final File directory;
    /**
     * A Set of loaded quest outlines
     */
    protected final Set<QuestOutline> questOutlines;

    public QuestLoader(final File directory) {
        if (directory == null) {
            throw new IllegalArgumentException();
        }

        this.directory = directory;
        questOutlines = new HashSet<QuestOutline>();
    }

    /**
     * Loads all of the quest outlines for this quest type
     *
     * @throws InvalidQuestException     If a quest is invalid
     * @throws InvalidObjectiveException If a quest objective is invalid
     */
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
