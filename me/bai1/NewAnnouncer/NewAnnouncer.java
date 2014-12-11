package me.bai1.NewAnnouncer;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.bai1.NewTrans.NewTrans;
import me.bai1.NewTrans.Language;

public class NewAnnouncer extends JavaPlugin implements Listener {
    public static String annPrefix = "";
    public static List<String> annMessages;
    public static String noPerm = "";
    public static int annNumber = 0;
    public static int annAnnouncements = 1;
    public static int annInterval = 300;
    public static boolean annRandom = false;
    public static boolean NTC = false; 
    
    // NewTrans compatibility
    public static Plugin NewTransT;
    public static NewTrans NewTrans;
    public static Language consoleLang;
    
    public void onEnable() {
        this.saveDefaultConfig();
        
        NewTransT = Bukkit.getPluginManager().getPlugin("NewTrans");
        if(NewTransT != null) {
            NTC = true;
            NewTrans = (NewTrans) Bukkit.getServer().getPluginManager().getPlugin("NewTrans");
            getLogger().info("Hooking into " + NewTransT.toString());
            getLogger().info("Setting console language");
            String langStr = getConfig().getString("lang");
            if(langStr.equalsIgnoreCase("NewTrans")) {
                consoleLang = NewTrans.consoleLang;
            } else {
                consoleLang = NewTrans.findLang(getConfig().getString("lang"));
            }
            getLogger().info("Language: " + consoleLang.getName());
        }
        
        loadProcess();
        
        getLogger().info("Registering commands");
        getCommand("newannouncer").setExecutor(new cmd_announce());
        getCommand("newannounce").setExecutor(new cmd_announce());
        getCommand("newann").setExecutor(new cmd_announce());
        getCommand("na").setExecutor(new cmd_announce());
        getCommand("an").setExecutor(new cmd_announce());
        getCommand("ann").setExecutor(new cmd_announce());
        getCommand("announcements").setExecutor(new cmd_listanns());
        getCommand("listannouncements").setExecutor(new cmd_listanns());
        getCommand("listanns").setExecutor(new cmd_listanns());
        getCommand("anns").setExecutor(new cmd_listanns());
        //getCommand("announcer").setExecutor(new cmd_announce());
        //getCommand("an").setExecutor(new cmd_announce());
        //getCommand("ann").setExecutor(new cmd_announce());
        
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin enabled");
    }
    
    public void loadProcess() {
        reloadConfig();
        getLogger().info("Setting 'no permission' message");
        noPerm = getConfig().getString("no-permission").replace("&","\u00A7");
        
        getLogger().info("Setting announcement prefix");
        annPrefix = getConfig().getString("prefix").replace("&", "\u00A7");
        
        getLogger().info("Setting interval between announcements");
        annInterval = getConfig().getInt("interval");
        
        getLogger().info("Setting announcement order");
        annRandom = getConfig().getBoolean("random");

        getLogger().info("Reading announceable messages");
        annMessages = getConfig().getStringList("messages");
        annAnnouncements = annMessages.size();
        
        setInterval(annInterval);
    }
    
    public void onDisable() {
        
    }
    
    public void setInterval(int interval) {
        Bukkit.getScheduler().cancelTasks(Bukkit.getServer().getPluginManager().getPlugin("NewAnnouncer"));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("NewAnnouncer"),new Runnable() {
            public void run() {
                NewAnnouncer NewAnnouncer = (NewAnnouncer) Bukkit.getServer().getPluginManager().getPlugin("NewAnnouncer");
                if(NewAnnouncer.getConfig().getStringList("messages") == null) {
                    NewAnnouncer.annMessages.add("This is an announcement");
                    NewAnnouncer.annMessages.add("This is another announcement");
                    NewAnnouncer.getConfig().set("messages", NewAnnouncer.annMessages);
                    NewAnnouncer.saveConfig();
                    NewAnnouncer.reloadConfig();
                }
                String annMessage = NewAnnouncer.annMessages.get(NewAnnouncer.annNumber);
                NewAnnouncer.broadcastMessage(annMessage);
            }
        },interval * 20L, interval * 20L);
    }
    
    public static void broadcastMessage(String annMessage1) {
        Bukkit.broadcastMessage(annPrefix + annMessage1.replace("&", "\u00A7"));
        final String annMessage = annMessage1;
        
        if(NTC == true) {
            new Thread( new Runnable() {
                public void run() {
                    NewAnnouncer.NewTrans.printTranslation(annMessage, "CONSOLE", consoleLang);
                }
            }).start();
        }
           
        if(NewAnnouncer.annRandom == true) {
            Random randomNum = new Random();
            annNumber = Math.abs(randomNum.nextInt() % annMessages.size());
        } else {
            if(annNumber >= (annAnnouncements - 1)) {
                annNumber = 0;
            } else {
                annNumber++;
            }
        }
    }
}