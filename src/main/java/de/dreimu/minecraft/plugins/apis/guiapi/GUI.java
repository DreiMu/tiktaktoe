package de.dreimu.minecraft.plugins.apis.guiapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


// Die Klasse GUI
public class GUI implements Listener {

    private ArrayList<Player> playersWhoUsedGUI = new ArrayList<Player>();

    // Ein Array aller Inventare
    private ArrayList<Inventory> inventoryArray = new ArrayList<Inventory>();
    // Das standard Inventar
    private Inventory standardInventory;

    // Die Liste der GUIListener
    private List<GUIListener> listeners = new ArrayList<GUIListener>();

    // Die Funktion zum hinzufügen der GUIListener
    public void addGUIListener(GUIListener listener){

        // Fügt der Liste der GUIListener den angegebenen GUIListener hinzu.
        listeners.add(listener);
    }

    // Abspeichern des Throwers in der Variable thrower
    Thrower thrower = new Thrower();

    // Hier wird das Plugin abgespeichert, um mehr Funktionen zu ermöglichen.
    public static Plugin plugin;

    // Eine HashMap in der Für jeden Spieler die Letzte geöffnete GUI Gespeichert wird, sodass sie mit einem Resume Command angezeigt werden kann
    private HashMap<Player,Inventory> playerToLatestOpenedInventory = new HashMap<Player,Inventory>();

    // Getter für playerToLatestOpenedGUI
    public ItemStack[] getLatestItemStackArray(Player player){

        // Gibt das inventar aus.
        return playerToLatestOpenedInventory.get(player).getContents();
    } 

    // Die Initiatoren
    public GUI(UUID guiUUID, String guiTitle) {
        // Die guiID wird gesetzt
        this.guiUUID = guiUUID;
        // Der Titel der GUI wird gesetzt
        setGuiTitle(guiTitle);

        this.standardInventory = Bukkit.createInventory(null, 27, guiTitle);
    }

    // Der guiTitle werden abgespeichert.
    private String guiTitle;

    // Die UUID wird abgespeichert
    private UUID guiUUID;

    // Eine Liste aller verwendeten GUIItems
    private static List<GUIItem> items = new ArrayList<GUIItem>();

    // GUIAufbauArray, eine Liste aller verwendeten GUIAubaus
    private ArrayList<GUIAufbau> guiAufbauList= new ArrayList<GUIAufbau>();

    // Die Funktion zum Setzen des Standard Aufbaus.
    public void setStandardGUIAufbau(UUID guiUUID) {

        // Der gesuchte guiAufbau wird in guiAufbau gespeichert.
        GUIAufbau guiAufbau = GUIAufbau.idToGuiAufbau(guiUUID);

        // Es wird ein Leeres ItemStackArray erstellt
        ItemStack[] itemStackArray = {};

        // Das ItemStackArray wird in itemStackArray gespeichert. 
        itemStackArray = guiAufbau.getItemStackArray();

        // Der Inhalt des Inventars wird auf den Standard GUI Aufbau gesetzt.
        Inventory inventory = this.standardInventory;
        inventory.clear();
        inventory.setContents(itemStackArray);
        this.addGUIAufbau(guiUUID);
    }

    public Boolean inventoryIsGUI(Inventory inventory) {
        return this.inventoryArray.contains(inventory);
    }

    // Die Funktion zum Anzeigen des Inventars / der GUI
    public InventoryView openGui(Player player, UUID guiUUID) {
        // Das Inventar wird dem übergebenen Spieler angezeigt.
        this.playersWhoUsedGUI.add(player);
        Inventory inv = this.getInventory(guiUUID, player);
        if(!this.inventoryArray.contains(inv)) {
            this.inventoryArray.add(inv);
        }
        return player.openInventory(inv);
    }

    // Die Funktion zum Anzeigen des zuletzt geöffneten Inventars.
    public InventoryView resume(Player player) throws Exception {
        try {
            return player.openInventory(getLatestOpenedInventory(player));
        } catch(Exception e) {
            throw e;
        }
    }

    public Inventory getLatestOpenedInventory(Player player) {
        return this.playerToLatestOpenedInventory.get(player);
    }

    public void closeAllInventorys() {
        this.playersWhoUsedGUI.forEach(player -> {
            if(this.inventoryIsGUI(player.getOpenInventory().getTopInventory())) {
                player.closeInventory();
            }
        });
    }

    // Wird aufgerufen, sobald ein Slot im Inventar gedrückt wird
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        // Wenn das angeklickte Inventar nicht in der Liste der GUIs ist, wird das Event ignoriert
        if (!this.inventoryIsGUI(e.getClickedInventory())) return;

        // Wenn das Event in einem GUI Inventar passiert, wird das Ziehen des Items abgebrochen.
        e.setCancelled(true);

        // Das angeklickte Item wird abgespeichert
        ItemStack clickedItem = e.getCurrentItem();

        // Der Spiieler, der das Item angeklickt hat, wird abgespeichert.
        Player player = (Player) e.getWhoClicked();
        
