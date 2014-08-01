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
package com.github.rolecraftdev.quest.objective.types;

import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.ObjectiveType;

import java.io.Serializable;

/**
 * A type of quest objective which requires killing a certain amount of
 * entities
 */
public class KillEntityObjectiveType implements ObjectiveType {
    @Override
    public ObjectiveResult getCompleted(final ObjectiveResult[] results,
            final Serializable value) {
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
