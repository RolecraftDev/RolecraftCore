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
package com.github.rolecraftdev.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.profession.PlayerProfessionSelectEvent;
import com.github.rolecraftdev.sign.RolecraftSign;
import com.github.rolecraftdev.sign.SignInteractionHandler;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles right-click interactions of {@link RolecraftSign}s of profession
 * type. Can be used to select professions etc.
 *
 * @since 0.1.0
 */
public final class ProfessionSignInteractionHandler
        implements SignInteractionHandler {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructs a new handler for profession-related {@link RolecraftSign}
     * interactions.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public ProfessionSignInteractionHandler(
            @Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void handleSignInteraction(@Nonnull final Player player,
            @Nonnull final RolecraftSign sign) {
        final String function = sign.getFunction().toLowerCase();
        final PlayerData data = plugin.getDataManager()
                .getPlayerData(player.getUniqueId());

        if (function.equals("select")) {
            if (data.getProfession() != null) {
                player.sendMessage(plugin.getMessage(
                        Messages.PROFESSION_ALREADY_SELECTED));
                return;
            }

            final String profName = sign.getData();
            if (!player.hasPermission("rolecraft.profession." + profName)) {
                player.sendMessage(plugin.getMessage(
                        Messages.PROFESSION_NO_PERMS,
                        MessageVariable.PROFESSION.value(profName)));
                return;
            }

            final Profession profession = plugin.getProfessionManager()
                    .getProfession(profName);
            if (profession == null) {
                player.sendMessage(plugin.getMessage(Messages.BROKEN_SIGN));
                return;
            }

            final PlayerProfessionSelectEvent event = RolecraftEventFactory
                    .professionSelected(profession, player);

            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
            } else {
                data.setProfession(profession.getId());
                player.sendMessage(
                        plugin.getMessage(Messages.PROFESSION_SELECTED,
                                MessageVariable.PROFESSION.value(profName)));
            }
        } else if (function.equals("abandon")) {
            if (data.getProfession() == null) {
                player.sendMessage(plugin.getMessage(Messages.NO_PROFESSION));
                return;
            }

            data.setProfession(null);
            player.sendMessage(plugin.getMessage(Messages.PROFESSION_ABANDONED));
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override @Nonnull
    public String getType() {
        return ProfessionManager.PROFEESSION_SIGN_TYPE;
    }
}
