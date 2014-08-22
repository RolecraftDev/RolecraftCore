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
