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
import com.github.rolecraftdev.chat.channel.ChatChannel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Listens to chat events for Rolecraft.
 *
 * @since 0.1.0
 */
public class ChatListener implements Listener {
    /**
     * The associated {@link RolecraftCore} plugin instance.
     */
    @Nonnull
    private final RolecraftCore plugin;
    /**
     * The {@link ChatManager} instance for the associated plugin instance.
     */
    @Nonnull
    private final ChatManager chatManager;

    /**
     * Instantiates the Rolecraft chat listener.
     *
     * @param plugin the main {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public ChatListener(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
        this.chatManager = plugin.getChatManager();
    }

    /**
     * @since 0.1.0
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID id = player.getUniqueId();
        final String message = event.getMessage();
        final ChatChannel currentChannel = chatManager.getCurrentChannel(id);

        String prefix = null, suffix = null;

        if (plugin.vaultChatHooked()) {
            prefix = plugin.getVaultChat().getPlayerPrefix(player);
            suffix = plugin.getVaultChat().getPlayerSuffix(player);
        }

        final String formattedMessage = chatManager.getFormatter()
                .formatMessage(player, prefix, suffix, currentChannel, message);
        if (currentChannel.getRange() < 0) { // channel has no range requirement
            chatManager.sendToChannel(currentChannel, player, formattedMessage);
        } else {
            chatManager.sendToChannel(currentChannel, player, formattedMessage,
                    player.getLocation());
        }
    }
}
