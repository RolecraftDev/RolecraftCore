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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 0.0.5
 */
public abstract class TreeCommandHandler extends BaseCommandHandler {
    private final Map<String, BaseCommandHandler> subcommands = new HashMap<String, BaseCommandHandler>();

    /**
     * @since 0.0.5
     */
    public TreeCommandHandler(@Nonnull final RolecraftCore plugin,
            @Nonnull final String name) {
        super(plugin, name);
    }

    /**
     * @since 0.0.5
     */
    public abstract void setupSubcommands();

    /**
     * @since 0.0.5
     */
    public void onCommandNoArgs(@Nonnull final CommandSender sender) {
        sendHelpMenu(sender);
    }

    /**
     * @since 0.0.5
     */
    public void onCommandInvalidArgs(@Nonnull final CommandSender sender) {
        sendHelpMenu(sender);
    }

    /**
     * @since 0.0.5
     */
    public void sendHelpMenu(@Nonnull final CommandSender sender) {
        List<BaseCommandHandler> cmds = new ArrayList<BaseCommandHandler>(
                subcommands.values());
        Collections.sort(cmds, new Comparator<BaseCommandHandler>() {
            @Override
            public int compare(BaseCommandHandler t, BaseCommandHandler t1) {
                return t.getName().compareToIgnoreCase(t1.getName());
            }
        });

        List<String> msgs = new ArrayList<String>();

        for (BaseCommandHandler handler : cmds) {
            if (handler.getPermission() == null
                    || sender.hasPermission(handler.getPermission())) {
                msgs.add(ChatColor.GREEN + "/" + getName() + " " + handler
                        .getName() + " - "
                        + ChatColor.YELLOW + handler.getDescription());
            }
        }

        CommandHelper.sendBanner(sender, msgs.toArray());
    }

    /**
     * @since 0.0.5
     */
    protected void addSubcommand(@Nonnull final BaseCommandHandler handler) {
        addSubcommand(handler.getName(), handler);
    }

    /**
     * @since 0.0.5
     */
    protected void addSubcommand(@Nullable String name,
            @Nonnull final BaseCommandHandler handler) {
        if (name == null) {
            name = handler.getName();
        }
        subcommands.put(name, handler);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(@Nonnull final CommandSender sender,
            @Nonnull final Arguments args) {
        if (args.length() == 0) {
            onCommandNoArgs(sender);
            return;
        }

        final BaseCommandHandler handler = subcommands.get(args.getRaw(0));
        if (handler != null) {
            Arguments newArgs = new Arguments(Arrays.copyOfRange
                    (args.toStringArray(), 1, args.length()));
            newArgs.withParams(handler.getParamsBase()
                    .createParams(newArgs, isSubcommand()));
            if (doesValidateUsage() && !newArgs.getParams().valid()) {
                sender.sendMessage(
                        ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
            handler.onCommand(sender, newArgs);
            return;
        }

        onCommandInvalidArgs(sender);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(@Nonnull final CommandSender sender,
            @Nonnull final String[] args) {
        if (args.length == 0) {
            onCommandNoArgs(sender);
            return;
        }

        BaseCommandHandler handler = subcommands.get(args[0]);
        if (handler != null) {
            handler.onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
            return;
        }

        onCommandInvalidArgs(sender);
    }
}
