package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import org.bukkit.command.CommandExecutor;

public abstract class RCCommand implements CommandExecutor {
    protected final RolecraftCore plugin;

    protected RCCommand(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    public abstract String[] getNames();
}
