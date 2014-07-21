package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.RCSubCommand;
import com.github.rolecraftdev.guild.GuildManager;

public abstract class GuildSubCommand extends RCSubCommand {
    protected final GuildManager guildManager;

    protected GuildSubCommand(final RolecraftCore plugin) {
        super(plugin);

        guildManager = plugin.getGuildManager();
    }
}
