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

public class Kingdom {

    private final UUID owner;
    private Location minLocation;
    private Location maxLocation;
    private Cuboid2d area;
    private Particle particleType;
    private int particleAmount;
    private final ArrayList<Location> ParticleLocations;
    private int particleRunnableId;
    private final ArrayList<Location> portLocations;

    //Permissions
    //1-Everyone, 2-Members+, 3-Admins+, 4- only owner
    private int interact;
    private int destroyBlocks;
    private int settings;
    private int addAdmins;
    private int addMembers;
    private int removeAdmins;
    private int removeMembers;
    private int PvP;
    private boolean TnTActive;
    private boolean invulerablePets;
    private int hitPets;
    private int enterKingdom;

    private final ArrayList<UUID> admins;
    private final ArrayList<UUID> members;

    public Kingdom(UUID owner){

        this.owner = owner;
        ParticleLocations = new ArrayList<>();
        particleType = Particle.ASH;
        admins = new ArrayList<>();
        members = new ArrayList<>();
        particleAmount = 10;
        portLocations = new ArrayList<>();


        //Standard permissions
        settings = 4;
        destroyBlocks = 2;
        addAdmins = 4;
        addMembers = 3;
        removeAdmins = 4;
        removeMembers = 3;
        interact = 2;
        PvP = 1;
        TnTActive = true;
        invulerablePets = false;
        hitPets = 3;
        enterKingdom = 1;

    }

    public void setMinLocation(Location location){
        this.minLocation = location;
        if(Bukkit.getPlayer(owner) != null){
            Bukkit.getPlayer(owner).sendMessage(ChatColor.GREEN  + "First location set!");
        }
    }
    public void setMaxLocation(Location location){
        this.maxLocation = location;

        if(minLocation != null && maxLocation != null){
            makeKingdom();
        }
    }

