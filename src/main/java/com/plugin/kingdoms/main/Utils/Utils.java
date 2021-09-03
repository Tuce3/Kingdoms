package com.plugin.kingdoms.main.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Utils {

    public static ItemStack giveHead(String headCode){

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 64);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", headCode));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x) {
            x.printStackTrace();
        }


        skull.setItemMeta(meta);
        skull.getItemMeta().toString();
        return skull;
    }

    public static ItemStack givePlayerHead(UUID id){

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        meta.setDisplayName(Bukkit.getOfflinePlayer(id).getName());


        skull.setItemMeta(meta);
        return skull;
    }

    public static Inventory fillWithItem(Inventory inv, ItemStack filler, ArrayList<Integer> list) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null && !list.contains(i)) {
                inv.setItem(i, filler);
            }
        }

        return inv;
    }

    public static Inventory fillWithItem(Inventory inv, ItemStack filler) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }

        return inv;
    }

    public static ItemStack itemBuilder(@NotNull Material mat, @NotNull int amount, @Nullable String displayName, @Nullable String locName, @Nullable String... lore){
        ItemStack stack =  new ItemStack(mat, amount);
        ItemMeta meta = stack.getItemMeta();
        if(displayName != null){
            meta.setDisplayName(displayName);
        }if(locName != null){
            meta.setLocalizedName(locName);
        }if(lore != null){
            meta.setLore(Arrays.asList(lore));
        }

        stack.setItemMeta(meta);
        return stack;

    }

    public static ItemStack giveActivatedItem(){
        return itemBuilder(Material.LIME_DYE, 1, ChatColor.GREEN + "Activated", null, null);
    }

    public static ItemStack giveDeactivatedItem(){
        return Utils.itemBuilder(Material.BLACK_CONCRETE, 1, ChatColor.GREEN + "Deactivated", null, null);
    }
}

