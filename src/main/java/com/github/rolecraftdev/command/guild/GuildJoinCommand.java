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
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GuildJoinCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildMgr;

    GuildJoinCommand(final RolecraftCore plugin) {
        super("join");
        this.plugin = plugin;
        guildMgr = plugin.getGuildManager();

        setUsage("/guild join <name>");
        setDescription("Joins the given guild if you are invited");
        setPermission("rolecraft.guild.join");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            sendUsageMessage(player);
            return;
        }
        if (!player.hasMetadata(GuildManager.GUILD_INVITE_METADATA)) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
            return;
        }
        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (!(val instanceof FixedMetadataValue)) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
            return;
        }

        final FixedMetadataValue fixed = (FixedMetadataValue) val;
        final String name = args.getRaw(0);
        final Guild guild = guildMgr.getGuild(name);
        if (fixed.asString().equalsIgnoreCase(guild.getId().toString())) {
            guild.addMember(player.getUniqueId(), guild.getDefaultRank());
            player.sendMessage(plugin.getMessage(Messages.GUILD_JOINED_PLAYER,
                    MsgVar.create("$guild", guild.getName())));
            guild.broadcastMessage(
                    plugin.getMessage(Messages.GUILD_JOINED_OTHERS,
                            MsgVar.create("$guild", guild.getName()),
                            MsgVar.create("$player", player.getName())));
        } else {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
        }
    }
}