    private void makeKingdom(){
        KingdomManager.getUnfinishedKingdomList().remove(this);

        if(KingdomManager.getPlayersInKingdoms().containsKey(owner)){
            if(Bukkit.getPlayer(owner) != null){
                Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can't build a new Kingdom while in a Kingdom!");
                return;
            }
        }
        if(minLocation.getWorld() != maxLocation.getWorld()){
            Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can't set the Locations in different Worlds!");
            return;
        }

        boolean kingdomCantBeCreated = false;

        for(Kingdom k : KingdomManager.getKingdomList()){
            if(k.getArea().containsLocation(area.getMaxMaxLocation()) || k.getArea().containsLocation(area.getMinMaxLocation()) || k.getArea().containsLocation(area.getMaxMinLocation()) || k.getArea().containsLocation(area.getMinLocation())){
                kingdomCantBeCreated = true;
            }
            if(area.containsLocation(k.getArea().getMinLocation()) || area.containsLocation(k.getArea().getMinMaxLocation()) || area.containsLocation(k.getArea().getMaxMinLocation()) || area.containsLocation(k.getArea().getMaxMinLocation())){
                kingdomCantBeCreated = true;
            }
        }

        if(kingdomCantBeCreated){
            if(Bukkit.getPlayer(owner) != null){
                Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You cant create a new Kingdom here!");
                return;
            }
        }

        area = new Cuboid2d(minLocation, maxLocation);

        if((area.getMaxMaxLocation().getBlockZ()-area.getMaxMinLocation().getBlockZ())*(area.getMaxMaxLocation().getBlockX()-area.getMinMaxLocation().getBlockX()) > KingdomManager.getMaxBlocks()){
            if(Bukkit.getPlayer(owner) != null){
                Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "Your Kingdom is too big!");
                if(Bukkit.getPlayer(owner).isOp()){
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.RED + "You can change how big a Kingdom can be with /kingdom settings setmaxblocks <number>");
                }
                return;
            }

        }

        if(Bukkit.getPlayer(owner) != null){
            Bukkit.getPlayer(owner).sendMessage(ChatColor.GREEN + "Kingdom created!");
        }

        KingdomManager.getKingdomList().add(this);
        calculateParticleLocation();
        spawnParticles();

    }

    private void spawnParticles(){

        particleRunnableId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
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

    public int getSettings(){
        return settings;
    }
    public void setSettings(int settings1){
        settings = settings1;
    }
    public int getAddAdmins(){
        return addAdmins;
    }
    public void setAddAdmins(int add){
        addAdmins = add;
    }
    public int getAddMembers(){
        return addMembers;
    }
    public void setAddMembers(int add){
        addMembers = add;
    }
    public int getRemoveAdmins(){
        return removeAdmins;
    }
    public void setRemoveAdmins(int rem){
        removeAdmins = rem;
    }
    public int getRemoveMembers(){
        return removeMembers;
    }
    public void setRemoveMembers(int rem){
        removeMembers = rem;
    }
    public void setParticleType(Particle p){
        particleType = p;
    }
    public void setParticleAmount(int p){
        particleAmount = p;
    }
    public void setTnTActive(boolean b){
        TnTActive = b;
    }
    public boolean getTnTActive(){
        return TnTActive;
    }
    public void setDestroyBlocks(int d){
        destroyBlocks = d;
    }
    public int getDestroyBlocks(){
        return destroyBlocks;
    }
    public void setInteract(int i){
        interact = i;
    }
    public int getInteract(){
        return interact;
    }
    public int getPvP(){
        return PvP;
    }
    public void setPvP(int p){
        PvP = p;
    }
    public int getHitPets(){
        return hitPets;
    }
    public boolean getPetInvulerable(){
        return invulerablePets;
    }
    public void setInvulerablePets(boolean t){
        invulerablePets = t;
    }
    public void setDamagePets(int p){
        hitPets = p;
    }
    public int getEnterKingdom(){
        return enterKingdom;
    }
    public void setEnterKingdom(int e){
        enterKingdom = e;
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

    public void openSettingsGui(Player player){
        Inventory gui = Bukkit.createInventory(null, 36, ChatColor.GOLD + "settings");

        ItemStack viewMembers = Utils.itemBuilder(Material.PLAYER_HEAD, 1, ChatColor.GOLD + "View Members and Admins", null, null);

        ItemStack accessSettings = Utils.itemBuilder(Material.BARRIER, 1, ChatColor.GOLD + "Settings", null, ChatColor.DARK_GREEN + "Manage who can change", ChatColor.DARK_GREEN + "settings");

        ItemStack destroyBlocks = Utils.itemBuilder(Material.COBBLESTONE, 1, ChatColor.GOLD + "Destroy/place Blocks", null, ChatColor.DARK_GREEN + "Manage who can place/destroy blocks");

        ItemStack Pvp = Utils.itemBuilder(Material.WOODEN_SWORD, 1, ChatColor.GOLD+"PvP settings", null, ChatColor.DARK_GREEN + "Manage who can PvP");

        ItemStack addremoveadminsmembers = Utils.itemBuilder(Material.SKELETON_SKULL, 1, ChatColor.GOLD+"Remove/add roles", null, ChatColor.DARK_GREEN + "Manage who can add/remove", ChatColor.DARK_GREEN + "Admin and Member rights");

        ItemStack enterKingdom = Utils.itemBuilder(Material.LEATHER_BOOTS, 1, ChatColor.GOLD + "Access settings", null, ChatColor.DARK_GREEN +"Change who can ", ChatColor.DARK_GREEN +"enter the kingdom");

        ItemStack TnT = Utils.itemBuilder(Material.TNT, 1, ChatColor.GOLD + "TnT", null, ChatColor.DARK_GREEN + "Manage if TnT should explode");

        ItemStack particleColor = Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GOLD + "Particle Color", null, ChatColor.DARK_GREEN + "Change the Particle Color");

        ItemStack invulnerablePets = Utils.itemBuilder(Material.NAME_TAG, 1, ChatColor.GOLD + "Pets", null, ChatColor.DARK_GREEN + "Manage Pets");

        ItemStack interact = Utils.itemBuilder(Material.SPRUCE_SIGN, 1, ChatColor.GOLD + "Interact", null, ChatColor.DARK_GREEN + "Manage who can ", ChatColor.DARK_GREEN + "Right click and hit Mobs");

        gui.setItem(4, accessSettings);
        gui.setItem(10, viewMembers);
        gui.setItem(13, interact);
        gui.setItem(16, destroyBlocks);
        gui.setItem(19, Pvp);
        gui.setItem(22, invulnerablePets);
        gui.setItem(25, particleColor);
        gui.setItem(28, addremoveadminsmembers);
        gui.setItem(31, enterKingdom);
        gui.setItem(34, TnT);

        player.openInventory(gui);

    }

    public void openAccessSettingsGui(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Access settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can access the settings"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can access the settings"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can access the settings"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can access the settings"));

        if(settings == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(settings == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(settings == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(settings == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openAddMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Add Members settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can add Members"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can add Members"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can add Members"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can add Members"));

        if(addMembers == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addMembers == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addMembers == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addMembers == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);
    }

    public void openAddAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Add Admins settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can add Admins"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can add Admins"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can add Admins"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can add Admins"));

        if(addAdmins == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addAdmins == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addAdmins == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(addAdmins == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);
    }

    public void openRemAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Remove Admins settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can remove Admins"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can remove Admins"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can remove Admins"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can remove Admins"));

        if(removeAdmins == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeAdmins == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeAdmins == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeAdmins == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);
    }

    public void openRemMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Remove Members settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can remove Members"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can remove Members"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can remove Members"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can remove Members"));

        if(removeMembers == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeMembers == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeMembers == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(removeMembers == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);
    }

    public void openAddRemoveMembersAdmins(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Add and remove settings");


        gui.setItem(1, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Add Admins", null, null));
        gui.setItem(3, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Remove Admins", null, null));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Remove Members", null, null));
        gui.setItem(7, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Add Members", null, null));

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
            gui.setItem(9, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(9, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(particleType != null && particleType == Particle.VILLAGER_HAPPY){
            gui.setItem(11, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(11, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(particleType != null && particleType == Particle.FALLING_LAVA){
            gui.setItem(13, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(13, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(particleType != null && particleType == Particle.FALLING_WATER){
            gui.setItem(15, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(15, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(particleType == null){
            gui.setItem(17, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(17, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openTnT(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, ChatColor.GOLD + "TnT settings");

        gui.setItem(3, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activate", null, null));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1, ChatColor.GREEN + "Deactivate", null, null));

        if(TnTActive){
            gui.setItem(12, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
            gui.setItem(14, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }else{
            gui.setItem(14, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
            gui.setItem(12, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openSeeAdminsMembers(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Admins, Owner and Members settings");

        gui.setItem(2, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, null));
        gui.setItem(4, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, null));
        gui.setItem(6, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, null));

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

    public void openSeeOwner(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Owner settings");

        gui.setItem(4, Utils.givePlayerHead(owner));

        player.openInventory(gui);

    }

    public void openDestroyPlaceBlocks(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Block settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can destory/place blocks"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can destory/place blocks"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can adestory/place blocks"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can destory/place blocks"));

        if(destroyBlocks == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(destroyBlocks == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(destroyBlocks == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(destroyBlocks == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openInteractInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Interact settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can Interact"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can Interact"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can Interact"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can Interact"));

        if(interact == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(interact == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(interact == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(interact == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openPvPInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "PvP settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can hit players"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can hit players"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can hit players"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can hit players"));

        if(PvP == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(PvP == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(PvP == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(PvP == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openDamagePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Damage Pets settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can damage Pets"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can damage Pets"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can damage Pets"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can damage Pets"));

        if(hitPets== 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(hitPets == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(hitPets == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(hitPets == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openInvulnerablePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, ChatColor.GOLD + "Invulnerable Pets settings");

        gui.setItem(3, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Invulnerable", null, "Pets are Invulerable"));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Not Invulerable", null, "Pets are not Invulerable"));

        if(invulerablePets){
            gui.setItem(12, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(12, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(!invulerablePets){
            gui.setItem(14, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(14, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

    public void openPetInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Pet settings");

        gui.setItem(3, Utils.itemBuilder(Material.YELLOW_DYE, 1, ChatColor.YELLOW + "Invulnerable Pets", null, ChatColor.DARK_GREEN + "Manage if Pets should be Invulerable"));

        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1, ChatColor.RED + "Damage Pets", null, ChatColor.DARK_GREEN + "Manage who can damage invulnerable Pets"));

        player.openInventory(gui);
    }

    public void openEnterKingdom(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Enter Kingdom settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can enter the Kingdom"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can enter the Kingdom"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can enter the Kingdom"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can enter the Kingdom"));

        if(enterKingdom == 1){
            gui.setItem(19, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(19, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(enterKingdom == 2){
            gui.setItem(21, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(21, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(enterKingdom == 3){
            gui.setItem(23, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(23, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(enterKingdom == 4){
            gui.setItem(25, Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null));
        }else{
            gui.setItem(25, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }

        player.openInventory(gui);

    }

}
