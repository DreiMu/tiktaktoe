package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class GUI implements Listener{

    public static Plugin plugin;

    public GUI() {}

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private String GUIID;
    private String GUITitle;

    private static List<GUIItem> items = new ArrayList<GUIItem>();

    public GUI(String GUIName, String GUITitle) {
        this.GUIID = GUIName;
        setGUITitle(GUITitle);
    }

    public void setStandardGUIAufbau(String guiID) {
        GUIAufbau guiAufbau = GUIAufbau.idToGuiAufbau(guiID);
        ItemStack[] itemStackList = {};
        String[] StringArray = guiAufbau.getGUIAufbau();
        itemStackList = guiAufbau.getItemStackList(StringArray);
        this.inventory.setContents(itemStackList);
    }

    public InventoryView openGui(Player player) {

        player.sendMessage(this.inventory.toString());
        return player.openInventory(this.inventory);

    }

    public InventoryView resume(Player player) throws Exception {
        try {
            return player.openInventory(this.inventory);
        } catch(Exception e) {
            throw e;
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (this.inventory != e.getInventory()) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        //ArrayList<String> usedIDs = GUIAufbau.getUsedIDs();

        final Player player = (Player) e.getWhoClicked();

        try{if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING)) {

            //ItemStack item = e.getCurrentItem();

            PersistentDataContainer dataContainer = e.getCurrentItem().getItemMeta().getPersistentDataContainer();

            String itemID = dataContainer.get(new NamespacedKey(GUI.plugin, "itemID"), PersistentDataType.STRING);

            GUIItem.idToGuiItem(itemID).runFunction(player, e.getClickedInventory(), e.getRawSlot());
        }} catch(Exception exception){}

        // Using slots click is a best option for your inventory click's
        player.sendMessage("You clicked at slot " + e.getRawSlot());

        if (!(clickedItem == null || clickedItem.getType() == Material.AIR)) player.sendMessage("Item: " + e.getCurrentItem().getItemMeta().getDisplayName());
    }


    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (this.inventory != e.getInventory()) {
          e.setCancelled(true);
        }
    }

    private static HashMap<String,GUI> IDtoGUI = new HashMap<String,GUI>();

    private Inventory inventory = Bukkit.createInventory(null, 27, "TikTakToe");

    public void setID(String newGUIID) throws IDIsAlreadyUsed {
        if(newGUIID == this.GUIID){return;} else {
            if(usedIDs.contains(newGUIID)) { 
                throw new IDIsAlreadyUsed("The Item ID: \""+newGUIID+"\" is already used!");
            } else {

                try {
                    IDtoGUI.remove(this.GUIID);
                    usedIDs.remove(this.GUIID);
                } catch(Exception e) {}

                IDtoGUI.put(this.GUIID, this);
                usedIDs.add(newGUIID);
                this.GUIID = newGUIID;
            }
        }
    } public String getID() {
        return this.GUIID;
    }

    public static void createGUIItem(String itemFunction, String[] itemFunctionInfos, String itemID, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) throws IDIsAlreadyUsed, FunctionDeclarationException {
        GUIItem guiItem = new GUIItem(
            itemFunction, 
            itemFunctionInfos, 
            itemID, 
            itemName, 
            enchanted, 
            guiItemMaterial, 
            itemLore);
        items.add(guiItem);
    }
    

    public void setGUITitle(String GUITitle) {
        this.GUITitle = GUITitle;
    }

    public String getGUITitle() {
        return this.GUITitle;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setGuiAufbau(Player player, String guiID) {
        player.getOpenInventory().getTopInventory().setContents(GUIAufbau.idToGuiAufbau(guiID).getItemStackList(GUIAufbau.idToGuiAufbau(guiID).getGUIAufbau()));
    }

    public static Inventory getInventoryFromID(String guiID) { return IDtoGUI.get(guiID).inventory; }
}
