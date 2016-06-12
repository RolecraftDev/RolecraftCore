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
package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.sign.RolecraftSign;
import com.github.rolecraftdev.sign.SignInteractionHandler;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Handles right-click interactions of {@link RolecraftSign}s of guildtype. Can
 * be used to join open guilds etc.
 *
 * @since 0.1.0
 */
public final class GuildSignInteractionHandler
        implements SignInteractionHandler {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;

    /**
     * Constructs a new handler for guild-related {@link RolecraftSign}
     * interactions.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public GuildSignInteractionHandler(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void handleSignInteraction(@Nonnull final Player player,
            @Nonnull final RolecraftSign sign) {
        // TODO
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Nonnull @Override
    public String getType() {
        return GuildManager.GUILD_SIGN_TYPE;
    }
}
