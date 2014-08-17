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

import org.apache.commons.lang.StringUtils;
import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;
import pw.ian.albkit.command.parser.ChatSection;

import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This class provides various utility methods for dealing with commands in
 * Rolecraft.
 */
public final class CommandHelper {
    /**
     * The amount of commands to display on a single page in a help message sent
     * to a user.
     */
    public static final int COMMANDS_PER_PAGE = 6;

    /**
     * Joins all of the arguments in the given {@link Arguments} object starting
     * from the given start index. Arguments are joined by a single whitespace
     * character.
     *
     * @param start The index to start joining arguments from
     * @param args  The {@link Arguments} to join into one string
     * @return A joined {@link String} of arguments from the given start index
     */
    public static String joinFrom(final int start, final Arguments args) {
        if (args.length() <= start) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length(); i++) {
            builder.append(args.getRaw(i)).append(" ");
        }
        // Use StringUtils#strip because String#trim also removes ASCII control
        // characters and not just unicode whitespace
        return StringUtils.strip(builder.toString());
    }

    /**
     * Gets a {@link Guild} from the given argument. If there is no argument
     * given, the method will attempt to get the {@link Guild} of the sender of
     * the command, if said sender is a player. Otherwise, the sender is alerted
     * that they must specify a {@link Guild}'s name. The sender is also alerted
     * if they specify a {@link Guild} that doesn't exist.
     *
     * @param mgr      - The {@link GuildManager} to work with
     * @param sender   - The {@link CommandSender} to send error messages to
     * @param guildArg - The argument which contains the {@link Guild}'s name,
     *                 or null if there isn't one
     * @return The {@link Guild} specified by the argument, or the sender's
     * {@link Guild} if {@code guildArg} is null, and else null
     */
    public static Guild getGuildFromArgs(final GuildManager mgr,
            final CommandSender sender, final String guildArg) {
        final Guild result;
        if (guildArg != null) {
            result = mgr.getGuild(guildArg);
            if (result == null) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "That guild doesn't exist!");
            }
        } else {
            if (sender instanceof Player) {
                result = mgr.getPlayerGuild(((Player) sender).getUniqueId());
                if (result == null) {
                    sender.sendMessage(
                            ChatColor.DARK_RED + "You don't have a guild!");
                }
            } else {
                result = null;
                sender.sendMessage(
                        ChatColor.DARK_RED + "You must specify a guild name!");
            }
        }

        return result;
    }

    /**
     * Gets a sublist of a specific page within the given list, using a page
     * number provided in the arguments array and using the given amount of
     * elements per page to decide the cut-off point for a page.
     *
     * @param sender          - The {@link CommandSender} to send error messages to
     * @param list            - The {@link List} to get a specific page of elements from
     * @param pageArg         - The page number argument as a string
     * @param elementsPerPage - The amount of elements on each page
     * @param <T>             - The type of the given {@link List}
     * @return The page
     */
    public static <T> List<T> getPageFromArgs(final CommandSender sender,
            final List<T> list, final ChatSection pageArg,
            final int elementsPerPage) {
        final int amount = list.size();
        final int pages = (int) Math.ceil(amount / elementsPerPage);

        int page = 1;
        if (pageArg != null) {
            if (pageArg.isInt()) {
                page = pageArg.asInt();
            } else {
                sender.sendMessage(ChatColor.DARK_RED +
                        "Invalid page argument!");
                return null;
            }

            if (page > pages || page < 1) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "That page doesn't exist!");
                return null;
            }
        }

        return list.subList(elementsPerPage * (page - 1), list.size() - 1);
    }

    /**
     * Sends the given {@link CommandSender} a basic overview of the given
     * {@link GuildRank} within the given {@link Guild}
     *
     * @param sender - The {@link CommandSender} to send messages to
     * @param guild  - The {@link Guild} the {@link GuildRank} is part of
     * @param rank   - The {@link GuildRank} to send information about
     */
    public static void sendRankInfo(final CommandSender sender,
            final Guild guild, final GuildRank rank) {
        sender.sendMessage(ChatColor.GOLD +
                "Rank " + rank.getName() + " in guild " + guild.getName());
        sender.sendMessage(ChatColor.GRAY +
                "Members: " + rank.getMembers().size());

        // Create a human readable version of the permitted actions Set
        final StringBuilder permitted = new StringBuilder();
        for (final GuildAction action : rank.getPermittedActions()) {
            permitted.append(action.getPlayerReadableName()).append(", ");
        }
        permitted.setLength(permitted.length() - 2);

        sender.sendMessage(ChatColor.GRAY +
                "Permitted Actions: " + permitted.toString());
    }

    /**
     * Displays a 'help message' to the given command sender, which consists of
     * a list of commands, containing their usage as well as a brief description
     * as to each command's functionality. The page of subcommands is taken from
     * the arguments if any are provided.
     *
     * @param sender   - The {@link CommandSender} to send the help messages to
     * @param commands - A {@link List} of sub-commands, used for sending
     *                 help to the {@link CommandSender}
     * @param pageArg  - The {@link ChatSection} argument which should be
     *                 used to extract a page number from
     */
    public static void displayCommandList(final CommandSender sender,
            final List<CommandHandler> commands, final ChatSection pageArg) {
        final List<CommandHandler> list = getPageFromArgs(sender, commands,
                pageArg, COMMANDS_PER_PAGE);

        if (list != null) {
            sender.sendMessage(ChatColor.GOLD + "[Commands]");
            for (final CommandHandler sub : list) {
                if (sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage(ChatColor.GOLD + sub.getUsage() + " - "
                            + sub.getDescription());
                }
            }
        }
    }

    /**
     * Should never be called.
     */
    private CommandHelper() {
    }
}
