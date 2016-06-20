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
import com.github.rolecraftdev.command.parser.ChatSection;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles the 'create' subcommand of the 'channel' command.
 *
 * @since 0.1.0
 */
public class ChannelCreateCommand extends PlayerCommandHandler {
    /**
     * Constructor.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public ChannelCreateCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "create");

        this.setPermission("rolecraft.channels.create");
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

        boolean mod = false, admin = false, def = false;
        int range = -1;
        ChatColor color = null;

        if (args.hasNonValueFlag("def") || args.hasNonValueFlag("d")) {
            def = true;
        }

        if (args.hasNonValueFlag("mod") || args.hasNonValueFlag("m")) {
            // only those with mod permission can create mod channels
            if (player.hasPermission("rolecraft.channels.mod")) {
                mod = true;
            } else {
                player.sendMessage(plugin.getMessage(Messages.NOT_ALLOWED));
                return;
            }
        }

        if (args.hasNonValueFlag("admin") || args.hasNonValueFlag("a")) {
            // only those with admin permission can create admin channels
            if (player.hasPermission("rolecraft.channels.admin")) {
                admin = true;
            } else {
                player.sendMessage(plugin.getMessage(Messages.NOT_ALLOWED));
                return;
            }
        }

        if (args.hasValueFlag("r")) {
            final ChatSection section = args.getValueFlag("r").getValue();

            if (!section.isInt()) {
                player.sendMessage(plugin.getMessage(Messages.INVALID_USAGE)
                        + " /channel create <name> [--a][--m][-r range][-c color]");
                return;
            }

            range = section.asInt();
        }

        if (args.hasValueFlag("c")) {
            final ChatSection section = args.getValueFlag("r").getValue();

            if (!section.isChatColor()) {
                player.sendMessage(plugin.getMessage(Messages.INVALID_USAGE)
                        + " /channel create <name> [--a][--m][-r range][-c color]");
                return;
            }

            color = section.asChatColor();
        }

        final ChatManager chatManager = plugin.getChatManager();
        final ChatChannel channel = new ChatChannel(chatManager.getNextId(),
                name, def, mod, admin, null, color, range);
        chatManager.addChannel(channel);
        chatManager.addToChannel(player.getUniqueId(), channel);
        player.sendMessage(plugin.getMessage(Messages.CHANNEL_CREATED,
                MessageVariable.CHANNEL.value(channel.getName())));
    }
}
