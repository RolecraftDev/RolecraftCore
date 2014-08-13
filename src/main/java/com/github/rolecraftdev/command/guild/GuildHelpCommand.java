package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;
import pw.ian.albkit.util.Messaging;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.GuildAction;

import org.bukkit.entity.Player;

public class GuildHelpCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    public GuildHelpCommand(final RolecraftCore plugin) {
        super("help");
        this.plugin = plugin;

        setDescription("Guild-related help");
        setUsage("/guild help [args...]");
        setPermission("rolecraft.guild.join");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            Messaging.sendBanner(plugin.getColorScheme(), player,
                    "Guilds",
                    "/guild help actions - Help with guild actions",
                    "/guild help commands - Help with commands",
                    "/guild help halls - Help with guild halls");
            return;
        }

        final String sub = args.getRaw(0).toLowerCase();
        if (sub.equals("actions")) {
            final StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (final GuildAction action : GuildAction.values()) {
                if (!first) {
                    builder.append(", ");
                }
                first = false;
                builder.append(action.getPlayerReadableName());
            }
            Messaging.sendBanner(plugin.getColorScheme(), player,
                    "Actions:",
                    builder.toString());
        } else if (sub.equals("commands")) {
            // TODO: show command help
        } else if (sub.equals("halls")) {
            Messaging.sendBanner(plugin.getColorScheme(), player,
                    "Guild Halls:",
                    "Guild Halls are predefined areas which only members of the guild can access.",
                    "Guild Halls allow for battles between guilds, storage for items and more.");
        }
    }
}
