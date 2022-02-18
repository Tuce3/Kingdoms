package com.plugin.kingdoms.main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;//

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KingdomManager {

    private List<Kingdom> kingdomList;
    private List<Kingdom> unfinishedKingdomList;
    private List<Kingdom> resizingKingdomList;
    private Map<UUID, Kingdom> playersInKingdoms;

    private Map<UUID, Kingdom> playersInKingdomSettings;

    private File datafile;
    private YamlConfiguration changeDataFile;

    private YamlConfiguration messageFile;
    private File MessagesFile;

    public KingdomManager() throws IOException {
        kingdomList = new ArrayList<>();
        unfinishedKingdomList = new ArrayList<>();
        resizingKingdomList = new ArrayList<>();
        playersInKingdoms = new HashMap<>();

        datafile = new File(Kingdoms.getInstance().getDataFolder(), "data.yml");
        if(!datafile.exists()){
            datafile.createNewFile();
        }
        if(Kingdoms.getInstance().getConfig().getBoolean("generatemessagefile")) {
            MessagesFile = new File(Kingdoms.getInstance().getDataFolder(), "messages.yml");
            if (!MessagesFile.exists()) {
                MessagesFile.createNewFile();
                messageFile = YamlConfiguration.loadConfiguration(MessagesFile);
                //Set all the messages
                setMessages();
            }

            messageFile = YamlConfiguration.loadConfiguration(MessagesFile);
            messageFile.save(MessagesFile);
            loadMessages();
        }

        changeDataFile = YamlConfiguration.loadConfiguration(datafile);
        changeDataFile.save(datafile);
    }

    public List<Kingdom> getKingdomList(){
        return kingdomList;
    }

    public List<Kingdom> getUnfinishedKingdomList(){
        return  unfinishedKingdomList;
    }
    public List<Kingdom> getResizingKingdomList() {
        return resizingKingdomList;
    }
    public void addKingdomToList(Kingdom k){
        kingdomList.add(k);
    }
    public Map<UUID, Kingdom> getPlayersInKingdomSettings() {
        return playersInKingdomSettings;
    }
    public void removeKingdomFromList(Kingdom k){
        kingdomList.remove(k);
    }
    public void clearKingdomList(){
        kingdomList.clear();
    }
    public YamlConfiguration getMessageFile() {
        return messageFile;
    }

    public Map<UUID, Kingdom> getPlayersInKingdoms(){
        return  playersInKingdoms;
    }
    public Kingdom getKingdom(int i){
        return kingdomList.get(i);
    }

    public void safeData(){

        if(kingdomList.size() == 0){
            changeDataFile.set("kingdoms", null);
        }
        for(int i = 0; i<kingdomList.size(); i++){


            Kingdom k = kingdomList.get(i);


            changeDataFile.set("kingdoms."+i+".owner", k.getOwner().toString());
            changeDataFile.set("kingdoms."+i+".minloc", k.getMinLocation());
            changeDataFile.set("kingdoms."+i+".maxloc", k.getMaxLocation());
            changeDataFile.set("kingdoms."+i+".name", k.getName());
            changeDataFile.set("kingdoms."+i+".toptitle", k.getTopTitle());
            if(k.getParticleType() != null) {
                changeDataFile.set("kingdoms." + i + ".particle", k.getParticleType().toString());
            }
            changeDataFile.set("kingdoms."+i+".particleamount", k.getParticleAmount());
            ////Locations, port und particle
            //for(int j = 0; j<k.getPortLocations().size(); j++){
            //    changeDataFile.set("kingdoms."+i+".portloc."+j, k.getPortLocations().get(j));
            //}
            //for(int y = 0; y<k.getParticleLocations().size();y++){
            //    changeDataFile.set("kingdoms."+i+".partloc."+y, k.getParticleLocations().get(y));
            //}

            changeDataFile.set("kingdoms."+i+".interact", k.getInteract());
            changeDataFile.set("kingdoms."+i+".destroyblocks", k.getDestroyBlocks());
            changeDataFile.set("kingdoms."+i+".settings", k.getSettings());
            changeDataFile.set("kingdoms."+i+".addadmins", k.getAddAdmins());
            changeDataFile.set("kingdoms."+i+".addmembers", k.getAddMembers());
            changeDataFile.set("kingdoms."+i+".removeadmins", k.getRemoveAdmins());
            changeDataFile.set("kingdoms."+i+".removemebers", k.getRemoveMembers());
            changeDataFile.set("kingdoms."+i+".pvp", k.getPvP());
            changeDataFile.set("kingdoms."+i+".tnt", k.getTnTActive());
            changeDataFile.set("kingdoms."+i+".pets", k.getPetInvulnerable());
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
        if(changeDataFile.getConfigurationSection("kingdoms") != null){
            for(int i = 0; i<changeDataFile.getConfigurationSection("kingdoms").getKeys(false).size(); i++){

                UUID owner = UUID.fromString(changeDataFile.getString("kingdoms."+i+".owner"));
                Location minLocation = changeDataFile.getLocation("kingdoms."+i+".minloc");
                Location maxLocation = changeDataFile.getLocation("kingdoms."+i+".maxloc");
                String name = changeDataFile.getString("kingdoms."+i+".name");
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
                String topTitle = changeDataFile.getString("kingdoms."+i+".toptitle");

                //Port locs, part locs, admins and members
                //Deleted for faster loads and saving times, as well as shorter data.yml files
                ////ArrayList<Location> portLocations = new ArrayList<>();
                ////if(changeDataFile.getConfigurationSection("kingdoms."+i+".portloc") != null){
                ////    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".portloc.").getKeys(false).size(); j++){
                ////        portLocations.add(changeDataFile.getLocation("kingdoms."+i+".portloc."+j));
                ////    }
                ////}
                //ArrayList<Location> partLocations = new ArrayList<>();
                //if(changeDataFile.getConfigurationSection("kingdoms."+i+".partloc") != null){
                //    for(int j = 0; j<changeDataFile.getConfigurationSection("kingdoms."+i+".partloc.").getKeys(false).size(); j++){
                //        partLocations.add(changeDataFile.getLocation("kingdoms."+i+".partloc."+j));
                //    }
                //}
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

                Kingdom idk = new Kingdom(owner, minLocation, maxLocation, type, particleAmount, interact, destroyblocks, settings, addadmins, addmembers, removeadmins, removemembers, pvp, pets, hitpets, enter, admins, members, tnt, name, topTitle);
                kingdomList.add(idk);

            }
        }

    }

    private void setMessages() {
        //Set all the messages in the file
        messageFile.options().header("All the messages with their description of what they do.\n"+
                "When using Colorcodes (& for Chatcolor) put '' around the messages, else it'll delete them."+"\n"+
                "When the Message end like this 'The Kingdoms new title is ' there will be a name/title after the 'is ', be aware of this.");
        for(Messages message : Messages.values()){
            messageFile.set(message.name(), message.getMessage());
        }
        try {
            messageFile.save(MessagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages(){
        for(String key : messageFile.getConfigurationSection("").getKeys(false)){
            Messages.valueOf(key).setMessage(ChatColor.translateAlternateColorCodes('&', messageFile.getString(key)));
        }
    }
}

