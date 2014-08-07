package com.github.rolecraftdev.magic.spell;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.github.rolecraftdev.RolecraftCore;

public class SpellManager {
    
    private RolecraftCore plugin;

    private Map<String,Spell> spells;

    private int maxRange;

    public SpellManager (RolecraftCore parent) {
        this.plugin = parent;
        spells = new HashMap<String,Spell>();
        this.maxRange = parent.getConfig().getInt("magicrange", 100);
        
        // Tier 1 spells
        spells.put("Freeze Block", new FreezeBlock(this));
        spells.put("Burn Block", new BurnBlock(this));
        spells.put("Lesser Sword", new LesserSword(this));
        spells.put("Break Block", new BreakBlock(this));
        spells.put("Weak Bow", new WeakBow(this));
        
        // Tier 2 spells
        spells.put("Freeze Ray", new FreezeRay(this));
        spells.put("Fire Beam", new FireBeam(this));
        spells.put("Farbreak", new Farbreak(this));
        spells.put("Stronger Bow", new StrongerBow(this));
        
        // Tier 3 spells
        spells.put("Silk Touch", new SilkTouch(this));
        spells.put("Excellent Bow", new ExcellentBow(this));
        spells.put("Multi-Arrow", new MultiArrow(this));
        spells.put("Arrow Shower", new ArrowShower(this));
        
        // Tier 4 spells
        spells.put("Bomb", new Bomb(this));
        spells.put("Meteor", new Meteor(this));
        spells.put("Farbreak Silk Touch", new FarbreakSilkTouch(this));
        spells.put("Mining Hammer", new MiningHammer(this));
        spells.put("Fly", new Fly(this));
        spells.put("Hand Cannon", new HandCannon(this));
        
        // Tier 5 spells
        spells.put("Avada Kedavra", new AvadaKedavra(this));
        
    }

    
    public Spell getSpell(String wandName) {
        return spells.get(wandName);
    }
    
    public float getMana(Player ply) {
        float mana =plugin.getDataManager().getPlayerData(ply.getUniqueId()).getMana();
        if(mana == -1) {
            return 0;
        }
        else {
            return mana;
        }
    }
    
    /**
     * Convience method
     * @param ply
     * @param amount
     */
    public void subtractMana (Player ply, float amount) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId()).substractMana(amount);
    }
    
    public void setMana (Player ply, float mana) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId()).setMana(mana);
    }
    
    public int getMagicModfier (Player ply) {
        // TODO: make this work
        return 0;
    }
    
    public int getRange () {
        return maxRange;
    }
}
