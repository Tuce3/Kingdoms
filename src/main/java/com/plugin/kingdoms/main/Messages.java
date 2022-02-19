package com.plugin.kingdoms.main;

import org.bukkit.ChatColor;

public enum Messages {


    CREATEDKINGDOMCOMMAND(ChatColor.GREEN + "Created a new Kingdom! Set the 2 Location via right clicking on blocks! You have " + Kingdoms.getInstance().getConfig().getInt("timeToBuildKingdom") + " seconds to do that!\""),
    SUCCESSFULLYDELETEDKINGDOMS(ChatColor.GREEN + "Deleted Kingdom! There is no way to reverse this Action!"),
    RESIZEDKINGDOMCOMMAND(ChatColor.GREEN + "Resized a kingdom! Right click on one of the corners of kingdom. Set the new location of the corner by right clicking on block! You have " + Kingdoms.getInstance().getConfig().getInt("timeToBuildKingdom") + " seconds to do that!\""),
    NORIGHTS(ChatColor.RED + "You don't have the right to use this command!"),
    MAXAMOUNTOFKINGDOMS(ChatColor.RED+"The max amount of Kingdoms has been reached! You can't create any more!"),
    MAXAMOUNTOFKINGDOMSPERPLAYER(ChatColor.RED+"You have reached the max amount of Kingdoms you can create!"),
    MAXSIZEKINGDOMSPERPLAYER(ChatColor.RED+"You have reached the max size of all your Kingdoms!"),
    ALREADYSTARTEDKINGDOM(ChatColor.RED + "You've already created a new Kingdom! Set the 2 Location first!"),
    ALREADYRESIZINGKINGDOM(ChatColor.RED + "You've already started resizing a new kingdom! Right click on one of the corners of the kingdom to move it. Right click again on a block to set the new location of that corner!"),
    MAXKINGDOMNAMELENNGTH(ChatColor.RED + ("The name can only be " + Kingdoms.getInstance().getConfig().getInt("maxnamelength") + " letters long!")),
    KINGDOMNAMEALREADYEXISTING(ChatColor.RED + "You already have a Kingdom with that name!"),
    HELPCOMMAND(ChatColor.YELLOW + "Kingdom help:"+"\n"+
    ChatColor.YELLOW + "All Kingdom commands start with /kingdom"+"\n"+
    ChatColor.YELLOW + "Kingdom settings:"+"\n"+
    ChatColor.YELLOW + "-/kingdom settings -  open settings gui of the Kingdom you're in"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings name <args>- Name your Kingdom!"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings title <args>- Set the title when entering your Kingdom, only possible when the Kingdom has a name"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings addadmin <player>- Add an Admin to the Kingdom"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings addadminvianame <playername> <kingdomname>- Add an Admin to a Kingdom via the name of the Kingdom(Only Owner)"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings removeadmin <player>- Remove an Admin from the Kingdom"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings removeadminvianame <playername> <kingdomname>- Remove an Admin from a Kingdom via the name of the Kingdom(Only Owner)"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings addmember <player>- Add a Member to the Kingdom"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings addmembervianame <playername> <kingdomname>- Add a Member to a Kingdom via the name of the Kingdom(Only Owner)"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings removemember <player>- Remove a Member from the Kingdom"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings removemembervianame <playername> <kingdomname>- Remove a Member from a Kingdom via the name of the Kingdom(Only Owner)"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings delete- Delete the Kingdom you're in"+"\n"+
    ChatColor.YELLOW + "  -/kingdom settings deleteall- Delete all Kingdoms!"+"\n"+
    ChatColor.YELLOW + "-/kingdom create <name(optional)>- Create a Kingdom with 2 right clicks!"),
    GIVENAMEBEFORESETTINGTITLE(ChatColor.RED+"You have to give the Kingdom a name before setting the title!"),
    MAXTITLELENGTH(ChatColor.RED+("The title can only be "+Kingdoms.getInstance().getConfig().getInt("maxtitlelength")+" letters long!")),
    NEWTITLESET(ChatColor.GREEN+"The Kingdoms new title is " ),
    NORIGTHSCHANGETITLE(ChatColor.RED+"You don't have the rights to change the title when entering the Kingdom!"),
    NORIGHTSRESIZEKINGDOM(ChatColor.RED + "You don't have the rights to resize this kingdom!"),
    NEEDTOBEINKINGDOM(ChatColor.RED+"You need to be in a Kingdom to use this command!"),
    SPECIFYPLAYERANDKINGDOM(ChatColor.RED+"You need to specify the Player and Kingdom name!"),
    MADEADMIN(ChatColor.GREEN+"Made the Player Admin!"),
    ALREADYADMIN(ChatColor.RED+"That Player is already an Admin!"),
    KINGDOMNOTSPECIFIED(ChatColor.RED+"Couldn't find the specified Kingdom!"),
    PLAYERNOTSPECIFIED(ChatColor.RED+"Couldn't find the specified Player!"),
    PLAYERNOLONGERADMIN(ChatColor.GREEN+"The Player is no longer Admin!"),
    SPECIFIEDPLAYERNOTADMIN(ChatColor.RED+"The specified Player is not an Admin!"),
    MADEMEMBER(ChatColor.GREEN+"Made the Player Member!"),
    ALREADYMEMBER(ChatColor.RED+"That Player is already a Member!"),
    NOLONGERMEMBER(ChatColor.GREEN+"The Player is no longer Member!"),
    SPECIFIEDPLAYERNOTMEMBER(ChatColor.RED+"The specified Player is not a Member!"),
    NEWKINGDOMNAME(ChatColor.GREEN+"The Kingdoms new name is "),
    NORIGHTSCHANGENAME(ChatColor.RED+"You don't have the rights to change the name of the Kingdom!"),
    DELETEDALLKINGDOMS(ChatColor.GREEN + "Deleted all Kingdoms! There is no way to reverse this Action!"),
    UNKNOWNCOMMAND(ChatColor.RED + "Unknown command!"),
    NORIGHTSACTION(ChatColor.RED + "You don't have the rights to perform this action!"),
    NORIGHTSENTERKINGDOM(ChatColor.RED + "You don't have the rights to enter the Kingdom of "),
    NORIGHTSENTERKINGDOMNAME(ChatColor.RED + "You don't have the rights to enter the Kingdom of "),
    CANTCREATEKINGDOMWHILEIINKINGDOM(ChatColor.RED + "You can't build a new Kingdom while in a Kingdom!"),
    FIRSTLOCATIONSET(ChatColor.GREEN  + "First location set!"),
    CORNERSELECTED(ChatColor.GREEN + "Selected corner to move!"),
    CORNERNOTSELECTED(ChatColor.RED + "You need to select a corner of your Kingdom!"),
    GAVEBLOCKS(ChatColor.GREEN + "You have given "),
    RECEIVEDBLOCKS(ChatColor.GREEN + "You have received "),
    CANTSETLOCATIONSINDIFFERENTWORLDS(ChatColor.RED + "You can't set the Locations in different Worlds!"),
    CANTCREATEKINGDOMHERE(ChatColor.RED + "You cant create a new Kingdom here!"),
    KINGDOMTOOBIG(ChatColor.RED + "Your Kingdom is too big!"),
    KINGDOMBIGGERTHANPERMISSION(ChatColor.RED + "You don't have the rights to build bigger kingdom than "),
    KINGDOMCREATED(ChatColor.GREEN + "Kingdom created!"),
    KINGDOMRESIZED(ChatColor.GREEN + "Kingdom resized!"),
    ACTIVATEDITEM(ChatColor.GREEN + "Activated"),
    DEACTIVATEDITEM(ChatColor.GREEN + "Deactivated"),
    VIEWADMINSANDMEMBERSITEM(ChatColor.GOLD + "View Members and Admins"),
    DESTROYPLACEBLOCKSITEM(ChatColor.GOLD + "Destroy/place Blocks"),
    DESTROYPLACEBLOCKLOREITEM(ChatColor.DARK_GREEN + "Manage who can place/destroy blocks"),

