package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

// Die Klasse GUIAufbau
public class GUIAufbau {

    // Das Inventory des GUIAufbau
    private HashMap<Player,Inventory> playerToInventory = new HashMap<Player,Inventory>();

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

    public Inventory getInventory(Player player) {
        if(!this.playerToInventory.containsKey(player)) {
            this.playerToInventory.put(player, Bukkit.createInventory(player, 27, this.guiName));
        }
        this.playerToInventory.get(player).setContents(this.getItemStackArray());
        // Gibt das Inventar der guiAnsicht zurück

        return this.playerToInventory.get(player);

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

        List<ItemStack> itemStackList = new ArrayList<ItemStack>();

        for(String temp: StringArray) {

            try {
                GUIItem guiItem = GUIItem.uuidToGuiItem(UUID.fromString(temp));
                
                ItemStack itemStack = guiItem.getItemStack();
                
                itemStackList.add(itemStack);
            } catch(Exception e) {
                itemStackList.add(null);
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
