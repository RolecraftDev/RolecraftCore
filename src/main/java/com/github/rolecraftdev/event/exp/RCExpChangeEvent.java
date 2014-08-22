package com.github.rolecraftdev.event.exp;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.github.rolecraftdev.RolecraftCore;

public class RCExpChangeEvent extends RCExpEvent {
    
    private float amount;
    
    private final int level;
    
    private final float experience;

    private static HandlerList handlers;
    
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
