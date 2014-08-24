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
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @since 0.0.5
 */
public class GuildListCommand extends CommandHandler {
    private final GuildManager guildMgr;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildListCommand(final RolecraftCore plugin) {
        super("list");
        guildMgr = plugin.getGuildManager();

        setDescription("List all of the guilds available");
        setPermission("rolecraft.guild.show");
        setUsage("/guild list [page]");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Set<Guild> guilds = guildMgr.getGuilds();
        if (guilds == null) {
            sender.sendMessage(ChatColor.DARK_RED +
                    "The plugin hasn't loaded yet!");
            return;
        }

        final List<Guild> onPage = CommandHelper
                .getPageFromArgs(sender, new ArrayList<Guild>(guilds),
                        args.length() > 0 ? args.get(0) : null, 6);
        if (onPage == null) {
            // No need to send an error message - getPageFromArgs does that for
            // us already, so we can just return here.
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Guilds");
        for (final Guild guild : onPage) {
            sender.sendMessage(ChatColor.GRAY + guild.getName());
        }
    }
}
