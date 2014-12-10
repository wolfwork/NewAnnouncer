package me.bai1.NewAnnouncer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_announce implements CommandExecutor {
    NewAnnouncer NewAnnouncer = (NewAnnouncer) Bukkit.getServer().getPluginManager().getPlugin("NewAnnouncer");
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(!(sender.hasPermission("newannouncer.access"))) {
            sender.sendMessage(NewAnnouncer.noPerm);
            return true;
        }
        
        if(args.length == 0) {
            showHelp(sender, command);
            return true;
        }
        
        String options = args[0];
        
        if(options.equalsIgnoreCase("add")) {
            if(args.length == 1) {
                sender.sendMessage(ChatColor.RED + "You must provide a message to add to the announcements index");
                return true;
            }
            
            String newAnnouncement = "";
            for(int i = 1; i < args.length; i++) {
                if(!(newAnnouncement.equalsIgnoreCase(""))) {
                    newAnnouncement = newAnnouncement + args[i] + " " ; 
                } else {
                    newAnnouncement = args[i] + " ";
                }
            }
            
            NewAnnouncer.annMessages.add(newAnnouncement.substring(0, newAnnouncement.length() -1));
            NewAnnouncer.getConfig().set("messages", NewAnnouncer.annMessages);
            NewAnnouncer.saveConfig();
            NewAnnouncer.reloadConfig();
            NewAnnouncer.annMessages = NewAnnouncer.getConfig().getStringList("messages");
            NewAnnouncer.annAnnouncements = NewAnnouncer.annMessages.size();
            sender.sendMessage(ChatColor.GREEN + "Announcement added");
            return true;
        }
        
        if(options.equalsIgnoreCase("prefix")) {
            if(args.length == 1) {
                sender.sendMessage(ChatColor.RED + "You must provide a prefix value");
                return true;
            }
            
            String newPrefix = "";
            for(int i = 1; i < args.length; i++) {
                if(!(newPrefix.equalsIgnoreCase(""))) {
                    newPrefix = newPrefix + args[i].replace(" ","") + " "; 
                } else {
                    newPrefix = args[i].replace(" ","") + " ";
                }
            }
            
            NewAnnouncer.getConfig().set("prefix", newPrefix.substring(0, newPrefix.length() -1));
            NewAnnouncer.saveConfig();
            NewAnnouncer.reloadConfig();
            NewAnnouncer.annPrefix = NewAnnouncer.getConfig().getString("prefix").replace("&", "\u00A7");
            sender.sendMessage("Prefix changed");
            return true;
        }
        
        if(args.length == 1) {
            if(options.equalsIgnoreCase("help")) {
                showHelp(sender, command);
                return true;
            }
            
            if(options.equalsIgnoreCase("random")) {
                sender.sendMessage(ChatColor.RED + "Value must be provided");
                return true;
            }
            
            if(options.equalsIgnoreCase("interval")) {
                sender.sendMessage(ChatColor.RED + "Value must be provided");
                return true;
            }
            
            if(options.equalsIgnoreCase("remove")) {
                sender.sendMessage(ChatColor.RED + "Index must be provided!");
                return true;
            }
            
            if(options.equalsIgnoreCase("broadcast")||options.equalsIgnoreCase("bc")) {
                sender.sendMessage(ChatColor.RED + "Index must be provided!");
                return true;
            }
            
            if(options.equalsIgnoreCase("reload")) {
                NewAnnouncer.getLogger().info("Reloading plugin");
                sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "NewAnnouncer" + ChatColor.GOLD + "] " + ChatColor.GREEN + "Reloading plugin");
                NewAnnouncer.loadProcess();
                NewAnnouncer.getLogger().info("Plugin reloaded");
                sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "NewAnnouncer" + ChatColor.GOLD + "] " + ChatColor.GREEN + "Plugin reloaded");
                return true;
            }
            
            if(options.equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.GREEN + "+---- " + ChatColor.AQUA + "Announcements" + ChatColor.GREEN + " -----");
                sender.sendMessage(ChatColor.GREEN + "| Prefix: " + NewAnnouncer.annPrefix.replace("&","\u00A7"));
                int i = 0;
                while(i < NewAnnouncer.annAnnouncements && i < 9/* && i < NewAnnouncer.annMessages.size()*/) {
                    if(NewAnnouncer.annMessages.get(i) == null) {
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "| " + (i + 1) + ": " + ChatColor.AQUA + NewAnnouncer.annMessages.get(i));
                    i++;
                }
                return true;
            }
            
            sender.sendMessage(ChatColor.RED + "\"/" + command.getName() + " " + args[0] + "\" is not a command");
            showHelp(sender, command);
            return true;
        }
        
        String index1 = args[1];
        
        if(args.length == 2) {
            if(options.equalsIgnoreCase("interval")) {
                try {
                    int index = Integer.parseInt(index1);
                    NewAnnouncer.getConfig().set("interval", index);
                    NewAnnouncer.annInterval = index;
                    NewAnnouncer.saveConfig();
                    NewAnnouncer.reloadConfig();
                    NewAnnouncer.setInterval(NewAnnouncer.annInterval);
                    sender.sendMessage(ChatColor.GREEN + "Interval changed to one announcement every " + index + " seconds");
                    return true;
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Value needs to be a number/integer!");
                    return true;
                }
            }
            if(options.equalsIgnoreCase("remove")) {
                try {
                    int index = Integer.parseInt(index1);
                    if(index <= NewAnnouncer.annMessages.size()) {
                        String exAnn = NewAnnouncer.annMessages.get(index - 1).replace("&","\u00A7");
                        NewAnnouncer.annMessages.remove(index - 1);
                        NewAnnouncer.getConfig().set("messages", NewAnnouncer.annMessages);
                        NewAnnouncer.saveConfig();
                        NewAnnouncer.reloadConfig();
                        NewAnnouncer.annMessages = NewAnnouncer.getConfig().getStringList("messages");
                        NewAnnouncer.annAnnouncements = NewAnnouncer.annMessages.size();
                        sender.sendMessage(ChatColor.GREEN + "Removed announcement at index " + index + ": " + ChatColor.AQUA + exAnn);
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "Announcement at index " + index + " does not exist!");
                    return true;
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Index needs to be a number/integer!");
                    return true;
                }
            }
            if(options.equalsIgnoreCase("random")) {
                String[] valuesOff = {"off","disabled","false"};
                String[] valuesOn = {"on","enabled","true"};
                for(int i = 0; i < 3; i++) {
                    if(index1.equalsIgnoreCase(valuesOn[i])) {
                        NewAnnouncer.getConfig().set("random", true);
                        NewAnnouncer.annRandom = true;
                        NewAnnouncer.saveConfig();
                        NewAnnouncer.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Announcement order modified successfully");
                        return true;
                    }
                    
                    if(index1.equalsIgnoreCase(valuesOff[i])) {
                        NewAnnouncer.getConfig().set("random", false);
                        NewAnnouncer.annRandom = false;
                        NewAnnouncer.saveConfig();
                        NewAnnouncer.reloadConfig();
                        if(NewAnnouncer.annRandom == true) {
                            Random randomNum = new Random();
                            NewAnnouncer.annNumber = Math.abs(randomNum.nextInt() % NewAnnouncer.annMessages.size());
                        } else {
                            if(NewAnnouncer.annNumber >= (NewAnnouncer.annMessages.size() - 1)) {
                                NewAnnouncer.annNumber = 0;
                            } else {
                                NewAnnouncer.annNumber++;
                            }
                        }
                        sender.sendMessage(ChatColor.GREEN + "Announcement order modified successfully");
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Cannot set whether to display in a random order to " + index1);
                return true;
            }
            
            if(options.equalsIgnoreCase("broadcast")||options.equalsIgnoreCase("bc")) {
                try {
                    int index = Integer.parseInt(index1);
                    if(index <= NewAnnouncer.annMessages.size()) {
                        String annMessage = NewAnnouncer.annMessages.get(index - 1);
                        if(annMessage == null) {
                            sender.sendMessage(ChatColor.RED + "Announcement at index " + index + " does not exist!");
                            return true;
                        }
                        NewAnnouncer.broadcastMessage(annMessage);
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Announcement at index " + index + " does not exist!");
                        return true;
                    }
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Index needs to be a number/integer!");
                    return true;
                }
            }
            
            sender.sendMessage(ChatColor.RED + "\"/" + command.getName() + " " + args[0] + " " + args[1] + "\" is not a command");
            showHelp(sender, command);
            return true;
        }
        
        if(args.length > 2) {
            sender.sendMessage(ChatColor.RED + "There is too many parameters for any known command");
            return true;
        }
        return true;
        
    }
    
    public void showHelp(CommandSender sender, Command command) {
        String prefix = ChatColor.GREEN + "| ";
        sender.sendMessage(ChatColor.GREEN + "+---- " + ChatColor.AQUA + "NewAnnouncer" + ChatColor.GREEN + " -----");
        sender.sendMessage(prefix + "/" + command.getName() + " | " + ChatColor.AQUA + "Shows this help");
        sender.sendMessage(prefix + "/" + command.getName() + " help | " + ChatColor.AQUA + "Shows this help");
        sender.sendMessage(prefix + "/" + command.getName() + " <bc|broadcast> <index> | " + ChatColor.AQUA + "Broadcasts the announcement corresponding to the index number");
        //sender.sendMessage(prefix + "");
        //sender.sendMessage(prefix + "");
        //sender.sendMessage(prefix + "");
        //sender.sendMessage(prefix + "");
        //sender.sendMessage(prefix + "");
    }
}