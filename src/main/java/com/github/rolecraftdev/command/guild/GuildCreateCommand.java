package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCreateCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    public GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin, "create");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild create <name>");
        setDescription("Create a guild");
        setPermission("rolecraft.guild.create");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 1) {
            player.sendMessage(ChatColor.DARK_RED
                    + "Invalid syntax, /guild create <name>");
            return;
        }
        final UUID playerId = player.getUniqueId();
        if (guildManager.getPlayerGuild(playerId) != null) {
            player.sendMessage(
                    ChatColor.DARK_RED + "You are already in a guild!");
            return;
        }
        if (plugin.useEconomy()) {
            final EconomyResponse response = plugin.getEconomy().bankHas(
                    player.getName(),
                    guildManager.getCreationCost());
            if (!response.transactionSuccess()) {
                player.sendMessage(
                        ChatColor.DARK_RED + "You can't afford to do that!");
                return;
            }
        }

        final String name = args.getArgument(1).rawString();
        final Guild guild = new Guild(guildManager);

        guild.setName(name);
        guild.setLeader(playerId);
        if (guildManager.addGuild(guild, false)) {
            GuildCreateEvent event = new GuildCreateEvent(plugin, guild);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                player.sendMessage(ChatColor.DARK_RED
                        + event.getCancelMessage());
                guildManager.removeGuild(guild);
            } else {
                player.sendMessage(ChatColor.GRAY
                        + "You created a guild named '" + name + "'!");

                if (plugin.useEconomy()) {
                    plugin.getEconomy().bankWithdraw(player.getName(),
                            guildManager.getCreationCost());
                }
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED
                    + "Couldn't create guild! The name may have already been taken!");
        }
    }
}
