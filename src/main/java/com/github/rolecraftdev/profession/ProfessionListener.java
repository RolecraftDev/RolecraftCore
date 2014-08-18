package com.github.rolecraftdev.profession;


import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class ProfessionListener implements Listener {
    private final ProfessionManager parent;

    public ProfessionListener(ProfessionManager professionManager) {
        this.parent = professionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void enforceArmorRules(InventoryClickEvent event) {
        if(event.getSlotType() == InventoryType.SlotType.ARMOR) {
            Material type = event.getCurrentItem().getType();
            parent.getPlayerProfession(event.getWhoClicked().getUniqueId()).getRuleValue(ProfessionRule.USABLE_ARMOR);
        }

    }
}
