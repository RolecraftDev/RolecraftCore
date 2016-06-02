package com.github.rolecraftdev.level;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.exp.RCExpEvent;

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
 * Listens for events with the intention of awarding experience to players.
 *
 * @since 0.0.5
 */
public final class ExperienceListener implements Listener {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the linked {@link RolecraftCore} object
     * @since 0.0.5
     */
    public ExperienceListener(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity deadEntity = event.getEntity();
        final EntityDamageEvent dmg = deadEntity.getLastDamageCause();

        if (dmg instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) dmg;
            if (e.getDamager() instanceof Player) {
                final Player damager = (Player) e.getDamager();
                final float expFromKill;

                if (e.getEntity() instanceof Player) {
                    expFromKill = ExperienceHelper
                            .expFromPlayerKill(damager, (Player) e.getEntity());
                } else {
                    final EntityType entityType = deadEntity.getType();
                    expFromKill = ExperienceHelper.expFromKill(entityType);
                }

                final UUID id = e.getDamager().getUniqueId();
                final PlayerData pd = plugin.getDataManager().getPlayerData(id);
                pd.addExperience(expFromKill, RCExpEvent.ChangeReason.KILLING);
            }
        }
    }
}
