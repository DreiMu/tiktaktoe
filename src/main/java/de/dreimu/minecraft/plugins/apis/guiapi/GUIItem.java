package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIItem {

    private static ArrayList<String> usedIDs = new ArrayList<String>();

    private String itemID;
    private String itemName;
    private Boolean enchanted;
    private ItemStack guiItem;
    private ItemMeta guiItemMeta;
    private List<String> itemLore;
    private Material itemMaterial;
    private GUIItemFunction guiItemFunction;

    public GUIItem(String itemFunction, String itemID, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) throws IDIsAlreadyUsed, FunctionDeclarationException{

        try {
            setID(itemID);
        } catch (IDIsAlreadyUsed e) {
            throw new IDIsAlreadyUsed(e.getMessage());
        }

        try {
            this.guiItemFunction = new GUIItemFunction(this, itemFunction);
        } catch (FunctionDeclarationException e) {
            //TODO: handle exception
        }
        
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
                } catch(Exception e) {}

                usedIDs.add(newItemID);
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

        this.guiItem.setItemMeta(this.guiItemMeta);

        return this.guiItem;
    }

    public ItemStack getItemStack() {
        return this.generateItem();
    }
}
