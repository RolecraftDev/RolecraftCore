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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.profession.ProfessionRule;
import com.github.rolecraftdev.profession.ProfessionRuleMap;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @since 0.0.5
 */
public final class ProfessionShowCommand extends PlayerCommandHandler {
    private final ProfessionManager professionManager;

    public ProfessionShowCommand(@Nonnull final RolecraftCore plugin) {
        super(plugin, "show");
        professionManager = plugin.getProfessionManager();

        setUsage("/profession show [profession]");
        setDescription(
                "View information about the given profession, or your profession if no profession is specified");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            // show information for the player's current profession if they have one
            final UUID playerId = player.getUniqueId();
            final Profession profession = professionManager
                    .getPlayerProfession(playerId);

            if (profession == null) {
                // TODO: add to messages system
                player.sendMessage("You don't have a profession!");
                return;
            }

            doShowProfessionInformation(player, profession);
        } else {
            // show information for the given profession
            final String professionName = args.getRaw(0);
            final Profession profession = professionManager
                    .getProfession(professionName);

            if (profession == null) {
                player.sendMessage(plugin.getMessage(
                        Messages.PROFESSION_NOT_EXISTS));
                return;
            }

            doShowProfessionInformation(player, profession);
        }
    }

    private void doShowProfessionInformation(@Nonnull final Player player,
            @Nonnull final Profession profession) {
        final ProfessionRuleMap ruleMap = profession.getRuleMap();
        final List<String> lines = new ArrayList<String>();
        for (final ProfessionRule<?> rule : ruleMap.getRuleKeys()) {
            final String name = rule.getName();
            final StringBuilder line = new StringBuilder();
            final Object value = ruleMap.get(rule);

            if (name.equals("usable-spells")) {
                final List list = (List) value;
                line.append("Usable Spells: ");
                // TODO: add to messages system

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-armor")) {
                final List list = (List) value;
                line.append("Usable Armor: ");
                // TODO: add to messages system

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-enchantments")) {
                final List list = (List) value;
                line.append("Usable Enchantments: ");
                // TODO: add to messages system

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            } else if (name.equals("usable-items")) {
                final List list = (List) value;
                line.append("Usable Items: ");
                // TODO: add to messages system

                for (final Object obj : list) {
                    line.append(obj.toString()).append(", ");
                }

                line.setLength(line.length() - 2); // remove final ", "
            }

            lines.add(line.toString());
        }

        CommandHelper.sendBanner(player, "Profession: " + profession.getName(),
                lines.toArray());
    }
}
