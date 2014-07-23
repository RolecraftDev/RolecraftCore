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

public final class GuildShowCommand extends GuildSubCommand {
    public GuildShowCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Guild guild;
        if (args.length > 1) {
            final String name = args[1];
            guild = guildManager.getGuild(name);
            if (guild == null) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "That guild doesn't exist!");
            }
        } else {
            if (sender instanceof Player) {
                guild = guildManager
                        .getPlayerGuild(((Player) sender).getUniqueId());
                if (guild == null) {
                    sender.sendMessage(
                            ChatColor.DARK_RED + "You don't have a guild!");
                }
            } else {
                guild = null;
                sender.sendMessage(ChatColor.DARK_RED
                        + "Invalid syntax, /guild show <name>");
            }
        }

        if (guild != null) {
            // TODO
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "show", "details", "info" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.show";
    }

    @Override
    public String getUsage() {
        return "/guild show <guild>";
    }

    @Override
    public String getDescription() {
        return "Shows information for the given guild";
    }
}
