package com.yoshigenius.lib.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializableLocation extends Location implements Serializable {
	
	public SerializableLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}
	
	public SerializableLocation(World world, double x, double y, double z) {
		this(world, x, y, z, 0, 0);
	}
	
	protected SerializableLocation() {
		this(Bukkit.getWorlds().get(0), 0, 0, 0);
	}

	@Override
	public String serialize() {
		String s = Serializer.SEPARATOR_INFO;
		return getWorld().getName() + s + getX() + s + getY() + s + getZ() + s + getYaw() + s + getPitch();
	}

	@Override
	public void deserialize(String s) {
		String[] data = s.split(";");
		World w = Bukkit.getWorld(data[0]);
		double x = Double.parseDouble(data[1]);
		double y = Double.parseDouble(data[2]);
		double z = Double.parseDouble(data[3]);
		float yaw = Float.parseFloat(data[4]);
		float pitch = Float.parseFloat(data[5]);
		this.setWorld(w);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setYaw(yaw);
		this.setPitch(pitch);
	}

}
