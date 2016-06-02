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
package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.entity.Player;

/**
 * @since 0.0.5
 */
public class GuildBroadcastCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildBroadcastCommand(final RolecraftCore plugin) {
        super("broadcast");
        this.plugin = plugin;

        setUsage("/guild broadcast [-r rank] <message>");
        setDescription("Broadcast a message to the guild");
        setPermission("rolecraft.guild.join");
        setValidateUsage(false);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final String message = CommandHelper.joinFrom(1, args);
        if (message == null) {
            sendUsageMessage(player);
            return;
        }

        final Guild guild = plugin.getGuildManager()
                .getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }
        if (guild.can(player.getUniqueId(), GuildAction.BROADCAST_MESSAGE)) {
            if (args.hasValueFlag("r")) {
                final String name = args.getValueFlag("r").getRawValue();
                final GuildRank rank = guild.getRank(name);
                if (rank == null) {
                    player.sendMessage(plugin.getMessage(
                            Messages.RANK_NOT_EXISTS, MsgVar.RANK.value(name)));
                    return;
                }

                rank.broadcastMessage(message);
            } else {
                guild.broadcastMessage(message);
            }
        } else {
            player.sendMessage(plugin.getMessage(Messages.BROADCAST_NO_PERMS));
        }
    }
}
