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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCreateCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin, "create");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild create <name>");
        setDescription("Create a guild");
        setPermission("rolecraft.guild.create");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            player.sendMessage(ChatColor.DARK_RED
                    + "Invalid syntax, /guild create <name>");
            return;
        }
        final UUID playerId = player.getUniqueId();
        if (guildManager.getPlayerGuild(playerId) != null) {
            player.sendMessage(
                    ChatColor.DARK_RED + "You are already in a guild!");
            return;
        }
        if (plugin.useEconomy()) {
            final EconomyResponse response = plugin.getEconomy().bankHas(
                    player.getName(),
                    guildManager.getCreationCost());
            if (!response.transactionSuccess()) {
                player.sendMessage(
                        ChatColor.DARK_RED + "You can't afford to do that!");
                return;
            }
        }

        final String name = args.getArgument(0).rawString();
        final Guild guild = new Guild(guildManager);

        guild.setName(name);
        guild.setLeader(playerId);
        if (guildManager.addGuild(guild, false)) {
            player.sendMessage(ChatColor.GRAY
                    + "You created a guild named '" + name + "'!");

            if (plugin.useEconomy()) {
                plugin.getEconomy().bankWithdraw(player.getName(),
                        guildManager.getCreationCost());
            }
        }
    }
}
