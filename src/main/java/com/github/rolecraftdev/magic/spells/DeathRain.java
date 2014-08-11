package com.github.rolecraftdev.magic.spells;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;

public class DeathRain implements Spell {

    private static HashSet<Byte> transparency;

    private SpellManager manager;

    static {
        transparency.add((byte) Material.GLASS.getId());
        transparency.add((byte) Material.STATIONARY_WATER.getId());
        transparency.add((byte) Material.WATER.getId());
    }

    public DeathRain(SpellManager spellManager) {
        manager = spellManager;
    }

    @Override
    public String getName() {
        return "Death Rain";
    }

    @Override
    public float estimateAttackMana(Player ply, LivingEntity entity,
            int modifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float estimateLeftClickMana(Player ply, Block block, int modifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float estimateRightClickMana(Player ply, Block block, int modifier) {
        return (800f - ((float) modifier) / 200f > 0) ? 800f - ((float) modifier) / 200f
                : 0;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        Block target = null;
        if (block != null) {
            target = block;
        }
        else {
            Block temp = ply.getTargetBlock(transparency, manager.getRange());
            if (temp != null) {
                target = temp;
            }
            else {
                return Float.MIN_VALUE;
            }
        }

        if (target.getWorld().getHighestBlockAt(target.getLocation()) != target) {
            ply.sendMessage("You must aim above ground to rain arrows!");
            return Float.MIN_VALUE;
        }

        Location center = new Location(target.getWorld(), target.getX(),
                target.getY() + 40, target.getZ());

        Vector velocity = target.getLocation().toVector()
                .subtract(center.toVector()).normalize().multiply(0.5d);
        World world = target.getWorld();

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z < 5; z++) {
                Arrow arrow = (Arrow) world.spawn(
                        new Location(world, center.getX() + x, center.getY(),
                                center.getZ() + z), Arrow.class);
                arrow.setMetadata("Multiplier", new FixedMetadataValue(manager.getPlugin(),new Float(4)));
                arrow.setMetadata("Explosion", new FixedMetadataValue(manager.getPlugin(), new Boolean(true)));
                arrow.setVelocity(velocity);
            }
        }

        return (800f - ((float) modifier) / 200f > 0) ?
                800f - ((float) modifier) / 200f : 0;
    }

    @Override
    public float leftClick(Player ply, Block block, int modifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float attack(Player ply, LivingEntity ent, int modifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Recipe getWandRecipe() {
        // same for each
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + getName());
        meta.addEnchant(Enchantment.LUCK, 10, true);
        String[] lore = {
                "A virtual airstrike, this wand is capabale of bringing an",
                "army to their knees with one cast" };
        meta.setLore(Arrays.asList(lore));
        result.setItemMeta(meta);
        ShapedRecipe recipe = new ShapedRecipe(result);
        // custom recipe stuff
        recipe.shape("WPB", "PEP", "BPW");
        recipe.setIngredient('W', Material.BOW);
        recipe.setIngredient('P', Material.EMERALD);
        recipe.setIngredient('E', Material.EMERALD_BLOCK);
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        return recipe;
    }

}
