package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GUIItem {

    private static ArrayList<UUID> usedUUIDs = new ArrayList<UUID>();

    private static HashMap<UUID,GUIItem> uuidToItem = new HashMap<UUID,GUIItem>();

    private String itemName;
    private Boolean enchanted;
    private ItemStack guiItem;
    private ItemMeta guiItemMeta;
    private List<String> itemLore;
    private Material itemMaterial;
    private GUIItemFunction guiItemFunction;
    private String itemFunction;
    private String[] itemFunctionInfos;
    private UUID uuid;
    

    public GUIItem(String itemFunction, String[] itemFunctionInfos, UUID uuid, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) {
        
        this.uuid = uuid;
        usedUUIDs.add(uuid);
        uuidToItem.put(uuid, this);

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

    public String[] getItemFunctionInfos() {
        return this.itemFunctionInfos;
    }

    public UUID getUUID() {
        return this.uuid;
    } public String getUUIDString() {
        return this.uuid.toString();
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
        dataContainer.set(new NamespacedKey(GUI.plugin, "itemUUID"), PersistentDataType.STRING, this.getUUIDString());

        this.guiItem.setItemMeta(this.guiItemMeta);

        return this.guiItem;
    }

    public ItemStack getItemStack() {
        return this.generateItem();
    }

    public static GUIItem uuidToGuiItem(UUID uuid) {
        return uuidToItem.get(uuid);
    }

    public void runFunction(InventoryClickEvent e, GUI plugin) {

        // TODO: remove debug
        System.out.println("runFunction");

        final Player player; player = (Player) e.getWhoClicked();

        int slot = e.getRawSlot();

        Inventory inv = e.getClickedInventory();

        PersistentDataContainer dataContainer = this.getItemStack().getItemMeta().getPersistentDataContainer();

        String function = dataContainer.get(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING);
        System.out.println(function);
        switch(function.toLowerCase()) {
            case "closegui":
                this.guiItemFunction.closeGUI(player);
                break;
            case "opengui":
                this.guiItemFunction.openGUI(player);
                break;
            case "setitem":
                this.guiItemFunction.setItem(inv, slot);
                break;
            case "setitems":
                this.guiItemFunction.setItems(player);
                break;
            case "customfunction":
                this.guiItemFunction.customFunction(e, player, inv, slot, plugin);
                break;
            default:
                System.out.println("default");
                break;
        }

    }

    public GUIItemFunction getItemFunction() {
        return this.guiItemFunction;
    }
}
