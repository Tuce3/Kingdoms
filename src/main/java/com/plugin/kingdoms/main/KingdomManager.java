package com.plugin.kingdoms.main;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
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
        changeDataFile.set("maxblocks", maxBlocks);
        for(int i = 0; i<kingdomList.size(); i++){

            Kingdom k = kingdomList.get(i);

            changeDataFile.set("kingdoms."+i+".owner", k.getOwner());
            changeDataFile.set("kingdoms."+i+".minloc", k.getMinLocation());
            changeDataFile.set("kingdoms."+i+".maxloc", k.getMaxLocation());
            changeDataFile.set("kingdoms."+i+".particle", k.getParticleType().toString());
            changeDataFile.set("kingdoms."+i+".particleamount", k.getParticleAmount());
            //Locations, port und particle
            for(int j = 0; j<k.getPortLocations().size(); j++){
                changeDataFile.set("kingdoms."+i+".portloc."+j, k.getPortLocations().get(j));
            }
            for(int y = 0; y<k.getParticleLocations().size();y++){
                changeDataFile.set("kingdoms."+i+".partloc."+y, k.getParticleLocations().get(y));
            }
            changeDataFile.set("kingdoms."+i+".interact", k.getInteract());
            changeDataFile.set("kingdoms."+i+".destroyblocks", k.getDestroyBlocks());
            changeDataFile.set("kingdoms."+i+".settings", k.getSettings());
            changeDataFile.set("kingdoms."+i+".addadmins", k.getAddAdmins());
            changeDataFile.set("kingdoms."+i+".addmembers", k.getAddMembers());
            changeDataFile.set("kingdoms."+i+".removeadmins", k.getRemoveAdmins());
            changeDataFile.set("kingdoms."+i+".removemebers", k.getRemoveMembers());
            changeDataFile.set("kingdoms."+i+".pvp", k.getPvP());
            changeDataFile.set("kingdoms."+i+".tnt", k.getTnTActive());
            changeDataFile.set("kingdoms."+i+".pets", k.getPetInvulerable());
            changeDataFile.set("kingdoms."+i+".hitpets", k.getHitPets());
            changeDataFile.set("kingdoms."+i+".enter", k.getEnterKingdom());
            //Admins und members
            for(int j = 0; j<k.getAdmins().size(); j++){
                changeDataFile.set("kingdoms."+i+".admins."+j, k.getAdmins().get(j));
            }
            for(int y = 0; y<k.getMembers().size();y++){
                changeDataFile.set("kingdoms."+i+".members."+y, k.getMembers().get(y));
            }
        }
    }

    public static void loadData(){



    }
}

