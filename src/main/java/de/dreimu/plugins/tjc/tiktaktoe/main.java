package de.dreimu.plugins.tjc.tiktaktoe;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static class TikTakToeListener implements Listener  {

        @EventHandler
        public boolean InventoryCloseEvent(InventoryCloseEvent event) {
            HumanEntity humanEntity = event.getPlayer();
            Player player = (Player) humanEntity;
            try {
                if(player instanceof Player){
                    player.sendMessage("message");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @EventHandler
        public void InventoryClickEvent(InventoryClickEvent event) {
            event.getWhoClicked().sendMessage("Action: "+event.getAction().toString()+"\nInventory: "+event.getInventory());
        }
    }
    
    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        TikTakToeListener tikTakToeListener = new TikTakToeListener();
        ExampleGui exampleGui = new ExampleGui();
        pluginManager.registerEvents(tikTakToeListener, this);
        pluginManager.registerEvents(exampleGui, this);
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equals("tiktaktoe")) {
            if(args.length > 0) {
                if(args[0].equals("debug")) {
                    if(sender.isOp()||sender.hasPermission("tiktaktoe.admin.debug")) {
                        if(player instanceof Player) {
                            if(args.length == 2) {
                                try {
                                    getLogger().log(Level.INFO, "msg");
                                    ExampleGui.initializeItems();
                                    ExampleGui.openInventory(player);

                                    GUI.openGui(player, args[1]);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            } else if(args.length >= 3 ) {
                                try {
                                    GUI.openGui(player, args[1], args[2]);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            }
                        }
                    } else {
                        sender.sendMessage("Du hast keine Rechte diesen Command zu benutzen!");
                    }
                } else if(args[0].equals("resume")) {
                    if(sender instanceof Player) {
                        try {
                        GUI.openGui(player, "game");
                        } catch(NullPointerException exception) {
                            player.sendMessage("Du hast kein laufendes Spiel oder irgendetwas ist schiefgelaufen.");
                        }
                    }
                }
            }
            getLogger().log(Level.INFO, "tiktaktoe cmd ran");
        }
        return true;
    }

    // This code is called before the server stops and after the /reload command
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }
}