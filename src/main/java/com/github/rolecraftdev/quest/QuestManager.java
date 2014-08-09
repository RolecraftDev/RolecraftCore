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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.quest.loading.JSQuestLoader;
import com.github.rolecraftdev.quest.loading.RCQQuestLoader;
import com.github.rolecraftdev.quest.loading.outline.ObjectiveResultOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestObjectiveOutline;
import com.github.rolecraftdev.quest.loading.outline.QuestOutline;
import com.github.rolecraftdev.quest.objective.ObjectiveResult;
import com.github.rolecraftdev.quest.objective.ObjectiveType;
import com.github.rolecraftdev.quest.objective.QuestObjective;
import com.github.rolecraftdev.quest.objective.types.KillEntityObjectiveType;
import com.github.rolecraftdev.quest.objective.types.KillHostileMobObjectiveType;
import com.github.rolecraftdev.util.QuestProgressionSerializer;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 * Handles quests in Rolecraft.
 */
public final class QuestManager {
    private final RolecraftCore plugin;
    private final Map<UUID, Quest> currentQuests;
    /**
     * The QuestLoader used for loading RCQ quests
     */
    private final RCQQuestLoader rcqLoader;
    /**
     * The QuestLoader used for loading JavaScript quests
     */
    private final JSQuestLoader jsLoader;
    private final Map<String, ObjectiveType> objectiveTypes;

    public QuestManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        currentQuests = new HashMap<UUID, Quest>();
        rcqLoader = new RCQQuestLoader(plugin,
                new File(plugin.getDataFolder(), "quests"));
        jsLoader = new JSQuestLoader(plugin, new File(plugin.getDataFolder(),
                "quests" + File.separator + "js"));
        objectiveTypes = new HashMap<String, ObjectiveType>();

        // Register objective types
        objectiveTypes.put(KillEntityObjectiveType.NAME,
                new KillEntityObjectiveType());
        objectiveTypes.put(KillHostileMobObjectiveType.NAME,
                new KillHostileMobObjectiveType());
        // TODO: More

        // Load all quest outlines
        rcqLoader.loadQuestOutlines();
        jsLoader.loadQuestOutlines();

        plugin.getServer().getPluginManager()
                .registerEvents(new QuestListener(plugin), plugin);
    }

    /**
     * Get the {@link RolecraftCore} plugin object this {@link QuestManager} is
     * attached to.
     * 
     * @return Its {@link RolecraftCore} object
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Gets the quest outline with the given quest name, which contains basic
     * data about said quest
     *
     * @param name The name of the quest to get the outline for
     * @return The outline for the quest with the given name
     */
    public QuestOutline getQuestOutline(final String name) {
        QuestOutline result = rcqLoader.getQuestOutline(name);
        if (result == null) {
            result = jsLoader.getQuestOutline(name);
        }
        return result;
    }

    public Quest getQuest(final UUID id) {
        return currentQuests.get(id);
    }

    /**
     * Gets a Set of all of the quests that the player with the given unique
     * identifier is currently progressing through
     *
     * @param playerId The player to get current quests for
     * @return A Set of current quests for the given player
     */
    public Set<Quest> getPlayerQuests(final UUID playerId) {
        final Set<Quest> quests = new HashSet<Quest>();
        for (final Quest quest : currentQuests.values()) {
            if (quest.getQuester().equals(playerId)) {
                quests.add(quest);
            }
        }
        return quests;
    }

    public ObjectiveType getObjectiveType(final String name) {
        return objectiveTypes.get(name);
    }

    public Set<UUID> getIds() {
        return currentQuests.keySet();
    }

    public void addQuest(final Quest quest) {
        currentQuests.put(quest.getQuestId(), quest);
    }

    public void removeQuest(final UUID id) {
        currentQuests.remove(id);
    }

    /**
     * Loads all quests for the given player
     *
     * @param data The data for the player to load quests for
     */
    public void loadPlayerQuests(final PlayerData data) {
        if (data.getQuestProgression() == null) {
            return;
        }
        for (final Entry<UUID, String> entry : data.getQuestProgression()
                .entrySet()) {
            currentQuests.put(entry.getKey(), QuestProgressionSerializer
                    .getQuest(this, entry.getKey(), entry.getValue(),
                            data.getPlayerId()));
        }
    }

    /**
     * Utility method for creating an instance of a specific quest for a
     * specific player
     *
     * @param quest  The name of the quest outline to use
     * @param player The player to create the quest for
     * @return An instance of the given quest for the given player
     */
    public Quest createQuest(final String quest, final UUID player) {
        final QuestOutline outline = rcqLoader.getQuestOutline(quest);
        final List<QuestObjective> objectives = new ArrayList<QuestObjective>();
        for (final QuestObjectiveOutline objectiveOutline : outline
                .getObjectives()) {
            final ObjectiveType objectiveType = getObjectiveType(
                    objectiveOutline.getTypeName());
            final List<ObjectiveResult> results = new ArrayList<ObjectiveResult>();
            for (final ObjectiveResultOutline resultOutline : objectiveOutline
                    .getResults()) {
                results.add(new ObjectiveResult(resultOutline, objectiveType));
            }
            objectives.add(new QuestObjective(objectiveOutline, objectiveType,
                    results.toArray(new ObjectiveResult[results.size()])));
        }

        final Quest retVal = new Quest(outline, player, objectives);
        for (final QuestObjective objective : objectives) {
            for (final ObjectiveResult result : objective.getResults()) {
                result.setQuest(retVal);
            }
        }

        return retVal;
    }
}
