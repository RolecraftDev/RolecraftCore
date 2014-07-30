package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildHomeCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    public GuildHomeCommand(final RolecraftCore plugin) {
        super(plugin, "home");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild home");
        setDescription("Teleports to the guild home");
        setPermission("rolecraft.guild.home");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);
        if (guild != null) {
            guild.teleportToHome(player);
            player.sendMessage(ChatColor.GRAY + "Teleporting to guild home!");
        } else {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
        }
    }
}
