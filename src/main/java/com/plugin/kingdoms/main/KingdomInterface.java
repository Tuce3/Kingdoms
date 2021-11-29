package com.plugin.kingdoms.main;


import com.plugin.kingdoms.main.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface KingdomInterface {

    int interact = Kingdoms.getInstance().getConfig().getInt("interact");
    int destroyBlocks = Kingdoms.getInstance().getConfig().getInt("destroyBlocks");
    int settings = Kingdoms.getInstance().getConfig().getInt("settings");
    int addAdmins = Kingdoms.getInstance().getConfig().getInt("addAdmins");
    int addMembers = Kingdoms.getInstance().getConfig().getInt("addMembers");
    int removeAdmins = Kingdoms.getInstance().getConfig().getInt("removeAdmins");
    int removeMembers = Kingdoms.getInstance().getConfig().getInt("removeMembers");
    int PvP = Kingdoms.getInstance().getConfig().getInt("PvP");
    boolean TnTActive = Kingdoms.getInstance().getConfig().getBoolean("TnTActive");
    boolean invulerablePets = Kingdoms.getInstance().getConfig().getBoolean("invulnerablePets");
    int hitPets = Kingdoms.getInstance().getConfig().getInt("hitPets");
    int enterKingdom = Kingdoms.getInstance().getConfig().getInt("enterKingdom");

    ItemStack activated = Utils.itemBuilder(Material.LIME_DYE, 1, Messages.ACTIVATEDITEM.getMessage(), null, null);

    ItemStack deactivated = Utils.itemBuilder(Material.GRAY_DYE, 1, Messages.DEACTIVATEDITEM.getMessage(), null, null);

    ItemStack viewMembers = Utils.itemBuilder(Material.PLAYER_HEAD, 1, Messages.VIEWADMINSANDMEMBERSITEM.getMessage(), null, null);

    ItemStack accessSettings = Utils.itemBuilder(Material.BARRIER, 1, Messages.SETTINGSITEM.getMessage(), null, Messages.SETTINGSLOREITEM.getMessage());

    ItemStack blockSettings = Utils.itemBuilder(Material.COBBLESTONE, 1, Messages.DESTROYPLACEBLOCKSITEM.getMessage(), null, Messages.DESTROYPLACEBLOCKLOREITEM.getMessage());

    ItemStack Pvp = Utils.itemBuilder(Material.WOODEN_SWORD, 1, Messages.PVPSETTINGSITEM.getMessage(), null, Messages.PVPSETTINGSLOREITEM.getMessage());

    ItemStack addremoveadminsmembers = Utils.itemBuilder(Material.SKELETON_SKULL, 1,Messages.REMOVEADDROLESITEM.getMessage(), null, Messages.REMOVEADDROLESLORE1ITEM.getMessage(),Messages.REMOVEADDROLESLORE2ITEM.getMessage() );

    ItemStack enterKingdomSettings = Utils.itemBuilder(Material.LEATHER_BOOTS, 1, Messages.ACCESSSETTINGSITEM.getMessage(), null, Messages.ACCESSSETTINGSLORE1ITEM.getMessage(), Messages.ACCESSSETTINGSLORE2ITEM.getMessage());

    ItemStack TnT = Utils.itemBuilder(Material.TNT, 1, Messages.TNTITEM.getMessage(), null, Messages.TNTLOREITEM.getMessage());

    ItemStack particleColor = Utils.itemBuilder(Material.LIME_DYE, 1, Messages.PARTICLEITEM.getMessage(), null, Messages.PARTICLELOREITEM.getMessage());

    ItemStack invulnerablePets = Utils.itemBuilder(Material.NAME_TAG, 1, Messages.PETSITEM.getMessage(), null, Messages.PETSLOREITEM.getMessage());

    ItemStack interactSettings = Utils.itemBuilder(Material.SPRUCE_SIGN, 1,Messages.INTERACTITEM.getMessage(), null, Messages.INTERACTLORE1ITEM.getMessage(), Messages.INTERACTLORE2ITEM.getMessage());

}
