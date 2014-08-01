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
import com.github.rolecraftdev.quest.loading.QuestLoader;
import com.github.rolecraftdev.quest.loading.exception.InvalidObjectiveException;
import com.github.rolecraftdev.quest.loading.exception.InvalidQuestException;

import java.io.File;
import java.util.*;

/**
 * Handles quests in Rolecraft.
 */
public final class QuestManager {
    private final RolecraftCore plugin;
    private final Map<UUID, Quest> currentQuests;
    private final QuestLoader loader;

    public QuestManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        currentQuests = new HashMap<UUID, Quest>();
        loader = new QuestLoader(new File(plugin.getDataFolder(), "quests"));

        try {
            loader.loadQuestOutlines();
        } catch (InvalidQuestException e) {
            e.printStackTrace();
        } catch (InvalidObjectiveException e) {
            e.printStackTrace();
        }

        plugin.getServer().getPluginManager()
                .registerEvents(new QuestListener(plugin), plugin);
    }

    public void addQuest(final Quest quest) {
        currentQuests.put(quest.getQuestId(), quest);
    }

    public Quest getQuest(final UUID id) {
        return currentQuests.get(id);
    }

    public Set<Quest> getPlayerQuests(final UUID playerId) {
        final Set<Quest> quests = new HashSet<Quest>();
        for (final Quest quest : currentQuests.values()) {
            if (quest.getQuester().equals(playerId)) {
                quests.add(quest);
            }
        }
        return quests;
    }

    public void removeQuest(final UUID id) {
        currentQuests.remove(id);
    }
}
