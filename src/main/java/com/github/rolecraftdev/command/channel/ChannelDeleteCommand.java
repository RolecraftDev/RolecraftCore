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
package com.github.rolecraftdev.command.channel;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.chat.ChatManager;
import com.github.rolecraftdev.chat.channel.ChatChannel;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles the 'delete' subcommand of the 'channel' command.
 *
 * @since 0.1.0
 */
public class ChannelDeleteCommand extends PlayerCommandHandler {
    /**
     * The {@link ChatManager} instance associated with the plugin.
     */
    private final ChatManager chatManager;

    /**
     * Constructor.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public ChannelDeleteCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "delete");
        this.chatManager = plugin.getChatManager();

        this.setPermission("rolecraft.channels.delete");
    }

    /**
     * @since 0.1.0
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() < 1) {
            player.sendMessage(plugin.getMessage(Messages.INVALID_USAGE)
                    + " /channel create <name> [--a][--m][-r range][-c color]");
            return;
        }

        final String name = args.getRaw(0);
        final ChatChannel channel = chatManager.getChannel(name);
        if (channel == null) {
            player.sendMessage(plugin.getMessage(Messages.CHANNEL_NOT_EXISTS));
            return;
        }

        chatManager.removeChannel(channel);
        player.sendMessage(plugin.getMessage(Messages.CHANNEL_DELETED,
                MessageVariable.CHANNEL.value(channel.getName())));
    }
}
