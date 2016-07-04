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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.data.GuildsLoadedEvent;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.ChunkLocation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages land claimed by {@link Guild}s in Rolecraft.
 *
 * @since 0.1.0
 */
public final class GuildTerritoryManager {
    /**
     * The associated {@link GuildManager} instance.
     */
    private final GuildManager guildManager;
    /**
     * The associated {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * A {@link Map} of {@link ChunkLocation} objects to {@link TerritoryData}
     * for the mapped chunk.
     */
    private final Map<ChunkLocation, TerritoryData> territory;

    /**
     * Constructor.
     *
     * @param guildManager the associated {@link GuildManager} instance
     * @since 0.1.0
     */
    public GuildTerritoryManager(@Nonnull final GuildManager guildManager) {
        this.guildManager = guildManager;
        this.plugin = guildManager.getPlugin();
        this.territory = new HashMap<ChunkLocation, TerritoryData>();

        this.plugin.getServer().getPluginManager().registerEvents(
                new Listener() {
                    @EventHandler(priority = EventPriority.LOWEST)
                    public void onGuildsLoaded(final GuildsLoadedEvent event) {
                        // TODO: load territory
                    }
                }, plugin);
    }

    /**
     * Save all territory data. Sync.
     *
     * @since 0.1.0
     */
    public void saveTerritory() {
        this.doSaveTerritory(this.territory);
    }

    /**
     * Save all territory asynchronously.
     *
     * @since 0.1.0
     */
    public void asyncSaveTerritory() {
        // copy map to avoid concurrency issues
        final Map<ChunkLocation, TerritoryData> map
                = new HashMap<ChunkLocation, TerritoryData>(this.territory);

        new BukkitRunnable() {
            @Override
            public void run() {
                GuildTerritoryManager.this.doSaveTerritory(map);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void doSaveTerritory(final Map<ChunkLocation, TerritoryData> map) {
        // TODO: save territory data
        // must be threadsafe as this is called async
    }

    /**
     * Gets {@link TerritoryData} for the chunk at the given {@link ChunkLocation}.
     *
     * @param location the location of the chunk to get data for
     * @return territory data for the given chunk location
     * @since 0.1.0
     */
    @Nonnull
    public TerritoryData getTerritoryData(
            @Nonnull final ChunkLocation location) {
        TerritoryData result = territory.get(location);

        if (result == null) {
            // no guild owns this territory but we can't return null
            result = new TerritoryData(location, null);
            territory.put(location, result);
        }

        if (result.getGuildId() != null
                && guildManager.getGuild(result.getGuildId()) == null) {
            // the guild which owned the territory has been disbanded
            // so we should update the territory data accordingly
            result.setGuildId(null);
        }

        return result;
    }

    /**
     * Gets the {@link Guild} which owns the chunk at the given
     * {@link ChunkLocation}. Will return {@code null} if the chunk doesn't have
     * an owner.
     *
     * @param location the location of the chunk to get the owner of
     * @return the guild which owns the given chunk
     * @since 0.1.0
     */
    @Nullable
    public Guild getTerritoryOwner(@Nonnull final ChunkLocation location) {
        final TerritoryData territoryData = territory.get(location);
        if (territoryData == null || territoryData.getGuildId() == null) {
            // no guild owns this territory
            return null;
        }

        // note: this may still return null
        final Guild guild = guildManager.getGuild(territoryData.getGuildId());
        if (guild == null) {
            // the guild which owned the territory has been disbanded
            territoryData.setGuildId(null);
            return null;
        }

        return guild;
    }

    /**
     * Checks whether the chunk with the given {@link ChunkLocation} is owned by
     * any {@link Guild}.
     *
     * @param location the location of the chunk to check ownership of
     * @return whether the given chunk has an owner
     * @since 0.1.0
     */
    public boolean hasOwner(@Nonnull final ChunkLocation location) {
        final TerritoryData territoryData = territory.get(location);
        return territoryData != null && territoryData.getGuildId() != null
                && guildManager.getGuild(territoryData.getGuildId()) != null;
    }
}
