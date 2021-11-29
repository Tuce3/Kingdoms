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
        boolean customMessages = Kingdoms.getInstance().getConfig().getBoolean("generatemessagefile");

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
                player.sendMessage(Messages.SUCCESSFULLYDELETEDKINGDOMS.getMessage());
                Kingdoms.getManager().safeData();
            } else {
                player.sendMessage(Messages.NORIGHTS.getMessage());
            }
            return false;
        }

        if (args.length >= 1) {
            Player player = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {
                if(Kingdoms.getManager().getKingdomList().size() >= Kingdoms.getInstance().getConfig().getLong("maxkingdoms") && !player.isOp()){
                    player.sendMessage(Messages.MAXAMOUNTOFKINGDOMS.getMessage());
                    return false;
                }
                long kingdomsOfPlayer = 0;
                for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                    if(k.getOwner().equals(player.getUniqueId())){
                        kingdomsOfPlayer++;
                    }
                }
                if(kingdomsOfPlayer >= Kingdoms.getInstance().getConfig().getLong("maxkingdomsperplayer") && !player.isOp()){
                    player.sendMessage(Messages.MAXAMOUNTOFKINGDOMSPERPLAYER.getMessage());
                    return false;
                }

                long kingdomsizeOfPlayer = 0;
                for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                    if(k.getOwner().equals(player.getUniqueId())){
                        kingdomsizeOfPlayer += k.getArea().getAreaSize();
                    }
                }
                if(kingdomsizeOfPlayer >= Kingdoms.getInstance().getConfig().getLong("maxblockperplayer") && !player.isOp()){
                    player.sendMessage(Messages.MAXSIZEKINGDOMSPERPLAYER.getMessage());
                    return false;
                }

                for (Kingdom k : Kingdoms.getManager().getUnfinishedKingdomList()) {
                    if (k.getOwner().equals(player.getUniqueId())) {
                        player.sendMessage(Messages.ALREADYSTARTEDKINGDOM.getMessage());
                        return false;
                    }
                }
                Kingdom k = new Kingdom(player.getUniqueId());
                if(args.length > 1) {
                    StringBuilder newName = new StringBuilder();
                    boolean canName = true;
                    for (int i = 1; i < args.length; i++) {
                        newName.append(args[i]);
                        newName.append(" ");
                    }
                    if (newName.length() > Kingdoms.getInstance().getConfig().getInt("maxnamelength") + 1) {
                        player.sendMessage(Messages.MAXKINGDOMNAMELENNGTH.getMessage());
                        canName = false;
                    }
                    for (Kingdom y : Kingdoms.getManager().getKingdomList()) {
                        if (y.getName() != null && y.getName() != null && y.getName().equalsIgnoreCase(newName.toString()) && y.getOwner().equals(player.getUniqueId())) {
                            player.sendMessage(Messages.KINGDOMNAMEALREADYEXISTING.getMessage());
                            canName = false;
                        }
                    }
                    if (canName) {
                        k.setName(newName.toString());
                    }
                }
                Kingdoms.getManager().getUnfinishedKingdomList().add(k);
                player.sendMessage(Messages.CREATEDKINGDOMCOMMAND.getMessage());
                PlayerCreateKingdom(k);
            } else if(args.length == 1 && args[0].equalsIgnoreCase("save") && player.isOp()){
                Kingdoms.getManager().safeData();
                player.sendMessage(ChatColor.GREEN+"Saved data.");
                System.out.println("Saved data");
                return false;
            } else if(args[0].equalsIgnoreCase("help")){

                player.sendMessage(Messages.HELPCOMMAND.getMessage());

            } else if (args[0].equalsIgnoreCase("settings")) {

                if(args.length >= 3 && args[1].equalsIgnoreCase("title")){
                    if(Kingdoms.getManager().getPlayersInKingdoms().containsKey(player.getUniqueId())){
                        if(Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).getOwner().equals(player.getUniqueId())){
                            if(Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).getName() == null){
                                player.sendMessage(Messages.GIVENAMEBEFORESETTINGTITLE.getMessage());
                            }
                            StringBuilder newTitle = new StringBuilder();
                            for(int i = 2; i<args.length;i++){
                                newTitle.append(args[i]);
                                newTitle.append(" ");
                            }
                            if(newTitle.length() > Kingdoms.getInstance().getConfig().getInt("maxtitlelength")+1){
                                player.sendMessage(Messages.MAXTITLELENGTH.getMessage());
                                return false;
                            }
                            player.sendMessage(Messages.NEWTITLESET.getMessage()+newTitle);
                            Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).setTopTitle(newTitle.toString());
                        }else{
                            player.sendMessage(Messages.NORIGTHSCHANGETITLE.getMessage());
                        }
                    }else{
                        player.sendMessage(Messages.NEEDTOBEINKINGDOM.getMessage());
                    }
                    return false;
                }

                if(args.length >= 4 && args[1].equalsIgnoreCase("addadmin")){
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(!k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEADMIN.getMessage());
                                }else{
                                    player.sendMessage(Messages.ALREADYADMIN.getMessage());
                                }
                                return false;
                            }
                        }
                        player.sendMessage(Messages.KINGDOMNOTSPECIFIED.getMessage());
                    }else{
                        player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                    }
                    return false;
                }
                if(args.length >= 4 && args[1].equalsIgnoreCase("removeadmin")){
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    player.sendMessage(Messages.PLAYERNOLONGERADMIN.getMessage());
                                }else {
                                    player.sendMessage(Messages.SPECIFIEDPLAYERNOTADMIN.getMessage());
                                }
                                return false;
                            }
                        }
                        player.sendMessage(Messages.KINGDOMNOTSPECIFIED.getMessage());
                    }else{
                        player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                    }
                    return false;
                }
                if(args.length >= 4 && args[1].equalsIgnoreCase("addmember")){
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(!k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEMEMBER.getMessage());
                                }else{
                                    player.sendMessage(Messages.ALREADYMEMBER.getMessage());
                                }
                                return false;
                            }
                        }
                        player.sendMessage(Messages.KINGDOMNOTSPECIFIED.getMessage());
                    }else{
                        player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                    }
                    return false;
                }
                if(args.length >= 4 && args[1].equalsIgnoreCase("removemember")){
                    if(Bukkit.getPlayer(args[2]) != null){
                        StringBuilder inputName = new StringBuilder();
                        for(int i = 3; i<args.length; i++){
                            inputName.append(args[i]);
                        }
                        for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                            if(k.getName() != null && k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId()) || k.getName() != null && k.getName().equals(inputName +" ") && k.getOwner().equals(player.getUniqueId())){
                                if(k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId())){
                                    player.sendMessage(Messages.NOLONGERMEMBER.getMessage());
                                }else {
                                    player.sendMessage(Messages.SPECIFIEDPLAYERNOTMEMBER.getMessage());
                                }
                                return false;
                            }
                        }
                        player.sendMessage(Messages.KINGDOMNOTSPECIFIED.getMessage());
                    }else{
                        player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
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
                            if(newName.length() > Kingdoms.getInstance().getConfig().getInt("maxnamelength")+1){
                                player.sendMessage(Messages.MAXKINGDOMNAMELENNGTH.getMessage());
                                return false;
                            }
                            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                                if(k.getName() != null && k.getName() != null && k.getName().equalsIgnoreCase(newName.toString()) && k.getOwner().equals(player.getUniqueId())){
                                    player.sendMessage(Messages.KINGDOMNAMEALREADYEXISTING.getMessage());
                                    return false;
                                }
                            }
                            player.sendMessage(Messages.NEWKINGDOMNAME.getMessage()+newName);
                            Kingdoms.getManager().getPlayersInKingdoms().get(player.getUniqueId()).setName(newName.toString());
                        }else{
                            player.sendMessage(Messages.NORIGHTSCHANGENAME.getMessage());
                        }
                    }else{
                        player.sendMessage(Messages.NEEDTOBEINKINGDOM.getMessage());
                    }
                    return false;
                }
                if(args.length == 2 && args[1].equalsIgnoreCase("deleteall")){
                    if(player.isOp()){
                        for(Kingdom y : Kingdoms.getManager().getKingdomList()){
                            Bukkit.getScheduler().cancelTask(y.getRunnableId());
                        }
                        Kingdoms.getManager().clearKingdomList();
                        player.sendMessage(Messages.DELETEDALLKINGDOMS.getMessage());

                        Kingdoms.getManager().safeData();

                    }else{
                        player.sendMessage(Messages.NORIGHTS.getMessage());
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
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        } else if (k.getMembers().contains(player.getUniqueId())) {
                            if (k.getSettings() <= 2) {
                                k.openSettingsGui(player);
                            } else {
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        } else {
                            if (k.getSettings() <= 1) {
                                k.openSettingsGui(player);
                            } else {
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        }
                    } else if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("addadmin")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEADMIN.getMessage());
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getAddAdmins() == 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getAdmins().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEADMIN.getMessage());
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        } else if (args[1].equalsIgnoreCase("addmember")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEMEMBER.getMessage());
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getAddMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEMEMBER.getMessage());
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getMembers().contains(player.getUniqueId()) && k.getAddMembers() <= 2) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    k.getMembers().add(Bukkit.getPlayer(args[2]).getUniqueId());
                                    player.sendMessage(Messages.MADEMEMBER.getMessage());
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        }else if (args[1].equalsIgnoreCase("removeadmin")) {
                            if (k.getOwner().equals(player.getUniqueId())) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(Messages.PLAYERNOLONGERADMIN.getMessage());
                                    }else{
                                        player.sendMessage(Messages.SPECIFIEDPLAYERNOTADMIN.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getRemoveAdmins() == 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getAdmins().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getAdmins().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(Messages.PLAYERNOLONGERADMIN.getMessage());
                                    }else{
                                        player.sendMessage(Messages.SPECIFIEDPLAYERNOTADMIN.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have the rights to use this command!");
                            }
                        } else if (args[1].equalsIgnoreCase("removemember")) {
                            if (k.getOwner().equals(player.getUniqueId()) && Bukkit.getPlayer(args[2]) != player) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(Messages.NOLONGERMEMBER.getMessage());
                                    }else{
                                        player.sendMessage(Messages.SPECIFIEDPLAYERNOTMEMBER.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getAdmins().contains(player.getUniqueId()) && k.getRemoveMembers() <= 3) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(Messages.NOLONGERMEMBER.getMessage());
                                    }else{
                                        player.sendMessage(Messages.SPECIFIEDPLAYERNOTMEMBER.getMessage());
                                    }
                                } else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            } else if (k.getMembers().contains(player.getUniqueId()) && k.getRemoveMembers() <=  2) {
                                if (Bukkit.getPlayer(args[2]) != null && Bukkit.getPlayer(args[2]) != player) {
                                    if (k.getMembers().contains(Bukkit.getPlayer(args[2]).getUniqueId())) {
                                        k.getMembers().remove(Bukkit.getPlayer(args[2]).getUniqueId());
                                        player.sendMessage(Messages.NOLONGERMEMBER.getMessage());
                                    }else{
                                        player.sendMessage(Messages.SPECIFIEDPLAYERNOTMEMBER.getMessage());
                                    }
                                }else {
                                    player.sendMessage(Messages.PLAYERNOTSPECIFIED.getMessage());
                                }
                            }else {
                                player.sendMessage(Messages.NORIGHTS.getMessage());
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOWNCOMMAND.getMessage());
                        }
                        }

                    } else {
                        player.sendMessage(Messages.NEEDTOBEINKINGDOM.getMessage());
                    }

                    if(args.length >= 2 && args[0].equalsIgnoreCase("settings") && !Arrays.asList(new String[]{"title", "addadmin", "addmember", "removeadmin", "removemember", "delete", "deleteall", "name"}).contains(args[1])){
                            StringBuilder inputName = new StringBuilder();
                            for(int i = 3; i<args.length; i++){
                                inputName.append(args[i]);
                            }
                            for(Kingdom k : Kingdoms.getManager().getKingdomList()){
                                if(k.getName() != null && (k.getName().equals(inputName.toString()) && k.getOwner().equals(player.getUniqueId())) || (k.getName() != null && k.getName().equals(inputName +" ")) && k.getOwner().equals(player.getUniqueId())){
                                    Kingdoms.getManager().getPlayersInKingdomSettings().put(player.getUniqueId(), k);
                                    k.openAccessSettingsGui(player);
                                    Kingdoms.getManager().getPlayersInKingdoms().put(player.getUniqueId(), k);
                                }
                            }
                            player.sendMessage(Messages.KINGDOMNOTSPECIFIED.getMessage());
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.UNKNOWNCOMMAND.getMessage());
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
            return StringUtil.copyPartialMatches(args[1], Arrays.asList(new String[]{"title", "addadmin", "addmember", "removeadmin", "removemember", "delete", "deleteall", "name"}), new ArrayList<>());
        }
        return null;
    }
}
