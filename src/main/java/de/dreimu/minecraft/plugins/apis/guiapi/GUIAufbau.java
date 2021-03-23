package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class GUIAufbau {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private ItemStack[] SlotToItem;

    private HashMap<String,ItemStack> usedItems;

    private String GUIid;

    private String GUIName;
    
    public GUIAufbau(String GUIid) throws IDIsAlreadyUsed {
        try {
            setGUIID(GUIid);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setGUIID(String newGUIID) throws IDIsAlreadyUsed {
        if(newGUIID == GUIid){return;} else {
            if(usedIDs.contains(newGUIID)) {
                throw new IDIsAlreadyUsed("The GUI ID: \""+newGUIID+"\" is already used!");
            } else {

                try {
                    usedIDs.remove(GUIid);
                } catch(Exception e) {}

                usedIDs.add(newGUIID);
                GUIid = newGUIID;
            }
        }
    } public String getGUIid() {
        return this.GUIid;
    } 

    public void setGUIName(String newGUIName) {
        this.GUIName = newGUIName;
    } public String getGUIName() {
        return this.GUIName;
    }

    public void setGUIAufbau(ItemStack[] GUIAufbau) {
        this.SlotToItem = GUIAufbau;
    } public ItemStack[] getGUIAufbau() {
        return this.SlotToItem;
    } 
}
