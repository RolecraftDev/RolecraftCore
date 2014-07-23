package com.github.rolecraftdev.data.storage;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {
	
	private LocationSerializer() {}
	
	public static Location deserialize(String serial) throws IllegalArgumentException {
		
		if (serial == null) return null;
		
		String[] values = serial.split(",");
		
		if(values.length != 6) {
			throw new IllegalArgumentException("ID:1 " + serial + " is not a valid serialization!");
		}
		
		World world = Bukkit.getServer().getWorld(UUID.fromString(values[0]));
		
		if(world == null) {
			throw new IllegalArgumentException("ID:2 " +values[0] + " is not a world on this server!");
		}
		
		double x = 0;
		double y = 0;
		double z = 0;
		float yaw = 0;
		float pitch = 0;
		
		try {
			x = Double.valueOf(values[1]);
			y = Double.valueOf(values[2]);
			z = Double.valueOf(values[3]);
			pitch = Float.valueOf(values[4]);
			yaw = Float.valueOf(values[5]);
		} catch (Exception ex) {
			throw new IllegalArgumentException("ID:3 " + "Could not cast double values: " + ex.getMessage());
		}
		
		
		Location temp = new Location(world, x, y, z, yaw,pitch);
		
		return temp;
	}
	
	public static String serialize(Location loc) {
		
		if (loc == null) return null;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(loc.getWorld().getUID().toString());
		sb.append(",");
		sb.append(loc.getX());
		sb.append(",");
		sb.append(loc.getY());
		sb.append(",");
		sb.append(loc.getZ());
		sb.append(",");
		sb.append(loc.getYaw());
		sb.append(",");
		sb.append(loc.getPitch());
		
		return sb.toString();
	}
}
