package com.github.rolecraftdev.magic.spell;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

public interface Spell {
    
    public String getName();
    
    
    /**
     * Used to test if a player has enough mana to perform this spell
     * 
     * @param ply The 
     * @param entity
     * @param modifier
     * @return Should return the same as attack(Player, LivingEntity, int),
     *  given the same values, without preforming the action
     */
    public float estimateAttackMana(Player ply, LivingEntity entity, int modifier);
    
    /**
     * Used to test if a player has enough mana to perform this spell
     * 
     * @param ply The 
     * @param block
     * @param modifier
     * @return Should return the same as leftClick(Player, Block, int),
     *  given the same values, without preforming the action
     */
    public float estimateLeftClickMana(Player ply, Block block, int modifier);
    
    /**
     * Used to test if a player has enough mana to perform this spell
     * 
     * @param ply The 
     * @param block
     * @param modifier
     * @return Should return the same as rightClick(Player, Block, int),
     *  given the same values, without preforming the action
     */
    public float estimateRightClickMana(Player ply, Block block, int modifier);
    
    /**
     * 
     * 
     * @param ply The player that cast the spell
     * @param block The block that was interacted with, if air, null
     * @param modifier A modifier based on a player's profession
     * @return the cost in mana
     */
    public float rightClick(Player ply, Block block, int modifier);
    
    /**
     * 
     * 
     * @param ply The player that cast the spell
     * @param block The block that was interacted with, if air, null
     * @param modifier A modifier based on a player's profession
     * @return the cost in mana
     */
    public float leftClick(Player ply, Block block, int modifier); 
    
    public float attack(Player ply, LivingEntity ent, int modifier);
    
    /**
     * Used when creating wands to cast this spell
     * 
     * @return a recipe for crafting
     */
    public Recipe getWandRecipe();

}
