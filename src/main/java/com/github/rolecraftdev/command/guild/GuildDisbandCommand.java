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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildDisbandCommand extends CommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    GuildDisbandCommand(final RolecraftCore plugin) {
        super(plugin, "disband");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild disband [name]");
        setDescription(
                "Disbands a guild, use /guild disband to disband your own");
        setPermission("rolecraft.guild.create");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Guild guild;
        if (args.length() > 0) {
            if (sender instanceof Player && !sender
                    .hasPermission("rolecraft.guild.disband.other")) {
                sender.sendMessage(plugin.getMessage(Messages.NO_PERMISSION));
                return;
            }

            guild = guildManager.getGuild(args.getRaw(0));
            if (guild == null) {
                sender.sendMessage(plugin.getMessage(
                        Messages.GUILD_NOT_EXISTS));
                return;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "Invalid syntax, " + getUsage());
                return;
            }

            final UUID player = ((Player) sender).getUniqueId();
            guild = guildManager.getPlayerGuild(player);

            if (guild == null) {
                sender.sendMessage(plugin.getMessage(Messages.NO_GUILD));
                return;
            } else {
                if (!player.equals(guild.getLeader())) {
                    sender.sendMessage(plugin.getMessage(
                            Messages.NOT_GUILD_LEADER));
                    return;
                }
            }
        }

        if (sender instanceof Player) {
            final Player player = (Player) sender;
            player.setMetadata(DataManager.CONFIRM_COMMAND_METADATA,
                    new FixedMetadataValue(plugin,
                            new GuildDisbandTask(guildManager, player,
                                    guild)));
        } else {
            guildManager.removeGuild(guild);
            sender.sendMessage(plugin.getMessage(Messages.GUILD_DISBANDED,
                    MsgVar.create("$name", guild.getName())));
        }
    }

    /**
     * Disbands a specific {@link Guild} and sends a message notifying the
     * {@link CommandSender} when completed.
     *
     * @since 0.0.5
     */
    public static final class GuildDisbandTask implements Runnable {
        private final GuildManager mgr;
        private final CommandSender sender;
        private final Guild guild;

        /**
         * Constructor.
         *
         * @param mgr the affected {@link GuildManager}
         * @param sender the executor of this event
         * @param guild the {@link Guild} that will be disbanded
         * @since 0.0.5
         */
        public GuildDisbandTask(final GuildManager mgr,
                final CommandSender sender, final Guild guild) {
            this.mgr = mgr;
            this.sender = sender;
            this.guild = guild;
        }

        /**
         * @since 0.0.5
         */
        @Override
        public void run() {
            mgr.removeGuild(guild);
            sender.sendMessage(mgr.getPlugin().getMessage(
                    Messages.GUILD_DISBANDED,
                    MsgVar.create("$name", guild.getName())));
        }
    }
}
