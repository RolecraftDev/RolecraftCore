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
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MessageVariable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildRankCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildRankCommand(final RolecraftCore plugin) {
        super(plugin, "rank");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild rank <rank> <new/delete/set> [args]");
        setDescription("Allows modification of guild ranks");
        setPermission("rolecraft.guild.create");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);

        if (guild == null) {
            // The player doesn't have a guild
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }
        if (!guild.getLeader().equals(id)) {
            // The player isn't the leader of the guild and therefore cannot
            // modify ranks
            player.sendMessage(plugin.getMessage(Messages.NOT_GUILD_LEADER));
            return;
        }
        if (args.length() == 0) {
            // Send usage string
            sendUsageMessage(player);
            return;
        }

        final String rankArg = args.getRaw(0);
        final GuildRank rank = guild.getRank(rankArg);
        if (rank == null) { // The rank doesn't exist
            if (args.length() < 2 || !isCreateAlias(
                    args.getRaw(1).toLowerCase())) {
                // The sender has entered a non-existent rank within their guild
                player.sendMessage(plugin.getMessage(Messages.RANK_NOT_EXISTS));
            } else { // There are 3+ args & the next is an alias of 'create'
                // Only returns false if the rank already exists
                final GuildRank newRank = new GuildRank(rankArg,
                        new HashSet<GuildAction>(), new HashSet<UUID>());
                if (guild.addRank(newRank)) {
                    RolecraftEventFactory.guildRankCreated(guild, newRank);
                    // Notify the sender that the rank was created
                    player.sendMessage(plugin.getMessage(Messages.RANK_CREATED,
                            MessageVariable.RANK.value(newRank.getName())));
                } else {
                    // Notify the sender that the rank already exists
                    player.sendMessage(plugin.getMessage(
                            Messages.RANK_ALREADY_EXISTS));
                }
            }

            return;
        }

        if (args.length() == 1) { // I.E the input is '/guild rank <name>'
            // Send information about the given rank
            CommandHelper.sendRankInfo(player, guild, rank);
            return;
        }

        final String command = args.getRaw(1).toLowerCase();
        if (isDeleteAlias(command)) {
            // Returns false if the rank is leader or default
            if (guild.removeRank(rank)) {
                RolecraftEventFactory.guildRankRemoved(guild, rank);
                // Alert the sender that the rank was removed
                player.sendMessage(plugin.getMessage(Messages.RANK_REMOVED,
                        MessageVariable.RANK.value(rank.getName())));
            } else {
                // Alert the sender that the rank wasn't removed
                player.sendMessage(plugin.getMessage(
                        Messages.CANNOT_REMOVE_RANK, MessageVariable.RANK.value(rank
                                .getName())));
            }

            return;
        }

        if (command.equals("set") || command.equals("modify")) {
            if (args.length() < 4) {
                // Invalid syntax
                player.sendMessage(ChatColor.DARK_RED +
                        "Usage: /guild rank <rank> set <permission> <yes/no>");
                return;
            }

            final String permission = args.getRaw(2).toLowerCase();
            final GuildAction perm = GuildAction.fromHumanReadable(permission);

            if (perm == null) { // The entered permission doesn't exist
                player.sendMessage(plugin.getMessage(Messages.INVALID_ACTION,
                        MessageVariable.ACTION.value(permission)));
                return;
            }

            String value = args.getRaw(3).toLowerCase();

            final boolean allow = value.equals("yes") || value.equals("y")
                    || value.equals("true");
            final boolean disallow = value.equals("no") || value.equals("n")
                    || value.equals("false");
            final boolean valid = allow || disallow;

            if (!valid) {
                // Value isn't valid
                player.sendMessage(plugin.getMessage(Messages.INVALID_VALUE,
                        MessageVariable.VALUE.value(value)));
                return;
            }

            if (allow) {
                value = "true";
                // Set the value of the permission to true
                rank.allowAction(perm);
                player.sendMessage(plugin.getMessage(Messages.VALUE_SET,
                        MessageVariable.ACTION.value(permission), MessageVariable.VALUE
                                .value(value)));
            } else {
                value = "false";
                // Leader must always have all permissions
                if (rank.equals(guild.getLeaderRank())) {
                    player.sendMessage(plugin.getMessage(
                            Messages.CANNOT_MODIFY_RANK, MessageVariable.RANK.value(rank
                                    .getName())));
                    return;
                }

                // Remove the permission from the rank + notify sender
                rank.disallowAction(perm);
                player.sendMessage(plugin.getMessage(Messages.VALUE_SET,
                        MessageVariable.ACTION.value(permission), MessageVariable.VALUE
                                .value(value)));
            }

            RolecraftEventFactory.guildRankModified(guild, rank, perm,
                    Boolean.parseBoolean(value));
            return;
        }

        // Arguments didn't match any valid usage
        player.sendMessage(ChatColor.DARK_RED + "Invalid usage, " + getUsage());
    }

    /**
     * Check whether the given string is an alias of the rank create command.
     *
     * @param arg the string to check
     * @return {@code true} if it is a defined alias; {@code false} otherwise
     */
    private boolean isCreateAlias(final String arg) {
        return arg.equals("create") || arg.equals("new") || arg.equals("make")
                || arg.equals("add");
    }

    /**
     * Check whether the given string is an alias of the rang delete command.
     *
     * @param arg the string to check
     * @return {@code true} if it is a defined alias; {@code false} otherwise
     */
    private boolean isDeleteAlias(final String arg) {
        return arg.equals("delete") || arg.equals("remove") || arg
                .equals("destroy");
    }
}
