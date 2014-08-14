package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GuildListCommand extends CommandHandler {
    private final GuildManager guildMgr;

    GuildListCommand(final RolecraftCore plugin) {
        super("list");
        guildMgr = plugin.getGuildManager();

        setDescription("List all of the guilds available");
        setPermission("rolecraft.guild.show");
        setUsage("/guild list [page]");
    }

    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Set<Guild> guilds = guildMgr.getGuilds();
        if (guilds == null) {
            sender.sendMessage(ChatColor.DARK_RED +
                    "The plugin hasn't loaded yet!");
            return;
        }

        final List<Guild> onPage = CommandHelper
                .getPageFromArgs(sender, new ArrayList<Guild>(guilds),
                        args.length() > 0 ? args.get(0) : null, 6);
        if (onPage == null) {
            // No need to send an error message - getPageFromArgs does that for
            // us already, so we can just return here.
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Guilds");
        for (final Guild guild : onPage) {
            sender.sendMessage(ChatColor.GRAY + guild.getName());
        }
    }
}
