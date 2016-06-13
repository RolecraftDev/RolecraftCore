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
package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Base command handler for commands which may only be executed by players.
 *
 * @since 0.0.5
 */
public abstract class PlayerCommandHandler extends CommandHandler {
    /**
     * The message sent to someone who tries to execute the command but is not a
     * player. Loaded from messages config.
     */
    private String notPlayerMessage;

    /**
     * @since 0.0.5
     */
    public PlayerCommandHandler(@Nonnull final RolecraftCore plugin,
            @Nonnull final String name) {
        super(plugin, name);

        notPlayerMessage = plugin.getMessage(Messages.NOT_PLAYER);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayerMessage);
            return;
        }

        onCommand((Player) sender, args);
    }

    /**
     * @since 0.0.5
     */
    public void onCommand(Player player, String[] args) {
        if ((isSubcommand() ? args.length + 1 : args.length) < getMinArgs()) {
            sendUsageMessage(player);
            return;
        }
        if ((isSubcommand() ? args.length + 1 : args.length) > getMaxArgs()) {
            sendUsageMessage(player);
            return;
        }

        Arguments newArgs = new Arguments(args);
        if (paramsBase != null) {
            newArgs.withParams(
                    paramsBase.createParams(newArgs, isSubcommand()));
            if (doesValidateUsage() && !newArgs.getParams().valid()) {
                player.sendMessage(plugin.getMessage(Messages.INVALID_USAGE)
                        + getUsage());
                return;
            }
        }
        this.onCommand(player, newArgs);
    }

    /**
     * @since 0.0.5
     */
    public void onCommand(Player player, Arguments args) {
    }
}
