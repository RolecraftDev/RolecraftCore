package com.github.rolecraftdev.util;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Utils {

    public static Entity getTarget(Player player, int range) {

        Block[] bs = player.getLineOfSight(null, range).toArray(new Block[0]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block b : bs) {
            for (Entity e : near) {
                if (e.getLocation().distance(b.getLocation()) < 2) {
                    return e;
                }
            }
        }
        return null;
    }
    
    public static LivingEntity getLivingTarget(Player player, int range) {
        Block[] bs = player.getLineOfSight(null, range).toArray(new Block[0]);
        List<Entity> near = player.getNearbyEntities(range, range, range);
        for (Block b : bs) {
            for (Entity e : near) {
                if (e.getLocation().distance(b.getLocation()) < 2) {
                    if(e instanceof LivingEntity)
                        return (LivingEntity) e;
                }
            }
        }
        return null;
    }
}
