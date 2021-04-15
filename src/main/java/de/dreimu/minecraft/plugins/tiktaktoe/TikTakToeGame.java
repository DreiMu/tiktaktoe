package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.dreimu.minecraft.plugins.apis.guiapi.FunctionDeclarationException;
import de.dreimu.minecraft.plugins.apis.guiapi.GUI;
import de.dreimu.minecraft.plugins.apis.guiapi.GUIItem;

public class TikTakToeGame {

    private static HashMap<Player,TikTakToePlayer> playerToTTTPlayer = new HashMap<Player,TikTakToePlayer>();

    private static HashMap<Player,UUID> playerToUUID = new HashMap<Player,UUID>();

    private static HashMap<String,TikTakToeGame> easyIDToGame = new HashMap<String,TikTakToeGame>();

    private String easyGameID;

    public String getEasyGameID() {
        return easyGameID;
    }

    private Boolean privat = true;

    private static HashMap<Boolean,ArrayList<TikTakToeGame>> privatToTTTGame = new HashMap<Boolean,ArrayList<TikTakToeGame>>();

    public static HashMap<Boolean,ArrayList<TikTakToeGame>> getPrivateGames() {
        return privatToTTTGame;
    }

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
    public TikTakToeGame(UUID gameUUID, UUID emptyFieldUUID, UUID redFieldUUID, UUID blueFieldUUID, String easyID, Boolean privat) {

        this.emptyFieldUUID = emptyFieldUUID;
        this.redFieldUUID = redFieldUUID;
        this.blueFieldUUID = blueFieldUUID;

        easyIDToGame.put(easyID, this);
        this.easyGameID = easyID;

        this.gameUUID = gameUUID;
        IdToGame.put(this.gameUUID, this);

        this.privat = privat;
        if (!privatToTTTGame.containsKey(privat)) {
            privatToTTTGame.put(privat, new ArrayList<TikTakToeGame>());
        }
        privatToTTTGame.get(privat).add(this);
    }

    // Ein Getter für die UUID
    public UUID getUUID() {
        return this.gameUUID;
    }

    // Ein Getter für easyIDToGame
    public static TikTakToeGame getGameFromEasyID(String easyID){
        if(easyIDToGame.containsKey(easyID)){
        return easyIDToGame.get(easyID);
        } else {
            throw new NullPointerException();
        }
    }  
    
    // Gibt das Game Objekt der angegebenen ID zurück
    public static TikTakToeGame getGameFromID(UUID UUID) {
        return IdToGame.get(UUID);
    }

    public static UUID getUUIDFromPlayer(Player player) {
        return playerToUUID.get(player);
    }

    public static TikTakToePlayer getTTTPlayerFromPlayer(Player player) {
        if (playerToTTTPlayer.containsKey(player)) {
            return playerToTTTPlayer.get(player);
        } else {
            return null;
        }
    }

