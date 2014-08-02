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
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.quest.objective.QuestObjective;
import com.github.rolecraftdev.quest.objective.types.KillEntityObjectiveType;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class QuestListener implements Listener {
    private final RolecraftCore plugin;
    private final QuestManager questMgr;
    private final DataManager dataMgr;

    public QuestListener(final RolecraftCore plugin) {
        this.plugin = plugin;
        questMgr = plugin.getQuestManager();
        dataMgr = plugin.getDataManager();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity dead = event.getEntity();
        final EntityType type = dead.getType();
        final EntityDamageEvent last = dead.getLastDamageCause();

        if (last instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent byEntity = (EntityDamageByEntityEvent) last;
            if (byEntity.getDamager() instanceof Player) {
                final Player player = (Player) byEntity.getDamager();
                final UUID playerId = player.getUniqueId();

                for (final Quest quest : questMgr.getPlayerQuests(playerId)) {
                    for (final QuestObjective objective : quest
                            .getCurrentObjectives()) {
                        if (objective.getObjectiveType()
                                instanceof KillEntityObjectiveType) {
                            objective.setValue(
                                    ((Number) objective.getValue()).intValue()
                                            + 1);
                        }
                    }
                }
            }
        }
    }
}
