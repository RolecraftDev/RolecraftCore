package com.github.rolecraftdev.command;

import org.bukkit.command.CommandSender;

public abstract class RCSubCommand {
    public abstract void execute(final CommandSender sender, final String[] args);

    public abstract String[] getNames();

    public abstract String getPermission();

    public abstract String getUsage();

    public abstract String getDescription();
}
