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
package com.github.rolecraftdev.command.other;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.Messages;

import com.traksag.channels.Channel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

/**
 * @since 0.0.5
 */
public class GCCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public GCCommand(final RolecraftCore plugin) {
        super(plugin, "gc");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/gc [message]");
        setDescription("Allows communicating in Guild chat");
        setPermission("rolecraft.guild.chat");
        setValidateUsage(false);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final Guild guild = guildManager.getPlayerGuild(player.getUniqueId());

        if (guild == null) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }

        final Channel channel = guild.getChannel();

        if (args.length() > 0) {
            final StringBuilder message = new StringBuilder();

            for (String arg : args.toStringArray()) {
                message.append(arg).append(" ");
            }

            // Add, send message, remove
            channel.onConnect(player);
            Bukkit.getPluginManager()
                    .callEvent(
                            new AsyncPlayerChatEvent(false, player, message
                                    .toString(), null));
            channel.onDisconnect(player);
        } else {
            if (channel.contains(player)) {
                channel.onDisconnect(player);
            } else {
                channel.onConnect(player);
            }
        }
    }
}
