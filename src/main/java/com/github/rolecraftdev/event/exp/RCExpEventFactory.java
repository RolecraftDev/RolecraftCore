package com.github.rolecraftdev.event.exp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.exp.RCExpEvent.ChangeReason;
import com.github.rolecraftdev.util.LevelUtil;

public class RCExpEventFactory {

    private RCExpEventFactory() {
    }

    /**
     * Creates and calls the appropriate events given the input
     * 
     * @param plugin The RC Core to use
     * @param player The player to call the event on
     * @param amount The amount of experience to add
     * @param reason The reason to add or remove experience
     * @return The event created and called by this factory
     */
    public static RCExpChangeEvent callRCExpEvent(RolecraftCore plugin,
            Player player, float amount, ChangeReason reason) {
        RCExpChangeEvent temp;
        
        float experience = plugin.getDataManager().getPlayerData(player.getUniqueId()).getExperience();
        
        if(LevelUtil.getLevel(experience) !=
                LevelUtil.getLevel(experience+amount)) {
            temp = new RCLevelChangeEvent(plugin, player, experience, reason);
        } 
        else {
            temp = new RCExpChangeEvent(plugin, player, experience, reason);
        }
        
        Bukkit.getPluginManager().callEvent(temp);

        return temp;
    }

}
