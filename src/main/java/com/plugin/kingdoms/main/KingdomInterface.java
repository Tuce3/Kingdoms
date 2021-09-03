package com.plugin.kingdoms.main;


import com.plugin.kingdoms.main.Utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface KingdomInterface {

    int interact = 2;
    int destroyBlocks = 2;
    int settings = 4;
    int addAdmins = 4;
    int addMembers = 3;
    int removeAdmins = 4;
    int removeMembers = 3;
    int PvP = 1;
    boolean TnTActive = true;
    boolean invulerablePets = false;
    int hitPets = 3;
    int enterKingdom = 1;

    ItemStack activated = Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null);

    ItemStack deactivated = Utils.itemBuilder(Material.GRAY_DYE, 1, ChatColor.GREEN + "Deactivated", null, null);


}
