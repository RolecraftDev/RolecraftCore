package com.github.rolecraftdev.magic.spell;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class BreakBlock implements Spell {
    public BreakBlock(SpellManager spellManager) {
    }

    @Override
    public String getName() {
        return "Break Block";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        return 3;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        return 0;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier) {
        if (block != null) {
            BlockBreakEvent event = new BlockBreakEvent(block, ply);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                block.setType(Material.AIR);
            }
        }
        return 3;
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
        recipe.shape("IPI", "PBP", "IPI");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        recipe.setIngredient('B', Material.IRON_BLOCK);
        return recipe;
    }

}
