package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GuildBroadcastCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    public GuildBroadcastCommand(final RolecraftCore plugin) {
        super("broadcast");
        this.plugin = plugin;

        setUsage("/guild broadcast [-r rank] <message>");
        setDescription("Broadcast a message to the guild");
        setPermission("rolecraft.guild.join");
        setValidateUsage(false);
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final String message = CommandHelper.joinFrom(1, args);
        if (message == null) {
            sendUsageMessage(player);
            return;
        }

        final Guild guild = plugin.getGuildManager()
                .getPlayerGuild(player.getUniqueId());
        if (guild == null) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }
        if (guild.can(player.getUniqueId(), GuildAction.BROADCAST_MESSAGE)) {
            if (args.hasValueFlag("r")) {
                final GuildRank rank = guild
                        .getRank(args.getValueFlag("r").getRawValue());
                if (rank == null) {
                    player.sendMessage(
                            ChatColor.DARK_RED + "That rank doesn't exist!");
                    return;
                }

                rank.broadcastMessage(message);
            } else {
                guild.broadcastMessage(message);
            }
        }
    }
}
