package com.github.rolecraftdev.command.other;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GCCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    public GCCommand(final RolecraftCore plugin) {
        super(plugin, "gc");
        this.plugin = plugin;

        setUsage("/gc [message]");
        setDescription("Allows communicating in Guild chat");
        setPermission("rolecraft.guild.chat");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID playerId = player.getUniqueId();
        final GuildManager manager = plugin.getGuildManager();
        final Guild guild = manager.getPlayerGuild(playerId);

        if (guild == null) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }

        if (args.length() > 0) {
            // TODO: send one-off message to guild channel
        } else {
            // TODO: switch channels
        }
    }
}
