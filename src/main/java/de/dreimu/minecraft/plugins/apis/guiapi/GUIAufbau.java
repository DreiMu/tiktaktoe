package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class GUIAufbau {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private static HashMap<String,GUIAufbau> idToGUIAufbau = new HashMap<String,GUIAufbau>();

    private String[] SlotToItem;

    private HashMap<String,GUIItem> usedItems;

    private String guiAufbauId;

    private String guiName;
    
    public GUIAufbau(String GUIid) throws IDIsAlreadyUsed {
        try {
            setGuiID(GUIid);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setGuiID(String newGuiID) throws IDIsAlreadyUsed {
        if(newGuiID == guiAufbauId){return;} else {
            if(usedIDs.contains(newGuiID)) {
                throw new IDIsAlreadyUsed("The GUI ID: \""+newGuiID+"\" is already used!");
            } else {

                try {
                    usedIDs.remove(guiAufbauId);
                    idToGUIAufbau.remove(guiAufbauId);
                } catch(Exception e) {}

                idToGUIAufbau.put(guiAufbauId, this);
                usedIDs.add(newGuiID);
                guiAufbauId = newGuiID;
            }
        }
    } public String getGUIid() {
        return this.guiAufbauId;
    } 

    public void setGUIName(String newGUIName) {
        this.guiName = newGUIName;
    } public String getGUIName() {
        return this.guiName;
    }

    public void setGUIAufbau(String[] GUIAufbau) {
        this.SlotToItem = GUIAufbau;
    } public String[] getGUIAufbau() {
        return this.SlotToItem;
    } 

    public static GUIAufbau idToGuiAufbau(String ID) {
        return idToGUIAufbau.get(ID);
    }

    public ItemStack[] getItemStackList() {
        List<ItemStack> itemStackList = new ArrayList<ItemStack>();
        for(int i = 0; i >= 27; i++) {
            
            itemStackList.add(GUIItem.idToGuiItem(this.SlotToItem[i]).getItemStack());
        }

        Object[] objectList = itemStackList.toArray();
        ItemStack[] stringArray =  Arrays.copyOf(objectList,objectList.length,ItemStack[].class);

        return stringArray;
    }
}
