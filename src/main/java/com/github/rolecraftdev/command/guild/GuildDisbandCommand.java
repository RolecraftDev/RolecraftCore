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

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class GuildDisbandCommand extends CommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    GuildDisbandCommand(final RolecraftCore plugin) {
        super(plugin, "disband");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild disband [name]");
        setDescription("Disbands a guild");
        setPermission("rolecraft.guild.create");
    }

    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Guild guild;
        if (args.length() > 0) {
            if (sender instanceof Player && !sender
                    .hasPermission("rolecraft.guild.disband.other")) {
                sender.sendMessage(ChatColor.DARK_RED
                        + "You don't have permission to do that!");
                return;
            }

            guild = guildManager.getGuild(args.getArgument(0).rawString());
            if (guild == null) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "That guild doesn't exist!");
                return;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "Invalid syntax, " + getUsage());
                return;
            }

            final UUID player = ((Player) sender).getUniqueId();
            guild = guildManager.getPlayerGuild(player);

            if (guild == null) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "You don't have a guild!");
                return;
            } else {
                if (!guild.getLeader().equals(player)) {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "You aren't the leader of your guild!");
                    return;
                }
            }
        }

        if (sender instanceof Player) {
            final Player player = (Player) sender;
            player.setMetadata(DataManager.CONFIRM_COMMAND_METADATA,
                    new FixedMetadataValue(plugin,
                            new GuildDisbandTask(guildManager, player,
                                    guild)));
        } else {
            guildManager.removeGuild(guild);
            sender.sendMessage(
                    ChatColor.GRAY + "Disbanded the guild: " + guild
                            .getName());
        }
    }

    /**
     * A Runnable implementation which disbands a specific guildold and sends a
     * message notifying the command sender. For use with RCConfirmCommand
     */
    public static final class GuildDisbandTask implements Runnable {
        private final GuildManager mgr;
        private final CommandSender sender;
        private final Guild guild;

        public GuildDisbandTask(final GuildManager mgr,
                final CommandSender sender, final Guild guild) {
            this.mgr = mgr;
            this.sender = sender;
            this.guild = guild;
        }

        @Override
        public void run() {
            mgr.removeGuild(guild);
            sender.sendMessage(
                    ChatColor.GRAY + "Disbanded guild: " + guild.getName());
        }
    }
}
