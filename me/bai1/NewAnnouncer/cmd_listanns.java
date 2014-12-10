package me.bai1.NewAnnouncer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_listanns implements CommandExecutor {
    NewAnnouncer NewAnnouncer = (NewAnnouncer) Bukkit.getServer().getPluginManager().getPlugin("NewAnnouncer");
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.hasPermission("newannouncer.list"))) {
            sender.sendMessage(NewAnnouncer.noPerm);
            return true;
        }
        
        if(args.length == 0) {
            listAnnouncements(sender, 1);
            return true;
        }
        
        if(args.length >= 1) {
            try {
                int index = Integer.parseInt(args[0]);
                if(index < 1) {
                    sender.sendMessage(ChatColor.RED + "Page number is too low");
                    return true; 
                }
                listAnnouncements(sender, index);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "Page index must be a number");
                return true;
            }
        }
        return true;
    }
    
    public void listAnnouncements(CommandSender sender, int page) {
        int displayed = page * 8;
        if((displayed - 8) >= NewAnnouncer.annAnnouncements) {
            sender.sendMessage(ChatColor.RED + "Page number is too high");
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "+---- " + ChatColor.AQUA + "Announcements" + ChatColor.GREEN + " -----");
        sender.sendMessage(ChatColor.GREEN + "| Prefix: " + NewAnnouncer.annPrefix.replace("&","\u00A7"));
        int i = displayed - 8;
        while(i <= (displayed - 1) && i < NewAnnouncer.annAnnouncements) {
            if(NewAnnouncer.annMessages.get(i) == null) {
                return;
            }
            sender.sendMessage(ChatColor.GREEN + "| " + (i + 1) + ": " + ChatColor.AQUA + NewAnnouncer.annMessages.get(i));
            i++;
        }
    }
}