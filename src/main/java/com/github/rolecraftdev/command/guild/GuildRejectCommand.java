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
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GuildRejectCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildMgr;

    public GuildRejectCommand(final RolecraftCore plugin) {
        super("reject");
        this.plugin = plugin;
        guildMgr = plugin.getGuildManager();

        setPermission("rolecraft.guild.join");
        setDescription("Rejects an invitation to a guild");
        setUsage("/guild reject");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (val == null || !(val instanceof FixedMetadataValue)) {
            player.sendMessage(ChatColor.DARK_RED
                    + "You don't have an invitation to a guild!");
            return;
        }

        player.removeMetadata(GuildManager.GUILD_INVITE_METADATA, plugin);
        player.sendMessage(ChatColor.GRAY +
                "Rejected your current guild invitation");
    }
}
