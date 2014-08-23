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
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;
import org.bukkit.entity.Player;
import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import java.util.UUID;

public class GuildLeaveCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildMgr;
    private final DataManager dataMgr;

    GuildLeaveCommand(final RolecraftCore plugin) {
        super("leave");
        this.plugin = plugin;
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
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }
        player.sendMessage(plugin.getMessage(Messages.GUILD_LEAVE,
                MsgVar.create("$guild",guild.getName())));
        guild.removeMember(playerId, false);
        dataMgr.getPlayerData(playerId).setGuild(null);
    }
}
