package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GUIItem {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private static HashMap<String,GUIItem> idToItem = new HashMap<String,GUIItem>();

    private String itemID;
    private String itemName;
    private Boolean enchanted;
    private ItemStack guiItem;
    private ItemMeta guiItemMeta;
    private List<String> itemLore;
    private Material itemMaterial;
    private GUIItemFunction guiItemFunction = new GUIItemFunction();
    private String itemFunction;
    private String[] itemFunctionInfos;
    

    public GUIItem(String itemFunction, String[] itemFunctionInfos, String itemID, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) throws IDIsAlreadyUsed, FunctionDeclarationException{
        
        try {
            setID(itemID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.itemFunctionInfos = itemFunctionInfos;

        try {
            this.guiItemFunction = new GUIItemFunction(itemFunctionInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.guiItem = new ItemStack(guiItemMaterial);
        
        this.itemFunction = itemFunction;
        this.itemName = itemName;
        this.enchanted = enchanted;
        this.itemMaterial = guiItemMaterial;
        this.itemLore = Arrays.asList(itemLore);
        this.guiItemMeta = this.guiItem.getItemMeta();

    }

    public void setID(String newItemID) throws IDIsAlreadyUsed {
        if(newItemID == this.itemID){return;} else {
            if(usedIDs.contains(newItemID)) { 
                throw new IDIsAlreadyUsed("The Item ID: \""+newItemID+"\" is already used!");
            } else {

                try {
                    usedIDs.remove(this.itemID);
                    idToItem.remove(this.itemID);
                } catch(Exception e) {}

                usedIDs.add(newItemID);
                idToItem.put(newItemID, this);
                this.itemID = newItemID;
            }
        }
    } public String getID() {
        return this.itemID;
    }

    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public void setLore(String itemLore) {
        this.itemLore = Arrays.asList(itemLore);
    }

    private ItemStack generateItem() {

        this.guiItem = new ItemStack(this.itemMaterial);
        this.guiItemMeta = this.guiItem.getItemMeta();
        this.guiItemMeta.setDisplayName(this.itemName);
        this.guiItemMeta.setLore(this.itemLore);
        if(this.enchanted == true) {
            this.guiItemMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        }

        this.guiItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);

        
        PersistentDataContainer dataContainer = this.guiItemMeta.getPersistentDataContainer();
        if(this.itemFunction != null) { 
            dataContainer.set(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING, this.itemFunction);
        } else {
            dataContainer.set(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING, "null");
        }
        dataContainer.set(new NamespacedKey(GUI.plugin, "itemID"), PersistentDataType.STRING, this.itemID);

        this.guiItem.setItemMeta(this.guiItemMeta);

        return this.guiItem;
    }

    public ItemStack getItemStack() {
        return this.generateItem();
    }

    public static GUIItem idToGuiItem(String ID) {
        return idToItem.get(ID);
    }

    public void runFunction(Player player, Inventory inv, int slot) {
        player.sendMessage("runFunction");
        PersistentDataContainer dataContainer = this.getItemStack().getItemMeta().getPersistentDataContainer();

        String function = dataContainer.get(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING);
        System.out.println(function);
        switch(function.toLowerCase()) {
            case "closegui":
                this.guiItemFunction.closeGUI(player);
                break;
            case "opengui":
                this.guiItemFunction.openGUI(player);
                System.out.println("openGUI");
                break;
            case "setitem":
                this.guiItemFunction.setItem(inv, slot);
                break;
            case "setitems":
                this.guiItemFunction.setItems(player);
                break;
            default:
                break;
        }

    }

    public GUIItemFunction getItemFunction() {
        return this.guiItemFunction;
    }
}
