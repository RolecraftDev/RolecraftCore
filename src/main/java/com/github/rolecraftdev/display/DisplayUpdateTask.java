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
package com.github.rolecraftdev.display;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

import java.util.UUID;

/**
 * The task used to regenerate magic mana and update all available
 * mana-displays.
 *
 * @since 0.0.5
 */
public class DisplayUpdateTask extends BukkitRunnable {
    private final DisplayUpdater displayUpdater;
    private final RolecraftCore plugin;

    /**
     * @since 0.0.5
     */
    public DisplayUpdateTask(DisplayUpdater displayUpdater) {
        this.displayUpdater = displayUpdater;
        this.plugin = displayUpdater.getPlugin();
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void run() {
        for (final PlayerData data : plugin.getDataManager()
                .getPlayerDatum()) {
            final UUID playerId = data.getPlayerId();
            final Objective display = displayUpdater.getDisplay(playerId);
            if (display != null) {
                display.getScore(DisplayUpdater.LEVEL)
                        .setScore(data.getLevel());
                display.getScore(DisplayUpdater.REQUIRED_EXP)
                        .setScore((int) data.getExpToNextLevel());
                display.getScore(DisplayUpdater.MANA)
                        .setScore((int) data.getMana());
            }
        }
    }
}
