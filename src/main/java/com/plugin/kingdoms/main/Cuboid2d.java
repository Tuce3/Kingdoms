package com.plugin.kingdoms.main;

import org.bukkit.Location;
import org.bukkit.World;

public class Cuboid2d {

    private final double minX;
    private final double minZ;
    private final double maxX;
    private final double maxZ;
    private final World world;

    public Cuboid2d(Location min, Location max){

        this.world = min.getWorld();

        this.minX = Math.min(min.getBlockX(), max.getBlockX());
        this.maxX = Math.max(min.getBlockX(), max.getBlockX());
        this.minZ = Math.min(min.getBlockZ(), max.getBlockZ());
        this.maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

    }

    public boolean containsLocation(Location loc){

        if(loc.getWorld().equals(world)){
            if(loc.getBlockX() <= maxX && loc.getBlockX() >= minX){
                if(loc.getBlockZ() <= maxZ && loc.getBlockZ() >= minZ){
                    return true;
                }
            }
        }

        return false;

    }

    public Location getMinLocation(){
        return new Location(world, minX, 0, minZ);
    }
    public Location getMaxMinLocation(){
        return new Location(world, maxX, 0, minZ);
    }
    public Location getMinMaxLocation(){
        return new Location(world, minX, 0, maxZ);
    }
    public Location getMaxMaxLocation(){
        return new Location(world, maxX, 0, maxZ);
    }
}
