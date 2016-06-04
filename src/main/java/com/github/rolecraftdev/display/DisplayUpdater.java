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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Regenerates the mana on all available {@link PlayerData} and updates the
 * mana-showing scoreboards of all of them.
 *
 * @since 0.0.5
 */
public class DisplayUpdater {
    /**
     * The name of the data objective.
     */
    public static final String DATA = "Data";

    public static final String MANA = "Mana";
    public static final String LEVEL = "Level";
    public static final String REQUIRED_EXP = "LevelUp";

    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * A {@link Map} of player {@link UUID}s to the display associated with that
     * player.
     */
    private final Map<UUID, Objective> displays;

    /**
     * Create an updater used for magic mana. This will automatically cause
     * magic mana to be regenerated in a cycle.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public DisplayUpdater(final RolecraftCore plugin) {
        this.plugin = plugin;
        displays = new HashMap<UUID, Objective>();

        new DisplayUpdateTask(this).runTaskTimer(plugin, 20L, 40L);
        plugin.getServer().getPluginManager()
                .registerEvents(new DisplayListener(plugin), plugin);
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public Objective getDisplay(@Nonnull final UUID playerId) {
        return this.displays.get(playerId);
    }

    /**
     * Creates a new mana-scoreboard and objective, which are together referred
     * to as a mana-display, for the specified player.
     *
     * @param player the player for which a new mana-display should be created
     * @since 0.0.5
     */
    public void createDisplayFor(final Player player) {
        final Scoreboard scoreboard = Bukkit.getScoreboardManager()
                .getNewScoreboard();

        final Objective display = scoreboard
                .registerNewObjective(DATA, "dummy");
        display.setDisplaySlot(DisplaySlot.SIDEBAR);

        displays.put(player.getUniqueId(), display);
        player.setScoreboard(scoreboard);
    }

    /**
     * Remove the mana-display of a player. Note that this will solely remove
     * the used objects from the backing map and thus, will <strong>not</strong>
     * alter scoreboard visibility.
     *
     * @param player
     * @since 0.0.5
     */
    public void disposeDisplayOf(final Player player) {
        displays.remove(player.getUniqueId());
    }

}
