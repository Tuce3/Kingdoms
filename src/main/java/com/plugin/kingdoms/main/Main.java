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


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance(){
        return instance;
    }
}
