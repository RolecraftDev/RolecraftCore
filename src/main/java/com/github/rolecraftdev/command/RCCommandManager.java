package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.guild.GuildCommand;

public final class RCCommandManager {
    private final RolecraftCore plugin;

    public RCCommandManager(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        plugin.getCommand("guild").setExecutor(new GuildCommand(plugin));
    }
}
