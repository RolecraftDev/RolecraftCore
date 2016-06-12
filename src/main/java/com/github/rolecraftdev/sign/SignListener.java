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
package com.github.rolecraftdev.sign;

import com.github.rolecraftdev.util.GeneralUtil;
import com.github.rolecraftdev.util.SimpleLocation;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listens for players interacting with signs in order to implement
 * {@link RolecraftSign} related features.
 *
 * @since 0.1.0
 */
public final class SignListener implements Listener {
    /**
     * The associated {@link RolecraftSignManager} instance.
     */
    private final RolecraftSignManager signManager;

    /**
     * Constructor.
     *
     * @param signManager the associated {@link RolecraftSignManager}
     * @since 0.1.0
     */
    public SignListener(final RolecraftSignManager signManager) {
        this.signManager = signManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlock();
        if (!GeneralUtil.isSign(block)) {
            return;
        }

        final BlockState state = block.getState();
        final Sign signState = (Sign) state;
        final String[] lines = signState.getLines();
        if (lines == null || lines.length < 3) {
            return;
        }

        final String firstLine = lines[0].trim().toLowerCase();
        if (!(firstLine.startsWith("[") && firstLine.endsWith("]"))) {
            return;
        }

        // cut out [ at start and ] at end to get the type name
        final String typeName = firstLine.substring(1, firstLine.length() - 2);
        if (!this.signManager.isRegisteredType(typeName)) {
            return;
        }

        final Player player = event.getPlayer();
        if (!player.hasPermission("rolecraft.sign.place")) {
            return;
        }

        final RolecraftSign sign = new RolecraftSign(typeName, lines[1],
                lines[2], new SimpleLocation(block.getLocation()));
        this.signManager.addSign(sign);
        // TODO: add plugin message that the player has placed a Rolecraft sign
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        final SimpleLocation location = new SimpleLocation(block.getLocation());

        if (this.signManager.isSignAt(location)) {
            if (!event.getPlayer().hasPermission("rolecraft.sign.place")) {
                event.setCancelled(true); // players who don't have permission to place these signs are also assumed not to have permission to break them
                return;
            }
            this.signManager.removeSignAt(location);
        }
    }

    /**
     * @since 0.1.0
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return; // we only want right clicks on signs
        }

        final Block block = event.getClickedBlock();
        if (!GeneralUtil.isSign(block)) {
            return;
        }

        final SimpleLocation location = new SimpleLocation(block.getLocation());
        final RolecraftSign sign = this.signManager.getSignAt(location);

        if (sign != null) {
            this.signManager.handleInteraction(event, sign);
        }
    }
}
