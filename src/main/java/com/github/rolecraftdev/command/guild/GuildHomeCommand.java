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
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MessageVariable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildHomeCommand extends PlayerCommandHandler {
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildHomeCommand(final RolecraftCore plugin) {
        super(plugin, "home");
        guildManager = plugin.getGuildManager();

        setUsage("/guild home");
        setDescription("Teleports to the guild home");
        setPermission("rolecraft.guild.home");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);

        if (guild == null) {
            player.sendMessage(plugin.getMessage(Messages.NO_GUILD));
            return;
        }

        if (args.length() > 0 && args.getRaw(0).equalsIgnoreCase("set")) {
            if (!player.hasPermission("rolecraft.guild.sethome") || !guild.can(
                    id, GuildAction.SET_HOME)) {
                player.sendMessage(plugin.getMessage(Messages.NO_PERMISSION));
                return;
            }

            guild.setHomeLocation(player.getLocation());
            player.sendMessage(plugin.getMessage(Messages.SET_GUILD_HOME));
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    guild.teleportToHome(player);
                }
            }.runTaskLater(plugin,
                    plugin.getConfig().getInt("teleportdelay") * 20);

            player.sendMessage(plugin.getMessage(Messages.GUILD_TP_HOME,
                    new MessageVariable("$time", plugin.getConfig().getInt(
                            "teleportdelay"))));
        }
    }
}
