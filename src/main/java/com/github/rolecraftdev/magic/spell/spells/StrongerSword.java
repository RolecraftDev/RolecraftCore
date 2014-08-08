package com.github.rolecraftdev.magic.spell.spells;

import com.github.rolecraftdev.magic.spell.Spell;
import com.github.rolecraftdev.magic.spell.SpellManager;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

public class StrongerSword implements Spell {

    public StrongerSword(SpellManager spellManager) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getName() {
        return "Strong Sword";
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