    SETTINGSITEM(ChatColor.GOLD + "Settings"),
    SETTINGSLOREITEM(ChatColor.DARK_GREEN + "Manage who can change settings"),
    PVPSETTINGSITEM(ChatColor.GOLD+"PvP settings"),
    PVPSETTINGSLOREITEM(ChatColor.DARK_GREEN + "Manage who can PvP"),
    REMOVEADDROLESITEM(ChatColor.GOLD+"Remove/add roles"),
    REMOVEADDROLESLORE1ITEM(ChatColor.DARK_GREEN + "Manage who can add/remove"),
    REMOVEADDROLESLORE2ITEM(ChatColor.DARK_GREEN + "Admin and Member rights"),

    ACCESSSETTINGSITEM(ChatColor.GOLD + "Access settings"),
    ACCESSSETTINGSLORE1ITEM(ChatColor.DARK_GREEN +"Change who can "),
    ACCESSSETTINGSLORE2ITEM(ChatColor.DARK_GREEN +"enter the kingdom"),
    TNTITEM(ChatColor.GOLD + "TnT"),
    TNTLOREITEM(ChatColor.DARK_GREEN + "Manage if TnT should explode"),
    PARTICLEITEM(ChatColor.GOLD + "Particle Color"),
    PARTICLELOREITEM(ChatColor.DARK_GREEN + "Change the Particle Color"),
    PETSITEM(ChatColor.GOLD + "Pets"),
    PETSLOREITEM(ChatColor.DARK_GREEN + "Manage Pets"),
    INTERACTITEM(ChatColor.GOLD + "Interact"),
    INTERACTLORE1ITEM(ChatColor.DARK_GREEN + "Manage who can "),
    INTERACTLORE2ITEM(ChatColor.DARK_GREEN + "Right click and hit Mobs"),
    SETTINGSINVENTORYNAME(ChatColor.GOLD + "settings"),
    ACCESSSETTINGINVENTORYNAME(ChatColor.GOLD + "Access settings"),
    EVERYONEITEMNAME(ChatColor.YELLOW + "Everyone"),
    MEMBERSITEMNAME(ChatColor.GOLD + "Members"),
    ADMINSITEMNAME(ChatColor.RED + "Admins"),
    OWNERITEMNAME(ChatColor.GREEN + "Owner"),
    EVERYONERIGHTITEMLORE("Everyone"),
    MEMBERSPLUSRIGHTITEMLORE("Members+"),
    ADMINSANDOWNERRIGHTITEMLORE("Admins and the Owner"),
    OWNERRIGHTITEMLORE("Only the Owner"),
    ACCESSSETTINGSRIGHT(" can access the settings"),
    ADDMEMBERSSETTINGSINVENTORYNAME(ChatColor.GOLD + "Add Members settings"),
    ADDMEMBERSRIGHT(" can add Members"),
    ADDADMINSINVENTORYNAME(ChatColor.GOLD + "Add Admins settings"),
    ADDADMINSRIGHT(" can add Admins"),
    REMOVEADMINSSETTINGSINVENTORYNAME(ChatColor.GOLD + "Remove Admins settings"),
    REMOVEADMINSRIGHT(" can remove Admins"),
    REMOVEMEMBERRIGHT(" can remove Members"),
    REMOVEMEMBERSETTINGSINVENTORYNAME(ChatColor.GOLD + "Remove Members settings"),
    ADDANDREMOVESETTINGSINVENTORYNAME(ChatColor.GOLD + "Add and remove settings"),
    ADDADMINSITEM(ChatColor.YELLOW + "Add Admins"),
    REMOVEADMINSITEM(ChatColor.GOLD + "Remove Admins"),
    REMOVEMEMBERSITEM(ChatColor.RED + "Remove Members"),
    ADDMEMBERSITEM(ChatColor.GREEN + "Add Members"),

