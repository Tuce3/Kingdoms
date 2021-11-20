package com.plugin.kingdoms.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KingdomCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        //Kingdom delete command first to ensure safe deleting

        if(args.length == 2 && args[1].equalsIgnoreCase("delete") && args[0].equalsIgnoreCase("settings")) {
            Player player = (Player) sender;
            Kingdom k = null;
            for (int i = 0; i < Kingdoms.getManager().getKingdomList().size(); i++) {
                if (Kingdoms.getManager().getKingdom(i).getArea().containsLocation(player.getLocation())) {
                    k = Kingdoms.getManager().getKingdom(i);
                    break;
                }
            }

            if (k != null && (player.isOp() || k.getOwner().equals(player.getUniqueId())) ) {
                Kingdoms.getManager().removeKingdomFromList(k);
                if(k.getRunnableId() != 0)Bukkit.getScheduler().cancelTask(k.getRunnableId());
                player.sendMessage(ChatColor.GREEN + "Deleted Kingdom! There is no way to reverse this Action!");
                Kingdoms.getManager().safeData();
            } else {
                player.sendMessage(ChatColor.RED + "You don't have the right to use this command!");
            }
            return false;
        }

        if (args.length >= 1) {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {
                if(Kingdoms.getManager().getKingdomList().size() >= Kingdoms.getInstance().getConfig().getLong("maxkingdoms") && !player.isOp()){
                    player.sendMessage(ChatColor.RED+"The max amount of Kingdoms has been reached! You can't create any more!");
                    return false;
                }
                long kingdomsOfPlayer = 0;
                for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                    if(k.getOwner().equals(player.getUniqueId())){
                        kingdomsOfPlayer++;
                    }
                }
                if(kingdomsOfPlayer >= Kingdoms.getInstance().getConfig().getLong("maxkingdomsperplayer") && !player.isOp()){
                    player.sendMessage(ChatColor.RED+"You have reached the max amount of Kingdoms you can create!");
                    return false;
                }

                long kingdomsizeOfPlayer = 0;
                for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                    if(k.getOwner().equals(player.getUniqueId())){
                        kingdomsizeOfPlayer += k.getArea().getAreaSize();
                    }
                }
                System.out.println(kingdomsizeOfPlayer);
                if(kingdomsizeOfPlayer >= Kingdoms.getInstance().getConfig().getLong("maxblockperplayer") && !player.isOp()){
                    player.sendMessage(ChatColor.RED+"You have reached the max size of all your Kingdoms!");
                    return false;
                }

                for (Kingdom k : Kingdoms.getManager().getUnfinishedKingdomList()) {
                    if (k.getOwner().equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "You've already created a new Kingdom! Set the 2 Location first!");
                        return false;
                    }
                }
                Kingdom k = new Kingdom(player.getUniqueId());
                Kingdoms.getManager().getUnfinishedKingdomList().add(k);
                player.sendMessage(ChatColor.GREEN + "Created a new Kingdom! Set the 2 Location via right clicking on blocks! You have 60 Seconds to do that!");
                PlayerCreateKingdom(k);
            } else if(args.length == 1 && args[0].equalsIgnoreCase("save") && player.isOp()){
                Kingdoms.getManager().safeData();
                player.sendMessage(ChatColor.GREEN+"Saved data.");
                System.out.println("Saved data");
                return false;
            } else if(args[0].equalsIgnoreCase("help")){

                player.sendMessage(ChatColor.YELLOW + "Kingdom help:");
                player.sendMessage(ChatColor.YELLOW + "All Kingdom commands start with /kingdom");
                player.sendMessage(ChatColor.YELLOW + "Kingdom settings:");
                player.sendMessage(ChatColor.YELLOW + "-/kingdom settings -  open settings gui of the Kingdom you're in");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings name <args>- Name your Kingdom!");

                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addadmin <player>- Add an Admin to the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addadminvianame <playername> <kingdomname>- Add an Admin to a Kingdom via the name of the Kingdom(Only Owner)");

                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removeadmin <player>- Remove an Admin from the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removeadminvianame <playername> <kingdomname>- Remove an Admin from a Kingdom via the name of the Kingdom(Only Owner)");

                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addmember <player>- Add a Member to the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addmembervianame <playername> <kingdomname>- Add a Member to a Kingdom via the name of the Kingdom(Only Owner)");

                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removemember <player>- Remove a Member from the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removemembervianame <playername> <kingdomname>- Remove a Member from a Kingdom via the name of the Kingdom(Only Owner)");

                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings delete- Delete the Kingdom you're in");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings deleteall- Delete all Kingdoms!");
                player.sendMessage(ChatColor.YELLOW + "-/kingdom create- Create a Kingdom with 2 right clicks!");
                if(player.isOp()) player.sendMessage(ChatColor.YELLOW+"-/kingdom save- Save data");

            } else if (args[0].equalsIgnoreCase("settings")) {

                if(args.length >= 2 && args[1].equalsIgnoreCase("addadminvianame")){
                    if(!(args.length >= 4)){
                        player.sendMessage(ChatColor.RED+"You need to specify the Player and Kingdom name!");
                        return false;
                    }
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            System.out.println("Name: "+k.getName()+" owner: "+Bukkit.getOfflinePlayer(k.getOwner()).getName()+ " specified name: "+inputName.toString());
                            System.out.println(inputName);
                            System.out.println(k.getName());
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(!k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN+"Made "+ChatColor.GREEN+Bukkit.getPlayer(args[2]).getName()+ChatColor.GREEN+" Admin!");
                                }else{
                                    player.sendMessage(ChatColor.RED+"That Player is already an Admin!");
                                }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Kingdom!");
                    }else{
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Player!");
                    }
                    return false;
                }
                if(args.length >= 2 && args[1].equalsIgnoreCase("removeadminvianame")){
                    if(!(args.length >= 4)){
                        player.sendMessage(ChatColor.RED+"You need to specify the Player and Kingdom name!");
                        return false;
                    }
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    player.sendMessage(ChatColor.GREEN+Bukkit.getPlayer(args[2]).getName()+ChatColor.GREEN+" is no longer Admin!");
                                }else {
                                    player.sendMessage(ChatColor.RED+"The specified Player is not an Admin!");
                                }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Kingdom!");
                    }else{
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Player!");
                    }
                    return false;
                }
                if(args.length >= 2 && args[1].equalsIgnoreCase("addmembervianame")){
                    if(!(args.length >= 4)){
                        player.sendMessage(ChatColor.RED+"You need to specify the Player and Kingdom name!");
                        return false;
                    }
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(!k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN+"Made "+ChatColor.GREEN+Bukkit.getPlayer(args[2]).getName()+ChatColor.GREEN+" Member!");
                                }else{
                                    player.sendMessage(ChatColor.RED+"That Player is already a Member!");
                                }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Kingdom!");
                    }else{
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Player!");
                    }
                    return false;
                }
                if(args.length >= 2 && args[1].equalsIgnoreCase("removemembervianame")){
                    if(!(args.length >= 4)){
                        player.sendMessage(ChatColor.RED+"You need to specify the Player and Kingdom name!");
                        return false;
                    }
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    player.sendMessage(ChatColor.GREEN+Bukkit.getPlayer(args[2]).getName()+ChatColor.GREEN+" is no longer Member!");
                                }else {
                                    player.sendMessage(ChatColor.RED+"The specified Player is not a Member!");
                                }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Kingdom!");
                    }else{
                        player.sendMessage(ChatColor.RED+"Couldn't find the specified Player!");
                    }
                    return false;
                }

                if(args.length >= 3 && args[1].equalsIgnoreCase("name")){
                    if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())){
                        if(Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).getOwner().equals(player.getUniqueId())){
                            StringBuilder newName = new StringBuilder();
                            for(int i = 2; i<args.length;i++){
                                newName.append(args[i]);
                                newName.append(" ");
                            }
                            if(newName.length() > 12){
                                player.sendMessage(ChatColor.RED+"The name can only be 12 letters long!");
                                return false;
                            }
                            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                                if(k.getName() != null && k.getName() != null && k.getName().equalsIgnoreCase(newName.toString()) && k.getOwner().equals(player.getUniqueId())){
                                    player.sendMessage(ChatColor.RED+"You already have a Kingdom named "+ChatColor.RED+newName+ChatColor.RED+"!");
                                    return false;
                                }
                            }
                            player.sendMessage(ChatColor.GREEN+"The Kingdoms new name is "+newName);
                            Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).setName(newName.toString());
                        }else{
                            player.sendMessage(ChatColor.RED+"You don't have the rights to change the name of the Kingdom!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED+"You need to be in a Kingdom to use this command!");
                    }
                    return false;
                }
                if(args.length == 2 && args[1].equalsIgnoreCase("deleteall")){
                    if(player.isOp()){
                        for(Kingdom y : Kingdoms.getManager().getKingdomList()){
                            Bukkit.getScheduler().cancelTask(y.getRunnableId());
                        }
                        Kingdoms.getManager().clearKingdomList();
                        player.sendMessage(ChatColor.GREEN + "Deleted all Kingdoms! There is no way to reverse this Action!");

                        Kingdoms.getManager().safeData();

                    }else{
                        player.sendMessage(ChatColor.RED + "You don't have the right to use this command!");
                    }
                    return  false;
                }
                if (Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())) {
                    Kingdom k = Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId());

                    if (args.length == 1) {
                        if (k.getOwner().equals(player.getUniqueId()) || player.isOp()) {
                            k.openSettingsGui(player);
                        } else if (k.getAdmins().contains(player.getUniqueId())) {
                            if (k.getSettings() <= 3) {
                                k.openSettingsGui(player);
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have the rights to open the Settings menu!");
                            }
                        } else if (k.getMembers().contains(player.getUniqueId())) {
                            if (k.getSettings() <= 2) {
                                k.openSettingsGui(player);
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have the rights to open the Settings menu!");
                            }
                        } else {
                            if (k.getSettings() <= 1) {
                                k.openSettingsGui(player);
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have the rights to open the Settings menu!");
                            }
                        }
                    } else if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("addadmin")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to an Admin");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getAddAdmins() == 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to an Admin");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have the rights to use this command!");
                            }
                        } else if (args[1].equalsIgnoreCase("addmember")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to a Member");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getAddMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to a Member");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else if (k.getMembers().contains(player.getUniqueId()) && k.getAddMembers() <= 2) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to a Member");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the rights to use this command!");
                            }
                        }else if (args[1].equalsIgnoreCase("removeadmin")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Admin");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "The specified Player is not an Admin");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getRemoveAdmins() == 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Admin");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "That Player is not an Admin");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have the rights to use this command!");
                            }
                        } else if (args[1].equalsIgnoreCase("removemember")) {
                            if (k.getOwner().equals(player.getUniqueId()) && Bukkit.getPlayer(args[2]) != player) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Member");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "The specified Player is not a Member");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getRemoveMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Member");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "The specified Player is not a Member");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else if (k.getMembers().contains(player.getUniqueId()) && k.getRemoveMembers() <=  2) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Member");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "The specified Player is not a Member");
                                    }
                                }else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find the specified Player!");
                                }
                            }else {
                                player.sendMessage(ChatColor.RED + "You dont have the rights to use this command!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Unknown command!");
                        }
                        }

                    } else {
                        player.sendMessage(ChatColor.RED + "You need to be in a Kingdom to use this Command!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Unknown command!");
                }
            }
        return false;
    }

    private void PlayerCreateKingdom(Kingdom k){

        Bukkit.getScheduler().scheduleSyncDelayedTask(Kingdoms.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(Kingdoms.getManager().getUnfinishedKingdomList().contains(k)){
                    if(Bukkit.getPlayer(k.getOwner()) != null){
                        Bukkit.getPlayer(k.getOwner()).sendMessage(ChatColor.RED + "You didnt create a Kingdom because you ran out of time!");
                    }
                    Kingdoms.getManager().getUnfinishedKingdomList().removeIf(k2 -> k2.getOwner().equals(k.getOwner()));
                }
            }
        }, 1200);



    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(new String[]{"create", "settings", "help", "save"}), new ArrayList<>());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("settings")){
            return StringUtil.copyPartialMatches(args[1], Arrays.asList(new String[]{"addadmin","addadminvianame", "addmember", "addmembervianame", "removeadmin", "removeadminvianame", "removemember",  "removemembervianame", "delete", "deleteall", "name"}), new ArrayList<>());
        }
        return null;
    }
}
