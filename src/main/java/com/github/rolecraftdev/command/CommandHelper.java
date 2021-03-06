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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.command.parser.ChatSection;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionRule;
import com.github.rolecraftdev.profession.ProfessionRuleMap;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides various utility methods for dealing with commands, help
 * messages and {@link MessageVariable}s.
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
     * @since 0.0.5
     */
    private CommandHelper() {
    }

    /**
     * Registers a command handler with a custom name.
     *
     * @param plugin the plugin to register the command to
     * @param name the name of the command
     * @param handler the handler for the command
     * @since 0.0.5
     */
    public static void registerCommand(@Nonnull final JavaPlugin plugin,
            @Nullable String name, @Nonnull final BaseCommandHandler handler) {
        if (name == null) {
            name = handler.getName();
        }

        PluginCommand cmd = plugin.getCommand(name);
        cmd.setExecutor(handler);
        if (handler instanceof TreeCommandHandler) {
            ((TreeCommandHandler) handler).setupSubcommands();
        }
    }

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
    public static void displayCommandList(@Nonnull final RolecraftCore plugin,
            @Nonnull final CommandSender sender,
            @Nonnull final List<BaseCommandHandler> commands,
            @Nullable final ChatSection pageArg) {
        final List<BaseCommandHandler> list = getPageFromArgs(plugin, sender,
                commands, pageArg, COMMANDS_PER_PAGE);
        if (list == null) {
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "[Commands]");
        for (final BaseCommandHandler sub : list) {
            if (sender.hasPermission(sub.getPermission())) {
                sender.sendMessage(ChatColor.GOLD + sub.getUsage() + " - "
                        + sub.getDescription());
            }
        }
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
    public static String joinFrom(final int start,
            @Nonnull final Arguments args) {
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
    public static Guild getGuildFromArgs(@Nonnull final GuildManager mgr,
            @Nonnull final CommandSender sender,
            @Nullable final ChatSection guildArg) {
        final Guild result;
        if (guildArg != null) {
            result = mgr.getGuild(guildArg.get());
            if (result == null) {
                sender.sendMessage(mgr.getPlugin().getMessage(
                        Messages.GUILD_NOT_EXISTS));
            }
        } else {
            if (sender instanceof Player) {
                result = mgr.getPlayerGuild(((Player) sender).getUniqueId());
                if (result == null) {
                    sender.sendMessage(mgr.getPlugin().getMessage(
                            Messages.NO_GUILD));
                }
            } else {
                result = null;
                sender.sendMessage(mgr.getPlugin().getMessage(
                        Messages.INVALID_USAGE));
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
    public static <T> List<T> getPageFromArgs(final RolecraftCore plugin,
            @Nonnull final CommandSender sender, @Nonnull final List<T> list,
            @Nullable final ChatSection pageArg, final int elementsPerPage) {
        final int amount = list.size();
        if (amount <= elementsPerPage) {
            return list;
        }

        int page = 1;
        if (pageArg != null) {
            if (pageArg.isInt()) {
                page = pageArg.asInt();
            } else {
                sender.sendMessage(plugin.getMessage(
                        Messages.INVALID_PAGE_NUMBER));
                return null;
            }
        }

        int from = Math.max(0, (page - 1) * elementsPerPage);
        int to = Math.min(amount, (page) * elementsPerPage);

        try {
            return list.subList(from, to);
        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage(plugin.getMessage(Messages.PAGE_NOT_EXISTS));
            return null;
        }
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
    public static void sendRankInfo(@Nonnull final CommandSender sender,
            @Nonnull final Guild guild, @Nonnull final GuildRank rank) {
        sender.sendMessage(ChatColor.GOLD +
                "Rank " + rank.getName() + " in guild " + guild.getName());
        sender.sendMessage(ChatColor.GRAY +
                "Members: " + rank.getMembers().size());

        // Create a human readable version of the permitted actions Set
        final String separator = ", ";
        final StringBuilder permitted = new StringBuilder();
        for (final GuildAction action : rank.getPermittedActions()) {
            permitted.append(action.getHumanReadableName()).append(separator);
        }

        permitted.setLength(permitted.length() - separator.length());
        sender.sendMessage(ChatColor.GRAY + "Permitted Actions: "
                + permitted.toString());
    }

    /**
     * Shows information for the given {@link Profession} to the given
     * {@link Player}.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @param player the {@link Player} to send information to
     * @param profession the {@link Profession} to send information about
     * @since 0.1.0
     */
    public static void showProfessionInformation(
            @Nonnull final RolecraftCore plugin, @Nonnull final Player player,
            @Nonnull final Profession profession) {
        final ProfessionRuleMap ruleMap = profession.getRuleMap();
        final List<String> lines = new ArrayList<String>();

        for (final ProfessionRule<?> rule : ruleMap.getRuleKeys()) {
            final String name = rule.getName();
            final StringBuilder line = new StringBuilder();
            final Object value = ruleMap.get(rule);

            if (name.equals("usable-spells")) {
                final List list = (List) value;
                line.append(plugin.getMessage(Messages.USABLE_SPELLS));

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-armor")) {
                final List list = (List) value;
                line.append(plugin.getMessage(Messages.USABLE_ARMOR));

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-enchantments")) {
                final List list = (List) value;
                line.append(plugin.getMessage(Messages.USABLE_ENCHANTMENTS));

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-items")) {
                final List list = (List) value;
                line.append(plugin.getMessage(Messages.USABLE_ITEMS));

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            }

            lines.add(line.toString());
        }

        sendBanner(player, "Profession: " + profession.getName(),
                lines.toArray());
    }

    /**
     * Sends a banner-formatted message to the given {@link CommandSender} made
     * up of the given {@link Object}s, using {@link Object#toString()}.
     *
     * @param sender the {@link CommandSender} to send the message to
     * @param message each line of the message to send
     * @since 0.0.5
     */
    public static void sendBanner(@Nonnull final CommandSender sender,
            @Nonnull final Object... message) {
        sender.sendMessage(ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + "-"
                + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH
                + "--------------------------------------------------"
                + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-");
        for (final Object line : message) {
            sender.sendMessage(ChatColor.GREEN + " " + line.toString());
        }
        sender.sendMessage(ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + "-"
                + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH
                + "--------------------------------------------------"
                + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-");
    }
}
