package com.github.rolecraftdev.event.exp;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.util.LevelUtil;

public class RCLevelChangeEvent extends RCExpChangeEvent {

    private static final HandlerList handlers = new HandlerList();
    
    private int newLevel;

    /**
     * Called whenever a player's level is anticipated to change, keep in mind that if a 
     * plugin alters the experience granted, getNewLevel() may equal getLevel()
     * 
     * @param plugin
     * @param player
     * @param amount
     * @param reason
     */
    protected RCLevelChangeEvent(RolecraftCore plugin, Player player, float amount, ChangeReason reason) {
        super(plugin, player, amount, reason);
        newLevel = LevelUtil.getLevel(this.getNewExperience());
    }
    
    @Override
    public void setAmount(float experience) {
        super.setAmount(experience);
        newLevel = LevelUtil.getLevel(this.getNewExperience());
    }
    
    public int getNewLevel() {
        return newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
