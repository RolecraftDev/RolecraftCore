package com.github.rolecraftdev.magic.spells;

import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

public class DestroyBlock implements Spell {

    private SpellManager manager;

    public DestroyBlock(SpellManager parent) {
        manager = parent;
    }

    @Override
    public String getName() {
        return "Destroy Block";
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float rightClick(Player ply, Block block, int modifier) {
        // TODO Auto-generated method stub
        return 0;
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
        // TODO Auto-generated method stub
        return null;
    }

}
