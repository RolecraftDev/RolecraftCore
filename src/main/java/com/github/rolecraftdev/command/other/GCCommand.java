package com.github.rolecraftdev.command.other;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.RCCommand;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class GCCommand extends RCCommand {
    public GCCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd,
            final String lbl, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Only players can chat!");
            return true;
        }

        final UUID player = ((Player) sender).getUniqueId();
        final GuildManager manager = plugin.getGuildManager();
        final Guild guild = manager.getPlayerGuild(player);

        if (guild == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return true;
        }

        if (args.length == 0) {
            // TODO: Set player chat channel
        } else {
            // TODO: Send one off chat message to guild channel
        }

        return true;
    }

    @Override
    public String[] getNames() {
        return new String[] { "gc", "guildchat", "gchat" };
    }
}
