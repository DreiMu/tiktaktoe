package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class Main implements Listener{

    private final static ItemStack backgroundItem = createGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Bitte nicht entnehmen!");
    private final static ItemStack emptyFieldItem = createGUIItem(Material.LIGHT_GRAY_CONCRETE, "Hier Klicken, um dieses Feld zu wählen");
    private final static ItemStack redFieldItem = createGUIItem(Material.RED_CONCRETE, "Dieses Feld ist besetzt");
    private final static ItemStack blueFieldItem = createGUIItem(Material.BLUE_CONCRETE, "Dieses Feld ist besetzt");
    private final static ItemStack leaveGameItem = createGUIItem(Material.RED_STAINED_GLASS_PANE, "Spiel verlassen");

    public Player player;
    public String GUIType;

    public Main Main(Player player, String GUIType) {
        this.player = player;
        this.GUIType = GUIType;
        return(this);
    }

    }
    
    public static void game(String GUIName, String ID, Player player) {
        setItems("game", ID, player);
    }

    public static void game(String GUIName, Player player) {
        game(GUIName, null, player);
    }

    public static void main(String GUIName, Player player) {
        setItems("main", null, player);
    }

    public static InventoryView openGui(Player player, String GUItype, String ID) {

        

        String guiName;

        if(Variables.guiTypeToName().get(GUItype)== null) {
            guiName = "Error";
        } else {
            guiName = Variables.guiTypeToName().get(GUItype);
        } 
        
        if(Variables.verteilung.containsKey(player)) {

            Inventory inv = Bukkit.createInventory(player, 27, guiName);
            Variables.verteilung.put(player, inv);
        }
        
        switch(GUItype) {
            case "game":
                game(guiName, ID, player);
                break;
            case "main":
                main(guiName, player);
                break;
            default:
                main(guiName, player);
                break;
        }

        return player.openInventory(inv);
    }

    public static InventoryView resume(Player player) throws Exception {
        try {
            return player.openInventory(Variables.verteilung.get(player));
        } catch(Exception e) {
            throw e;
        }
    }

    public static InventoryView openGui(Player player, String GUItype) {
        return(openGui(player, GUItype, null));
    }

    private static ItemStack createGUIItem(Material material, String name, String...lore) {
        final ItemStack GUIItem = new ItemStack(material);
        final ItemMeta GUIItemMeta = GUIItem.getItemMeta();
        GUIItemMeta.setDisplayName(name);
        GUIItem.setItemMeta(GUIItemMeta);
        return GUIItem;
    }

    private static void setInventory(HashMap<Integer,String> array, Player player) {
        ItemStack item;

        if(Variables.verteilung.containsKey(player)) {
            inv = Variables.verteilung.get(player);
        } else {
            inv = Bukkit.createInventory(player, 27, "name!");
            Variables.verteilung.put(player, inv);
        }

        for (int i = 0; i < array.size(); i++) {
            item = null;
            switch (array.get(i)){
                case "background":
                    item = backgroundItem;
                    break;
                case "empty":
                    item = emptyFieldItem;
                    break;
                case "leaveGame":
                    item = leaveGameItem;
                    break;
                case "redField":
                    item = redFieldItem;
                    break;
                case "blueField":
                    item = blueFieldItem;
                    break;
                default:
                    item = backgroundItem;
                    break;
            }

            inv.setItem(i, item);
        }

        Variables.verteilung.put(player, inv);
        
    }

    public static void setItems(String GUItype, String ID, Player player) {
        setInventory(Variables.guiTypeToArray(GUItype, ID), player);
    } public static void setItems(String GUItype, Player player) {
        setItems(GUItype, null, player);
    }

    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());

        e.getInventory().setItem(e.getRawSlot(), backgroundItem);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public static void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }

}