    TNTSETTINGSINVENTORYNAME(ChatColor.GOLD + "TnT settings"),
    TNTACTIVATEDITEM(ChatColor.GREEN + "Activate"),
    TNTDEACTIVATEDITEM(ChatColor.GREEN + "Deactivate"),
    ADMINSOWNERANDMEMBERSETTINGSINVENTORYNAME(ChatColor.GOLD + "Admins, Owner and Members settings"),
    VIEWMEMBERSITEM(ChatColor.GOLD + "Members"),
    VIEWADMINSITEM(ChatColor.RED + "Admins"),
    VIEWOWNER(ChatColor.GREEN + "Owner"),

    BLOCKSETTINGSINVENTORYNAME(ChatColor.GOLD + "Block settings"),
    BLOCKSETTINGSITEMNAME("can destory/place blocks"),
    INTERACTSETTINGSINVENTORYNAME(ChatColor.RED + "Interact settings"),
    INTERACTSETTINGSITEMNAME(" can Interact"),
    PVPSETTINGSINVENTORYNAME(ChatColor.GOLD + "PvP settings"),
    PVPSETTINGSITEMNAME(" can hit players"),
    DAMAGEPETSETTINGSINVENTORYNAME(ChatColor.GOLD + "Damage Pets settings"),
    DAMAGEPETSRIGHTITEM("can damage Pets"),
    INVULNERABLEPETSSETTINGSINVENTORYNAME(ChatColor.GOLD + "Invulnerable Pets settings"),
    INVULNERABLEPETS(ChatColor.YELLOW + "Invulnerable"),
    INVULNERABLEPETSITEMLORE("Pets are Invulerable"),
    NOTINVULNERABLEPETS(ChatColor.YELLOW + "Not Invulerable"),
    NOTINVULNERABLEPETSITEMLORE("Pets are not Invulerable"),
    PETSETTINGSINVENTORY(ChatColor.GOLD + "Pet settings"),
    INVULNERABLEPETSSETTINGSINVENTORYITEMNAME(ChatColor.YELLOW + "Invulnerable Pets"),
    MANAGEIFPETSSHOULDBEINVULNERABLE(ChatColor.DARK_GREEN + "Manage if Pets should be Invulerable"),
    DAMAGEPETSSETTINGS(ChatColor.RED + "Damage Pets"),
    WHOCANHITINVULNERABLEPETSETTINGSITEM(ChatColor.DARK_GREEN + "Manage who can damage invulnerable Pets"),
    ENTERKINGDOMSETTINGS(ChatColor.GOLD + "Enter Kingdom settings"),
    ENTERKINGDOMRIGHTS("can enter the Kingdom"),

    OWNERSETTINGSINVENTORYNAME(ChatColor.GOLD + "Owner settings"),
    ADMINSSETTINGSINVENTORYNAME(ChatColor.GOLD + "Admins settings"),
    MEMBERSSETTINGSINVENTORYNAME(ChatColor.GOLD + "Members settings"),

    PARTICLESETTINGSINVENTORYNAME(ChatColor.GOLD + "Particle settings"),
    ASHPARTICLESITEMNAME(ChatColor.GRAY + "Ash particles"),
    VILLAGERPARTICLESITEMNAME(ChatColor.GREEN + "Villager particles"),
    LAVAPARTICLESITEMNAME(ChatColor.RED + "Lava particles"),
    WATERPARTICLESITEMNAME(ChatColor.BLUE + "Water particles"),
    NOPARTICLESITEMNAME("No particles");

    Messages(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;


}
