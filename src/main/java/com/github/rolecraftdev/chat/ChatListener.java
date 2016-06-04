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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens to chat events for the purpose of redirecting them to guild chat.
 *
 * @since 0.0.5
 */
public class ChatListener implements Listener {
    private final RolecraftCore plugin;

    /**
     * Instantiates the chat listener for Rolecraft.
     *
     * @param plugin the main {@link RolecraftCore} plugin instance
     * @since 0.0.5
     */
    public ChatListener(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
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
