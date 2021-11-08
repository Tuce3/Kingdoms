package com.plugin.kingdoms.main;

import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public final class Kingdoms extends JavaPlugin {

    private static Kingdoms instance;
    private static KingdomManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        try {
            manager = new KingdomManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("kingdom").setExecutor(new KingdomCommand());
        getCommand("kingdom").setTabCompleter(new KingdomCommand());
        Bukkit.getPluginManager().registerEvents(new KingdomListener(), this);

        Kingdoms.getManager().loadData();

        //Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
        //    @Override
        //    public void run() {
        //        Kingdoms.getManager().safeData();
        //    }
        //}, 400, 600);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        manager.safeData();
        instance = null;
    }

    public static Kingdoms getInstance(){
        return instance;
    }
    public static KingdomManager getManager(){
        return  manager;
    }
}
