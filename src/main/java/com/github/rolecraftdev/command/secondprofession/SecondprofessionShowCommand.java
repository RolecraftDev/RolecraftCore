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
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @since 0.1.0
 */
public final class SecondprofessionShowCommand extends PlayerCommandHandler {
    private final ProfessionManager professionManager;

    /**
     * @since 0.1.0
     */
    public SecondprofessionShowCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "show");
        professionManager = plugin.getProfessionManager();

        setUsage("/secondprofession show");
        setDescription("View information about your second profession");
        setSubcommand(true);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        // show information for the player's current profession if they have one
        final UUID playerId = player.getUniqueId();
        final Profession profession = professionManager
                .getPlayerSecondProfession(playerId);

        if (profession == null) {
            player.sendMessage(plugin.getMessage(Messages.NO_SECOND_PROFESSION));
            return;
        }

        CommandHelper.showProfessionInformation(plugin, player, profession);
    }
}
