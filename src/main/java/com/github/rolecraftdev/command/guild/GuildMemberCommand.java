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
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.command.parser.ChatSection;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MessageVariable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildMemberCommand extends PlayerCommandHandler {
    private final GuildManager guildMgr;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildMemberCommand(final RolecraftCore plugin) {
        super(plugin, "member");
        guildMgr = plugin.getGuildManager();

        setUsage("/guild member <member> <invite/kick/rank> [args]");
        setDescription("Invite, kick, promote or demote guild members");
        setPermission("rolecraft.guild.create");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final Guild guild = guildMgr.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }
        if (args.length() < 2) {
            sendUsageMessage(player);
            return;
        }

        final String command = args.getRaw(1);
        final ChatSection targetArg = args.get(0);
        final OfflinePlayer offline = targetArg.asOfflinePlayer();

        if (!offline.hasPlayedBefore()) {
            player.sendMessage(plugin.getMessage(Messages.PLAYER_NOT_EXISTS));
            return;
        }

        final Player target = targetArg.asPlayer();
        final PlayerData targetData = plugin.getDataManager()
                .getPlayerData(offline.getUniqueId());
        final Guild targetGuild = guildMgr
                .getPlayerGuild(offline.getUniqueId());

        if (command.equalsIgnoreCase("invite")) {
            if (!guild.can(player.getUniqueId(), GuildAction.INVITE)) {
                player.sendMessage(plugin.getMessage(Messages.NO_PERMISSION));
                return;
            }
            if (target == null) {
                player.sendMessage(
                        plugin.getMessage(Messages.PLAYER_NOT_ONLINE));
                return;
            }

            if (target.hasMetadata(GuildManager.GUILD_INVITE_METADATA)) {
                player.sendMessage(
                        plugin.getMessage(Messages.PLAYER_CONSIDERING_INVITE));
                return;
            }

            target.setMetadata(GuildManager.GUILD_INVITE_METADATA,
                    new FixedMetadataValue(plugin, guild.getId()));
            target.sendMessage(plugin.getMessage(
                    Messages.PLAYER_INVITED_RECEIVER, MessageVariable.GUILD.value(guild
                            .getName())));
            player.sendMessage(plugin.getMessage(
                    Messages.PLAYER_INVITED_SENDER, MessageVariable.PLAYER.value(target
                            .getName())));
        } else {
            final UUID id = offline.getUniqueId();
            if (!guild.equals(targetGuild)) {
                player.sendMessage(plugin.getMessage(
                        Messages.PLAYER_NOT_IN_GUILD));
                return;
            }
            if (guild.getLeader().equals(id)) {
                player.sendMessage(plugin.getMessage(Messages.NO_PERMISSION));
                return;
            }

            if (command.equalsIgnoreCase("kick")) {
                if (!guild.can(player.getUniqueId(), GuildAction.KICK_MEMBER)) {
                    player.sendMessage(plugin.getMessage(
                            Messages.NO_PERMISSION));
                    return;
                }

                // We do this async because if the player is offline we can't
                // access the PlayerData object until the data is loaded from
                // SQL
                new KickPlayerTask(plugin, targetData)
                        .runTaskAsynchronously(plugin);
                player.sendMessage(plugin.getMessage(Messages.PLAYER_KICKED,
                        MessageVariable.PLAYER.value(targetData.getPlayerName())));
            } else if (command.equalsIgnoreCase("rank")) {
                if (!guild.can(
                        player.getUniqueId(), GuildAction.MODIFY_RANKS)) {
                    player.sendMessage(plugin.getMessage(
                            Messages.NO_PERMISSION));
                    return;
                }
                if (args.length() < 4) {
                    player.sendMessage(ChatColor.DARK_RED
                            + "Invalid syntax, /guild member <name> rank <action> <value>");
                    return;
                }

                final String action = args.getRaw(2);
                final String rankArg = args.getRaw(3);
                final GuildRank rank = guild.getRank(rankArg);

                if (rank == null) {
                    player.sendMessage(plugin.getMessage(
                            Messages.RANK_NOT_EXISTS, MessageVariable.RANK
                                    .value(rankArg)));
                    return;
                }

                final boolean add = action.equalsIgnoreCase("add");
                final boolean remove = action.equalsIgnoreCase("remove");
                final boolean valid = add || remove;

                if (!valid) {
                    player.sendMessage(
                            plugin.getMessage(Messages.INVALID_MEMBER_ACTION));
                    return;
                }

                if (add) {
                    rank.addMember(offline.getUniqueId());
                    player.sendMessage(plugin.getMessage(
                            Messages.ADDED_PLAYER_TO_RANK, MessageVariable.RANK
                                    .value(rank.getName()), MessageVariable.PLAYER
                                    .value(offline.getName())));

                    if (target != null) {
                        target.sendMessage(plugin.getMessage(
                                Messages.PLAYER_ADDED_TO_RANK, MessageVariable.RANK
                                        .value(rank.getName())));
                    }
                } else {
                    rank.removeMember(offline.getUniqueId());
                    player.sendMessage(plugin.getMessage(
                            Messages.REMOVED_PLAYER_FROM_RANK, MessageVariable.RANK
                                    .value(rank.getName()), MessageVariable.PLAYER
                                    .value(offline.getName())));

                    if (target != null) {
                        target.sendMessage(plugin.getMessage(
                                Messages.PLAYER_REMOVED_FROM_RANK, MessageVariable.RANK
                                        .value(rank.getName())));
                    }
                }
            }
        }
    }

    /**
     * Kick a player from his {@link Guild}. This will also send the player a
     * message if he's online.
     */
    private final class KickPlayerTask extends BukkitRunnable {
        /**
         * The associated {@link RolecraftCore} instance.
         */
        private final RolecraftCore plugin;
        /**
         * The kicked player's {@link PlayerData}.
         */
        private final PlayerData data;

        /**
         * Constructor.
         *
         * @param plugin the associated {@link RolecraftCore} instance
         * @param data the {@link PlayerData} of the player that will be kicked
         */
        KickPlayerTask(final RolecraftCore plugin, final PlayerData data) {
            this.plugin = plugin;
            this.data = data;
        }

        @Override
        public void run() {
            // This is used because it is possible that the data will be loading
            // if it was only loaded so that the player can be kicked
            while (!data.isLoaded()) {
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    //noinspection ConstantConditions
                    plugin.getGuildManager().getGuild(data.getGuild())
                            .removeMember(data.getPlayerId(), true);

                    final Player player = Bukkit.getPlayer(data.getPlayerId());
                    if (player != null) {
                        player.sendMessage(plugin.getMessage(
                                Messages.KICKED_FROM_GUILD, MessageVariable.PLAYER
                                        .value(data.getPlayerName())));
                    } else {
                        // If the player is offline we unload the data because
                        // the data was only loaded so they could be kicked
                        plugin.getDataManager()
                                .unloadAndSaveData(data.getPlayerId());
                    }
                }
            }.runTask(plugin);
        }
    }
}
