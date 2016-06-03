package com.github.rolecraftdev.command;

import com.github.rolecraftdev.command.parser.Arguments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TreeCommandHandler extends CommandHandler {
    private final Map<String, CommandHandler> subcommands = new HashMap<String, CommandHandler>();

    public TreeCommandHandler(String name) {
        super(name);
    }

    public TreeCommandHandler(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract void setupSubcommands();

    public void onCommandNoArgs(CommandSender sender) {
        sendHelpMenu(sender);
    }

    public void onCommandInvalidArgs(CommandSender sender) {
        sendHelpMenu(sender);
    }

    public void sendHelpMenu(CommandSender sender) {
        List<CommandHandler> cmds = new ArrayList<CommandHandler>(subcommands.values());
        Collections.sort(cmds, new Comparator<CommandHandler>() {
            @Override
            public int compare(CommandHandler t, CommandHandler t1) {
                return t.getName().compareToIgnoreCase(t1.getName());
            }
        });

        List<String> msgs = new ArrayList<String>();

        for (CommandHandler handler : cmds) {
            if (handler.getPermission() == null
                    || sender.hasPermission(handler.getPermission())) {
                msgs.add(ChatColor.GREEN + "/" + getName() + " " + handler
                        .getName() + " - "
                        + ChatColor.YELLOW + handler.getDescription());
            }
        }

        CommandHelper.sendBanner(sender, msgs.toArray());
    }

    protected void addSubcommand(CommandHandler handler) {
        addSubcommand(handler.getName(), handler);
    }

    protected void addSubcommand(String name, CommandHandler handler) {
        subcommands.put(handler.getName(), handler);
    }

    @Override
    public void onCommand(CommandSender sender, Arguments args) {
        if (args.length() == 0) {
            onCommandNoArgs(sender);
            return;
        }

        CommandHandler handler = subcommands.get(args.getRaw(0));
        if (handler != null) {
            Arguments newArgs = new Arguments(Arrays.copyOfRange
                    (args.toStringArray(), 1, args.length()));
            newArgs.withParams(handler.getParamsBase().createParams(newArgs));
            if (doesValidateUsage() && !newArgs.getParams().valid()) {
                sender.sendMessage(ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
            handler.onCommand(sender, newArgs);
            return;
        }

        onCommandInvalidArgs(sender);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            onCommandNoArgs(sender);
            return;
        }

        CommandHandler handler = subcommands.get(args[0]);
        if (handler != null) {
            handler.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            return;
        }

        onCommandInvalidArgs(sender);
    }
}
