package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// Die Klasse GUIAufbau
public class GUIAufbau {

    // Das Inventory des GUIAufbau
    private Inventory inventory;

    // Speichert für jede ID den dazugehörigen GUIAufbau
    private static HashMap<UUID,GUIAufbau> uuidToGUIAufbau = new HashMap<UUID,GUIAufbau>();

    // Speichert eine Liste für die Items der GUI
    private String[] SlotToItem;

    // Speichert die ID
    private UUID guiUUID;

    // Speichert den Namen
    private String guiName;
    
    // Gibt die UUID zurück
    public UUID getUUID() {
        return this.guiUUID;
    }

    // Gibt einen String der UUID zurück
    public String getUUIDString() {
        return this.guiUUID.toString();
    }

    // Gibt die angegebenen Parameter an init() weiter
    public GUIAufbau(UUID uuid, String name){init(uuid,name);}
    public GUIAufbau(String name){init(name);}

    // Setzt this.guiUUID auf den übergebenen Wert uuid und this.guiName auf name
    private void init(UUID uuid, String name){
        this.guiUUID=uuid;
        this.guiName=name;
        uuidToGUIAufbau.put(uuid, this);
    }

    // setzt this.guiUUID auf eine zufällig generierte UUID und this.guiName auf name.
    private void init(String name){
        init(this.guiUUID=UUID.randomUUID(),name);
    }

    public Inventory getInventory() {
        if(this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, 27, this.guiName);
        }
        this.inventory.setContents(this.getItemStackArray());
        // Gibt das Inventar der guiAnsicht zurück
        return this.inventory;

    }

    public void setGUIAufbau(String[] GUIAufbau) {
        this.SlotToItem = GUIAufbau;
    } public String[] getGUIAufbau() {
        return this.SlotToItem;
    } 

    public static GUIAufbau idToGuiAufbau(UUID uuid) {
        return uuidToGUIAufbau.get(uuid);
    }

    private static ItemStack[] StringArrayToItemStackArray(String[] StringArray) {

        //System.out.println("DEBUG!");

        List<ItemStack> itemStackList = new ArrayList<ItemStack>();

        for(String temp: StringArray) {

            try {
                GUIItem guiItem = GUIItem.uuidToGuiItem(UUID.fromString(temp));
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

    public ItemStack[] getItemStackArray() {

        return StringArrayToItemStackArray(this.getGUIAufbau());
        
    }
}
