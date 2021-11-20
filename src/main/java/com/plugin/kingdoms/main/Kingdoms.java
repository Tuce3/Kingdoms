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

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        instance = this;
        try {
            manager = new KingdomManager();
        } catch (IOException e) {
            e.printStackTrace();
        }


        getCommand("kingdom").setExecutor(new KingdomCommand());
        getCommand("kingdom").setTabCompleter(new KingdomCommand());
        Bukkit.getPluginManager().registerEvents(new KingdomListener(), this);

        //YAML files
        Kingdoms.getManager().loadData();

        if(getConfig().getBoolean("autosave")){
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    Kingdoms.getManager().safeData();
                }
            }, 400, getConfig().getLong("autosavetime"));
        }

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
