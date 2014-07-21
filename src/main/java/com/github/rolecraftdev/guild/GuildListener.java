package com.github.rolecraftdev.guild;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public final class GuildListener implements Listener {
    private final GuildManager guildManager;

    GuildListener(final GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event
                .getEntity() instanceof Player) {
            final UUID playerId = ((Player) event.getEntity()).getUniqueId();
            final UUID damagerId = ((Player) event.getDamager()).getUniqueId();

            if (guildManager.getPlayerGuild(playerId)
                    .equals(guildManager.getPlayerGuild(damagerId))) {
                event.setCancelled(true);
            }
        }
    }
}
