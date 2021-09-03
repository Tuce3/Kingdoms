package com.plugin.kingdoms.main;

import com.plugin.kingdoms.main.Utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

public class KingdomListener implements Listener {

    private void checkAllowed(int settings, Cancellable c, Player p, Kingdom k){
        if(k.getAdmins().contains(p.getUniqueId())){
            if(settings == 4){
                c.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You don't have the rights to perform this action!");
            }
        }else if(k.getOwner().equals(p.getUniqueId())){
            if(settings > 4){
                c.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You don't have the rights to perform this action!");
            }
        }else if(k.getMembers().contains(p.getUniqueId())){
            if(settings > 2){
                c.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You don't have the rights to perform this action!");
            }
        }else{
            if(settings > 1){
                c.setCancelled(true);
                p.sendMessage(ChatColor.RED + "You don't have the rights to perform this action!");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() == EquipmentSlot.HAND){
            for(int i = 0; i< Kingdoms.getManager().getUnfinishedKingdomList().size(); i++){
                Kingdom k = Kingdoms.getManager().getUnfinishedKingdomList().get(i);
                if(k.getOwner().equals(e.getPlayer().getUniqueId())){
                    if(k.getMinLocation() == null){
                        k.setMinLocation(e.getClickedBlock().getLocation(), false);
                    }else if(k.getMaxLocation() == null){
                        k.setMaxLocation(e.getClickedBlock().getLocation(), false);
                    }
                }
            }
        }

        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation()) || (e.getClickedBlock() != null && k.getArea().containsLocation(e.getClickedBlock().getLocation()))){
                checkAllowed(k.getInteract(), e,e.getPlayer(), k);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Kingdom kingdomPlayerIsIn = null;
        Kingdom kingdomPlayerIsInNow = null;
        Player player = e.getPlayer();

        if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(e.getPlayer().getUniqueId())){
            kingdomPlayerIsIn = Kingdoms.getManager().getPlayersInKingdoms().get(e.getPlayer().getUniqueId());
        }

