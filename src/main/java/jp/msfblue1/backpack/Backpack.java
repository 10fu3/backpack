package jp.msfblue1.backpack;

import jp.msfblue1.backpack.DataManager.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Backpack extends JavaPlugin implements Listener{

    public static Integer howmany = 0;
    public final static String PREFIX = ChatColor.DARK_GRAY+"["+ChatColor.BLUE+"BackPack"+ChatColor.DARK_GRAY+"] ";
    //private Map<Player,DataManager> Cache = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.howmany = Integer.valueOf(getConfig().getString("maxchest","0"));
        getCommand("pack").setExecutor(new CommandManager());
        Bukkit.getPluginManager().registerEvents(this,this);
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+"起動しました");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.YELLOW +"10fu3は本プラグインのすべての権利を保有します。");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.RED +"10fu3は本プラグインの利用・動作によって発生した一切の責任を負いません。");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+"Developer URL : https://github.com/10fu3");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+"Copyright Ⓒ 2017 10fu3 All Rights Reserved.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.RED +"終了しました");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+"Developer URL : https://github.com/10fu3");
        Bukkit.getConsoleSender().sendMessage(PREFIX+ChatColor.GREEN+"Copyright Ⓒ 2017 10fu3 All Rights Reserved.");
    }

    @EventHandler
    public void getOpenInv(InventoryOpenEvent e){

    }

    @EventHandler
    public void getCloseInv(InventoryCloseEvent e){
        //Bukkit.getConsoleSender().sendMessage(String.valueOf(e.getInventory().getName()));
        DataManager manager = new DataManager(e.getPlayer().getUniqueId().toString());
        if(e.getInventory() != null){
            manager.getData(e.getInventory().getName()).ifPresent(d->{
                if(d.getInventory() != null){
                    d.setInventory(e.getInventory());
                    manager.saveData(d);
                }
            });
        }
    }
}
