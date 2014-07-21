package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildHomeCommand extends GuildSubCommand {
    private final GuildManager guildManager;

    public GuildHomeCommand(final RolecraftCore plugin) {
        super(plugin);

        guildManager = plugin.getGuildManager();
    }

    @Override
    public void execute(final CommandSender sender,
            final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "Only players can teleport!");
        }

        final Player player = (Player) sender;
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);
        if (guild != null) {
            guild.teleportToHome(player);
            sender.sendMessage(ChatColor.GRAY + "Teleporting to guild home!");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "home" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.home";
    }

    @Override
    public String getUsage() {
        return "/guild home";
    }

    @Override
    public String getDescription() {
        return "Teleports to the guild's home";
    }
}
