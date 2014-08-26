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
package com.github.rolecraftdev.magic;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Listens for {@link Event}s that can be used to attend to the custom
 * {@link Arrow} metadata.
 *
 * @since 0.0.5
 */
public class ProjectileListener implements Listener {
    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        final Entity damager = e.getDamager();
        if (!(damager instanceof Arrow)) {
            return;
        }

        if (damager.hasMetadata("Multiplier")) {
            e.setDamage(e.getDamage() * damager.getMetadata("Multiplier")
                    .get(0).asFloat());
        }
        if (damager.hasMetadata("Knockback")) {
            e.getEntity().setVelocity(damager.getVelocity().multiply(
                    damager.getMetadata("Knockback").get(0).asFloat()));
        }
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(ProjectileHitEvent e) {
        final Entity entity = e.getEntity();
        if (!(entity instanceof Arrow)) {
            return;
        }

        if (entity.hasMetadata("Explosion")) {
            entity.getWorld().playEffect(e.getEntity().getLocation(),
                    Effect.SMOKE, null);
            entity.getWorld().playSound(e.getEntity().getLocation(),
                    Sound.EXPLODE, 1.0f, 0.0f);
        }
    }
}
