package com.github.rolecraftdev.quest;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
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
                final PlayerData data = dataMgr.getPlayerData(playerId);

                for (final UUID questId : data.getQuests()) {
                    final Quest quest = questMgr.getQuest(questId);
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
