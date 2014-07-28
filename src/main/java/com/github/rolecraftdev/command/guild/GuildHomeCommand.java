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
import com.github.rolecraftdev.guild.Guild;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildHomeCommand extends GuildSubCommand {
    public GuildHomeCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender,
            final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "Only players can teleport!");
        }

        final Player player = (Player) sender;
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);
        if (guild != null) {
            guild.teleportToHome(player);
            sender.sendMessage(ChatColor.GRAY + "Teleporting to guild home!");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "home" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.home";
    }

    @Override
    public String getUsage() {
        return "/guild home";
    }

    @Override
    public String getDescription() {
        return "Teleports to the guild's home";
    }
}