        for(int i = 0; i< Kingdoms.getManager().getKingdomList().size(); i++){
            Kingdom k = Kingdoms.getManager().getKingdomList().get(i);
            boolean isAllowed = true;
            if(k.getAdmins().contains(player.getUniqueId())){
                if(k.getEnterKingdom() == 4){
                    isAllowed = false;
                }
            }else if(k.getMembers().contains(player.getUniqueId())){
                if(k.getEnterKingdom() > 2){
                    isAllowed = false;
                }
            }else if(k.getOwner().equals(player.getUniqueId())){
                if(k.getEnterKingdom() > 4){
                    isAllowed = false;
                }
            }else{
                if(k.getEnterKingdom() > 1){
                    isAllowed = false;
                }
            }

            if(k.getArea().containsLocation(e.getPlayer().getLocation().getBlock().getLocation()) && isAllowed){
                kingdomPlayerIsInNow = k;
            }else if(k.getArea().containsLocation(e.getPlayer().getLocation().getBlock().getLocation()) && !isAllowed){
                Location portLocation = player.getLocation();
                double smallestDistance = -5;
                for(Location loc : k.getPortLocations()){
                    if(smallestDistance == -5){
                        smallestDistance = loc.distance(player.getLocation());
                        portLocation = loc;
                    }else{
                        if(smallestDistance > loc.distance(player.getLocation())){
                            smallestDistance = loc.distance(player.getLocation());
                            portLocation = loc;
                        }
                    }
                }

                player.teleport(new Location(portLocation.getWorld(), portLocation.getBlockX(), portLocation.getWorld().getHighestBlockAt(portLocation).getLocation().getY()+ 1, portLocation.getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                e.getPlayer().sendMessage(ChatColor.RED + "You don't have the rights to enter this Kingdom!");
            }
        }

        if(kingdomPlayerIsIn != null || kingdomPlayerIsInNow != null){
            if(kingdomPlayerIsIn == null && kingdomPlayerIsInNow != null){
                Kingdoms.getManager().getPlayersInKingdoms().put(e.getPlayer().getUniqueId(), kingdomPlayerIsInNow);
                e.getPlayer().sendTitle(ChatColor.AQUA + "You entered a Kingdom", ChatColor.AQUA + "of " + ChatColor.AQUA + Bukkit.getOfflinePlayer(kingdomPlayerIsInNow.getOwner()).getName(), 10, 80, 10);
            }else if(kingdomPlayerIsIn != null && kingdomPlayerIsInNow == null){
                e.getPlayer().sendTitle(ChatColor.AQUA + "You left a Kingdom of", ChatColor.AQUA + Bukkit.getOfflinePlayer(kingdomPlayerIsIn.getOwner()).getName(), 10, 80, 10);
                Kingdoms.getManager().getPlayersInKingdoms().remove(e.getPlayer().getUniqueId());
            }

        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e){
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        if(e.getView().getTitle().contains(ChatColor.GOLD+ "settings") || e.getView().getTitle().contains("settings")){
            e.setCancelled(true);

            if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())) {
                Kingdom k = Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId());
                if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "settings")){
                    if (e.getCurrentItem().equals(Utils.itemBuilder(Material.BARRIER, 1, ChatColor.GOLD + "Settings", null, ChatColor.DARK_GREEN + "Manage who can change", ChatColor.DARK_GREEN + "settings"))) {
                        k.openAccessSettingsGui(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.SKELETON_SKULL, 1, ChatColor.GOLD+"Remove/add roles", null, ChatColor.DARK_GREEN + "Manage who can add/remove", ChatColor.DARK_GREEN + "Admin and Member rights"))){
                        k.openAddRemoveMembersAdmins(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.LIME_DYE, 1, ChatColor.GOLD + "Particle Color", null, ChatColor.DARK_GREEN + "Change the Particle Color"))){
                        k.openBorderParticleColor(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.TNT, 1, ChatColor.GOLD + "TnT", null, ChatColor.DARK_GREEN + "Manage if TnT should explode"))){
                        k.openTnT(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.PLAYER_HEAD, 1, ChatColor.GOLD + "View Members and Admins", null, null))){
                        k.openSeeAdminsMembers(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.COBBLESTONE, 1, ChatColor.GOLD + "Destroy/place Blocks", null, ChatColor.DARK_GREEN + "Manage who can place/destroy blocks"))){
                        k.openDestroyPlaceBlocks(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.SPRUCE_SIGN, 1, ChatColor.GOLD + "Interact", null, ChatColor.DARK_GREEN + "Manage who can ", ChatColor.DARK_GREEN + "Right click and hit Mobs"))){
                        k.openInteractInventory(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.WOODEN_SWORD, 1, ChatColor.GOLD+"PvP settings", null, ChatColor.DARK_GREEN + "Manage who can PvP"))){
                        k.openPvPInventory(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.NAME_TAG, 1, ChatColor.GOLD + "Pets", null, ChatColor.DARK_GREEN + "Manage Pets"))){
                        k.openPetInventory(player);
                    }else if(e.getCurrentItem().equals(Utils.itemBuilder(Material.LEATHER_BOOTS, 1, ChatColor.GOLD + "Access settings", null, ChatColor.DARK_GREEN +"Change who can ", ChatColor.DARK_GREEN +"enter the kingdom"))){
                        k.openEnterKingdom(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"access settings")) {
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 19) {
                            k.setSettings(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setSettings(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setSettings(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setSettings(4);
                        }

                        k.openAccessSettingsGui(player);
                    }
                }else if(e.getView().getTitle().equals(ChatColor.GOLD  + "Add and remove settings")){
                    if(e.getRawSlot() == 1){
                        k.openAddAdmins(player);
                    }else if(e.getRawSlot() == 3){
                        k.openRemAdmins(player);
                    }else if(e.getRawSlot() == 5){
                        k.openRemMembers(player);
                    }else if(e.getRawSlot() == 7){
                        k.openAddMembers(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"remove members settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 19) {
                            k.setRemoveMembers(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setRemoveMembers(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setRemoveMembers(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setRemoveMembers(4);
                        }

                        k.openRemMembers(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"add members settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 19) {
                            k.setAddMembers(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setAddMembers(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setAddMembers(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setAddMembers(4);
                        }

                        k.openAddMembers(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"remove admins settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 19) {
                            k.setRemoveAdmins(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setRemoveAdmins(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setRemoveAdmins(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setRemoveAdmins(4);
                        }

                        k.openRemAdmins(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"add admins settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 19) {
                            k.setAddAdmins(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setAddAdmins(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setAddAdmins(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setAddAdmins(4);
                        }

                        k.openAddAdmins(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"Particle settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 11) {
                            k.setParticleType(Particle.VILLAGER_HAPPY);
                            k.setParticleAmount(2);
                        } else if (e.getRawSlot() == 13) {
                            k.setParticleType(Particle.FALLING_LAVA);
                            k.setParticleAmount(3);
                        } else if (e.getRawSlot() == 15) {
                            k.setParticleType(Particle.FALLING_WATER);
                            k.setParticleAmount(3);
                        } else if (e.getRawSlot() == 17) {
                            k.setParticleType(null);
                        }else if(e.getRawSlot() == 9){
                            k.setParticleType(Particle.ASH);
                            k.setParticleAmount(10);
                        }

                        k.openBorderParticleColor(player);
                    }
                }else if(e.getView().getTitle().equals(ChatColor.GOLD + "TnT settings")){

                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 14) {
                            k.setTnTActive(false);
                        } else if (e.getRawSlot() == 12) {
                            k.setTnTActive(true);
                        }

                        k.openTnT(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Admins, Owner and Members settings")){
                    if(e.getRawSlot() == 2){
                        k.openSeeMembers(player, 1);
                    }else if(e.getRawSlot() == 4){
                        k.openSeeAdmins(player, 1);
                    }else if(e.getRawSlot() == 6){
                        k.openSeeOwner(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Admins settings")){
                    if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Next page")){
                        k.openSeeAdmins(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())+1);
                    }else if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Last page")){
                        k.openSeeAdmins(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())-1);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Members settings")){
                    if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Next page")){
                        k.openSeeMembers(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())+1);
                    }else if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Last page")){
                        k.openSeeMembers(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())-1);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"Block settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {

                        if (e.getRawSlot() == 19) {
                            k.setDestroyBlocks(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setDestroyBlocks(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setDestroyBlocks(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setDestroyBlocks(4);
                        }
                        k.openDestroyPlaceBlocks(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"Interact settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {

                        if (e.getRawSlot() == 19) {
                            k.setInteract(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setInteract(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setInteract(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setInteract(4);
                        }
                        k.openInteractInventory(player);
                    }
                }else if(e.getView().getTitle().equals(ChatColor.GOLD + "PvP settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {

                        if (e.getRawSlot() == 19) {
                            k.setPvP(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setPvP(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setPvP(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setPvP(4);
                        }
                        k.openPvPInventory(player);
                    }
                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Pet settings")){

                    if(e.getRawSlot() == 3){
                        k.openInvulnerablePetsInventory(player);
                    }else if(e.getRawSlot() == 5){
                        k.openDamagePetsInventory(player);
                    }

                }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Invulnerable Pets settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                        if (e.getRawSlot() == 12) {
                            k.setInvulerablePets(true);
                        } else if (e.getRawSlot() == 14) {
                            k.setInvulerablePets(false);
                        }

                        k.openInvulnerablePetsInventory(player);
                    }

                }else if(e.getView().getTitle().equals(ChatColor.GOLD + "Damage Pets settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {

                        if (e.getRawSlot() == 19) {
                            k.setDamagePets(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setDamagePets(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setDamagePets(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setDamagePets(4);
                        }
                        k.openDamagePetsInventory(player);
                    }
                }else if(e.getView().getTitle().equals(ChatColor.GOLD + "Enter Kingdom settings")){
                    if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {

                        if (e.getRawSlot() == 19) {
                            k.setEnterKingdom(1);
                        } else if (e.getRawSlot() == 21) {
                            k.setEnterKingdom(2);
                        } else if (e.getRawSlot() == 23) {
                            k.setEnterKingdom(3);
                        } else if (e.getRawSlot() == 25) {
                            k.setEnterKingdom(4);
                        }
                        k.openEnterKingdom(player);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onTnTExplode(EntityExplodeEvent e){
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getLocation())){
                if(!k.getTnTActive()){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getBlock().getLocation())){
                checkAllowed(k.getDestroyBlocks(), e,e.getPlayer(), k);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getBlock().getLocation())){
                checkAllowed(k.getDestroyBlocks(), e,e.getPlayer(), k);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e){

        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation()) || k.getArea().containsLocation(e.getRightClicked().getLocation())){
                checkAllowed(k.getInteract(), e,e.getPlayer(), k);
            }
        }

    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e){

        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation()) || k.getArea().containsLocation(e.getRightClicked().getLocation())){
                checkAllowed(k.getInteract(), e,e.getPlayer(), k);
            }
        }


    }

    @EventHandler
    public void onPlayerPunchEntity(EntityDamageByEntityEvent e){
        if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() != EntityType.PLAYER){
            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                if(k.getArea().containsLocation(e.getDamager().getLocation()) || k.getArea().containsLocation(e.getEntity().getLocation())){
                    checkAllowed(k.getInteract(), e,(Player) e.getDamager(), k);
                }if(k.getArea().containsLocation(e.getEntity().getLocation())){
                    if(e.getEntity() instanceof Tameable) {
                        Tameable t = (Tameable) e.getEntity();
                        if(t.isTamed()) {
                            checkAllowed(k.getHitPets(), e,(Player) e.getDamager(), k);
                        }
                    }

                }
            }

        }

        if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER){

            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                if(k.getArea().containsLocation(e.getDamager().getLocation()) || k.getArea().containsLocation(e.getEntity().getLocation())){
                    checkAllowed(k.getPvP(), e,(Player) e.getDamager(), k);
                }
            }

        }
    }

}
