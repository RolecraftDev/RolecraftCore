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
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handles the setting and viewing of a guild's flags.
 *
 * @since 0.1.0
 */
public class GuildFlagCommand extends PlayerCommandHandler {
    /**
     * The {@link RolecraftCore} plugin's {@link GuildManager} object.
     */
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public GuildFlagCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "flag");
        this.guildManager = plugin.getGuildManager();

        setUsage("/guild flag {set/view} <flag> [value]");
        setDescription("Change or view guild flag settings");
        setPermission(
                "rolecraft.guild.create"); // uses internal guild permissions
        setSubcommand(true);
    }

    /**
     * @since 0.1.0
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);

        if (guild == null) {
            // The player doesn't have a guild
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }

        if (args.length() < 2) {
            // Send usage string as set in constructor
            sendUsageMessage(player);
            return;
        }

        final String action = args.getRaw(0).toLowerCase();
        if (!(action.equals("set") || action.equals("view"))) {
            sendUsageMessage(player);
            return;
        }

        final String flag = args.getRaw(1).toLowerCase();
        if (action.equals("set")) {
            if (args.length() < 3) {
                // Send usage string as set in constructor
                sendUsageMessage(player);
                return;
            }

            final String newVal = args.getRaw(1).toLowerCase();
            if (flag.equals("open")) {
                if (Boolean.valueOf(newVal) == null) {
                    // Send usage string as set in constructor
                    sendUsageMessage(player);
                    return;
                }

                if (!guild.can(id, GuildAction.SET_OPEN)) {
                    player.sendMessage(
                            plugin.getMessage(Messages.GUILD_NO_PERMISSION));
                    return;
                }

                guild.setOpen(Boolean.parseBoolean(newVal));
            } else {
                player.sendMessage(
                        plugin.getMessage(Messages.GUILD_FLAG_NOT_EXISTS));
                return;
            }

            player.sendMessage(plugin.getMessage(Messages.GUILD_FLAG_SET,
                    MessageVariable.FLAG_NAME.value(flag),
                    MessageVariable.FLAG_VALUE.value(newVal)));
        } else {
            String flagValue = null;
            if (flag.equals("open")) {
                flagValue = String.valueOf(guild.isOpen());
            } // others added later

            if (flagValue == null) {
                player.sendMessage(
                        plugin.getMessage(Messages.GUILD_FLAG_NOT_EXISTS));
                return;
            }

            player.sendMessage(plugin.getMessage(Messages.GUILD_FLAG_INFO,
                    MessageVariable.FLAG_NAME.value(flag),
                    MessageVariable.FLAG_VALUE.value(flagValue)));
        }
    }
}
