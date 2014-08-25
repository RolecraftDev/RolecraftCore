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

import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.messages.MsgVar;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;
import pw.ian.albkit.command.parser.ChatSection;

import java.util.List;

/**
 * This class provides various utility methods for dealing with commands, help
 * messages and {@link MsgVar}s.
 *
 * @since 0.0.5
 */
public final class CommandHelper {
    /**
     * The amount of commands to display on a single page; in a help message for
     * example.
     *
     * @since 0.0.5
     */
    private static final int COMMANDS_PER_PAGE = 6;

    /**
     * Displays a help message to the given {@link CommandSender}, which
     * consists of a list of commands, containing their usage as well as a brief
     * description of each command's functionality. The page of sub-commands is
     * retrieved from the arguments when provided.
     *
     * @param sender the {@link CommandSender} to send the help messages to
     * @param commands the {@link List} of commands
     * @param pageArg the {@link ChatSection} which should be used to extract a
     *        page number from
     * @since 0.0.5
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
     * Applies the values of the specified {@link MsgVar}s in the given message.
     *
     * @param message the affected message
     * @param vars the {@link MsgVar}s that should be applied
     * @return the given message after the {@link MsgVar}s have been applied
     * @since 0.0.5
     */
    public static String applyVars(String message, final MsgVar... vars) {
        if (vars == null) {
            return message;
        }

        for (final MsgVar var : vars) {
            message = var.replace(message);
        }
        return message;
    }

    /**
     * Joins all of the arguments in the given {@link Arguments}, starting from
     * the specified start index. All arguments are separated from each other by
     * a single whitespace character.
     *
     * @param start the index to start joining arguments from
     * @param args the {@link Arguments} to retrieve the arguments from
     * @return a string of all arguments in the given {@link Arguments},
     *         starting at the specified start index
     * @since 0.0.5
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
     * Gets a {@link Guild} from the given {@link ChatSection}. If no argument
     * given, this method will attempt to return the {@link Guild} of the
     * {@link CommandSender}. Whenever the result is {@code null}, the
     * {@link CommandSender} will be warned with an appropriate message.
     *
     * @param mgr the {@link GuildManager} to work with
     * @param sender the sender of the {@link ChatSection}
     * @param guildArg the argument which contains the {@link Guild}'s name
     * @return the {@link Guild} specified by the {@link ChatSection} or the
     *         {@link CommandSender}'s when it is {@code null}
     * @since 0.0.5
     */
    public static Guild getGuildFromArgs(final GuildManager mgr,
            final CommandSender sender, final ChatSection guildArg) {
        final Guild result;
        if (guildArg != null) {
            result = mgr.getGuild(guildArg.get());
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
     * Get a sublist of a specific page within the given list using a
     * {@link ChatSection} as page number. Note that this also sends the
     * {@link CommandSender} a message when this somehow fails.
     *
     * @param sender the sender of the {@link ChatSection}
     * @param list the {@link List} to acquire a page of elements from
     * @param pageArg the page number
     * @param elementsPerPage the amount of elements per page
     * @param <T> the type of the given {@link List}
     * @return a bunch of elements from the given {@link List} that has been
     *         constructed by using the other given parameter values
     * @since 0.0.5
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
     * Sends the {@link CommandSender} a basic overview of the {@link GuildRank}
     * within the specified {@link Guild}.
     *
     * @param sender whom to send the overview
     * @param guild the applicable {@link Guild}
     * @param rank the {@link GuildRank} information should be gathered about
     * @since 0.0.5
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
     * @since 0.0.5
     */
    private CommandHelper() {
    }
}
