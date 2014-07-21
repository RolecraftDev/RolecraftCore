package com.github.rolecraftdev.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Various utility / helper methods for dealing with commands in Rolecraft, such
 * as a method for sending help messages to a user which can be used in multiple
 * places
 */
public final class CommandHelper {
    /**
     * The amount of commands to display on a single page in a help message sent
     * to a user
     */
    private static final int COMMANDS_PER_PAGE = 6;

    /**
     * Should never be called
     */
    private CommandHelper() {
    }

    /**
     * Displays a 'help message' to the given command sender, which consists of
     * a list of commands, containing their usage as well as a brief description
     * as to each command's functionality. The page of subcommands is taken from
     * the arguments if any are provided
     *
     * @param sender      The CommandSender object to send the help messages to,
     *                    i.e the player who typed /command help
     * @param subCommands A List of subcommands which are used for sending help
     *                    to the command sender
     * @param args        The arguments provided by the user in their request
     *                    for help. For example, the user may have typed
     *                    /command help 1, in which case the arguments would be
     *                    { "help", "1" }
     * @param startIndex  The first index in the arguments array which can be
     *                    used as a page number for this method. For example, in
     *                    an array containing { "help", "1" } the startIndex
     *                    would be 1, as "help" is an argument which was parsed
     *                    by the command executor, and thus isn't part of the
     *                    arguments that this method cares about
     */
    public static void displayCommandList(final CommandSender sender,
            final List<RCSubCommand> subCommands, final String[] args,
            final int startIndex) {
        // Get the number of available pages
        final int numCommands = subCommands.size();
        final int pages = (int) Math.ceil(numCommands / COMMANDS_PER_PAGE);
        int page = 1;

        if (args != null && args.length > startIndex) {
            try {
                page = Integer.parseInt(args[startIndex]);
                if (page > pages || page < 1) {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "Invalid page number!");
                }
            } catch (final NumberFormatException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid page number!");
            }
        }

        if (pages > 1) {
            sender.sendMessage(
                    ChatColor.GOLD + "[Commands - Page 1/" + pages + "]");
        } else {
            sender.sendMessage(ChatColor.GOLD + "[Commands]");
        }

        final int orig = COMMANDS_PER_PAGE * (page - 1);
        for (int cmdNo = orig; cmdNo < orig + COMMANDS_PER_PAGE; cmdNo++) {
            final RCSubCommand sub = subCommands.get(cmdNo);
            if (sender.hasPermission(sub.getPermission())) {
                sender.sendMessage(ChatColor.GOLD + sub.getUsage() + " - "
                        + sub.getDescription());
            }
        }
    }
}
