package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import org.bukkit.command.CommandSender;

public class GuildCreateCommand extends GuildSubCommand {
    public GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

    }

    @Override
    public String[] getNames() {
        return new String[] { "create", "new" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.create";
    }

    @Override
    public String getUsage() {
        return "/guild create <name>";
    }

    @Override
    public String getDescription() {
        return "Creates a new guild with the given name";
    }
}
