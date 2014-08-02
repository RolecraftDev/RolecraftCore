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
package com.github.rolecraftdev.quest;

import com.github.rolecraftdev.quest.loading.outline.QuestOutline;
import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.QuestObjective;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a quest in Rolecraft.
 */
public final class Quest {
    /**
     * The quest outline, which contains basic data about this quest which is
     * the same no matter what the stage of the quest
     */
    private final QuestOutline outline;
    /**
     * The unique identifier of this quest, used for database storage
     */
    private final UUID questId;
    /**
     * The unique identifier of the player embarking on this quest
     */
    private final UUID quester;
    /**
     * The objectives for this quest
     */
    private final List<QuestObjective> objectives;
    /**
     * The ids of the current objectives for this quest
     */
    private final List<Integer> currentObjectiveIds;

    public Quest(final QuestOutline outline, final UUID quester,
            final List<QuestObjective> objectives) {
        this.outline = outline;
        this.quester = quester;
        this.objectives = objectives;

        questId = UUID.randomUUID();
        currentObjectiveIds = new ArrayList<Integer>();
    }

    public Quest(final QuestOutline outline, final UUID id, final UUID quester,
            final List<QuestObjective> objectives,
            final List<Integer> currentObjectiveIds) {
        this.outline = outline;
        this.questId = id;
        this.quester = quester;
        this.objectives = objectives;
        this.currentObjectiveIds = currentObjectiveIds;
    }

    /**
     * Called when an objective is completed and deals with switching to the
     * next objective, ending the quest and sending messages to the player
     *
     * @param objective The objective which was completed
     * @param result    The end result of the completed objective
     */
    public void objectiveComplete(final QuestObjective objective,
            final ObjectiveResult result) {
        final Player player = Bukkit.getPlayer(quester);
        player.sendMessage(ChatColor.GOLD + "You completed your objective: "
                + objective.getOutline().getDescription().get(0));
        currentObjectiveIds.remove(objective.getOutline().getId());

        final int outcome = result.getOutline().getOutcome();
        if (outcome == -1) {
            player.sendMessage(ChatColor.GOLD + "Quest complete: " +
                    objective.getQuest().getOutline().getName());
            // TODO: Add rewards
        } else {
            for (final QuestObjective next : objectives) {
                if (next.getOutline().getId() == outcome) {
                    currentObjectiveIds.add(next.getOutline().getId());
                    player.sendMessage(ChatColor.GOLD + "New objective: " + next
                            .getOutline().getDescription().get(0));
                }
            }
        }
    }

    public QuestOutline getOutline() {
        return outline;
    }

    public UUID getQuestId() {
        return questId;
    }

    public UUID getQuester() {
        return quester;
    }

    public List<QuestObjective> getObjectives() {
        return new ArrayList<QuestObjective>(objectives);
    }

    public List<Integer> getCurrentObjectiveIds() {
        return new ArrayList<Integer>(currentObjectiveIds);
    }

    public List<QuestObjective> getCurrentObjectives() {
        final List<QuestObjective> results = new ArrayList<QuestObjective>();
        for (final QuestObjective objective : objectives) {
            if (currentObjectiveIds.contains(objective.getOutline().getId())) {
                results.add(objective);
            }
        }
        return results;
    }
}
