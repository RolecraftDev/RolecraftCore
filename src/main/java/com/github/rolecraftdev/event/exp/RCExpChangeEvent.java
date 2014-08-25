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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * A {@link RCExpEvent} that gets called when a player's experience level is
 * altered.
 *
 * @since 0.0.5
 */
public class RCExpChangeEvent extends RCExpEvent {
    private static final HandlerList handlers = new HandlerList();

    private final int level;
    private final float experience;
    private final ChangeReason reason;

    private float amount;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param player the affected player
     * @param amount the amount that is added to the player's experience
     * @param reason the reason for this change
     * @since 0.0.5
     */
    protected RCExpChangeEvent(RolecraftCore plugin, Player player,
            float amount, ChangeReason reason) {
        super(plugin, player);
        this.amount = amount;
        this.reason = reason;

        level = plugin.getDataManager().getPlayerData(player.getUniqueId())
                .getLevel();
        experience = plugin.getDataManager().getPlayerData(player.getUniqueId())
                .getExperience();
    }

    /**
     * Get the reason for this experience change.
     *
     * @return the reason
     * @since 0.0.5
     */
    public ChangeReason getReason() {
        return reason;
    }

    /**
     * Obtain the amount that is added to the player's current experience.
     *
     * @return the experience that is added
     * @since 0.0.5
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Set the amount of experience that is to be added or subtracted from the
     * player's experience. A negative amount means that the player will lose
     * experience.
     *
     * @param newAmount the new additional amount
     * @since 0.0.5
     */
    public void setAmount(float newAmount) {
        amount = newAmount;
    }

    /**
     * Get the level of the player before the addition or removal will take
     * place.
     *
     * @return the player's current level
     * @since 0.0.5
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the experience of the player before the addition or removal will take
     * place.
     *
     * @return the player's current experience
     * @since 0.0.5
     */
    public float getExperience() {
        return experience;
    }

    /**
     * Get the amount of experience the player will have after the additional
     * experience is included.
     *
     * @return the player's new experience amount
     * @since 0.0.5
     */
    public float getNewExperience() {
        return experience + amount;
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
