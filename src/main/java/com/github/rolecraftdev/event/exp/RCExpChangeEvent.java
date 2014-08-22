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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.github.rolecraftdev.RolecraftCore;

public class RCExpChangeEvent extends RCExpEvent {
    
    private float amount;
    
    private final int level;
    
    private final float experience;

    private static final HandlerList handlers = new HandlerList();
    
    private final ChangeReason reason;

    protected RCExpChangeEvent(RolecraftCore plugin, Player player, float amount, ChangeReason reason) {
        super(plugin, player);
        this.amount = amount;
        this.reason = reason;
        
        level = plugin.getDataManager().getPlayerData(player.getUniqueId()).getLevel();
        experience = plugin.getDataManager().getPlayerData(player.getUniqueId()).getExperience();
        
    }
    
    public ChangeReason getReason() {
        return reason;
    }
    
    public float getAmount() {
        return amount;
    }
    
    public void setAmount(float newAmount) {
        amount = newAmount;
    }
    
    /**
     * @return The player's level before the addition or removal of experience
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * @return The player's experience before the addition or removal of experience
     */
    public float getExperience() {
        return experience;
    }
    
    /**
     * @return The player's new experience amount
     */
    public float getNewExperience () {
        return experience + amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
