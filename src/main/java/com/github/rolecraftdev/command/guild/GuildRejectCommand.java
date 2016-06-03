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
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

/**
 * @since 0.0.5
 */
public class GuildRejectCommand extends PlayerCommandHandler {
    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildRejectCommand(final RolecraftCore plugin) {
        super(plugin, "reject");

        setPermission("rolecraft.guild.join");
        setDescription("Rejects an invitation to a guild");
        setUsage("/guild reject");
        setSubcommand(true);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final List<MetadataValue> metadata = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA);
        if (metadata == null || metadata.size() == 0) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD_INVITE));
            return;
        }

        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (val == null || !(val instanceof FixedMetadataValue)) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD_INVITE));
            return;
        }

        player.removeMetadata(GuildManager.GUILD_INVITE_METADATA, plugin);
        player.sendMessage(plugin.getMessage(Messages.GUILD_INVITE_REJECTED));
    }
}