    // Gibt die Spieler dieses Spiels zurück
    public Player[] getPlayers() {
        try {
            Player[] tempReturnArray = {player1.getPlayer(),player2.getPlayer()};
            return tempReturnArray;
        } catch(Exception e) {
            try {
                Player[] temp2ReturnArray = {player1.getPlayer()};
                return temp2ReturnArray;
            } catch (Exception e2) {
                Player[] temp3ReturnArray = {};
                return temp3ReturnArray;
            }
        }
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
    private int[][] verteilungsArray = {
        {0,0,0},
        {0,0,0},
        {0,0,0}
    };

    // Setzt den Spieler 1
    public void setPlayer1(TikTakToePlayer player1) {
        try {
            throw new FunctionDeclarationException("errorMessage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            playerToUUID.remove(this.player1.getPlayer());
            playerToTTTPlayer.remove(this.player1.getPlayer());
        } catch (Exception e){}
        playerToUUID.put(player1.getPlayer(),this.gameUUID);
        playerToTTTPlayer.put(player1.getPlayer(), player1);
        this.player1 = player1;
        this.activePlayer = player1;
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
    public int[][] getVerteilung() {
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
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                if(verteilungsArray[x][y] == 1)
                    returnArray[x][y] = true;
                else {
                    returnArray[x][y] = null;
                }
            }
        }

        // Gibt das Array aus.
        return returnArray;
    } 

    public static UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    public static String generateRandomEasyUUID() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {

            int index
                = (int)(AlphaNumericString.length()
                        * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                        .charAt(index));
        }
        return sb.toString();
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
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                if(verteilungsArray[x][y] == 2)
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
    public ItemStack[][] getItemStackArray(int[][] verteilungsArray) {
        //trueItem kann "redGameField" oder "redGameField" sein, falseItem ist das andere.

        //Leeres itemStackArray wird erstellt.
        ItemStack empytyItem = GUIItem.uuidToGuiItem(emptyFieldUUID).getItemStack();
        ItemStack[][] itemStackArray = {{empytyItem,empytyItem,empytyItem},{empytyItem,empytyItem,empytyItem},{empytyItem,empytyItem,empytyItem}};

        //TODO: Wird 2x ausgeführt...

        // Iteriert über dieses Array
        for(int i = 0; i < verteilungsArray.length && i < itemStackArray.length; i++) {
            for(int j = 0; j < verteilungsArray[i].length && j < itemStackArray[i].length; j++) {

                //wenn das Feld im VerteilungsArray 1 ist, wird das GUIItem mit der ID der Variable trueItem verwendet, wenn es false ist, wird falseItem verwendet, bei keinem von beiden wird "emptyGameField" verwendet.
                if(verteilungsArray[i][j] == 1) {
                    itemStackArray[i][j] = GUIItem.uuidToGuiItem(blueFieldUUID).getItemStack();
                } else if(verteilungsArray[i][j] == 2) {
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
    public Boolean playerClicked(TikTakToePlayer player, int slot, GUIItem hisItem) {
        // Erlaubte Werte:
        // slot = 3-5 12-14 21-23 
        // field = 0-8
        int row = 0;
        int field = 0;

        // this.activePlayer = player;

        if(player.isActivePlayer()) {
            if(player==player1) {
                player1.getPlayer().playSound(player1.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, (float) 1.0, (float) 0.5);
                player2.getPlayer().playSound(player2.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, (float) 1.0, (float) 0.52);
            } else {
                player2.getPlayer().playSound(player2.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, (float) 1.0, (float) 0.5);
                player1.getPlayer().playSound(player1.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, (float) 1.0, (float) 0.52);
            }
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
            if(player.isPlayer1()) {
                this.verteilungsArray[row][field] = 1;
            } else {
                this.verteilungsArray[row][field] = 2;
            }

            if(player.isActivePlayer()) {
                try {
                    TikTakToePlayer winner = this.getWininngPlayer();
                    TikTakToePlayer looser;
                    if(winner == player1) {
                        looser = player2;
                    } else {
                        looser = player1;
                    }
                    if(winner != null) {
                        Bukkit.broadcastMessage(winner.getPlayer().getDisplayName()+" hat ein TikTakToe Spiel gegen "+looser.getPlayer().getDisplayName()+" gewonnen!");
                        this.getPlayers()[0].getWorld().spawnParticle(Particle.FIREWORKS_SPARK,  this.getPlayers()[0].getLocation(), 100, 1, 1, 1);
                        looser.getPlayer().playSound(looser.getPlayer().getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.MASTER, (float) 1.0, (float)1.0);
                        winner.getPlayer().playSound(winner.getPlayer().getLocation(), Sound.BLOCK_BEACON_ACTIVATE, SoundCategory.MASTER, (float) 1.0, (float)1.0);
                    }
                } catch(Exception ex) {}
            }

            return true;
        }
        return false;
    }

    public void updateInventory(Player player, UUID redGameGUIItemUUID, UUID blueGameGUIItemUUID, UUID gameUUID, GUI gui) {
        TikTakToePlayer tikTakToePlayer = TikTakToeGame.getTTTPlayerFromPlayer(player);
        TikTakToeGame tikTakToeGame = TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player));

        GUIItem playerItem;

        if(tikTakToePlayer.isPlayer1()) {
            playerItem = GUIItem.uuidToGuiItem(redGameGUIItemUUID);
        } else {
            playerItem = GUIItem.uuidToGuiItem(blueGameGUIItemUUID);
        }

        ItemStack[][] gameFieldItemStacks = TikTakToeGame.getGameFromID(gameUUID).getItemStackArray(TikTakToeGame.getGameFromID(gameUUID).verteilungsArray);
        ItemStack[] oldContents = player.getOpenInventory().getTopInventory().getContents();
        ItemStack[] inventoryStacks = {
        oldContents[0],     oldContents[1],     oldContents[2],     gameFieldItemStacks[0][0],  gameFieldItemStacks[0][1],  gameFieldItemStacks[0][2],  oldContents[6],     oldContents[7],     oldContents[8],
        oldContents[9],     oldContents[10],    oldContents[11],    gameFieldItemStacks[1][0],  gameFieldItemStacks[1][1],  gameFieldItemStacks[1][2],  oldContents[15],    oldContents[16],    oldContents[17],
        oldContents[18],    oldContents[19],    oldContents[20],    gameFieldItemStacks[2][0],  gameFieldItemStacks[2][1],  gameFieldItemStacks[2][2],  oldContents[24],    oldContents[25],    oldContents[26]};
        
        player.getOpenInventory().getTopInventory().setContents(inventoryStacks);
        Player[] players = TikTakToeGame.getGameFromID(TikTakToeGame.getUUIDFromPlayer(player)).getPlayers();
        Player player1 = players[0];

        if(gui.inventoryIsGUI(player1.getOpenInventory().getTopInventory())) {
            player1.getOpenInventory().getTopInventory().setContents(inventoryStacks);
        }

        if(players.length == 2) {
            Player player2 = players[1];

            if(gui.inventoryIsGUI(player2.getOpenInventory().getTopInventory())) {
                player2.getOpenInventory().getTopInventory().setContents(inventoryStacks);
            }
        }
    }

    public TikTakToePlayer getWininngPlayer() {
        if(isPlayerWinning(1)) {
            this.activePlayer = null;
            System.out.println(player1.getPlayer().getName());
            return this.player1;
        }

        if(isPlayerWinning(2)) {
            System.out.println(player1.getPlayer().getName());
            this.activePlayer = null;
            return this.player2;
        }

        return null;
    }

    private boolean isPlayerWinning(int i) {
        Boolean[][] tempArray;
        switch (i) {
            case 2:
                tempArray = this.getArrayForPlayer2();
                break;
            default:
                tempArray = this.getArrayForPlayer1();
                break;
        }

        for(int row = 0; row<=3; row++) {
            if(tempArray[row][0]!=null&&tempArray[row][1]!=null&&tempArray[row][2]!=null) {
                System.out.println("true");
                return true;
            }
        }

        for(int column = 0; column<=3; column++) {
            if(tempArray[0][column]!=null&&tempArray[1][column]!=null&&tempArray[2][column]!=null) {
                System.out.println("true");
                return true;
            }
        }

        if((tempArray[0][0]!=null&&tempArray[1][1]!=null&&tempArray[2][2]!=null)||(tempArray[2][0]!=null&&tempArray[1][1]!=null&&tempArray[0][2]!=null)) {
            System.out.println("true");
            return true;
        }

        return false;
    }

    public void changeActivePlayer() {
        if(player1.isActivePlayer()) {
            this.activePlayer = player2;
        } else {
            this.activePlayer = player1;
        }
    }

    public void resetGamefield() {
        int[][] emptyArray = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
        };
        this.verteilungsArray = emptyArray;
    }

    public void delete() {
        Main main = (Main) GUI.plugin;
        privatToTTTGame.get(this.privat).remove(this);
        try {
            TikTakToeGame.playerToTTTPlayer.remove(this.player1.getPlayer());
        }catch(Exception e) {}
        try {
            main.getGUI().setGuiAufbau(player1.getPlayer(), main.getMainUUID());
            player1.delete();
            player1 = null;
        } catch(Exception e) {}
        try {
            TikTakToeGame.playerToTTTPlayer.remove(this.player2.getPlayer());
        }catch(Exception e) {}
        try {
            main.getGUI().openGui(player2.getPlayer(), main.getMainUUID());
            player2.delete();
            player2 = null;
        }catch(Exception e) {}
    }

    public void setPrivat() {
        this.privat = true;
        try {
            privatToTTTGame.get(false).remove(this);
        } catch (Exception e) {}

        if (privatToTTTGame.get(this.privat) == null) {
            privatToTTTGame.put(this.privat, new ArrayList<TikTakToeGame>());
        }

        privatToTTTGame.get(true).add(this);
    }

    public void setOeffentlich() {
        try {
            this.privat = false;
        
            privatToTTTGame.get(true).remove(this);

            if (privatToTTTGame.get(this.privat) == null) {
                privatToTTTGame.put(this.privat, new ArrayList<TikTakToeGame>());
            }
            
            privatToTTTGame.get(false).add(this);
        } catch (Exception e) {throw e;}

        
    }

    public static ArrayList<TikTakToeGame> getOeffentlichGames() {
        ArrayList<TikTakToeGame> games = new ArrayList<TikTakToeGame>();

        return privatToTTTGame.get(false);
    }

    public static void setGamePrivat(TikTakToeGame game) {
        try {
            privatToTTTGame.get(false).remove(game);
        } catch (Exception e) {}

        privatToTTTGame.get(true).add(game);
    }

    public static void setGameOeffentlich(TikTakToeGame game) {
        try {
            privatToTTTGame.get(true).remove(game);
        } catch (Exception e) {}

        privatToTTTGame.get(false).add(game);
    }
}
