package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.dreimu.minecraft.plugins.apis.guiapi.GUIItem;

public class TikTakToeGame {

    private static HashMap<Player,TikTakToePlayer> playerToTTTPlayer = new HashMap<Player,TikTakToePlayer>();

    private static HashMap<Player,UUID> playerToUUID = new HashMap<Player,UUID>();

    private UUID redFieldUUID;
    private UUID emptyFieldUUID;
    private UUID blueFieldUUID;

    // Speichert zu jeder gameID das Passende TikTakToeGame ab
    private static HashMap<UUID,TikTakToeGame> IdToGame = new HashMap<UUID,TikTakToeGame>();

    // Die gameID
    private UUID gameUUID;

    // Speichert den Aktiven Spieler.
    private TikTakToePlayer activePlayer;

    // Wird beim erstellen eines Spiels aufgerufen.
    public TikTakToeGame(UUID gameUUID, UUID emptyFieldUUID, UUID redFieldUUID, UUID blueFieldUUID) {
        this.emptyFieldUUID = emptyFieldUUID;
        this.redFieldUUID = redFieldUUID;
        this.blueFieldUUID = blueFieldUUID;

        this.gameUUID = gameUUID;
        IdToGame.put(this.gameUUID, this);
    }
    
    // Gibt das Game Objekt der angegebenen ID zurück
    public static TikTakToeGame getGameFromID(UUID UUID) {
        return IdToGame.get(UUID);
    }

    public static UUID getUUIDFromPlayer(Player player) {
        return playerToUUID.get(player);
    }

    public static TikTakToePlayer getTTTPlayerFromPlayer(Player player) {
        return playerToTTTPlayer.get(player);
    }

    // Gibt die Spieler dieses Spiels zurück
    public Player[] getPlayers() {
        Player[] returnArray = {player1.getPlayer(),player2.getPlayer()};
        return returnArray;
    }

    public boolean isActivePlayer(TikTakToePlayer player) {
        // Gibt aus, ob der Angegebene Spieler der Aktive Spieler ist, dh., ob dieser seinen "Stein" setzen darf, oder nicht.
        if(activePlayer == player) {
            return true;
        } else {
            return false;
        }
    }

    // Speichert den Spieler 1
    private TikTakToePlayer player1;
    // Speichert den Spieler 2
    private TikTakToePlayer player2;

    // Erstellt ein leeres, zweidimensionales Array.
    private Boolean[][] verteilungsArray = {
        {null,null,null},
        {null,null,null},
        {null,null,null}
    };

    // Setzt den Spieler 1
    public void setPlayer1(TikTakToePlayer player1) {
        try {
            playerToUUID.remove(this.player1.getPlayer());
            playerToTTTPlayer.remove(this.player1.getPlayer());
        } catch (Exception e){}
        playerToUUID.put(player1.getPlayer(),this.gameUUID);
        playerToTTTPlayer.put(player1.getPlayer(), player1);
        this.player1 = player1;
    } 
    
    // Gibt den Spieler 1 zurück.
    public TikTakToePlayer getPlayer1() {
        return player1;
    }

    // Setzt den Spieler 2
    public void setPlayer2(TikTakToePlayer player2) {
        try {
            playerToUUID.remove(this.player2.getPlayer());
            playerToTTTPlayer.remove(this.player2.getPlayer());
        } catch (Exception e){}
        playerToUUID.put(player2.getPlayer(),this.gameUUID);
        playerToTTTPlayer.put(player2.getPlayer(), player2);
        this.player2 = player2;
    } 
    
    // Gibt den Spieler 2 zurück.
    public TikTakToePlayer getPlayer2() {
        return player2;
    }

    // gibt die Verteilung aus
    public Boolean[][] getVerteilung() {
        return this.verteilungsArray;
    }

