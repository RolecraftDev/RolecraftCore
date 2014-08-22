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
     * @param plugin
     * @param player
     * @param amount
     * @param reason
     * @return
     */
    public static RCExpEvent callRCExpEvent(RolecraftCore plugin,
            Player player, float amount, ChangeReason reason) {
        RCExpEvent temp = null;
        
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
