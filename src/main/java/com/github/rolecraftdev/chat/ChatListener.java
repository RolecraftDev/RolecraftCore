package com.github.rolecraftdev.chat;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.data.PlayerSettings;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens to chat events for the purpose of redirecting them to guild chat.
 *
 * @since 0.0.5
 */
public class ChatListener implements Listener {
    private final RolecraftCore plugin;

    public ChatListener(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final DataManager dataManager = this.plugin.getDataManager();
        final PlayerData playerData = dataManager.getPlayerData(playerId);
        final PlayerSettings settings = playerData.getSettings();

        if (settings.isGuildChat()) {
            final GuildManager guildManager = this.plugin.getGuildManager();
            final UUID guildId = playerData.getGuild();

            if (guildId == null) {
                return;
            }

            final Guild guild = guildManager.getGuild(guildId);

            if (guild == null) {
                return;
            }

            event.setCancelled(true);

            guild.getChannel()
                    .onMessage(player.getDisplayName(), event.getMessage());
        }
    }
}
