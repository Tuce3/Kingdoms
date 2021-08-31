package com.plugin.kingdoms.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            new KingdomManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("kingdom").setExecutor(new KingdomCommand());
        getCommand("kingdom").setTabCompleter(new KingdomCommand());
        Bukkit.getPluginManager().registerEvents(new KingdomListener(), this);

        instance = this;

        KingdomManager.loadData();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                KingdomManager.safeData();
            }
        }, 400, 600);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        KingdomManager.safeData();
    }

    public static Main getInstance(){
        return instance;
    }
}
