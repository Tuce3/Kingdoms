package com.plugin.kingdoms.main;


import com.plugin.kingdoms.main.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        this.activated = KingdomInterface.activated;
        this.deactivated = KingdomInterface.deactivated;


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
    public boolean getPetInvulnerable(){
        return invulerablePets;
    }
    public void setInvulnerablePets(boolean t){
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
        Inventory gui = Bukkit.createInventory(null, 36, Messages.SETTINGSINVENTORYNAME.getMessage());


        gui.setItem(4, KingdomInterface.accessSettings);
        gui.setItem(10, KingdomInterface.viewMembers);
        gui.setItem(13, KingdomInterface.interactSettings);
        gui.setItem(16, KingdomInterface.blockSettings);
        gui.setItem(19, KingdomInterface.Pvp);
        gui.setItem(22, KingdomInterface.invulnerablePets);
        gui.setItem(25, KingdomInterface.particleColor);
        gui.setItem(28, KingdomInterface.addremoveadminsmembers);
        gui.setItem(31, KingdomInterface.enterKingdomSettings);
        gui.setItem(34, KingdomInterface.TnT);

        player.openInventory(gui);

    }

    public void openAccessSettingsGui(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.ACCESSSETTINGINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.ACCESSSETTINGSRIGHT.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.ACCESSSETTINGSRIGHT.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.ACCESSSETTINGSRIGHT.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.ACCESSSETTINGSRIGHT.getMessage()));

        player.openInventory(setActivated(gui, settings));

    }

    public void openAddMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, Messages.ADDMEMBERSSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.ADDMEMBERSRIGHT.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.ADDMEMBERSRIGHT.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.ADDMEMBERSRIGHT.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.ADDMEMBERSRIGHT.getMessage()));

        player.openInventory(setActivated(gui, addMembers));
    }

    public void openAddAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, Messages.ADDADMINSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.ADDADMINSRIGHT.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.ADDADMINSRIGHT.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.ADDADMINSRIGHT.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.ADDADMINSRIGHT.getMessage()));

        player.openInventory(setActivated(gui, addAdmins));
    }

    public void openRemAdmins(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, Messages.REMOVEADMINSSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.REMOVEADMINSRIGHT.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.REMOVEADMINSRIGHT.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.REMOVEADMINSRIGHT.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.REMOVEADMINSRIGHT.getMessage()));

        player.openInventory(setActivated(gui, removeAdmins));
    }

    public void openRemMembers(Player player){
        Inventory gui = Bukkit.createInventory(null, 27, Messages.REMOVEMEMBERSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.REMOVEMEMBERRIGHT.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.REMOVEMEMBERRIGHT.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.REMOVEMEMBERRIGHT.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.REMOVEMEMBERRIGHT.getMessage()));

        player.openInventory(setActivated(gui, removeMembers));
    }

    public void openAddRemoveMembersAdmins(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, Messages.ADDANDREMOVESETTINGSINVENTORYNAME.getMessage());


        gui.setItem(1, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.ADDADMINSITEM.getMessage(), null, null));
        gui.setItem(3, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.REMOVEADMINSITEM.getMessage(), null, null));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,Messages.REMOVEMEMBERSITEM.getMessage(), null, null));
        gui.setItem(7, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.ADDMEMBERSITEM.getMessage(), null, null));

        player.openInventory(gui);

    }

    public void openTnT(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, Messages.TNTSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(3, Utils.itemBuilder(Material.LIME_DYE, 1, Messages.TNTACTIVATEDITEM.getMessage(), null, null));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1, Messages.TNTDEACTIVATEDITEM.getMessage(), null, null));

        if(TnTActive){
            gui.setItem(12, activated);
            gui.setItem(14, deactivated);
        }else{
            gui.setItem(14, activated);
            gui.setItem(12, deactivated);
        }

        player.openInventory(gui);

    }

    public void openSeeAdminsMembers(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, Messages.ADMINSOWNERANDMEMBERSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(2, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.VIEWMEMBERSITEM.getMessage(), null, null));
        gui.setItem(4, Utils.itemBuilder(Material.RED_DYE, 1,Messages.VIEWADMINSITEM.getMessage(), null, null));
        gui.setItem(6, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.VIEWOWNER.getMessage(), null, null));

        player.openInventory(gui);

    }

    public void openDestroyPlaceBlocks(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.BLOCKSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.BLOCKSETTINGSITEMNAME.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.BLOCKSETTINGSITEMNAME.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1,Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.BLOCKSETTINGSITEMNAME.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1,Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.BLOCKSETTINGSITEMNAME.getMessage()));

        player.openInventory(setActivated(gui, destroyBlocks));

    }

    public void openInteractInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.INTERACTSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1, Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.INTERACTSETTINGSITEMNAME.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1, Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.INTERACTSETTINGSITEMNAME.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1, Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.INTERACTSETTINGSITEMNAME.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1, Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.INTERACTSETTINGSITEMNAME.getMessage()));

        player.openInventory(setActivated(gui, interact));

    }

    public void openPvPInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.PVPSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.PVPSETTINGSITEMNAME.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.PVPSETTINGSITEMNAME.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1, Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.PVPSETTINGSITEMNAME.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1, Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.PVPSETTINGSITEMNAME.getMessage()));

        player.openInventory(setActivated(gui, PvP ));

    }

    public void openDamagePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.DAMAGEPETSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.DAMAGEPETSRIGHTITEM.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.DAMAGEPETSRIGHTITEM.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1, Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.DAMAGEPETSRIGHTITEM.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1, Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.DAMAGEPETSRIGHTITEM.getMessage()));

        player.openInventory(setActivated(gui, hitPets));

    }

    public void openInvulnerablePetsInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 18, Messages.INVULNERABLEPETSSETTINGSINVENTORYNAME.getMessage());

        gui.setItem(3, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.INVULNERABLEPETS.getMessage(), null, Messages.INVULNERABLEPETSITEMLORE.getMessage()));
        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1,Messages.NOTINVULNERABLEPETS.getMessage(), null, Messages.NOTINVULNERABLEPETSITEMLORE.getMessage()));

        if(invulerablePets){
            gui.setItem(12, activated);
        }else{
            gui.setItem(12, deactivated);
        }
        if(!invulerablePets){
            gui.setItem(14, activated);
        }else{
            gui.setItem(14, deactivated);
        }

        player.openInventory(gui);

    }

    public void openPetInventory(Player player){

        Inventory gui = Bukkit.createInventory(null, 9, Messages.PETSETTINGSINVENTORY.getMessage());

        gui.setItem(3, Utils.itemBuilder(Material.YELLOW_DYE, 1, Messages.INVULNERABLEPETSSETTINGSINVENTORYITEMNAME.getMessage(), null, Messages.MANAGEIFPETSSHOULDBEINVULNERABLE.getMessage()));

        gui.setItem(5, Utils.itemBuilder(Material.RED_DYE, 1, Messages.DAMAGEPETSSETTINGS.getMessage(), null, Messages.WHOCANHITINVULNERABLEPETSETTINGSITEM.getMessage()));

        player.openInventory(gui);
    }

    public void openEnterKingdom(Player player){

        Inventory gui = Bukkit.createInventory(null, 27, Messages.ENTERKINGDOMSETTINGS.getMessage());

        gui.setItem(10, Utils.itemBuilder(Material.YELLOW_DYE, 1,Messages.EVERYONEITEMNAME.getMessage(), null, Messages.EVERYONERIGHTITEMLORE.getMessage()+Messages.ENTERKINGDOMRIGHTS.getMessage()));
        gui.setItem(12, Utils.itemBuilder(Material.ORANGE_DYE, 1,Messages.MEMBERSITEMNAME.getMessage(), null, Messages.MEMBERSPLUSRIGHTITEMLORE.getMessage()+Messages.ENTERKINGDOMRIGHTS.getMessage()));
        gui.setItem(14, Utils.itemBuilder(Material.RED_DYE, 1, Messages.ADMINSITEMNAME.getMessage(), null, Messages.ADMINSANDOWNERRIGHTITEMLORE.getMessage()+Messages.ENTERKINGDOMRIGHTS.getMessage()));
        gui.setItem(16, Utils.itemBuilder(Material.GREEN_DYE, 1, Messages.OWNERITEMNAME.getMessage(), null, Messages.OWNERRIGHTITEMLORE.getMessage()+Messages.ENTERKINGDOMRIGHTS.getMessage()));

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