    // Gibt die Verteilung der "Steine" von Spieler 1 aus.
    public Boolean[][] getArrayForPlayer1() {

        // Erstellt ein leeres, zweidimensionales Array.
        Boolean[][] returnArray = {
            {null,null,null},
            {null,null,null},
            {null,null,null}
        };

        // Iteriert über die einzelnen Einträge in Verteilzng und wenn der Eintrag true ist, setzt er im zurückgegebenen Array an dieser Stelle ein true, ansonsten lässt er diesen Eintrag leer.
        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 9; y++) {
                System.out.print("x: \""+x+"\", y: \""+y+"\"");
                if(verteilungsArray[x][y] == true)
                    returnArray[x][y] = true;
                else {
                    returnArray[x][y] = null;
                }
            }
        }

        // Gibt das Array aus.
        return returnArray;
    } 
    
    // Gibt die Verteilung der "Steine" von Spieler 2 aus.
    public Boolean[][] getArrayForPlayer2() {

        // Erstellt ein leeres, zweidimensionales Array.
        Boolean[][] returnArray = {
            {null,null,null},
            {null,null,null},
            {null,null,null}
        };

        // Iteriert über die einzelnen Einträge in Verteilzng und wenn der Eintrag false ist, setzt er im zurückgegebenen Array an dieser Stelle ein true, ansonsten lässt er diesen Eintrag leer.
        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 9; y++) {
                System.out.print("x: \""+x+"\", y: \""+y+"\"");
                if(verteilungsArray[x][y] == false)
                    returnArray[x][y] = true;
                else {
                    returnArray[x][y] = null;
                }
            }
        }

        // Gibt das Array aus.
        return returnArray;
    }

    // Gibt ein ItemStackArray aus
    public ItemStack[][] getItemStackArray(Boolean[][] verteilungsArray) {
        //trueItem kann "redGameField" oder "redGameField" sein, falseItem ist das andere.

        //Leeres itemStackArray wird erstellt.
        ItemStack[][] itemStackArray = {{null,null,null},{null,null,null},{null,null,null}};

        // Iteriert über dieses Array
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {

                //wenn das Feld im VerteilungsArray true ist, wird das GUIItem mit der ID der Variable trueItem verwendet, wenn es false ist, wird falseItem verwendet, bei keinem von beiden wird "emptyGameField" verwendet.
                if(verteilungsArray[i][j] == true) {
                    itemStackArray[i][j] = GUIItem.uuidToGuiItem(blueFieldUUID).getItemStack();
                } else if(verteilungsArray[i][j] == false) {
                    itemStackArray[i][j] = GUIItem.uuidToGuiItem(redFieldUUID).getItemStack();
                } else {
                    itemStackArray[i][j] = GUIItem.uuidToGuiItem(emptyFieldUUID).getItemStack();
                }
            }
        }

        // Gibt das Array aus.
        return itemStackArray;
    }

    // Modifiziert die Verteiung und gibt ein überarbeitetes, zweidimensionales ItemStackArray aus.
    public ItemStack[][] playerClicked(TikTakToePlayer player, int slot, GUIItem hisItem) {
        // Erlaubte Werte:
        // slot = 3-5 12-14 21-23 
        // field = 0-8
        int row = 0;
        int field = 0;

        if(player.isActivePlayer()) {
            // Teilt den Integer slot, der den Inhalt 3, 4, 5, 12, 13, 14, 21, 22 oder 23 haben kann zu row und field um, welche Indexe für ein Zweidimensionales Array sind- 
            if(slot >= 3 && slot <= 5) {
                row = 0;
                field = slot - 3;
            } else if(slot >= 12 && slot <= 14) {
                row = 1;
                field = slot - 12;
            } else if(slot >= 21 && slot <= 23) {
                row = 2;
                field = slot - 21;
            } else {
                // Wenn slot einen falschen Wert hat, wird die Funktion mit einer NullPointerException abgebrochen.
                throw new NullPointerException();
            }

            // Setzt im verteilungsArray den übergebenen Slot auf den Wert des Spielers. 
            this.verteilungsArray[row][field] = player.isPlayer1();
        }

        // Gibt das Array als ItemStack Array aus.
        return this.getItemStackArray(verteilungsArray);
    }
}
