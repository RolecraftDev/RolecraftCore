package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import org.bukkit.command.CommandSender;

public abstract class RCSubCommand {
    protected final RolecraftCore plugin;

    protected RCSubCommand(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    public abstract void execute(final CommandSender sender,
            final String[] args);

    public abstract String[] getNames();

    public abstract String getPermission();

    public abstract String getUsage();

    public abstract String getDescription();
}
