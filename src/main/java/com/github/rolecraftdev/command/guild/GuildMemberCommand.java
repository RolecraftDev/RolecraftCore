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

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;
import pw.ian.albkit.command.parser.ChatSection;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GuildMemberCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildMgr;

    public GuildMemberCommand(final RolecraftCore plugin) {
        super(plugin, "member");
        this.plugin = plugin;
        guildMgr = plugin.getGuildManager();

        setUsage("/guild member <member> <invite/kick/rank> <args>");
        setDescription("Invite, kick, promote or demote guild members");
        setPermission("rolecraft.guild.create");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final Guild guild = guildMgr.getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }
        if (args.length() < 3) {
            sendUsageMessage(player);
            return;
        }

        final String command = args.getArgument(2).rawString();
        final ChatSection targetArg = args.getArgument(1);
        final OfflinePlayer offline = targetArg.asOfflinePlayer();

        if (!offline.hasPlayedBefore()) {
            player.sendMessage(ChatColor.DARK_RED +
                    "That player doesn't exist!");
            return;
        }

        final Player target = targetArg.asPlayer();
        final PlayerData targetData = plugin.getDataManager()
                .getPlayerData(offline.getUniqueId());
        final Guild targetGuild = guildMgr
                .getPlayerGuild(offline.getUniqueId());

        if (command.equalsIgnoreCase("invite")) {
            if (!guild.can(player.getUniqueId(), GuildAction.INVITE)) {
                player.sendMessage(ChatColor.DARK_RED +
                        "You can't do that!");
                return;
            }
            if (target == null) {
                player.sendMessage(ChatColor.DARK_RED +
                        "That player isn't online!");
                return;
            }

            // TODO: Invite player to guild (no more checks needed)
            // We need an invitation system first
        } else {
            final UUID id = offline.getUniqueId();
            if (!targetGuild.equals(guild)) {
                player.sendMessage(ChatColor.DARK_RED +
                        "That player isn't in your guild!");
                return;
            }
            if (guild.getLeader().equals(id)) {
                player.sendMessage(ChatColor.DARK_RED +
                        "You can't do that!");
                return;
            }

            if (command.equalsIgnoreCase("kick")) {
                if (!guild.can(player.getUniqueId(), GuildAction.KICK_MEMBER)) {
                    player.sendMessage(ChatColor.DARK_RED +
                            "You can't do that!");
                    return;
                }

                // We do this async because if the player is offline we can't
                // access the PlayerData object until the data is loaded from
                // SQL
                new KickPlayerTask(plugin, targetData)
                        .runTaskAsynchronously(plugin);
                player.sendMessage(ChatColor.GRAY +
                        "Kicked " + offline.getName() + " from the guild!");
            } else if (command.equalsIgnoreCase("rank")) {
                if (!guild.can(
                        player.getUniqueId(), GuildAction.MODIFY_RANKS)) {
                    player.sendMessage(ChatColor.DARK_RED +
                            "You can't do that!");
                    return;
                }
                if (args.length() < 5) {
                    player.sendMessage(ChatColor.DARK_RED
                            + "You must specify an action and a value for it!");
                    return;
                }

                final String action = args.getArgument(3).rawString();
                final String rankArg = args.getArgument(4).rawString();
                final GuildRank rank = guild.getRank(rankArg);

                if (rank == null) {
                    player.sendMessage(ChatColor.DARK_RED +
                            "The rank '" + rankArg + "' doesn't exist!");
                    return;
                }

                final boolean add = action.equalsIgnoreCase("add");
                final boolean remove = action.equalsIgnoreCase("remove");
                final boolean valid = add || remove;

                if (!valid) {
                    player.sendMessage(ChatColor.DARK_RED
                            + "Action must be 'add' or 'remove'!");
                    return;
                }

                if (add) {
                    rank.addMember(offline.getUniqueId());
                    player.sendMessage(ChatColor.DARK_RED +
                            "Added player '" + offline.getName() + "' to rank "
                            + rank.getName() + "!");

                    if (target != null) {
                        target.sendMessage(ChatColor.RED
                                + "You were added to the guild rank " + rank
                                .getName());
                    }
                } else if (remove) {
                    rank.removeMember(offline.getUniqueId());
                    player.sendMessage(ChatColor.DARK_RED +
                            "Removed player '" + offline.getName()
                            + "' from rank " + rank.getName() + "!");

                    if (target != null) {
                        target.sendMessage(ChatColor.RED
                                + "You were removed from the guild rank " + rank
                                .getName());
                    }
                }
            }
        }
    }

    /**
     * A BukkitRunnable to kick a player from a guild and notify them if they
     * are online
     */
    private final class KickPlayerTask extends BukkitRunnable {
        /**
         * The {@link RolecraftCore} plugin
         */
        private final RolecraftCore plugin;
        /**
         * The {@link PlayerData} of the player to kick
         */
        private final PlayerData data;

        public KickPlayerTask(final RolecraftCore plugin,
                final PlayerData data) {
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
                    plugin.getGuildManager().getGuild(data.getGuild())
                            .removeMember(data.getPlayerId(), true);

                    final Player player = Bukkit.getPlayer(data.getPlayerId());
                    if (player != null) {
                        player.sendMessage(ChatColor.RED +
                                "You were kicked from your guild!");
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
