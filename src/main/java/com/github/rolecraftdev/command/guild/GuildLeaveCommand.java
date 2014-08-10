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
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildLeaveCommand extends PlayerCommandHandler {
    private final GuildManager guildMgr;
    private final DataManager dataMgr;

    public GuildLeaveCommand(final RolecraftCore plugin) {
        super("leave");
        guildMgr = plugin.getGuildManager();
        dataMgr = plugin.getDataManager();

        setUsage("/guild leave");
        setDescription("Leave your guild");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID playerId = player.getUniqueId();
        final Guild guild = guildMgr.getPlayerGuild(playerId);
        if (guild == null) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }

        guild.removeMember(playerId, false);
        dataMgr.getPlayerData(playerId).setGuild(null);
    }
}