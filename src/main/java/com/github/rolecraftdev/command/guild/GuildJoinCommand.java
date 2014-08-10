package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GuildJoinCommand extends PlayerCommandHandler {
    private final GuildManager guildMgr;

    public GuildJoinCommand(final RolecraftCore plugin) {
        super("join");
        guildMgr = plugin.getGuildManager();

        setUsage("/guild join <name>");
        setDescription("Joins the given guild if you are invited");
        setPermission("rolecraft.guild.join");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 1) {
            sendUsageMessage(player);
            return;
        }
        if (!player.hasMetadata(GuildManager.GUILD_INVITE_METADATA)) {
            player.sendMessage(ChatColor.DARK_RED +
                    "You aren't invited to that guild!");
            return;
        }
        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (!(val instanceof FixedMetadataValue)) {
            player.sendMessage(ChatColor.DARK_RED +
                    "You aren't invited to that guild!");
            return;
        }

        final FixedMetadataValue fixed = (FixedMetadataValue) val;
        final String name = args.getRaw(1);
        final Guild guild = guildMgr.getGuild(name);
        if (fixed.asString().equalsIgnoreCase(guild.getId().toString())) {
            guild.addMember(player.getUniqueId(), guild.getDefaultRank());
            player.sendMessage(ChatColor.GRAY +
                    "You joined " + guild.getName());
            guild.broadcastMessage(ChatColor.GRAY + player.getName()
                    + " has joined the guild!");
        } else {
            player.sendMessage(ChatColor.DARK_RED +
                    "You aren't invited to that guild!");
        }
    }
}
