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
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.util.messages.ColourScheme;
import com.github.rolecraftdev.util.messages.MessageHelper;

import org.bukkit.entity.Player;

/**
 * @since 0.0.5
 */
public class GuildHelpCommand extends PlayerCommandHandler {
    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildHelpCommand(final RolecraftCore plugin) {
        super("help");

        setDescription("Guild-related help");
        setUsage("/guild help [args...]");
        setPermission("rolecraft.guild.join");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            MessageHelper.sendBanner(ColourScheme.DEFAULT, player,
                    "Guilds",
                    "/guild help actions - Help with guild actions",
                    "/guild help commands - Help with commands",
                    "/guild help halls - Help with guild halls");
            return;
        }

        final String sub = args.getRaw(0).toLowerCase();
        if (sub.equals("actions")) {
            final StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (final GuildAction action : GuildAction.values()) {
                if (!first) {
                    builder.append(", ");
                }
                first = false;
                builder.append(action.getHumanReadableName());
            }
            MessageHelper.sendBanner(ColourScheme.DEFAULT, player,
                    "Actions:",
                    builder.toString());
        } else if (sub.equals("commands")) {
            // TODO: show command help
        } else if (sub.equals("halls")) {
            MessageHelper.sendBanner(ColourScheme.DEFAULT, player,
                    "Guild Halls:",
                    "Guild Halls are predefined areas which only members of the guild can access.",
                    "Guild Halls allow for battles between guilds, storage for items and more.");
        }
    }
}
