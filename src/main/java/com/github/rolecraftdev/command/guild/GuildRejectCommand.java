package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class GuildRejectCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;
    private final GuildManager guildMgr;

    public GuildRejectCommand(final RolecraftCore plugin) {
        super("reject");
        this.plugin = plugin;
        guildMgr = plugin.getGuildManager();

        setPermission("rolecraft.guild.join");
        setDescription("Rejects an invitation to a guild");
        setUsage("/guild reject");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final MetadataValue val = player
                .getMetadata(GuildManager.GUILD_INVITE_METADATA).get(0);
        if (val == null || !(val instanceof FixedMetadataValue)) {
            player.sendMessage(ChatColor.DARK_RED
                    + "You don't have an invitation to a guild!");
            return;
        }

        player.removeMetadata(GuildManager.GUILD_INVITE_METADATA, plugin);
        player.sendMessage(ChatColor.GRAY +
                "Rejected your current guild invitation");
    }
}
