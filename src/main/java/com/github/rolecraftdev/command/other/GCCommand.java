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
import com.github.rolecraftdev.chat.GuildChannel;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.data.PlayerSettings;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

/**
 * @since 0.0.5
 */
public class GCCommand extends PlayerCommandHandler {
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public GCCommand(final RolecraftCore plugin) {
        super(plugin, "gc");
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

        final GuildChannel channel = guild.getChannel();
        if (channel == null) {
            // only happens if the guild is a 'null' guild - shouldn't be ever
            // here, in theory
            return;
        }

        if (args.length() > 0) {
            final StringBuilder message = new StringBuilder();

            for (final String arg : args.toStringArray()) {
                message.append(arg).append(" ");
            }

            // Add, send message, remove
            channel.onMessage(player.getDisplayName(), message.toString());
        } else {
            final PlayerData data = this.plugin.getDataManager()
                    .getPlayerData(player.getUniqueId());
            final PlayerSettings settings = data.getSettings();

            if (settings.isGuildChat()) {
                settings.setGuildChat(false);
            } else {
                settings.setGuildChat(true);
            }
        }
    }
}
