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
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.guild.Guild;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCreateCommand extends GuildSubCommand {
    public GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "Only players can make guilds!");
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(ChatColor.DARK_RED
                    + "Invalid syntax, /guild create <name>");
            return;
        }
        final Player thePlayer = ((Player) sender);
        final UUID player = thePlayer.getUniqueId();
        if (guildManager.getPlayerGuild(player) != null) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "You are already in a guild!");
            return;
        }
        if (plugin.useEconomy()) {
            final EconomyResponse response = plugin.getEconomy().bankHas(
                    thePlayer.getName(),
                    guildManager.getCreationCost());
            if (!response.transactionSuccess()) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "You can't afford to do that!");
                return;
            }
        }

        final String name = args[1];
        final Guild guild = new Guild(guildManager);

        guild.setName(name);
        guild.setLeader(player);
        if (guildManager.addGuild(guild, false)) {
            GuildCreateEvent event = new GuildCreateEvent(plugin, guild);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                sender.sendMessage(ChatColor.DARK_RED
                        + event.getCancelMessage());
                guildManager.removeGuild(guild);
            } else {
                sender.sendMessage(ChatColor.GRAY
                        + "You created a guild named '" + name + "'!");

                if (plugin.useEconomy()) {
                    plugin.getEconomy().bankWithdraw(thePlayer.getName(),
                            guildManager.getCreationCost());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED
                    + "Couldn't create guild! The name may have already been taken!");
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "create", "new" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.create";
    }

    @Override
    public String getUsage() {
        return "/guild create <name>";
    }

    @Override
    public String getDescription() {
        return "Creates a new guild with the given name";
    }
}
