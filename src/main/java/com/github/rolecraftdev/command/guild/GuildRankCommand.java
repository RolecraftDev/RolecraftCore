package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class GuildRankCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildManager;

    public GuildRankCommand(final RolecraftCore plugin) {
        super(plugin, "rank");
        this.plugin = plugin;
        guildManager = plugin.getGuildManager();

        setUsage("/guild rank <rank> <new/delete/set> <args>");
        setDescription("Allows modification of guild ranks");
        setPermission("rolecraft.guild.create");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        final Guild guild = guildManager.getPlayerGuild(id);

        if (guild == null) {
            // The player doesn't have a guild
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }
        if (!guild.getLeader().equals(id)) {
            // The player isn't the leader of the guild and therefore cannot
            // modify ranks
            player.sendMessage(ChatColor.DARK_RED
                    + "You must be guild leader to do that!");
            return;
        }
        if (args.length() == 1) {
            // Send usage string
            player.sendMessage(ChatColor.DARK_RED + getUsage());
            return;
        }

        final String rankArg = args.getArgument(1).rawString();
        final GuildRank rank = guild.getRank(rankArg);
        if (rank == null) { // The rank doesn't exist
            if (args.length() < 3 || !isCreateAlias(
                    args.getArgument(2).rawString().toLowerCase())) {
                // The sender has entered a non-existent rank within their guild
                player.sendMessage(
                        ChatColor.DARK_RED + "That rank doesn't exist!");
            } else { // There are 3+ args & the next is an alias of 'create'
                // Only returns false if the rank already exists
                if (guild.addRank(new GuildRank(rankArg,
                        new HashSet<GuildAction>(), new HashSet<UUID>()))) {
                    // Notify the sender that the rank was created
                    player.sendMessage(
                            ChatColor.GRAY + "Created the rank: " + rankArg);
                } else {
                    // Notify the sender that the rank already exists
                    player.sendMessage(ChatColor.DARK_RED +
                            "That rank already exists!");
                }
            }

            return;
        }

        if (args.length() == 2) { // I.E the input is '/guild rank <name>'
            // Send information about the given rank
            CommandHelper.sendRankInfo(player, guild, rank);
            return;
        }

        final String command = args.getArgument(2).rawString().toLowerCase();
        if (isDeleteAlias(command)) {
            // Returns false if the rank is leader or default
            if (guild.removeRank(rank)) {
                // Alert the sender that the rank was removed
                player.sendMessage(ChatColor.GRAY +
                        "Removed the rank: " + rankArg);
            } else {
                // Alert the sender that the rank wasn't removed
                player.sendMessage(ChatColor.DARK_RED +
                        "Can't remove the " + rankArg + " rank!");
            }

            return;
        }

        if (command.equals("set") || command.equals("modify")) {
            if (args.length() < 5) {
                // Invalid syntax
                player.sendMessage(ChatColor.DARK_RED +
                        "Usage: /guild rank <rank> set <permission> <yes/no>");
                return;
            }

            final String permission = args.getArgument(3).rawString()
                    .toLowerCase();
            final String value = args.getArgument(4).rawString().toLowerCase();
            final GuildAction perm = GuildAction.fromHumanReadable(permission);

            if (perm == null) { // The entered permission doesn't exist
                player.sendMessage(ChatColor.DARK_RED +
                        "Invalid action: " + permission);
                return;
            }

            if (value.equals("yes") || value.equals("y") || value
                    .equals("true")) {
                // Set the value of the permission to true
                rank.allowAction(perm);
                player.sendMessage(ChatColor.GRAY +
                        "Set value for permission " + permission
                        + " to yes for rank" + rank.getName());
            } else if (value.equals("no") || value.equals("n") || value
                    .equals("false")) {
                // Leader must always have all permissions
                if (rank.getName().equals("Leader")) {
                    player.sendMessage(ChatColor.DARK_RED
                            + "Cannot remove permissions from the leader!");
                    return;
                }

                // Remove the permission from the rank + notify sender
                rank.disallowAction(perm);
                player.sendMessage(ChatColor.GRAY +
                        "Set value for permission " + permission
                        + " to no for rank " + rank.getName());
            } else {
                // Value isn't valid
                player.sendMessage(ChatColor.DARK_RED + value +
                        " isn't a valid value!");
            }

            return;
        }

        // Arguments didn't match any valid usage
        player.sendMessage(ChatColor.DARK_RED + "Invalid usage, " + getUsage());
    }

    private boolean isCreateAlias(final String arg) {
        return arg.equals("create") || arg.equals("new") || arg.equals("make")
                || arg.equals("add");
    }

    private boolean isDeleteAlias(final String arg) {
        return arg.equals("delete") || arg.equals("remove") || arg
                .equals("destroy");
    }
}