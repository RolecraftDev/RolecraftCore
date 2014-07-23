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
import com.github.rolecraftdev.command.RCCommand;
import com.github.rolecraftdev.command.RCSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class GuildCommand extends RCCommand {
    private final Set<GuildSubCommand> subCommands = new HashSet<GuildSubCommand>();

    public GuildCommand(final RolecraftCore plugin) {
        super(plugin);

        subCommands.add(new GuildHomeCommand(plugin));
        subCommands.add(new GuildShowCommand(plugin));
        subCommands.add(new GuildCreateCommand(plugin));
        // TODO: Add more subcommands
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd,
            final String label, final String[] args) {
        if (args.length == 0) {
            CommandHelper.displayCommandList(sender,
                    new ArrayList<RCSubCommand>(subCommands), null, 0);
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                CommandHelper.displayCommandList(sender,
                        new ArrayList<RCSubCommand>(subCommands), args, 1);
            } else {
                GuildSubCommand subCommand = null;
                for (final GuildSubCommand sub : subCommands) {
                    for (final String name : sub.getNames()) {
                        if (name.equalsIgnoreCase(args[0])) {
                            subCommand = sub;
                            break;
                        }
                    }
                }

                if (subCommand != null) {
                    if (sender.hasPermission(subCommand.getPermission())
                            || sender instanceof ConsoleCommandSender) {
                        subCommand.execute(sender, args);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED
                                + "You don't have permission to do that!");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "That subcommand doesn't exist!");
                }
            }
        }

        return true;
    }

    @Override
    public String[] getNames() {
        return new String[] { "guild" };
    }

    public void registerSubCommand(GuildSubCommand subCommand) {
        subCommands.add(subCommand);
    }
}
