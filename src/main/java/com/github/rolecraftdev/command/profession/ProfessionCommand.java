package com.github.rolecraftdev.command.profession;

import pw.ian.albkit.command.TreeCommandHandler;

import com.github.rolecraftdev.RolecraftCore;

public class ProfessionCommand extends TreeCommandHandler {
    private final RolecraftCore plugin;

    public ProfessionCommand(final RolecraftCore plugin, final String name) {
        super(plugin, name);
        this.plugin = plugin;

        setUsage("/profession list [page]");
        setDescription("View a list of permissions at the given page");
        setPermission("rolecraft.profession.list");
    }

    @Override
    public void setupSubcommands() {
        addSubcommand("list", new ProfessionListCommand(plugin));
        // TODO more
    }
}
