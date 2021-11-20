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

    ItemStack activated = Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null);

    ItemStack deactivated = Utils.itemBuilder(Material.GRAY_DYE, 1, ChatColor.GREEN + "Deactivated", null, null);


    ItemStack viewMembers = Utils.itemBuilder(Material.PLAYER_HEAD, 1, ChatColor.GOLD + "View Members and Admins", null, null);

    ItemStack accessSettings = Utils.itemBuilder(Material.BARRIER, 1, ChatColor.GOLD + "Settings", null, ChatColor.DARK_GREEN + "Manage who can change", ChatColor.DARK_GREEN + "settings");

    ItemStack blockSettings = Utils.itemBuilder(Material.COBBLESTONE, 1, ChatColor.GOLD + "Destroy/place Blocks", null, ChatColor.DARK_GREEN + "Manage who can place/destroy blocks");

    ItemStack Pvp = Utils.itemBuilder(Material.WOODEN_SWORD, 1, ChatColor.GOLD+"PvP settings", null, ChatColor.DARK_GREEN + "Manage who can PvP");

    ItemStack addremoveadminsmembers = Utils.itemBuilder(Material.SKELETON_SKULL, 1, ChatColor.GOLD+"Remove/add roles", null, ChatColor.DARK_GREEN + "Manage who can add/remove", ChatColor.DARK_GREEN + "Admin and Member rights");

    ItemStack enterKingdomSettings = Utils.itemBuilder(Material.LEATHER_BOOTS, 1, ChatColor.GOLD + "Access settings", null, ChatColor.DARK_GREEN +"Change who can ", ChatColor.DARK_GREEN +"enter the kingdom");

    ItemStack TnT = Utils.itemBuilder(Material.TNT, 1, ChatColor.GOLD + "TnT", null, ChatColor.DARK_GREEN + "Manage if TnT should explode");

    ItemStack particleColor = Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GOLD + "Particle Color", null, ChatColor.DARK_GREEN + "Change the Particle Color");

    ItemStack invulnerablePets = Utils.itemBuilder(Material.NAME_TAG, 1, ChatColor.GOLD + "Pets", null, ChatColor.DARK_GREEN + "Manage Pets");

    ItemStack interactSettings = Utils.itemBuilder(Material.SPRUCE_SIGN, 1, ChatColor.GOLD + "Interact", null, ChatColor.DARK_GREEN + "Manage who can ", ChatColor.DARK_GREEN + "Right click and hit Mobs");

}
