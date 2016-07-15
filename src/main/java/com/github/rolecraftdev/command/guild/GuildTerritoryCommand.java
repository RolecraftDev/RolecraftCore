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
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handles the 'territory' subcommand of the 'guild' command.
 *
 * @since 0.1.0
 */
public class GuildTerritoryCommand extends PlayerCommandHandler {
    /**
     * The {@link RolecraftCore} plugin's {@link GuildManager} object.
     */
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.1.0
     */
    public GuildTerritoryCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "territory");
        this.guildManager = plugin.getGuildManager();

        setUsage("/guild territory {claim/unclaim/status}");
        setDescription("Change or view guild territory");
        setPermission(
                "rolecraft.guild.create"); // uses internal guild permissions
        setSubcommand(true);
    }

    /**
     * @since 0.1.0
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() < 1) {
            // Send usage string as set in constructor
            sendUsageMessage(player);
            return;
        }

        final String action = args.getRaw(0).toLowerCase();
        if (action.equals("status")) {
            // TODO check territory status of current position
            return;
        }

        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);

        if (guild == null) {
            // The player doesn't have a guild
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }

        if (action.equals("claim")) {
            // TODO: claim current position
            return;
        } else if (action.equals("unclaim")) {
            // TODO: unclaim current position
            return;
        }

        sendUsageMessage(player);
    }
}
