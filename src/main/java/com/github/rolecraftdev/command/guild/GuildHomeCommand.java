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
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildHomeCommand extends PlayerCommandHandler {
    private final GuildManager guildManager;

    public GuildHomeCommand(final RolecraftCore plugin) {
        super(plugin, "home");
        guildManager = plugin.getGuildManager();

        setUsage("/guild home");
        setDescription("Teleports to the guild home");
        setPermission("rolecraft.guild.home");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);
        if (args.length() > 0 && args.getRaw(0).equalsIgnoreCase("set")) {
            if (!player.hasPermission("rolecraft.guild.sethome") || !guild.can(id,
                    GuildAction.SET_HOME)) {
                player.sendMessage(ChatColor.DARK_RED
                        + "You don't have permission to do that!");
                return;
            }

            guild.setHomeLocation(player.getLocation());
            player.sendMessage(
                    ChatColor.GRAY + "The guild's home location has been set!");
        } else {
            if (guild != null) {
                guild.teleportToHome(player);
                player.sendMessage(
                        ChatColor.GRAY + "Teleporting to guild home!");
            } else {
                player.sendMessage(
                        ChatColor.DARK_RED + "You don't have a guild!");
            }
        }
    }
}
