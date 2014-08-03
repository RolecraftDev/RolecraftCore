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
package com.github.rolecraftdev.quest.loading.outline;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a basic outline for a Rolecraft quest
 */
public final class QuestOutline {
    /**
     * The name of the quest
     */
    private final String name;
    /**
     * A description of the quest, where each element is a new line
     */
    private final List<String> description;
    /**
     * A list of outlines for the quest's objectives
     */
    private final List<QuestObjectiveOutline> objectives;
    /**
     * The id of the first objective for the quest
     */
    private final int startingObjective;

    public QuestOutline(final String name, final List<String> description,
            final List<QuestObjectiveOutline> objectives,
            final int startingObjective) {
        this.name = name;
        this.description = description;
        this.objectives = objectives;
        this.startingObjective = startingObjective;
    }

    public String getName() {
        return name;
    }

    public List<String> getDescription() {
        return new ArrayList<String>(description);
    }

    public List<QuestObjectiveOutline> getObjectives() {
        return new ArrayList<QuestObjectiveOutline>(objectives);
    }

    public int getStartingObjective() {
        return startingObjective;
    }
}
