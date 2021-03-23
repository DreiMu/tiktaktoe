package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class GUI {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private String GUIID;
    private String GUITitle;

    private static List<GUIItem> items;

    public GUI(String GUIName, String GUITitle) {
        this.GUIID = GUIName;
        setGUITitle(GUITitle);
    }

    private static HashMap<String,GUI> IDtoGUI = new HashMap<String,GUI>();

    private Inventory inventory = Bukkit.createInventory(null, 27);

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
        items.add(new GUIItem(itemFunction, itemFunctionInfos, itemID, itemName, enchanted, guiItemMaterial, itemLore));
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

    public static Inventory getInventoryFromID(String guiID) { return IDtoGUI.get(guiID).inventory; }
}
