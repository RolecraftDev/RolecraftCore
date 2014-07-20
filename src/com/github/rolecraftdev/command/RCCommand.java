package com.github.rolecraftdev.command;

import org.bukkit.command.CommandExecutor;

public abstract class RCCommand implements CommandExecutor {
    public abstract String[] getNames();
}
