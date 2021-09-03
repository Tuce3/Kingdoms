package com.plugin.kingdoms.main;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KingdomManager {

    private ArrayList<Kingdom> kingdomList;
    private ArrayList<Kingdom> unfinishedKingdomList;
    private HashMap<UUID, Kingdom> playersInKingdoms;
    private int maxBlocks;

    private File datafile;
    private YamlConfiguration changeDataFile;

    public KingdomManager() throws IOException {
        kingdomList = new ArrayList<>();
        unfinishedKingdomList = new ArrayList<>();
        playersInKingdoms = new HashMap<>();
        maxBlocks = 500000;


        datafile = new File(Kingdoms.getInstance().getDataFolder(), "data.yml");
        if(datafile.exists()){
            datafile.createNewFile();
        }

        changeDataFile = YamlConfiguration.loadConfiguration(datafile);
        changeDataFile.save(datafile);
    }

    public ArrayList<Kingdom> getKingdomList(){
        return kingdomList;
    }

    public ArrayList<Kingdom> getUnfinishedKingdomList(){
        return  unfinishedKingdomList;
    }

    public HashMap<UUID, Kingdom> getPlayersInKingdoms(){
        return  playersInKingdoms;
    }

    public int getMaxBlocks(){
        return maxBlocks;
    }
    public void setMaxBlocks(int k){
        maxBlocks = k;
    }

    public void safeData(){

        changeDataFile.set("maxblocks", maxBlocks);
        if(kingdomList.size() == 0){
            changeDataFile.set("kingdoms", null);
        }
        for(int i = 0; i<kingdomList.size(); i++){


            Kingdom k = kingdomList.get(i);

            changeDataFile.set("kingdoms."+i+".owner", k.getOwner().toString());
            changeDataFile.set("kingdoms."+i+".minloc", k.getMinLocation());
            changeDataFile.set("kingdoms."+i+".maxloc", k.getMaxLocation());
            if(k.getParticleType() != null) {
                changeDataFile.set("kingdoms." + i + ".particle", k.getParticleType().toString());
            }
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
                changeDataFile.set("kingdoms."+i+".admins."+j, k.getAdmins().get(j).toString());
            }
            for(int y = 0; y<k.getMembers().size();y++){
                changeDataFile.set("kingdoms."+i+".members."+y, k.getMembers().get(y).toString());
            }
        }
        try {
            changeDataFile.save(datafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){

        kingdomList.clear();
        if(changeDataFile.getConfigurationSection("maxblocks") != null){
            maxBlocks = changeDataFile.getInt("maxblocks");
        }
        if(changeDataFile.getConfigurationSection("kingdoms") != null){
            for(int i = 0; i<changeDataFile.getConfigurationSection("kingdoms").getKeys(false).size(); i++){

                UUID owner = UUID.fromString(changeDataFile.getString("kingdoms."+i+".owner"));
                Location minLocation = changeDataFile.getLocation("kingdoms."+i+".minloc");
                Location maxLocation = changeDataFile.getLocation("kingdoms."+i+".maxloc");
                Particle type = null;
                if(changeDataFile.getString("kingdoms." + i + ".particle") != null){
                    type = Particle.valueOf(changeDataFile.getString("kingdoms." + i + ".particle"));
                }

                int particleAmount = changeDataFile.getInt("kingdoms."+i+".particleamount");
                int interact = changeDataFile.getInt("kingdoms."+i+".interact");
                int destroyblocks = changeDataFile.getInt("kingdoms."+i+".destroyblocks");
                int settings = changeDataFile.getInt("kingdoms."+i+".settings");
                int addadmins = changeDataFile.getInt("kingdoms."+i+".addadmins");
                int addmembers = changeDataFile.getInt("kingdoms."+i+".addmembers");
                int removeadmins = changeDataFile.getInt("kingdoms."+i+".removeadmins");
                int removemembers = changeDataFile.getInt("kingdoms."+i+".removemembers");
                int pvp = changeDataFile.getInt("kingdoms."+i+".pvp");
                boolean tnt = changeDataFile.getBoolean("kingdoms."+i+".tnt");
                boolean pets = changeDataFile.getBoolean("kingdoms."+i+".pets");
                int hitpets = changeDataFile.getInt("kingdoms."+i+".hitpets");
                int enter = changeDataFile.getInt("kingdoms."+i+".enter");


                //Port locs, part locs, admins and members
                ArrayList<Location> portLocations = new ArrayList<>();
                if(changeDataFile.getConfigurationSection("kingdoms."+i+".portloc") != null){
                    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".portloc.").getKeys(false).size(); j++){
                        portLocations.add(changeDataFile.getLocation("kingdoms."+i+".portloc."+j));
                    }
                }
                ArrayList<Location> partLocations = new ArrayList<>();
                if(changeDataFile.getConfigurationSection("kingdoms."+i+".partloc") != null){
                    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".partloc.").getKeys(false).size(); j++){
                        partLocations.add(changeDataFile.getLocation("kingdoms."+i+".partloc."+j));
                    }
                }
                ArrayList<UUID> admins = new ArrayList<>();
                if(changeDataFile.getConfigurationSection("kingdoms."+i+".admins") != null){
                    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".admins.").getKeys(false).size(); j++){
                        admins.add(UUID.fromString(changeDataFile.getString(("kingdoms."+i+".admins."+j))));
                    }
                }
                ArrayList<UUID> members = new ArrayList<>();
                if(changeDataFile.getConfigurationSection("kingdoms."+i+".members") != null){
                    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".members.").getKeys(false).size(); j++){
                        members.add(UUID.fromString(changeDataFile.getString("kingdoms."+i+".members."+j)));
                    }
                }

                Kingdom idk = new Kingdom(owner, minLocation, maxLocation, type, particleAmount, interact, destroyblocks, settings, addadmins, addmembers, removeadmins, removemembers, pvp, pets, hitpets, enter, portLocations, partLocations, admins, members, tnt);
                kingdomList.add(idk);

            }
        }

    }
}

