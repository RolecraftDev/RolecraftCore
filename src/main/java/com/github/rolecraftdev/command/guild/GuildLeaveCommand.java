package com.github.rolecraftdev.command.guild;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildLeaveCommand extends PlayerCommandHandler {
    private final GuildManager guildMgr;
    private final DataManager dataMgr;

    public GuildLeaveCommand(final RolecraftCore plugin) {
        super("leave");
        guildMgr = plugin.getGuildManager();
        dataMgr = plugin.getDataManager();

        setUsage("/guild leave");
        setDescription("Leave your guild");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID playerId = player.getUniqueId();
        final Guild guild = guildMgr.getPlayerGuild(playerId);
        if (guild == null) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have a guild!");
            return;
        }

        guild.removeMember(playerId, false);
        dataMgr.getPlayerData(playerId).setGuild(null);
    }
}
