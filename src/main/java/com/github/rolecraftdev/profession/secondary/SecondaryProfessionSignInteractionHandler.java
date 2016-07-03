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
package com.github.rolecraftdev.profession.secondary;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.profession.secondary.PlayerSecondProfessionSelectEvent;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.sign.RolecraftSign;
import com.github.rolecraftdev.sign.SignInteractionHandler;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles secondary profession-related {@link RolecraftSign}s.
 *
 * @since 0.1.0
 */
public final class SecondaryProfessionSignInteractionHandler
        implements SignInteractionHandler {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * Whether secondary professions are enabled in Rolecraft's configuration.
     */
    private final boolean enabled;

    /**
     * Constructor.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public SecondaryProfessionSignInteractionHandler(
            @Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfigValues().allowSecondProfessions();
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void handleSignInteraction(@Nonnull final Player player,
            @Nonnull final RolecraftSign sign) {
        if (!enabled) {
            return;
        }

        final PlayerData pData = plugin.getPlayerData(player);
        final String function = sign.getFunction().toLowerCase();

        if (function.equals("select")) {
            if (pData.getProfession() == null) {
                player.sendMessage(plugin.getMessage(Messages.NO_PROFESSION));
                return;
            }
            if (pData.getSecondProfession() != null) {
                player.sendMessage(plugin.getMessage(
                        Messages.SECOND_PROFESSION_ALREADY_SELECTED));
                return;
            }

            final String data = sign.getData();
            final Profession profession = plugin.getProfessionManager()
                    .getProfession(data);

            if (!player.hasPermission("rolecraft.profession.secondary") ||
                    !player.hasPermission(
                            "rolecraft.profession." + data.toLowerCase())) {
                player.sendMessage(plugin.getMessage(
                        Messages.PROFESSION_NO_PERMS,
                        MessageVariable.PROFESSION.value(data)));
                return;
            }

            if (profession == null) {
                player.sendMessage(plugin.getMessage(Messages.BROKEN_SIGN));
                return;
            }

            final PlayerSecondProfessionSelectEvent event = RolecraftEventFactory
                    .secondProfessionSelected(profession, player);

            if (event.isCancelled()) {
                player.sendMessage(event.getCancelMessage());
            } else {
                pData.setSecondProfession(profession.getId());
                player.sendMessage(
                        plugin.getMessage(Messages.SECOND_PROFESSION_SELECTED,
                                MessageVariable.PROFESSION.value(data)));
            }
        } else if (function.equals("abandon")) {
            if (pData.getSecondProfession() == null) {
                player.sendMessage(
                        plugin.getMessage(Messages.NO_SECOND_PROFESSION));
                return;
            }

            pData.setSecondProfession(null);
            player.sendMessage(
                    plugin.getMessage(Messages.SECOND_PROFESSION_ABANDONED));
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Nonnull @Override
    public String getType() {
        return ProfessionManager.SECONDPROFESSION_SIGN_TYPE;
    }
}
