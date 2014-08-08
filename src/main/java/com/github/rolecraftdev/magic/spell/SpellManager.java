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
package com.github.rolecraftdev.magic.spell;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.magic.spell.spells.*;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SpellManager {

    private RolecraftCore plugin;

    private Map<String, Spell> spells;

    private int maxRange;

    public SpellManager(RolecraftCore parent) {
        this.plugin = parent;
        spells = new HashMap<String, Spell>();
        this.maxRange = parent.getConfig().getInt("magicrange", 100);

        // Tier 1 spells
        spells.put("Freeze Block", new FreezeBlock(this));
        spells.put("Burn Block", new BurnBlock(this));
        spells.put("Lesser Sword", new LesserSword(this));

        spells.put("Weak Bow", new WeakBow(this));

        // Tier 2 spells
        spells.put("Freeze Ray", new FreezeRay(this));
        spells.put("Fire Beam", new FireBeam(this));
        spells.put("Farbreak", new Farbreak(this));
        spells.put("Stronger Bow", new StrongerBow(this));
        spells.put("Stronger Sword", new StrongerSword(this));
        spells.put("Break Block", new BreakBlock(this));

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
        float mana = plugin.getDataManager().getPlayerData(ply.getUniqueId())
                .getMana();
        if (mana == -1) {
            return 0;
        } else {
            return mana;
        }
    }

    /**
     * Convience method
     *
     * @param ply
     * @param amount
     */
    public void subtractMana(Player ply, float amount) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId())
                .substractMana(amount);
    }

    public void setMana(Player ply, float mana) {
        plugin.getDataManager().getPlayerData(ply.getUniqueId()).setMana(mana);
    }

    public int getMagicModfier(Player ply) {
        // TODO: make this work
        return 0;
    }

    public int getRange() {
        return maxRange;
    }

    public RolecraftCore getParent() {
        return plugin;
    }
}
