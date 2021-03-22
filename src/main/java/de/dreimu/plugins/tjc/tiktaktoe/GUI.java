package de.dreimu.plugins.tjc.tiktaktoe;

import java.util.ArrayList;

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

public class GUI implements Listener{

    public static Inventory inv;


    private static ItemStack backgroundItem = createGUIItem(Material.LIGHT_GRAY_STAINED_GLASS, "Bitte nicht entnehmen!", "");

    private static ItemStack emptyFieldItem = createGUIItem(Material.LIGHT_GRAY_CONCRETE, "Hier Klicken, um dieses Feld zu wählen");

    private static ItemStack redFieldItem = createGUIItem(Material.RED_CONCRETE, "Dieses Feld ist besetzt");

    private static ItemStack blueFieldItem = createGUIItem(Material.BLUE_CONCRETE, "Dieses Feld ist besetzt");

    private static ItemStack leaveGameItem = createGUIItem(Material.RED_STAINED_GLASS_PANE, "Spiel verlassen");



    public static void game(String GUIName, String ID) {
        setItems("game", ID);
    }

    public static void game(String GUIName) {
        game(GUIName, null);
    }

    public static void main(String GUIName) {
        setItems("main", null);
    }

    public static InventoryView openGui(Player player, String GUItype, String ID) {

        String guiName;

        if(Variables.guiTypeToName().get(GUItype)== null) {
            guiName = "Error";
        } else {
            guiName = Variables.guiTypeToName().get(GUItype);
        }
        
        switch(GUItype) {
            case "game":
                game(guiName, ID);
                break;
            case "main":
                main(guiName);
                break;
            default:
                main(guiName);
                break;
        }


        return player.openInventory(inv);
    }

    public static InventoryView openGui(Player player, String GUItype) {
        return(openGui(player, GUItype, null));
    }

    private static ItemStack createGUIItem(Material material, String name, String...lore) {
        ItemStack GUIItem = new ItemStack(material);
        ItemMeta gUIItemMeta = GUIItem.getItemMeta();
        gUIItemMeta.setDisplayName(name);
        GUIItem.setItemMeta(gUIItemMeta);
        return GUIItem;
    }

    private static void setInventory( ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            ItemStack item;
            switch (array.get(i)){
                case "background":
                    item = backgroundItem;
                case "empty":
                    item = emptyFieldItem;
                case "leaveGame":
                    item = leaveGameItem;
                case "redField":
                    item = redFieldItem;
                case "blueField":
                    item = blueFieldItem;
                default:
                    item = emptyFieldItem;
            }

            inv.setItem(i, item);
        }
    }

    public static void setItems(String GUItype, String ID) {
        setInventory(Variables.guiTypeToArray(GUItype, ID));
    } public static void setItems(String GUItype) {
        setItems(GUItype, null);
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
    }

    // Cancel dragging in our inventory
    @EventHandler
    public static void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }

}