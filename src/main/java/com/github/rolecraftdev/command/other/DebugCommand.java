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
package com.github.rolecraftdev.command.other;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.magic.Spell;

import org.bukkit.entity.Player;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

/**
 * @since 0.0.5
 */
public class DebugCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public DebugCommand(final RolecraftCore plugin) {
        super("debug");
        setUsage("/debug wand");
        this.plugin = plugin;
    }

    /**
     * @since 0.0.5
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            return;
        }
        if (args.getRaw(0).equalsIgnoreCase("wand")) {
            for (final Spell spell : plugin.getSpellManager().getSpells()) {
                player.getInventory().addItem(
                        spell.getWandRecipe().getResult());
            }
            player.updateInventory();
        }
    }
}