        // Spielt einen Klick Ton an
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, SoundCategory.MASTER, (float) 1.0, (float) 0.5);

        // Wenn das Item den NBT Tag function des Plugins hat, wird diese Funktion aufgerufen
        try{if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(GUI.plugin, "function"), PersistentDataType.STRING)) {

            // Speichert die itemID ab.
            UUID itemUUID = UUID.fromString(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(GUI.plugin, "itemUUID"), PersistentDataType.STRING));

            // führt die Funktion des Items aus
            GUIItem.uuidToGuiItem(itemUUID).runFunction(e, this);
        }} catch(Exception exception){}

        // Wenn das Item nicht Luft oder gar kein Item ist, wird dem Spieler eine Nachricht geschickt, in der Itemname ist.
    }


    // Lässt dich nichts falsches in dem Inventar machen, indem es das Event des Ziehen eines Items im Inventar abbricht.
    @EventHandler public void onInventoryClick(InventoryDragEvent e) {if (!this.inventoryArray.contains(e.getInventory())) {e.setCancelled(true);}} 

    // Setzt, sobald man eine GUI schließt diese zusammen mit dem Spieler, der das getan hat, in die HashMap playerToLatestOpenedGUI
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {

        // Der Spieler wird abgespeichert 
        Player player = (Player) e.getPlayer();

        // Wenn das Inventar eine GUI ist und das Inventar von einem Player geschlossen wurde, wird das Inventar zusammen mit dem Spieler in playerToLatestOpenedGUI abgespeichert.
        if (this.inventoryArray.contains(e.getInventory()) && player instanceof Player) {
            // Das Inventar wird zusammen mit dem Spieler in playerToLatestOpenedGUI abgespeichert
            playerToLatestOpenedInventory.put(player, e.getInventory());
        }
    }

    // Fügt einen neuen GUIAufbau der Liste und dem Array guiAufbauList beziehungsweise inventoryArray hinzu.
    public void addGUIAufbau(UUID guiAufbauUUID) {
        this.guiAufbauList.add(GUIAufbau.idToGuiAufbau(guiAufbauUUID));
    }

    // Fügt einen Neuen Listener hinzu.
    public void initListener(GUIListener listener) {
        this.addGUIListener(listener);
    }

    // Die HashMap, in der für jede ID die dazugehörige GUI gespeichert wird.
    private static HashMap<String,GUI> IDtoGUI = new HashMap<String,GUI>();

    // Einfach ein Getter für die UUID...
    public UUID getID(){return this.guiUUID;}

    // Die Funktion leitet einfach an die gleich benannte funktion von thrower weiter
    public void slotWasClicked(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin){this.thrower.slotWasClicked(e, player, inv, slot, plugin);}

    // Die Funktion zum erstellen eines neuen GUIItems
    public static void createGUIItem(String itemFunction, String[] itemFunctionInfos, UUID itemUUID, String itemName, Boolean enchanted, Material guiItemMaterial, String...itemLore) {
        
        // Es wird ein neues GUIItem erstellt
        GUIItem guiItem = new GUIItem(itemFunction,itemFunctionInfos,itemUUID,itemName,enchanted,guiItemMaterial,itemLore);

        // Dieses wird der Item Liste hinzugefügt.
        items.add(guiItem);
    }
    
    // Ein Setter für guiTitle
    public void setGuiTitle(String guiTitle){this.guiTitle = guiTitle;}
    // Ein Getter für guiTitle
    public String getGuiTitle(){return this.guiTitle;}

    // Ein Getter für das standard Inventar
    public Inventory getStandardInventory(){return this.standardInventory;}

    // Ändert das geöffnete Inventar des Spielers zu dem angegebenen GUIAufbau
    public void setGuiAufbau(Player player, UUID guiAufbauUUID) {

        // Setzt das Inventar des angegebenen Spielers zu dem angegebenem GUIAufbau
        player.getOpenInventory().getTopInventory().setContents(GUIAufbau.idToGuiAufbau(guiAufbauUUID).getItemStackArray());
    }

    // Ein getter für das Standard Inventar der angegebenen GUI
    public static Inventory getInventoryFromID(String guiID){return IDtoGUI.get(guiID).standardInventory;}

    // Diese Klasse schickt an alle Listener ein Signal, sobald slotWasClicked aufgerufen wird.
    class Thrower {
    
        // Deklariert die Funktion
        public void slotWasClicked(InventoryClickEvent e, Player player, Inventory inv, int slot, GUI plugin){

            // Geht durch alle gespeicherten Listener ...
            for (GUIListener listener : plugin.listeners) {

                // ... und ruft deren Funktion slotWasClicked auf
                listener.slotWasClicked(e, player, inv, slot, plugin);
            }
        }
    }

    public Plugin getMain() {
        return plugin;
    }

    public Inventory getInventory(UUID aufbauUUID, Player player) {
        Inventory inv = GUIAufbau.idToGuiAufbau(aufbauUUID).getInventory(player);
        if(!this.inventoryArray.contains(inv)) {
            this.inventoryArray.add(inv);
        }
        return GUIAufbau.idToGuiAufbau(aufbauUUID).getInventory(player);
    }
}