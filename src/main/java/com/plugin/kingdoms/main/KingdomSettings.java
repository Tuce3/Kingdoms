package com.plugin.kingdoms.main;


import com.plugin.kingdoms.main.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KingdomSettings implements KingdomInterface{

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
    private ItemStack deactivated;
    private ItemStack activated;


    protected KingdomSettings(){

        //Standard permissions
        settings = KingdomInterface.settings;
        destroyBlocks = KingdomInterface.destroyBlocks;
        addAdmins = KingdomInterface.addAdmins;
        addMembers = KingdomInterface.addMembers;
        removeAdmins = KingdomInterface.removeAdmins;
        removeMembers = KingdomInterface.removeMembers;
        interact = KingdomInterface.interact;
        PvP = KingdomInterface.PvP;
        TnTActive = KingdomInterface.TnTActive;
        invulerablePets = KingdomInterface.invulerablePets;
        hitPets = KingdomInterface.hitPets;
        enterKingdom = KingdomInterface.enterKingdom;
        this.activated = KingdomInterface.activated;
        this.deactivated = KingdomInterface.deactivated;



    }

    protected  KingdomSettings(int interact, int destroyBlocks, int settings, int addAdmins, int addMembers, int removeAdmins, int removeMembers, int pvp, boolean pets, int hitPets, int enterKingdom, boolean tnTActive){

         this.TnTActive = tnTActive;
         this.enterKingdom = enterKingdom;
         this.hitPets = hitPets;
         this.invulerablePets = pets;
         this.PvP = pvp;
         this.removeMembers = removeMembers;
         this.removeAdmins = removeAdmins;
         this.addMembers = addMembers;
         this.addAdmins = addAdmins;
         this.settings = settings;
         this.destroyBlocks = destroyBlocks;
         this.interact = interact;



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

        player.openInventory(setActivated(gui, settings));

    }

    public void openAddMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Add Members settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can add Members"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can add Members"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can add Members"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can add Members"));

        player.openInventory(setActivated(gui, addMembers));
    }

    public void openAddAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Add Admins settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can add Admins"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can add Admins"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can add Admins"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can add Admins"));

        player.openInventory(setActivated(gui, addAdmins));
    }

    public void openRemAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Remove Admins settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can remove Admins"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can remove Admins"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can remove Admins"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can remove Admins"));

        player.openInventory(setActivated(gui, removeAdmins));
    }

    public void openRemMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Remove Members settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can remove Members"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can remove Members"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can remove Members"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can remove Members"));

        player.openInventory(setActivated(gui, removeMembers));
    }

    public void openAddRemoveMembersAdmins(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Add and remove settings");


        gui.setItem(1, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Add Admins", null, null));
        gui.setItem(3, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Remove Admins", null, null));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Remove Members", null, null));
        gui.setItem(7, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Add Members", null, null));

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


    public void openDestroyPlaceBlocks(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Block settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can destory/place blocks"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can destory/place blocks"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can adestory/place blocks"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can destory/place blocks"));

        player.openInventory(setActivated(gui, destroyBlocks));

    }

    public void openInteractInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Interact settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can Interact"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can Interact"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can Interact"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can Interact"));

        player.openInventory(setActivated(gui, interact));

    }

    public void openPvPInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "PvP settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can hit players"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can hit players"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can hit players"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can hit players"));

        player.openInventory(setActivated(gui, PvP ));

    }

    public void openDamagePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Damage Pets settings");

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Everyone", null, "Everyone can damage Pets"));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,ChatColor.GOLD + "Members", null, "Members+ can damage Pets"));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Admins", null, "Admins and the Owner can damage Pets"));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,ChatColor.GREEN + "Owner", null, "Only the Owner can damage Pets"));

        player.openInventory(setActivated(gui, hitPets));

    }

    public void openInvulnerablePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, ChatColor.GOLD + "Invulnerable Pets settings");

        gui.setItem(3, Utils.itemBuilder(Material.YELLOW_DYE, 1,ChatColor.YELLOW + "Invulnerable", null, "Pets are Invulerable"));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,ChatColor.RED + "Not Invulerable", null, "Pets are not Invulerable"));

        if(invulerablePets){
            gui.setItem(12, activated);
        }else{
            gui.setItem(12, Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null));
        }
        if(!invulerablePets){
            gui.setItem(14, activated);
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

        player.openInventory(setActivated(gui, enterKingdom));

    }

    private Inventory setActivated(Inventory gui, int setting){
        if(setting == 1){
            gui.setItem(19, activated);
        }else{
            gui.setItem(19, deactivated);
        }
        if(setting == 2){
            gui.setItem(21, activated);
        }else{
            gui.setItem(21, deactivated);
        }
        if(setting == 3){
            gui.setItem(23, activated);
        }else{
            gui.setItem(23, deactivated);
        }
        if(setting == 4){
            gui.setItem(25, activated);
        }else{
            gui.setItem(25, deactivated);
        }

        return gui;
    }


}
