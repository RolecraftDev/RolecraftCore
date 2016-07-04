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
package com.github.rolecraftdev.guild.territory;

import com.github.rolecraftdev.util.ChunkLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Holds data for a chunk of territory.
 *
 * @since 0.1.0
 */
public final class TerritoryData {
    /**
     * The chunk location of this territory.
     */
    @Nonnull
    private final ChunkLocation location;

    /**
     * The {@link UUID} of the guild owning this territory, or {@code null} if
     * the territory is not owned.
     */
    @Nullable
    private UUID guildId;

    /**
     * Constructor.
     *
     * @param location the chunk location of the territory
     * @param guildId the id of the guild which owns the territory
     * @since 0.1.0
     */
    public TerritoryData(@Nonnull final ChunkLocation location,
            @Nullable final UUID guildId) {
        this.location = location;
        this.guildId = guildId;
    }

    /**
     * Gets the chunk location of this territory.
     *
     * @return this territory's chunk location
     * @since 0.1.0
     */
    @Nonnull
    public ChunkLocation getLocation() {
        return location;
    }

    /**
     * Gets the {@link UUID} of the guild which owns this territory. Will return
     * {@code null} if this territory has no owner.
     *
     * @return the id of the guild owning this territory
     * @since 0.1.0
     */
    @Nullable
    public UUID getGuildId() {
        return guildId;
    }

    /**
     * Sets the guild which owns this territory.
     *
     * @param guildId the new guild to own the territory
     * @since 0.1.0
     */
    public void setGuildId(@Nullable final UUID guildId) {
        this.guildId = guildId;
    }

    /**
     * Check whether this territory is owned by a guild.
     *
     * @return whether this territory is owned by a guild
     * @since 0.1.0
     */
    public boolean isOwned() {
        return guildId != null;
    }
}
