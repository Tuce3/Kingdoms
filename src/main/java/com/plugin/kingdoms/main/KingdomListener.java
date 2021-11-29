package com.plugin.kingdoms.main;

import org.bukkit.*;
import org.bukkit.block.Block;
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
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public class KingdomListener implements Listener {

    private void checkAllowed(int settings, Cancellable c, Player p, Kingdom k){
        if(k.getAdmins().contains(p.getUniqueId())){
            if(settings == 4){
                c.setCancelled(true);
                p.sendMessage(Messages.NORIGHTSACTION.getMessage());
            }
        }else if(k.getOwner().equals(p.getUniqueId())){
            if(settings > 4){
                c.setCancelled(true);
                p.sendMessage(Messages.NORIGHTSACTION.getMessage());
            }
        }else if(k.getMembers().contains(p.getUniqueId())){
            if(settings > 2){
                c.setCancelled(true);
                p.sendMessage(Messages.NORIGHTSACTION.getMessage());
            }
        }else{
            if(settings > 1){
                c.setCancelled(true);
                p.sendMessage(Messages.NORIGHTSACTION.getMessage());
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
        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(k.getArea().containsLocation(e.getPlayer().getLocation())){
                if((Kingdoms.getManager().getPlayersInKingdoms().containsKey(e.getPlayer().getUniqueId()) && Kingdoms.getManager().getPlayersInKingdoms().get(e.getPlayer().getUniqueId()) != k) ||
                        (!Kingdoms.getManager().getPlayersInKingdoms().containsKey(e.getPlayer().getUniqueId()))){
                    Player player = e.getPlayer();
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
                    if(player.isOp()) isAllowed = true;
                    if(Bukkit.getOfflinePlayer(k.getOwner()).isBanned()) isAllowed = true;
                    if((k.getArea().containsLocation(e.getPlayer().getLocation().getBlock().getLocation()) && isAllowed)){
                        Kingdoms.getManager().getPlayersInKingdoms().put(player.getUniqueId(), k);
                        if(k.getName() == null){
                            player.sendTitle(ChatColor.AQUA + "You entered a Kingdom", ChatColor.AQUA + "of " + ChatColor.AQUA + Bukkit.getOfflinePlayer(k.getOwner()).getName(), 10, 50, 10);
                        }else{
                            if(k.getTopTitle() == null){
                                player.sendTitle(ChatColor.AQUA + "You entered", ChatColor.AQUA + k.getName(), 10, 50, 10);
                            }else{
                                player.sendTitle(ChatColor.AQUA + k.getTopTitle(), ChatColor.AQUA + k.getName(), 10, 50, 10);
                            }
                        }
                    }else if((k.getArea().containsLocation(e.getPlayer().getLocation().getBlock().getLocation()) && !isAllowed)){
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
                        Block portLocY = new Location(player.getWorld(), portLocation.getBlockX(), player.getLocation().getBlockY(), portLocation.getBlockZ()).getBlock();
                        player.teleport(new Location(portLocation.getWorld(), portLocation.getBlockX(),
                                (portLocY.getRelative(0, 1, 0).getType() == Material.AIR || portLocY.getRelative(0, 1, 0).getType() == Material.CAVE_AIR) && (portLocY.getType() == Material.AIR || portLocY.getType() == Material.CAVE_AIR)? player.getLocation().getY() : portLocation.getWorld().getHighestBlockAt(portLocation).getLocation().getY()+ 1,
                                portLocation.getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        if(k.getName() == null){
                            e.getPlayer().sendMessage(Messages.NORIGHTSENTERKINGDOM.getMessage()+ChatColor.RED+Bukkit.getOfflinePlayer(k.getOwner()).getName()+ChatColor.RED+"!");
                        }else{
                            e.getPlayer().sendMessage(Messages.NORIGHTSENTERKINGDOMNAME.getMessage()+ ChatColor.RED+k.getName()+ChatColor.RED+"!");
                        }
                    }
                }

                return;
            }
        }
        if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(e.getPlayer().getUniqueId())){
            Kingdoms.getManager().getPlayersInKingdoms().remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent e){
        if(e.getCurrentItem() == null) return;

        Player player = (Player) e.getWhoClicked();

        //if(!e.getView().getTitle().contains(ChatColor.GOLD+ "settings") && !e.getView().getTitle().contains("settings")) return;

        //e.setCancelled(true);

        if(!Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId()) && !Kingdoms.getManager().getPlayersInKingdomSettings().containsKey(player.getUniqueId())) return;

        Kingdom k = Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId()) ? Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()) : Kingdoms.getManager().getPlayersInKingdomSettings().get(player.getUniqueId());

        if(e.getView().getTitle().equalsIgnoreCase(Messages.SETTINGSITEM.getMessage())){
            e.setCancelled(true);
            if (e.getCurrentItem().equals(KingdomInterface.accessSettings)) {
                k.openAccessSettingsGui(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.addremoveadminsmembers)){
                k.openAddRemoveMembersAdmins(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.particleColor)){
                k.openBorderParticleColor(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.TnT)){
                k.openTnT(player);
            }else if(e.getCurrentItem().equals(KingdomInterface.viewMembers)){
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
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.ACCESSSETTINGINVENTORYNAME.getMessage())) {
            e.setCancelled(true);
            k.setSettings(checkSlot(e.getRawSlot()));
            k.openAccessSettingsGui(player);
        }else if(e.getView().getTitle().equals(Messages.ADDANDREMOVESETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            if(e.getRawSlot() == 1){
                k.openAddAdmins(player);
            }else if(e.getRawSlot() == 3){
                k.openRemAdmins(player);
            }else if(e.getRawSlot() == 5){
                k.openRemMembers(player);
            }else if(e.getRawSlot() == 7){
                k.openAddMembers(player);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.REMOVEMEMBERSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setRemoveMembers(checkSlot(e.getRawSlot()));
            k.openRemMembers(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.ADDMEMBERSSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setAddMembers(checkSlot(e.getRawSlot()));
            k.openAddMembers(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.REMOVEADMINSSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setRemoveAdmins(checkSlot(e.getRawSlot()));
            k.openRemAdmins(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.ADDADMINSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setAddAdmins(checkSlot(e.getRawSlot()));
            k.openAddAdmins(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.PARTICLESETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
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

        }else if(e.getView().getTitle().equals(Messages.TNTSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);

            if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                if (e.getRawSlot() == 14) {
                    k.setTnTActive(false);
                } else if (e.getRawSlot() == 12) {
                    k.setTnTActive(true);
                }

                k.openTnT(player);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.ADMINSOWNERANDMEMBERSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            if(e.getRawSlot() == 2){
                k.openSeeMembers(player, 1);
            }else if(e.getRawSlot() == 4){
                k.openSeeAdmins(player, 1);
            }else if(e.getRawSlot() == 6){
                k.openSeeOwner(player);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.ADMINSSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Next page")){
                k.openSeeAdmins(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())+1);
            }else if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Last page")){
                k.openSeeAdmins(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())-1);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.MEMBERSSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Next page")){
                k.openSeeMembers(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())+1);
            }else if(e.getCurrentItem().getType() == Material.TIPPED_ARROW && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Last page")){
                k.openSeeMembers(player, Integer.parseInt(e.getCurrentItem().getItemMeta().getLocalizedName())-1);
            }
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.BLOCKSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setDestroyBlocks(checkSlot(e.getRawSlot()));
            k.openDestroyPlaceBlocks(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.INTERACTSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setInteract(checkSlot(e.getRawSlot()));
            k.openInteractInventory(player);
        }else if(e.getView().getTitle().equals(Messages.PVPSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setPvP(checkSlot(e.getRawSlot()));
            k.openPvPInventory(player);
        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.PETSETTINGSINVENTORY.getMessage())){
            e.setCancelled(true);

            if(e.getRawSlot() == 3){
                k.openInvulnerablePetsInventory(player);
            }else if(e.getRawSlot() == 5){
                k.openDamagePetsInventory(player);
            }

        }else if(e.getView().getTitle().equalsIgnoreCase(Messages.INVULNERABLEPETSSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            if (e.getCurrentItem().equals(KingdomInterface.deactivated)) {
                if (e.getRawSlot() == 12) {
                    k.setInvulnerablePets(true);
                } else if (e.getRawSlot() == 14) {
                    k.setInvulnerablePets(false);
                }

                k.openInvulnerablePetsInventory(player);
            }

        }else if(e.getView().getTitle().equals(Messages.DAMAGEPETSETTINGSINVENTORYNAME.getMessage())){
            e.setCancelled(true);
            k.setDamagePets(checkSlot(e.getRawSlot()));
            k.openDamagePetsInventory(player);
        }else if(e.getView().getTitle().equals(Messages.ENTERKINGDOMSETTINGS.getMessage())){
            e.setCancelled(true);
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
        if(Kingdoms.getInstance().getConfig().getBoolean("destroyoutsideofkingdom")) return;
        List<Material> blockMaterials = new ArrayList<>();
        List<Location> blockLocs = new ArrayList<>();
        for (Kingdom k : Kingdoms.getManager().getKingdomList()){
            if(!k.getArea().containsLocation(e.getLocation()) && k.getTntCheckArea().containsLocation(e.getLocation())){
                for(Block block : e.blockList()){
                    if(k.getArea().containsLocation(block.getLocation())){
                        blockMaterials.add(block.getType());
                        blockLocs.add(block.getLocation());
                    }
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(Kingdoms.getInstance(), new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < blockLocs.size(); i++){
                    blockLocs.get(i).getBlock().setType(blockMaterials.get(i));
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getPlayer().isOp()) return;
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

    @EventHandler
    public void onBan(PlayerKickEvent e){
        if(e.getPlayer().isBanned() && Kingdoms.getInstance().getConfig().getBoolean("deleteonban")){
            for(int i = 0; i < Kingdoms.getManager().getKingdomList().size(); i++){
                Kingdom k = Kingdoms.getManager().getKingdom(i);
                if(k.getOwner().equals(e.getPlayer().getUniqueId())){
                    Kingdoms.getManager().removeKingdomFromList(k);
                    if(k.getRunnableId() != 0)Bukkit.getScheduler().cancelTask(k.getRunnableId());
                    Kingdoms.getManager().safeData();
                }
            }
        }
    }
    @EventHandler
    public void onBan(PlayerQuitEvent e){
        if(e.getPlayer().isBanned() && Kingdoms.getInstance().getConfig().getBoolean("deleteonban")){
            for(int i = 0; i < Kingdoms.getManager().getKingdomList().size(); i++){
                Kingdom k = Kingdoms.getManager().getKingdom(i);
                if(k.getOwner().equals(e.getPlayer().getUniqueId())){
                    Kingdoms.getManager().removeKingdomFromList(k);
                    if(k.getRunnableId() != 0)Bukkit.getScheduler().cancelTask(k.getRunnableId());
                    Kingdoms.getManager().safeData();
                }
            }
        }
    }
}
