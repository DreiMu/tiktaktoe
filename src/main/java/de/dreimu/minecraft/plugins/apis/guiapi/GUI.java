package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class GUI {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private String GUIID;
    private String GUITitle;

    public GUI(String GUIName, String GUITitle) {
        this.GUIID = GUIName;
        setGUITitle(GUITitle);
    }

    private static HashMap<String,GUIItem> idToGuiItem = new HashMap<String,GUIItem>();

    private Inventory inventory = Bukkit.createInventory(null, 27);

    public void setID(String newGUIID) throws IDIsAlreadyUsed {
        if(newGUIID == this.GUIID){return;} else {
            if(usedIDs.contains(newGUIID)) { 
                throw new IDIsAlreadyUsed("The Item ID: \""+newGUIID+"\" is already used!");
            } else {

                try {
                    usedIDs.remove(this.GUIID);
                } catch(Exception e) {}

                usedIDs.add(newGUIID);
                this.GUIID = newGUIID;
            }
        }
    } public String getID() {
        return this.GUIID;
    }

    public static void createGUIItem(String itemFunction, String itemID, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) throws IDIsAlreadyUsed, FunctionDeclarationException {
        GUIItem item = new GUIItem(itemFunction, itemID, itemName, enchanted, guiItemMaterial, itemLore);

        idToGuiItem.put(itemID, item);
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
}
