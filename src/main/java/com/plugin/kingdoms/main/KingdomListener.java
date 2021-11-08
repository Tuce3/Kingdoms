package com.plugin.kingdoms.main;

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

    private int checkSlot(int rawSlot){
        if (rawSlot == 19) {
            return 1;
        } else if (rawSlot == 21) {
            return 2;
        } else if (rawSlot == 23) {
            return 3;
        } else if (rawSlot == 25) {
            return 4;
        }
        return rawSlot;
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
        if(e.getPlayer().isOp()) return;
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation()) || (e.getClickedBlock() != null && k.getArea().containsLocation(e.getClickedBlock().getLocation()))){
                checkAllowed(k.getInteract(), e,e.getPlayer(), k);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        if(e.getPlayer().isOp()) return;

        Kingdom kingdomPlayerIsIn = null;
        Kingdom kingdomPlayerIsInNow = null;
        Player player = e.getPlayer();

        if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(e.getPlayer().getUniqueId())){
            kingdomPlayerIsIn = Kingdoms.getManager().getPlayersInKingdoms().get(e.getPlayer().getUniqueId());
        }

        for(int i = 0; i< Kingdoms.getManager().getKingdomList().size(); i++){
            Kingdom k = Kingdoms.getManager().getKingdom(i);
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

        if(!e.getView().getTitle().contains(ChatColor.GOLD+ "settings") && !e.getView().getTitle().contains("settings")) return;

        e.setCancelled(true);

        if(!Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())) return;

        Kingdom k = Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId());

        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "settings")){
            if (e.getCurrentItem().equals(KingdomInterface.accessSettings)) {
                k.openAccessSettingsGui(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.addremoveadminsmembers)){
                k.openAddRemoveMembersAdmins(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.particleColor)){
                k.openBorderParticleColor(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.TnT)){
                k.openTnT(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.addremoveadminsmembers)){
                k.openSeeAdminsMembers(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.blockSettings)){
                k.openDestroyPlaceBlocks(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.interactSettings)){
                k.openInteractInventory(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.Pvp)){
                k.openPvPInventory(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.invulnerablePets)){
                k.openPetInventory(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.enterKingdomSettings)){
                k.openEnterKingdom(player);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"access settings")) {
            k.setSettings(checkSlot(e.getRawSlot()));
            k.openAccessSettingsGui(player);
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
            k.setRemoveMembers(checkSlot(e.getRawSlot()));
            k.openRemMembers(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"add members settings")){
            k.setAddMembers(checkSlot(e.getRawSlot()));
            k.openAddMembers(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"remove admins settings")){
            k.setRemoveAdmins(checkSlot(e.getRawSlot()));
            k.openRemAdmins(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"add admins settings")){
            k.setAddAdmins(checkSlot(e.getRawSlot()));
            k.openAddAdmins(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"Particle settings")){
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
            k.setDestroyBlocks(checkSlot(e.getRawSlot()));
            k.openDestroyPlaceBlocks(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD  +"Interact settings")){
            k.setInteract(checkSlot(e.getRawSlot()));
            k.openInteractInventory(player);
        }else if(e.getView().getTitle().equals(ChatColor.GOLD + "PvP settings")){
            k.setPvP(checkSlot(e.getRawSlot()));
            k.openPvPInventory(player);
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
            k.setDamagePets(checkSlot(e.getRawSlot()));
            k.openDamagePetsInventory(player);
        }else if(e.getView().getTitle().equals(ChatColor.GOLD + "Enter Kingdom settings")){
            k.setEnterKingdom(checkSlot(e.getRawSlot()));
            k.openEnterKingdom(player);
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
        if(e.getPlayer().isOp()) return;
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getBlock().getLocation())){
                checkAllowed(k.getDestroyBlocks(), e,e.getPlayer(), k);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
        if(e.getPlayer().isOp()) return;
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation()) || k.getArea().containsLocation(e.getRightClicked().getLocation())){
                checkAllowed(k.getInteract(), e,e.getPlayer(), k);
            }
        }

    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e){
        if(e.getPlayer().isOp()) return;
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
                if(((Player) e.getDamager()).isOp()) return;
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
