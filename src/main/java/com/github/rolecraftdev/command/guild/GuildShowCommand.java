package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class GuildShowCommand extends GuildSubCommand {
    public GuildShowCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        final Guild guild;
        if (args.length > 1) {
            final String name = args[1];
            guild = guildManager.getGuild(name);
            if (guild == null) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "That guild doesn't exist!");
            }
        } else {
            if (sender instanceof Player) {
                guild = guildManager
                        .getPlayerGuild(((Player) sender).getUniqueId());
                if (guild == null) {
                    sender.sendMessage(
                            ChatColor.DARK_RED + "You don't have a guild!");
                }
            } else {
                guild = null;
                sender.sendMessage(ChatColor.DARK_RED
                        + "Invalid syntax, /guild show <name>");
            }
        }

        if (guild != null) {
            sender.sendMessage(ChatColor.GOLD + guild.getName());
            sender.sendMessage(
                    ChatColor.GOLD + "Members: " + guild.getMembers().size());
            sender.sendMessage(
                    ChatColor.GOLD + "Officers: " + guild.getOfficers().size());
            sender.sendMessage(ChatColor.GOLD + "Leader: " + plugin.getServer()
                    .getPlayer(guild.getLeader()).getName());
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "show", "details", "info" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.show";
    }

    @Override
    public String getUsage() {
        return "/guild show <guild>";
    }

    @Override
    public String getDescription() {
        return "Shows information for the given guild";
    }
}
