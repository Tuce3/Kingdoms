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
    private Particle particleType;
    private int particleAmount;
    private ArrayList<Location> ParticleLocations;
    private int particleRunnableId;
    private ArrayList<Location> portLocations;


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

    public Kingdom(UUID owner, Location minLocation, Location maxLocation, Particle type, int particleAmount, int interact, int destroyBlocks, int settings, int addAdmins, int addMembers, int removeAdmins, int removeMembers, int pvp, boolean pets, int hitPets, int enterKingdom, ArrayList<UUID> admins, ArrayList<UUID> members, boolean tnTActive){

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

    }

    public void setMinLocation(Location location, boolean dataload){
        this.minLocation = location;
        if(Bukkit.getPlayer(owner) != null && !dataload){
            Bukkit.getPlayer(owner).sendMessage(ChatColor.GREEN  + "First location set!");
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

        if(!dataload) {
            if (Kingdoms.getManager().getPlayersInKingdoms().containsKey(owner)) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can't build a new Kingdom while in a Kingdom!");
                    return;
                }
            }
            if (minLocation.getWorld() != maxLocation.getWorld()) {
                Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can't set the Locations in different Worlds!");
                return;
            }

            boolean kingdomCantBeCreated = false;

            for (Kingdom k : Kingdoms.getManager().getKingdomList()) {
                if (k.getArea().containsLocation(area.getMaxMaxLocation()) || k.getArea().containsLocation(area.getMinMaxLocation()) || k.getArea().containsLocation(area.getMaxMinLocation()) || k.getArea().containsLocation(area.getMinLocation())) {
                    kingdomCantBeCreated = true;
                }
                if (area.containsLocation(k.getArea().getMinLocation()) || area.containsLocation(k.getArea().getMinMaxLocation()) || area.containsLocation(k.getArea().getMaxMinLocation()) || area.containsLocation(k.getArea().getMaxMinLocation())) {
                    kingdomCantBeCreated = true;
                }
            }

            if (kingdomCantBeCreated) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You cant create a new Kingdom here!");
                    return;
                }
            }

            if ((area.getMaxMaxLocation().getBlockZ() - area.getMaxMinLocation().getBlockZ()) * (area.getMaxMaxLocation().getBlockX() - area.getMinMaxLocation().getBlockX()) > Kingdoms.getManager().getMaxBlocks()) {
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "Your Kingdom is too big!");
                    if (Bukkit.getPlayer(owner).isOp()) {
                        Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can change how big a Kingdom can be with /kingdom settings setmaxblocks <number>");
                    }
                    return;
                }

            }
            Kingdoms.getManager().addKingdomToList(this);
        }
        calculateParticleLocation();

        if(Bukkit.getPlayer(owner) != null){
            Bukkit.getPlayer(owner).sendMessage(ChatColor.GREEN + "Kingdom created!");
        }
        spawnParticles();
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
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMaxMaxLocation().getBlockX(), 0, area.getMinLocation().getBlockZ()+i ));
            portLocations.add(new Location(minLocation.getWorld(), area.getMaxMaxLocation().getBlockX()+2, 0, area.getMinLocation().getBlockZ()+i ));
        }
        for(int i = 0; i<area.getMaxMaxLocation().getBlockX()-area.getMinMaxLocation().getBlockX(); i++){
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()+i, 0, area.getMaxMaxLocation().getBlockZ()));
            portLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()+i, 0, area.getMaxMaxLocation().getBlockZ()+2));
        }

        for(int i = 0; i<area.getMinMaxLocation().getBlockZ()-area.getMinLocation().getBlockZ(); i++){
            ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX(), 0, area.getMinLocation().getBlockZ()+i ));
            portLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()-2, 0, area.getMinLocation().getBlockZ()+i ));
        }
        for(int i = 0; i<area.getMaxMinLocation().getBlockX()-area.getMinLocation().getBlockX(); i++){
            if(ParticleLocations.contains(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()+i, 0, area.getMinLocation().getBlockZ()))){
                ParticleLocations.add(area.getMaxMaxLocation());
            }else{
                ParticleLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()+i, 0, area.getMinLocation().getBlockZ()));
                portLocations.add(new Location(minLocation.getWorld(), area.getMinLocation().getBlockX()+i, 0, area.getMinLocation().getBlockZ()-2));
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

    public void openSeeOwner(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Owner settings");

        gui.setItem(4, Utils.givePlayerHead(owner));

        player.openInventory(gui);

    }
    public void openSeeAdmins(Player player, int page){

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Admins settings");

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

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Members settings");

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

        Inventory gui = Bukkit.createInventory(null, 18, ChatColor.GOLD + "Particle settings");

        gui.setItem(0, Utils.itemBuilder(Material.GRAY_DYE, 1,ChatColor.GRAY + "Ash particles", null, null));

        gui.setItem(2, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Villager particles", null, null));

        gui.setItem(4, Utils.itemBuilder(Material.RED_DYE, 1, ChatColor.RED + "Lava particles", null, null));

        gui.setItem(6, Utils.itemBuilder(Material.BLUE_DYE, 1, ChatColor.BLUE + "Water particles", null, null));

        gui.setItem(8, Utils.itemBuilder(Material.BLACK_DYE, 1, "No particles", null, null));


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
