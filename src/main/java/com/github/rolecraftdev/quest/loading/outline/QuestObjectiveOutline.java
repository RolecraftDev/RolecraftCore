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
 * Represents a basic outline of an objective for a quest
 */
public class QuestObjectiveOutline {
    private final int id;
    private final String typeName;
    private final boolean optional;
    private final List<String> description;
    private final List<ObjectiveResultOutline> results;

    public QuestObjectiveOutline(final int id, final String typeName,
            final boolean optional, final List<String> description,
            final List<ObjectiveResultOutline> results) {
        this.id = id;
        this.typeName = typeName;
        this.optional = optional;
        this.description = description;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isOptional() {
        return optional;
    }

    public List<String> getDescription() {
        return new ArrayList<String>(description);
    }

    public List<ObjectiveResultOutline> getResults() {
        return new ArrayList<ObjectiveResultOutline>(results);
    }
}
