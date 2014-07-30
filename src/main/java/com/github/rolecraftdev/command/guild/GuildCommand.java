package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.TreeCommandHandler;

import com.github.rolecraftdev.RolecraftCore;

public class GuildCommand extends TreeCommandHandler {
    private final RolecraftCore plugin;

    public GuildCommand(final RolecraftCore plugin) {
        super(plugin, "guild");
        this.plugin = plugin;
    }

    @Override
    public void setupSubcommands() {
        addSubcommand("create", new GuildCreateCommand(plugin));
        addSubcommand("disband", new GuildDisbandCommand(plugin));
        addSubcommand("home", new GuildHomeCommand(plugin));
        addSubcommand("rank", new GuildRankCommand(plugin));
        addSubcommand("show", new GuildShowCommand(plugin));
        // TODO more
    }
}
