package com.github.rolecraftdev.magic.spell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class BurnBlock implements Spell {

    private SpellManager parent;

    public BurnBlock(SpellManager spellManager) {
        this.parent= spellManager;
    }

    @Override
    public String getName() {
        return "Burn Block";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        return 5;
    }

    @SuppressWarnings("deprecation")
    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        Block toIgnite = ply.getLastTwoTargetBlocks(null, parent.getRange()).get(0);
        BlockState state = block.getState();
        block.setType(Material.FIRE);
        new BlockPlaceEvent(toIgnite, state, block, null, ply, true);
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
        recipe.shape("NNI","NIN","INN");
        recipe.setIngredient('N', Material.FLINT_AND_STEEL);
        recipe.setIngredient('I', Material.IRON_INGOT);
        
        return recipe;
    }

}
