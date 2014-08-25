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
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildShowCommand extends CommandHandler {
    private final GuildManager guildManager;
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildShowCommand(final RolecraftCore plugin) {
        super(plugin, "show");
        guildManager = plugin.getGuildManager();
        this.plugin = plugin;

        setUsage("/guild show <name>");
        setDescription("Shows info about a guild");
        setPermission("rolecraft.guild.show");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Guild guild = CommandHelper.getGuildFromArgs(guildManager, sender,
                args.length() > 0 ? args.get(0) : null);

        if (guild != null) {
            sender.sendMessage(plugin.getMessage(Messages.GUILD_INFO,
                    MsgVar.create("$guild", guild.getName())));
            StringBuilder members = new StringBuilder();
            int onlineMembers = 0;
            for (UUID id : guild.getMembers()) {
                if (plugin.getServer().getPlayer(id) == null) {
                    continue;
                } else {
                    members.append(plugin.getServer().getPlayer(id));
                    members.append(',');
                    onlineMembers++;
                }
            }
            // remove trailing comma
            members.deleteCharAt(members.length() - 1);
            sender.sendMessage(plugin.getMessage(Messages.GUILD_MEMBERS,
                    MsgVar.create("$totalnumber",
                            String.valueOf(guild.getMembers().size())),
                    MsgVar.create("$onlinenumber",
                            String.valueOf(onlineMembers)),
                    MsgVar.create("$members", members.toString())));
            sender.sendMessage(plugin.getMessage(Messages.GUILD_INFLUENCE,
                    MsgVar.create("$influence",
                            String.valueOf(guild.getInfluence()))));
            sender.sendMessage(plugin.getMessage(Messages.GUIlD_LEADER,
                    MsgVar.create("$leader", guild.getLeader().toString())));
            StringBuilder ranks = new StringBuilder();
            for (GuildRank rank : guild.getRanks()) {
                ranks.append(rank.getName());
                ranks.append(',');
            }
            ranks.deleteCharAt(members.length() - 1);
            sender.sendMessage(plugin.getMessage(Messages.GUILD_RANK,
                    MsgVar.create("$ranks", ranks.toString())));
        }
    }
}
