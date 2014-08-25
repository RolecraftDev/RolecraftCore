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
import com.github.rolecraftdev.event.RolecraftEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * A {@link RolecraftEvent} that is called when a player's experience is somehow
 * affected.
 *
 * @since 0.0.5
 */
public abstract class RCExpEvent extends RolecraftEvent implements Cancellable {
    /**
     * A reason as to why the experience of a player is affected.
     *
     * @since 0.0.5
     */
    public enum ChangeReason {
        /**
         * Awarded for killing something.
         *
         * @since 0.0.5
         */
        KILLING,
        /**
         * Deducted for dying.
         *
         * @since 0.0.5
         */
        DEATH,
        /**
         * Changed by a plugin other than Rolecraft.
         *
         * @since 0.0.5
         */
        CUSTOM,
        /**
         * Harvesting crops/breeding animals.
         *
         * @since 0.0.5
         */
        HARVEST,
        /**
         * Done via command.
         *
         * @since 0.0.5
         */
        COMMAND,
        /**
         * Defaults to this when nothing is specified.
         *
         * @since 0.0.5
         * @deprecated a reason is required
         */
        DEFAULT
    }

    private final Player concern;

    private boolean cancelled;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param player the affected player
     * @since 0.0.5
     */
    protected RCExpEvent(RolecraftCore plugin, Player player) {
        super(plugin);
        concern = player;
    }

    /**
     * Get the affected player.
     *
     * @return the affected player
     * @since 0.0.5
     */
    public final Player getPlayer() {
        return concern;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
