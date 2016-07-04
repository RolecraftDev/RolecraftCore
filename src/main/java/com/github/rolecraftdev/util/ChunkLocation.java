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
package com.github.rolecraftdev.util;

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * Holds simple location data about a chunk.
 *
 * @since 0.1.0
 */
public final class ChunkLocation {
    /**
     * The name of the world this chunk is in.
     */
    @Nonnull
    private final String world;
    /**
     * The x co-ordinate of the chunk.
     */
    private final int chunkX;
    /**
     * The z co-ordinate of the chunk.
     */
    private final int chunkZ;

    /**
     * Constructor.
     *
     * @param world the name of the world the chunk is in
     * @param chunkX the x co-ordinate of the chunk
     * @param chunkZ the z co-ordinate of the chunk
     * @since 0.1.0
     */
    public ChunkLocation(@Nonnull String world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    /**
     * Constructor.
     *
     * @param world the world the chunk is in
     * @param chunkX the x co-ordinate of the chunk
     * @param chunkZ the z co-ordinate of the chunk
     * @since 0.1.0
     */
    public ChunkLocation(@Nonnull World world, int chunkX, int chunkZ) {
        this(world.getName(), chunkX, chunkZ);
    }

    /**
     * Constructor.
     *
     * @param chunk the chunk to get the location data from
     * @since 0.1.0
     */
    public ChunkLocation(Chunk chunk) {
        this(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    /**
     * Constructor.
     *
     * @param location the location to get the chunk from
     * @since 0.1.0
     */
    public ChunkLocation(Location location) {
        this(location.getChunk());
    }

    /**
     * Gets the Bukkit {@link Chunk} with this location.
     *
     * @return the Chunk at this location
     * @since 0.1.0
     */
    @Nonnull
    public Chunk getChunk() {
        return getWorld().getChunkAt(chunkX, chunkZ);
    }

    /**
     * Gets the name of the world this chunk is in.
     *
     * @return the name of the world this chunk is in
     * @since 0.1.0
     */
    @Nonnull
    public String getWorldName() {
        return world;
    }

    /**
     * The Bukkit {@link World} this chunk is in.
     *
     * @return the World this chunk is in
     * @since 0.1.0
     */
    @Nonnull
    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    /**
     * Gets the x co-ordinate this chunk is at.
     *
     * @return this chunk's x co-ordinate
     * @since 0.1.0
     */
    public int getChunkX() {
        return chunkX;
    }

    /**
     * Gets the z co-ordinate this chunk is at.
     *
     * @return this chunk's z co-ordinate
     * @since 0.1.0
     */
    public int getChunkZ() {
        return chunkZ;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChunkLocation that = (ChunkLocation) o;
        return chunkX == that.chunkX && chunkZ == that.chunkZ && world
                .equals(that.world);
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public int hashCode() {
        int result = world.hashCode();
        result = 31 * result + chunkX;
        result = 31 * result + chunkZ;
        return result;
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     * @see {@link #fromString(String)}
     */
    @Override
    public String toString() {
        return "CL:" + world + "," + chunkX + "," + chunkZ + "";
    }

    /**
     * Gets a ChunkLocation from the given serialized string, as returned by
     * {@link #toString()}.
     *
     * @param string the serialized chunk location
     * @return the ChunkLocation object deserialized from the given string
     * @since 0.1.0
     * @see {@link #toString()}
     */
    @Nonnull
    public static ChunkLocation fromString(@Nonnull final String string) {
        Validate.notNull(string);

        final String[] split = string.split(GeneralUtil.QUOTED_COLON)[1]
                .split(GeneralUtil.QUOTED_COMMA);
        return new ChunkLocation(split[0], Integer.parseInt(split[1]),
                Integer.parseInt(split[2]));
    }
}
