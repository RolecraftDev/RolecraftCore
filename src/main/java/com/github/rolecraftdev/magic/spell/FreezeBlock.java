package com.github.rolecraftdev.magic.spell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class FreezeBlock implements Spell {

    public FreezeBlock(SpellManager spellManager) {
    }

    @Override
    public String getName() {
        return "Freeze Block";
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        return 5;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        if(block != null) {
            Cancellable bbe = new BlockBreakEvent(block, ply);
            if(!bbe.isCancelled()) {
                BlockState state = block.getState();
                block.setType(Material.ICE);
                Cancellable bpe = new BlockPlaceEvent(block, state, null, null, ply, true);
                if(bpe.isCancelled()) {
                    state.update();
                }
            }
        }
        return 5;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float attack(Player ply, LivingEntity ent, int modifier) {
        return 0;
    }

    @Override
    public Recipe getWandRecipe() {
        // same for each
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("SSI","SIS","ISS");
        recipe.setIngredient('S', Material.SNOW_BALL);
        recipe.setIngredient('I', Material.IRON_INGOT);
        
        return recipe;
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

}
