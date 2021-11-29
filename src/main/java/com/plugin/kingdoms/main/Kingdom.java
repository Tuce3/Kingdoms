package com.plugin.kingdoms.main;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.plugin.kingdoms.main.Utils.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.UUID;

public class Kingdom extends KingdomSettings{

    private final UUID owner;
    private Location minLocation;
    private Location maxLocation;
    private Cuboid2d area;
    private Cuboid2d checkArea;
    private Cuboid2d tntCheckArea;
    private Particle particleType;
    private int particleAmount;
    private ArrayList<Location> ParticleLocations;
    private int particleRunnableId;
    private ArrayList<Location> portLocations;
    private String name = null;
    private String topTitle = null;


    private final ArrayList<UUID> admins;
    private final ArrayList<UUID> members;

    public Kingdom(UUID owner){
        super();
        this.owner = owner;
        ParticleLocations = new ArrayList<>();
        particleType = Particle.ASH;
        admins = new ArrayList<>();
        members = new ArrayList<>();
        particleAmount = 10;
        portLocations = new ArrayList<>();

    }

    public Kingdom(UUID owner, Location minLocation, Location maxLocation, Particle type, int particleAmount, int interact, int destroyBlocks, int settings, int addAdmins, int addMembers, int removeAdmins, int removeMembers, int pvp, boolean pets, int hitPets, int enterKingdom, ArrayList<UUID> admins, ArrayList<UUID> members, boolean tnTActive, String name, String topTitle){

        super( interact,  destroyBlocks,  settings,  addAdmins,  addMembers,  removeAdmins,  removeMembers,  pvp,  pets,  hitPets,  enterKingdom,  tnTActive);
        this.owner = owner;
        this.admins = admins;
        this.members = members;
        this.particleAmount = particleAmount;
        this.particleType = type;

        this.ParticleLocations = new ArrayList<>();
        this.portLocations = new ArrayList<>();
        this.setMinLocation(minLocation, true);
        this.setMaxLocation(maxLocation, true);
        this.name = name;
        this.topTitle = topTitle;

    }

    public void setMinLocation(Location location, boolean dataload){
        this.minLocation = location;
        if(Bukkit.getPlayer(owner) != null && !dataload){
            Bukkit.getPlayer(owner).sendMessage(Messages.FIRSTLOCATIONSET.getMessage());
        }
    }
    public void setMaxLocation(Location location, boolean dataload){
        this.maxLocation = location;

        if(minLocation != null && maxLocation != null){
            makeKingdom(dataload);
        }
    }

    private void makeKingdom(boolean dataload) {
        Kingdoms.getManager().getUnfinishedKingdomList().remove(this);



        area = new Cuboid2d(minLocation, maxLocation);
        checkArea = new Cuboid2d(new Location(maxLocation.getWorld(), Math.min(minLocation.getX(), maxLocation.getX())-Kingdoms.getInstance().getConfig().getLong("mindistance"), 0d, Math.min(minLocation.getZ(), maxLocation.getZ())-Kingdoms.getInstance().getConfig().getLong("mindistance")), new Location(maxLocation.getWorld(), Math.max(minLocation.getX(), maxLocation.getX())+Kingdoms.getInstance().getConfig().getLong("mindistance"), 0d, Math.max(minLocation.getZ(), maxLocation.getZ())+Kingdoms.getInstance().getConfig().getLong("mindistance")));
        tntCheckArea = new Cuboid2d(new Location(maxLocation.getWorld(), Math.min(minLocation.getX(), maxLocation.getX())-20, 0d, Math.min(minLocation.getZ(), maxLocation.getZ())-20), new Location(maxLocation.getWorld(), Math.max(minLocation.getX(), maxLocation.getX())+20, 0d, Math.max(minLocation.getZ(), maxLocation.getZ())+20));


        if(!dataload) {
            if (Kingdoms.getManager().getPlayersInKingdoms().containsKey(owner)) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(Messages.CANTCREATEKINGDOMWHILEIINKINGDOM.getMessage());
                    return;
                }
            }
            if (minLocation.getWorld() != maxLocation.getWorld()) {
                Bukkit.getPlayer(owner).sendMessage(Messages.CANTSETLOCATIONSINDIFFERENTWORLDS.getMessage());
                return;
            }

            boolean kingdomCantBeCreated = false;

            for (Kingdom k : Kingdoms.getManager().getKingdomList()) {
                if (k.getCheckArea().containsLocation(area.getMaxMaxLocation()) || k.getCheckArea().containsLocation(area.getMinMaxLocation()) || k.getCheckArea().containsLocation(area.getMaxMinLocation()) || k.getCheckArea().containsLocation(area.getMinMinLocation())) {
                    kingdomCantBeCreated = true;
                }
                if (checkArea.containsLocation(k.getArea().getMinMinLocation()) || checkArea.containsLocation(k.getArea().getMinMaxLocation()) || checkArea.containsLocation(k.getArea().getMaxMinLocation()) || checkArea.containsLocation(k.getArea().getMaxMinLocation())) {
                    kingdomCantBeCreated = true;
                }
            }

