package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.dreimu.minecraft.plugins.apis.guiapi.GUI;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIAufbau;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIItemFunction;

public class Main extends JavaPlugin {

    public static class TikTakToeListener implements Listener  {

    }

    private ArrayList<GUIAufbau> guiAufbauList = new ArrayList<GUIAufbau>();

    
    private GUI gui = new GUI();
    
    // This code is called after the server starts and after the /reload command
    @Override
    public void onEnable() {
        GUI.plugin = this;
        PluginManager pluginManager = getServer().getPluginManager();
        TikTakToeListener tikTakToeListener = new TikTakToeListener();
        pluginManager.registerEvents(tikTakToeListener, this);
        pluginManager.registerEvents(gui, this);
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());

        String[] backgroundGUIItemFunctionList = {};
        String[] openGameGUIItemFunctionList = {"gameGUI"};
        String[] openMainGUIItemFunctionList = {"mainGUI"};
        String[] selectEmptyFieldFunctionList = {"this", "emptyGameField"};
        String[] selectBlueFieldFunctionList = {"this", "blueGameField"};
        String[] selectRedFieldFunctionList = {"this", "redGameField"};
        try {
            {
                String[] mainGuiAufbau = {
                    "background","background","background","background","background","background","background","background","background",
                    "background","background","background","background","setGameGUI","background","background","background","background",
                    "background","background","background","background","background","background","background","background","background",};
                    guiAufbauList.add(0, new GUIAufbau("mainGUI"));
                    guiAufbauList.get(0).setGUIAufbau(mainGuiAufbau);
                    guiAufbauList.get(0).setGUIName("Main");
            }
            {
                String[] gameGuiAufbau = {
                    "background","background","background","emptyGameField","emptyGameField","emptyGameField","background","background","background",
                    "background","background","background","emptyGameField","emptyGameField","emptyGameField","background","background","background",
                    "background","background","background","emptyGameField","emptyGameField","emptyGameField","background","background","setMainGUI",};
                guiAufbauList.add(1, new GUIAufbau("gameGUI"));
                guiAufbauList.get(1).setGUIAufbau(gameGuiAufbau);
                guiAufbauList.get(1).setGUIName("Game");
            }
            {
                GUI.createGUIItem("null", backgroundGUIItemFunctionList, "background", "Bitte nicht Entnehmen!", false, Material.LIGHT_GRAY_STAINED_GLASS_PANE, "test", "test2");
                GUI.createGUIItem("setItems", openGameGUIItemFunctionList, "setGameGUI", "Play!", true, Material.BEACON , "test", "test2");
                GUI.createGUIItem("setItems", openMainGUIItemFunctionList, "setMainGUI", "Back to main menu", false, Material.RED_STAINED_GLASS_PANE , "test", "test2");
                GUI.createGUIItem("setItem", selectRedFieldFunctionList, "emptyGameField", "Wähle dieses Feld aus", false, Material.LIGHT_GRAY_CONCRETE , "test", "test2");
                GUI.createGUIItem("setItem", selectBlueFieldFunctionList, "redGameField", "Wähle dieses Feld aus", false, Material.RED_CONCRETE , "test", "test2");
                GUI.createGUIItem("setItem", selectEmptyFieldFunctionList, "blueGameField", "Wähle dieses Feld aus", false, Material.BLUE_CONCRETE , "test", "test2");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            gui.setStandardGUIAufbau("gameGUI");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(label.equals("tiktaktoe")) {
            if(player instanceof Player) {
                this.gui.openGui(player);
                gui.setGuiAufbau(player, "mainGUI");
            }
            if(args.length > 0) {
                    if(args[0].equals("debug")) {
                    if(sender.isOp()||sender.hasPermission("tiktaktoe.admin.debug")) {
                        if(player instanceof Player) {
                            if(args.length == 2) {
                                try {
                                    getLogger().log(Level.INFO, "msg");
                                    
                                    gui.setGuiAufbau(player, "mainGUI");
                                    InventoryView inventoryView = this.gui.openGui(player);
                                    
                                } catch(Exception e) {
                                    e.printStackTrace();
                                    getLogger().log(Level.WARNING, e.getMessage());
                                }
                            } else if(args.length >= 3 ) {
                                try {
                                    
                                    gui.setGuiAufbau(player, "mainGUI");
                                    this.gui.openGui(player);
                                    
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
                            this.gui.resume(player);
                        } catch(Exception e) {
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