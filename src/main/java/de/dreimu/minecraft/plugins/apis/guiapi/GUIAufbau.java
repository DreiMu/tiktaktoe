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

    private String guiAufbauId;

    private String guiName;
    
    public GUIAufbau(String GUIid) throws IDIsAlreadyUsed, Exception {

        try {
            setGuiID(GUIid);
        } catch (Exception e) {
            throw e;
        }
    }

    public GUIAufbau() {}

    public static Integer usedIDsLenght() {
        return usedIDs.size();
    }

    public static ArrayList<String> getUsedIDs() {
        return usedIDs;
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

                idToGUIAufbau.put(newGuiID, this);
                usedIDs.add(newGuiID);
                this.guiAufbauId = newGuiID;
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

    private static ItemStack[] StringArrayToItemStackArray(String[] StringArray) {

        //System.out.println("DEBUG!");

        List<ItemStack> itemStackList = new ArrayList<ItemStack>();

        for(String temp: StringArray) {

            try {
                GUIItem guiItem = GUIItem.idToGuiItem(temp);
                //System.out.println(temp);
                ItemStack itemStack = guiItem.getItemStack();
                //System.out.println(itemStack.getItemMeta().getDisplayName());
                
                itemStackList.add(itemStack);
                //System.out.println(itemStack.getType());
            } catch(Exception e) {
                e.printStackTrace();
            }

        }

        Object[] objectList = itemStackList.toArray();
        ItemStack[] stringArray = Arrays.copyOf(objectList,objectList.length,ItemStack[].class);

        return stringArray;
    }

    public ItemStack[] getItemStackList(String[] StringArray) {

        return StringArrayToItemStackArray(StringArray);
        
    }
}