            if (kingdomCantBeCreated) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(Messages.CANTCREATEKINGDOMHERE.getMessage());
                    return;
                }
            }

            if (area.getAreaSize() > Kingdoms.getInstance().getConfig().getLong("maxblocks") && !Bukkit.getOfflinePlayer(owner).isOp()) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(Messages.KINGDOMTOOBIG.getMessage());
                    return;
                }

            }
            long kingdomsizeOfPlayer = 0;
            kingdomsizeOfPlayer += area.getAreaSize();
            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                if(k.getOwner().equals(owner)){
                    kingdomsizeOfPlayer += k.getArea().getAreaSize();
                }
            }
            if(kingdomsizeOfPlayer > Kingdoms.getInstance().getConfig().getLong("maxblockperplayer") && !Bukkit.getOfflinePlayer(owner).isOp()){
                if(Bukkit.getPlayer(owner) != null){
                    Bukkit.getPlayer(owner).sendMessage(Messages.MAXSIZEKINGDOMSPERPLAYER.getMessage());

                }
                return;
            }
            Kingdoms.getManager().addKingdomToList(this);
        }
        calculateParticleLocation();

        if(Bukkit.getPlayer(owner) != null){
            Bukkit.getPlayer(owner).sendMessage(Messages.KINGDOMCREATED.getMessage());
        }
        if(Kingdoms.getInstance().getConfig().getBoolean("particles")){
            spawnParticles();
        }
    }

    private void spawnParticles(){

        particleRunnableId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Kingdoms.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(particleType != null) {
                    for (Location loc : ParticleLocations) {
                        loc.getWorld().spawnParticle(particleType, new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getWorld().getHighestBlockAt(loc).getLocation().getBlockY() + 1.5, loc.getBlockZ() + 0.5), particleAmount);
                    }
                }
            }
        }, 0, 10);

    }

    private void calculateParticleLocation(){
        for(int i = 0; i<area.getMaxMaxLocation().getBlockZ()-area.getMaxMinLocation().getBlockZ(); i++){
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMaxMaxLocation().getBlockX(), 0, area.getMinMinLocation().getBlockZ()+i ));
            portLocations.add(new Location(minLocation.getWorld(), area.getMaxMaxLocation().getBlockX()+2, 0, area.getMinMinLocation().getBlockZ()+i ));
        }
        for(int i = 0; i<area.getMaxMaxLocation().getBlockX()-area.getMinMaxLocation().getBlockX(); i++){
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()+i, 0, area.getMaxMaxLocation().getBlockZ()));
            portLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()+i, 0, area.getMaxMaxLocation().getBlockZ()+2));
        }

        for(int i = 0; i<area.getMinMaxLocation().getBlockZ()-area.getMinMinLocation().getBlockZ(); i++){
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX(), 0, area.getMinMinLocation().getBlockZ()+i ));
            portLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()-2, 0, area.getMinMinLocation().getBlockZ()+i ));
        }
        for(int i = 0; i<area.getMaxMinLocation().getBlockX()-area.getMinMinLocation().getBlockX(); i++){
            if(ParticleLocations.contains(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()+i, 0, area.getMinMinLocation().getBlockZ()))){
                ParticleLocations.add(area.getMaxMaxLocation());
            }else{
                ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()+i, 0, area.getMinMinLocation().getBlockZ()));
                portLocations.add(new Location(minLocation.getWorld(), area.getMinMinLocation().getBlockX()+i, 0, area.getMinMinLocation().getBlockZ()-2));
            }
        }
    }

    //getters
    public Location getMinLocation(){
        return minLocation;
    }
    public Location getMaxLocation(){
        return maxLocation;
    }
    public UUID getOwner(){
        return owner;
    }
    public Cuboid2d getArea(){
        return area;
    }
    public ArrayList<UUID> getAdmins(){
        return admins;
    }
    public ArrayList<UUID> getMembers(){
        return members;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTopTitle() {
        return topTitle;
    }
    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }


    public void setParticleType(Particle p){
        particleType = p;
    }
    public void setParticleAmount(int p){
        particleAmount = p;
    }
    public ArrayList<Location> getPortLocations(){
        return portLocations;
    }
    public Particle getParticleType(){
        return particleType;
    }
    public int getParticleAmount(){
        return  particleAmount;
    }
    public ArrayList<Location> getParticleLocations() {
        return ParticleLocations;
    }
    public int getRunnableId(){
        return particleRunnableId;
    }
    public Cuboid2d getCheckArea() {
        return checkArea;
    }
    public void setCheckArea(Cuboid2d checkArea) {
        this.checkArea = checkArea;
    }
    public Cuboid2d getTntCheckArea() {
        return tntCheckArea;
    }

    public void openSeeOwner(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, Messages.OWNERSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(4, Utils.givePlayerHead(owner));

        player.openInventory(gui);

    }
    public void openSeeAdmins(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, Messages.ADMINSSETTINGSINVENTORYNAME.getMessage());

        int i;

        for(i = (page-1)*52; i<admins.size(); i++){

            int k = i-((page-1)*52);

            if(k >= 45){
                k = k+1;
            }

            gui.setItem(k, Utils.givePlayerHead(admins.get(i)));

            if(i%52 == 0 && (page-1)*52 != i){
                break;
            }
        }

        if(i == admins.size()){

            ItemStack noNextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) noNextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            meta.setDisplayName(ChatColor.RED + "No next page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            noNextPage.setItemMeta(meta);

            gui.setItem(53, noNextPage);
        }else{
            ItemStack NextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) NextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.POISON));
            meta.setDisplayName(ChatColor.GREEN + "Next page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            NextPage.setItemMeta(meta);

            gui.setItem(53, NextPage);
        }

        if(i-52 <= 0){

            ItemStack noNextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) noNextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            meta.setDisplayName(ChatColor.RED + "No last page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            noNextPage.setItemMeta(meta);

            gui.setItem(45, noNextPage);
        }else{
            ItemStack NextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) NextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.POISON));
            meta.setDisplayName(ChatColor.GREEN + "Last page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            NextPage.setItemMeta(meta);

            gui.setItem(45, NextPage);
        }

        player.openInventory(gui);

    }
    public void openSeeMembers(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, Messages.MEMBERSSETTINGSINVENTORYNAME.getMessage());

        int i;

        for(i = (page-1)*52; i<members.size(); i++){

            int k = i-((page-1)*52);

            if(k >= 45){
                k = k+1;
            }

            gui.setItem(k, Utils.givePlayerHead(members.get(i)));

            if(i%52 == 0 && (page-1)*52 != i){
                break;
            }
        }

        if(i == members.size()){

            ItemStack noNextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) noNextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            meta.setDisplayName(ChatColor.RED + "No next page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            noNextPage.setItemMeta(meta);

            gui.setItem(53, noNextPage);
        }else{
            ItemStack NextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) NextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.POISON));
            meta.setDisplayName(ChatColor.GREEN + "Next page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            NextPage.setItemMeta(meta);

            gui.setItem(53, NextPage);
        }

        if(i-52 <= 0){

            ItemStack noNextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) noNextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
            meta.setDisplayName(ChatColor.RED + "No last page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            noNextPage.setItemMeta(meta);

            gui.setItem(45, noNextPage);
        }else{
            ItemStack NextPage = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) NextPage.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.POISON));
            meta.setDisplayName(ChatColor.GREEN + "Last page");
            meta.setLore(null);
            meta.setLocalizedName(String.valueOf(page));
            NextPage.setItemMeta(meta);

            gui.setItem(45, NextPage);
        }

        player.openInventory(gui);

    }
    public void openBorderParticleColor(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, Messages.PARTICLESETTINGSINVENTORYNAME.getMessage());

        gui.setItem(0, Utils.itemBuilder(Material.GRAY_DYE, 1,Messages.ASHPARTICLESITEMNAME.getMessage(), null, null));

        gui.setItem(2, Utils.itemBuilder(Material.LIME_DYE, 1, Messages.VILLAGERPARTICLESITEMNAME.getMessage(), null, null));

        gui.setItem(4, Utils.itemBuilder(Material.RED_DYE, 1, Messages.LAVAPARTICLESITEMNAME.getMessage(), null, null));

        gui.setItem(6, Utils.itemBuilder(Material.BLUE_DYE, 1, Messages.WATERPARTICLESITEMNAME.getMessage(), null, null));

        gui.setItem(8, Utils.itemBuilder(Material.BLACK_DYE, 1, Messages.NOPARTICLESITEMNAME.getMessage(), null, null));


        if(particleType != null && particleType == Particle.ASH){
            gui.setItem(9, KingdomInterface.activated);
        }else{
            gui.setItem(9, KingdomInterface.deactivated);
        }
        if(particleType != null && particleType == Particle.VILLAGER_HAPPY){
            gui.setItem(11, KingdomInterface.activated);
        }else{
            gui.setItem(11, KingdomInterface.deactivated);
        }
        if(particleType != null && particleType == Particle.FALLING_LAVA){
            gui.setItem(13, KingdomInterface.activated);
        }else{
            gui.setItem(13, KingdomInterface.deactivated);
        }
        if(particleType != null && particleType == Particle.FALLING_WATER){
            gui.setItem(15, KingdomInterface.activated);
        }else{
            gui.setItem(15, KingdomInterface.deactivated);
        }
        if(particleType == null){
            gui.setItem(17, KingdomInterface.activated);
        }else{
            gui.setItem(17, KingdomInterface.deactivated);
        }

        player.openInventory(gui);

    }

}
