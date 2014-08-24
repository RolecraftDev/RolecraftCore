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
package com.github.rolecraftdev.command.other;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

/**
 * @since 0.0.5
 */
public class RCConfirmCommand extends PlayerCommandHandler {
    private final RolecraftCore plugin;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RCConfirmCommand(final RolecraftCore plugin) {
        super(plugin, "rcconfirm");
        this.plugin = plugin;

        setUsage("/rcconfirm");
        setDescription("Used to confirm sensitive commands");
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        final List<MetadataValue> values = player
                .getMetadata(DataManager.CONFIRM_COMMAND_METADATA);
        if (values == null || values.isEmpty()) {
            player.sendMessage(plugin.getMessage(Messages.NOTHING_TO_CONFIRM));
            return;
        }

        for (final MetadataValue value : values) {
            if (!(value instanceof Runnable)) {
                continue;
            }
            ((Runnable) value.value()).run();
        }
    }
}
