package com.github.rolecraftdev.command.profession;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ProfessionSelectCommand extends PlayerCommandHandler {
    private final ProfessionManager professionMgr;

    public ProfessionSelectCommand(final RolecraftCore plugin) {
        super("select");
        professionMgr = plugin.getProfessionManager();
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() < 2) {
            sendUsageMessage(player);
            return;
        }

        final Profession profession = professionMgr
                .getProfession(args.getArgument(1).rawString());
        if (profession == null) {
            player.sendMessage(
                    ChatColor.DARK_RED + "That profession doesn't exist!");
            return;
        }

        final UUID playerId = player.getUniqueId();
        final PlayerData data = professionMgr.getPlugin().getDataManager()
                .getPlayerData(playerId);
        data.setProfession(profession.getId());
        player.sendMessage(ChatColor.GRAY +
                "You've joined the " + profession.getName() + " profession!");
    }
}
