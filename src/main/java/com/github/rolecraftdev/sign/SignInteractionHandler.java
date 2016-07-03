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

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * A SignInteractionHandler is registered with the {@link RolecraftSignManager}
 * object and has it's {@link #handleSignInteraction(Player, RolecraftSign)}
 * method called whenever a {@link RolecraftSign} is interacted with of the same
 * type as that which is returned by {@link #getType()}.
 *
 * @since 0.1.0
 */
public interface SignInteractionHandler {
    /**
     * Handle an interaction with the given {@link RolecraftSign}, made by the
     * given {@link Player}.
     *
     * @param player the {@link Player} who interacted with the sign
     * @param sign the {@link RolecraftSign} the player interacted with
     * @since 0.1.0
     */
    void handleSignInteraction(@Nonnull final Player player,
            @Nonnull final RolecraftSign sign);

    /**
     * Gets the name of the type of {@link RolecraftSign} this handler is
     * designed to handle.
     *
     * @return this handler's type
     * @since 0.1.0
     */
    @Nonnull String getType();
}
