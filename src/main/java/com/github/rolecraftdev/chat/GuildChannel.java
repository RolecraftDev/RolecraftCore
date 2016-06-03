package com.github.rolecraftdev.chat;

import org.apache.commons.lang.Validate;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a chat channel for a guild - anybody in the guild receives
 * messages sent to the guild's channel.
 *
 * @since 0.0.5
 */
public class GuildChannel {
    private final RolecraftCore plugin;
    private final UUID guildId;

    public GuildChannel(@Nonnull final RolecraftCore plugin,
            @Nonnull final UUID guildId) {
        this.plugin = plugin;
        this.guildId = guildId;
    }

    public GuildChannel(@Nonnull final RolecraftCore plugin,
            @Nonnull final Guild guild) {
        this.plugin = plugin;
        this.guildId = guild.getId();
    }

    public void onMessage(String senderName, String message) {
        final DataManager dataManager = this.plugin.getDataManager();
        final GuildManager guildManager = this.plugin.getGuildManager();
        final Guild guild = guildManager.getGuild(this.guildId);

        Validate.notNull(guild);

        for (final PlayerData playerData : dataManager.getPlayerDatum()) {
            if (playerData.getSettings().isGuildChatSpy()) {
                final Player player = this.plugin.getServer()
                        .getPlayer(playerData.getPlayerId());
                if (player != null) {
                    player.sendMessage("[Guild] [" + guild.getName() + "] ["
                            + senderName + "] " + message);
                }
            }
        }

        final Set<UUID> guildMembers = guild.getMembers();
        if (guildMembers == null || guildMembers.size() == 0) {
            return;
        }

        for (final UUID playerId : guild.getMembers()) {
            final Player player = plugin.getServer().getPlayer(playerId);
            player.sendMessage("[Guild] [" + senderName + "] " + message);
        }
    }
}
