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
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildJoinCommand extends PlayerCommandHandler {
    private final DataManager dataMgr;
    private final GuildManager guildMgr;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildJoinCommand(final RolecraftCore plugin) {
        super(plugin, "join");
        dataMgr = plugin.getDataManager();
        guildMgr = plugin.getGuildManager();

        setUsage("/guild join <name>");
        setDescription("Joins the given guild if you are invited");
        setPermission("rolecraft.guild.join");
        setSubcommand(true);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            sendUsageMessage(player);
            return;
        }
        final String name = args.getRaw(0);
        final Guild guild = guildMgr.getGuild(name);
        if (guild == null) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_EXISTS));
            return;
        }

        if (guild.isOpen()) {
            completeGuildAdd(player, guild);
        }
        if (!player.hasMetadata(GuildManager.GUILD_INVITE_METADATA)) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
            return;
        }
        // TODO: fix this for multiple invites
        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (!(val instanceof FixedMetadataValue)) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
            return;
        }

        final FixedMetadataValue fixed = (FixedMetadataValue) val;

        if (fixed.asString().equalsIgnoreCase(guild.getId().toString())) {
            completeGuildAdd(player, guild);
        } else {
            player.sendMessage(plugin.getMessage(Messages.GUILD_NOT_INVITED));
        }
    }

    /**
     * Add the given player to the given {@link Guild}. This will automatically
     * send the appropriate chat messages and update the player's
     * {@link PlayerData}.
     *
     * @param player the player to add
     * @param guild the {@link Guild} the given player should be added to
     */
    private void completeGuildAdd(final Player player, final Guild guild) {
        final UUID pid = player.getUniqueId();
        final PlayerData data = dataMgr.getPlayerData(pid);
        if (data.getGuild() != null) {
            player.sendMessage(plugin.getMessage(Messages.ALREADY_IN_GUILD));
            return;
        }

        dataMgr.getPlayerData(player.getUniqueId()).setGuild(guild.getId());
        guild.addMember(pid, guild.getDefaultRank());
        player.sendMessage(plugin.getMessage(Messages.GUILD_JOINED_PLAYER,
                MessageVariable.GUILD.value(guild.getName())));
        guild.broadcastMessage(plugin.getMessage(Messages.GUILD_JOINED_OTHERS,
                MessageVariable.GUILD.value(guild.getName()),
                MessageVariable.PLAYER.value(player
                        .getName())));
    }
}
