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
package com.github.rolecraftdev.event.exp;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.util.LevelUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * A {@link RCExpChangeEvent} that gets called when a player's level is
 * anticipated to change.
 *
 * @since 0.0.5
 */
public class RCLevelChangeEvent extends RCExpChangeEvent {
    private static final HandlerList handlers = new HandlerList();

    private int newLevel;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param player the affected player
     * @param amount the amount that is added to the player's experience
     * @param reason the reason for this change
     * @since 0.0.5
     */
    public RCLevelChangeEvent(final RolecraftCore plugin,
            final Player player, final float amount,
            final ChangeReason reason) {
        super(plugin, player, amount, reason);
        newLevel = LevelUtil.getLevel(getNewExperience());
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void setAmount(final float experience) {
        super.setAmount(experience);
        newLevel = LevelUtil.getLevel(getNewExperience());
    }

    /**
     * Get the level the player will have after the additional experience is
     * included.
     *
     * @return the player's new level
     * @since 0.0.5
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 0.0.5
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
