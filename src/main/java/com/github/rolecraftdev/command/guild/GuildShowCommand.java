package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.command.CommandSender;

public class GuildShowCommand extends CommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    public GuildShowCommand(final RolecraftCore plugin) {
        super(plugin, "show");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild show <name>");
        setDescription("Shows info about a guild");
        setPermission("rolecraft.guild.show");
    }

    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Guild guild = CommandHelper.getGuildFromArgs(guildManager, sender,
                args.length() > 1 ? args.getArgument(1).rawString() : null);

        if (guild != null) {
            // TODO: Show information about the guild
        }
    }
}
