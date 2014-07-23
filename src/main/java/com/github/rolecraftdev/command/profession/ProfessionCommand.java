package com.github.rolecraftdev.command.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.RCCommand;
import com.github.rolecraftdev.command.RCSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class ProfessionCommand extends RCCommand {
    private final Set<ProfessionSubCommand> subCommands = new HashSet<ProfessionSubCommand>();

    public ProfessionCommand(final RolecraftCore plugin) {
        super(plugin);

        // TODO: Add subcommands
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd,
            final String label, final String[] args) {
        if (args.length == 0) {
            CommandHelper.displayCommandList(sender,
                    new ArrayList<RCSubCommand>(subCommands), null, 0);
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                CommandHelper.displayCommandList(sender,
                        new ArrayList<RCSubCommand>(subCommands), args, 1);
            } else {
                ProfessionSubCommand subCommand = null;
                for (final ProfessionSubCommand sub : subCommands) {
                    for (final String name : sub.getNames()) {
                        if (name.equalsIgnoreCase(args[0])) {
                            subCommand = sub;
                            break;
                        }
                    }
                }

                if (subCommand != null) {
                    if (sender.hasPermission(subCommand.getPermission())
                            || sender instanceof ConsoleCommandSender) {
                        subCommand.execute(sender, args);
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED
                                + "You don't have permission to do that!");
                    }
                } else {
                    sender.sendMessage(ChatColor.DARK_RED
                            + "That subcommand doesn't exist!");
                }
            }
        }

        return true;
    }

    @Override
    public String[] getNames() {
        return new String[] { "profession", "prof" };
    }

    public void registerSubCommand(final ProfessionSubCommand command) {
        subCommands.add(command);
    }
}
