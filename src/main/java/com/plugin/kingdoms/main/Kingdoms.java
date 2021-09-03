package com.plugin.kingdoms.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Kingdoms extends JavaPlugin {

    private static Kingdoms instance;
    private static KingdomManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            manager = new KingdomManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("kingdom").setExecutor(new KingdomCommand());
        getCommand("kingdom").setTabCompleter(new KingdomCommand());
        Bukkit.getPluginManager().registerEvents(new KingdomListener(), this);

        instance = this;

        Kingdoms.getManager().loadData();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Kingdoms.getManager().safeData();
            }
        }, 400, 600);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Kingdoms.getManager().safeData();
        instance = null;
    }

    public static Kingdoms getInstance(){
        return instance;
    }
    public static KingdomManager getManager(){
        return  manager;
    }
}
