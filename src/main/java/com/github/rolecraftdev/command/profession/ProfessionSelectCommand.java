/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
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

        setUsage("/profession select <profession>");
        setDescription("Selects a profession");
        setPermission("rolecraft.profession.use");
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

        if (data.getProfession() != null) {
            player.sendMessage(ChatColor.DARK_RED +
                    "You already have a profession!");
            return;
        }

        data.setProfession(profession.getId());
        player.sendMessage(ChatColor.GRAY +
                "You've joined the " + profession.getName() + " profession!");
    }
}
