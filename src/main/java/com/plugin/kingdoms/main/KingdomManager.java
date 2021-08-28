package com.plugin.kingdoms.main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KingdomManager {

    private static ArrayList<Kingdom> kingdomList;
    private static ArrayList<Kingdom> unfinishedKingdomList;
    private static HashMap<UUID, Kingdom> playersInKingdoms;
    private static int maxBlocks;

    private static File datafile;
    private static YamlConfiguration changeDataFile;

    public KingdomManager() throws IOException {
        kingdomList = new ArrayList<>();
        unfinishedKingdomList = new ArrayList<>();
        playersInKingdoms = new HashMap<>();
        maxBlocks = 500000;


        datafile = new File(Bukkit.getServer().getPluginManager().getPlugin("KingdomsPlugin").getDataFolder(), "data.yml");
        if(datafile.exists()){
            datafile.createNewFile();
        }

        changeDataFile = YamlConfiguration.loadConfiguration(datafile);
        changeDataFile.save(datafile);
    }

    public static ArrayList<Kingdom> getKingdomList(){
        return kingdomList;
    }

    public static ArrayList<Kingdom> getUnfinishedKingdomList(){
        return unfinishedKingdomList;
    }

    public static HashMap<UUID, Kingdom> getPlayersInKingdoms(){
        return playersInKingdoms;
    }

    public static int getMaxBlocks(){
        return maxBlocks;
    }
    public static void setMaxBlocks(int k){
        maxBlocks = k;
    }

    public static void safeData(){

    }

    public static void loadData(){



    }
}

