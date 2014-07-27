package com.github.rolecraftdev.listener;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.util.LevelUtil;

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

/**
 * Processes events which have an impact on players in Rolecraft, such as
 * processing the {@link EntityDeathEvent} to award experience to players
 */
public final class PlayerListener implements Listener {
    private final RolecraftCore plugin;

    public PlayerListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity deadEntity = event.getEntity();
        final EntityDamageEvent dmg = deadEntity.getLastDamageCause();

        if (dmg instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) dmg;
            if (e.getDamager() instanceof Player) {
                final UUID id = ((Player) e.getDamager()).getUniqueId();
                final EntityType entityType = deadEntity.getType();
                final PlayerData pd = plugin.getDataManager().getPlayerData(id);

                pd.addExperience(LevelUtil.expFromKill(entityType));
            }
        }
    }
}
