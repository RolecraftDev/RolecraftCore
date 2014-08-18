package com.github.rolecraftdev.profession;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.messages.MsgVar;

public class ProfessionListener implements Listener {
    private final ProfessionManager parent;

    public ProfessionListener(ProfessionManager professionManager) {
        this.parent = professionManager;
    }

    // enforce armor wearing rules
    /**
     * In the profession file, these are determined by the tag usable-armor
     * followed by a list of tags that are the names of items as defined in
     * {@link org.bukkit.Material}
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void enforceArmorRules(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                Material type = event.getCurrentItem().getType();
                final List<?> usable = parent.getPlayerProfession(
                        event.getWhoClicked().getUniqueId()).getRuleValue(
                        ProfessionRule.USABLE_ARMOR);
                String material = type.toString().replace("Material.", "").toLowerCase();
                if (usable == null) {
                    if (parent.getPlugin().getConfig()
                            .getBoolean("professiondefaults.armor")) {
                        return;
                    }
                    else {
                        ((Player) event.getWhoClicked()).sendMessage(parent
                                .getPlugin().getMessage(
                                        Messages.SPELL_CAST,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getWhoClicked()
                                                                .getUniqueId())
                                                        .getName())));
                        event.setCancelled(true);
                    }
                }
                else {
                    if (usable.contains(material)) {
                        return;
                    }
                    else {
                        ((Player) event.getWhoClicked()).sendMessage(parent
                                .getPlugin().getMessage(
                                        Messages.SPELL_CAST,
                                        MsgVar.create(
                                                "$profession",
                                                parent.getPlayerProfession(
                                                        event.getWhoClicked()
                                                                .getUniqueId())
                                                        .getName())));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
