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
package com.github.rolecraftdev.experience;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.experience.RCExperienceEvent;

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
                pd.addExperience(expFromKill,
                        RCExperienceEvent.ChangeReason.KILLING);
            }
        }
    }
}
