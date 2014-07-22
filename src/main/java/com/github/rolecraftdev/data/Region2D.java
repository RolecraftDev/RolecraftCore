package com.github.rolecraftdev.data;

import org.bukkit.Location;

public class Region2D {
    private final int minX, minZ;
    private final int maxX, maxZ;

    public Region2D(final int minX, final int minZ, final int maxX,
            final int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean containsLocation(final Location location) {
        final double x = location.getX();
        final double z = location.getZ();

        return x >= minX && z >= minZ && x <= maxX && z <= maxZ;
    }

    @Override
    public String toString() {
        return "R2D:" + minX + ";" + minZ + ";" + maxX + ";" + maxZ;
    }

    public Region2D fromString(final String string) {
        final String[] split = string.split(":")[1].split(";");
        return new Region2D(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]), Integer.parseInt(split[2]),
                Integer.parseInt(split[3]));
    }
}
