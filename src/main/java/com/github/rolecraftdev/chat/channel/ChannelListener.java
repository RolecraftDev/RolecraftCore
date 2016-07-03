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
package com.github.rolecraftdev.chat.channel;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.chat.ChatManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.data.GuildsLoadedEvent;
import com.github.rolecraftdev.event.data.PlayerDataLoadedEvent;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.guild.Guild;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

/**
 * Listens to events in order to modify channel-related things. E.g. adding a
 * player to the default channels when they join.
 *
 * @since 0.1.0
 */
public class ChannelListener implements Listener {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * The associated {@link ChatManager} instance.
     */
    private final ChatManager chatManager;

    /**
     * Constructor.
     *
     * @param chatManager the {@link RolecraftCore} {@link ChatManager} instance
     * @since 0.1.0
     */
    public ChannelListener(@Nonnull final ChatManager chatManager) {
        this.chatManager = chatManager;
        this.plugin = chatManager.getPlugin();
    }

    /**
     * @since 0.1.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDataLoaded(final PlayerDataLoadedEvent event) {
        final Set<ChatChannel> channels = chatManager.getAllChannels();
        final PlayerData data = event.getPlayerData();
        final UUID playerId = data.getPlayerId();
        final Player player = plugin.getServer().getPlayer(playerId);

        for (final ChatChannel channel : channels) {
            if (channel.isDefault()) {
                this.chatManager.addToChannel(playerId, channel);
                continue;
            }

            if (channel.isMod() && player
                    .hasPermission("rolecraft.channels.mod")) {
                this.chatManager.addToChannel(playerId, channel);
                continue;
            }

            if (channel.isAdmin() && player
                    .hasPermission("rolecraft.channels.admin")) {
                this.chatManager.addToChannel(playerId, channel);
                continue;
            }

            final String channelGuild = channel.getGuild();
            if (channelGuild != null) {
                final UUID guildId = data.getGuild();
                if (guildId != null) {
                    if (channelGuild.equals(plugin.getGuildManager()
                            .getGuild(guildId).getName())) {
                        this.chatManager.addToChannel(playerId, channel);
                    }
                }
            }
        }
    }

    /**
     * @since 0.1.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGuildsLoaded(final GuildsLoadedEvent event) {
        for (final Guild guild : event.getGuildManager().getGuilds()) {
            final String name = guild.getName();
            boolean found = false;

            for (final ChatChannel channel : chatManager.getAllChannels()) {
                if (channel.getGuild() != null
                        && channel.getGuild().equals(name)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                chatManager.addChannel(new ChatChannel(chatManager.getNextId(),
                        guild.getName(), false, false, false, guild.getName(),
                        null, -1));
            }
        }
    }

    /**
     * @since 0.1.0
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGuildCreate(final GuildCreateEvent event) {
        final Guild guild = event.getGuild();
        chatManager.addChannel(new ChatChannel(chatManager.getNextId(),
                guild.getName(), false, false, false, guild.getName(), null,
                -1));
    }
}
