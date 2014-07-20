package com.github.rolecraftdev.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class CommandHelper {
    private static final int COMMANDS_PER_PAGE = 6;

    public static void displayCommandList(final CommandSender sender,
            final List<RCSubCommand> subCommands, final String[] args,
            final String baseCommand, final int startIndex) {
        final int numCommands = subCommands.size();
        final int pages = (int) Math.ceil(numCommands / COMMANDS_PER_PAGE);
        int page = 1;

        if (args != null && args.length > 0) {
            try {
                page = Integer.parseInt(args[startIndex]);
                if (page > pages || page < 0) {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "Invalid page number!");
                }
            } catch (final NumberFormatException e) {
                sender.sendMessage(ChatColor.DARK_RED + "Invalid page number!");
            }
        }

        if (pages > 1) {
            sender.sendMessage("[Commands - Page 1/" + pages + "]");
        } else {
            sender.sendMessage("[Commands]");
        }

        final int orig = COMMANDS_PER_PAGE * (page - 1);
        for (int cmdNo = orig; cmdNo < orig + COMMANDS_PER_PAGE; cmdNo++) {
            final RCSubCommand sub = subCommands.get(cmdNo);
            if (sender.hasPermission(sub.getPermission())) {
                sender.sendMessage(sub.getUsage() + " - "
                        + sub.getDescription());
            }
        }
    }

    private CommandHelper() {
    }
}
