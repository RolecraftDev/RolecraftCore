package com.github.rolecraftdev.data.dataobjects;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public final class ChunkLocation {
    private final String world;
    private final int x, z;

    public ChunkLocation(final Chunk chunk) {
        world = chunk.getWorld().getName();
        x = chunk.getX();
        z = chunk.getZ();
    }

    public ChunkLocation(final String world, final int x, final int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Chunk getChunk() {
        return Bukkit.getServer().getWorld(world).getChunkAt(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChunkLocation)) {
            return false;
        }
        ChunkLocation other = (ChunkLocation) o;
        return other.world.equalsIgnoreCase(world) && other.x == x
                && other.z == z;
    }

    @Override
    public int hashCode() {
        return x * z * world.hashCode();
    }
}
