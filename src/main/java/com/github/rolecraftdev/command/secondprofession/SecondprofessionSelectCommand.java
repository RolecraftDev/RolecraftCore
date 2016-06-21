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
package com.github.rolecraftdev.command.secondprofession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.profession.secondary.PlayerSecondProfessionSelectEvent;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @since 0.1.0
 */
public class SecondprofessionSelectCommand extends PlayerCommandHandler {
    private final ProfessionManager professionMgr;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.1.0
     */
    public SecondprofessionSelectCommand(final RolecraftCore plugin) {
        super(plugin, "select");
        professionMgr = plugin.getProfessionManager();

        setUsage("/secondprofession select <profession>");
        setDescription("Selects a second profession");
        setPermission("rolecraft.profession.secondary");
        setSubcommand(true);
    }

    /**
     * @since 0.1.0
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() < 1) {
            sendUsageMessage(player);
            return;
        }

        final Profession profession = professionMgr
                .getProfession(args.getRaw(0));
        if (profession == null) {
            player.sendMessage(plugin.getMessage(
                    Messages.PROFESSION_NOT_EXISTS));
            return;
        }

        final UUID playerId = player.getUniqueId();
        final PlayerData data = professionMgr.getPlugin().getDataManager()
                .getPlayerData(playerId);

        if (data.getSecondProfession() != null) {
            player.sendMessage(plugin.getMessage(
                    Messages.SECOND_PROFESSION_ALREADY_SELECTED));
            return;
        }
        if (!player.hasPermission("rolecraft.profession.secondary") ||
                !player.hasPermission("rolecraft.profession." + profession
                .getName())) {
            player.sendMessage(plugin.getMessage(Messages.PROFESSION_NO_PERMS,
                    MessageVariable.PROFESSION.value(profession.getName())));
            return;
        }

        final PlayerSecondProfessionSelectEvent event = RolecraftEventFactory
                .secondProfessionSelected(profession, player);
        if (event.isCancelled()) {
            player.sendMessage(event.getCancelMessage());
        } else {
            data.setSecondProfession(profession.getId());
            player.sendMessage(plugin.getMessage(
                    Messages.SECOND_PROFESSION_SELECTED,
                    MessageVariable.PROFESSION.value(profession.getName())));
        }
    }
}
