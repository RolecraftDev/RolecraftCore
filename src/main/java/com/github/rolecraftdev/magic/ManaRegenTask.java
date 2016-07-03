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
package com.github.rolecraftdev.magic;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * Automatically regenerates player mana at the specified configuration rate.
 *
 * @since 0.0.5
 */
public class ManaRegenTask extends BukkitRunnable {
    private final RolecraftCore plugin;
    private final float maximumMana;

    /**
     * @since 0.0.5
     */
    public ManaRegenTask(final RolecraftCore plugin) {
        this.plugin = plugin;
        this.maximumMana = plugin.getConfigValues().getMaximumMana();
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void run() {
        for (final PlayerData data : plugin.getDataManager().getPlayerDatum()) {
            if (plugin.getSpellManager().canCast(
                    plugin.getServer().getPlayer(data.getPlayerId()))) {
                data.addMana(data.getManaRegenRate());
                // do not exceed maximum player mana
                if (data.getMana() > maximumMana) {
                    data.setMana(maximumMana);
                }
            }
        }
    }
}
