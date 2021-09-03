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

        if (args.length >= 1 && sender instanceof Player) {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {
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
            } else if(args[0].equalsIgnoreCase("help")){

                player.sendMessage(ChatColor.YELLOW + "Kingdom help:");
                player.sendMessage(ChatColor.YELLOW + "All Kingdom commands start with /kingdom");
                player.sendMessage(ChatColor.YELLOW + "Kingdom settings:");
                player.sendMessage(ChatColor.YELLOW + "-/kingdom settings -  open settings gui of the Kingdom you're in");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addadmin <player>- Add an Admin to the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removeadmin <player>- Remove an Admin from the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings addmember <player>- Add a Member to the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings removemember <player>- Remove a Member from the Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings setmaxblocks <amount>- Set the max size of a Kingdom");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings delete- Delete the Kingdom you're in");
                player.sendMessage(ChatColor.YELLOW + "  -/kingdom settings deleteall- Delete all Kingdoms!");
                player.sendMessage(ChatColor.YELLOW + "-/kingdom create- Create a Kingdom with 2 right clicks");

            } else if (args[0].equalsIgnoreCase("settings")) {
                if(args.length == 3 && args[1].equalsIgnoreCase("setmaxblocks")){
                    if(player.isOp()) {
                        try {
                            Kingdoms.getManager().setMaxBlocks(Integer.parseInt(args[2]));
                            player.sendMessage(ChatColor.GREEN + "Max size of a Kingdom is now " + args[2]);
                        } catch (NumberFormatException x) {
                            player.sendMessage(ChatColor.RED + "Invalid Argument! Please pass a Number!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "You need to be Operator to use this Command!");
                    }
                    return false;
                }
                if(args.length == 2 && args[1].equalsIgnoreCase("deleteall")){
                    if(player.isOp()){
                        for(Kingdom y : Kingdoms.getManager().getKingdomList()){
                            Bukkit.getScheduler().cancelTask(y.getRunnableId());
                        }
                        Kingdoms.getManager().getKingdomList().clear();
                        player.sendMessage(ChatColor.GREEN + "Deleted all Kingdoms! There is no way to reverse this Action!");

                        Kingdoms.getManager().safeData();

                    }else{
                        player.sendMessage(ChatColor.RED + "You don't have the right to use this command!");
                    }
                    return  false;
                }
                if (Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())) {
                    Kingdom k = Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId());

                    if(args.length == 2 && args[1].equalsIgnoreCase("delete")){
                        if(player.isOp() || k.getOwner().equals(player.getUniqueId())){
                            Kingdoms.getManager().getKingdomList().remove(k);
                            Bukkit.getScheduler().cancelTask(k.getRunnableId());
                            player.sendMessage(ChatColor.GREEN + "Deleted Kingdom! There is no way to reverse this Action!");
                            Kingdoms.getManager().safeData();
                        }else{
                            player.sendMessage(ChatColor.RED + "You don't have the right to use this command!");
                        }
                    }

                    if (args.length == 1) {
                        if (k.getOwner().equals(player.getUniqueId())) {
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
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the rights to use this command!");
                            }
                        } else if (args[1].equalsIgnoreCase("addmember")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to a Member");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getAddMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(ChatColor.GREEN + "Made " + args[2] + ChatColor.GREEN + " to a Member");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
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
                                        player.sendMessage(ChatColor.RED + "That Player is not an Admin");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
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
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You dont have the rights to use this command!");
                            }
                        } else if (args[1].equalsIgnoreCase("removemember")) {
                            if (k.getOwner().equals(player.getUniqueId()) && Bukkit.getPlayer(args[2]) != player) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Member");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "That Player is not a Member");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getRemoveMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(ChatColor.GREEN + args[2] + ChatColor.GREEN + " is no longer Member");
                                    }else{
                                        player.sendMessage(ChatColor.RED + "That Player is not a Member");
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
                                        player.sendMessage(ChatColor.RED + "That Player is not a Member");
                                    }
                                }else {
                                    player.sendMessage(ChatColor.RED + "Couldn't find that player!");
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
            return StringUtil.copyPartialMatches(args[0], Arrays.asList(new String[]{"create", "settings", "help"}), new ArrayList<>());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("settings")){
            return StringUtil.copyPartialMatches(args[1], Arrays.asList(new String[]{"addadmin", "addmember", "removeadmin", "removemember", "setmaxblocks", "delete", "deleteall"}), new ArrayList<>());
        }
        return null;
    }
}
