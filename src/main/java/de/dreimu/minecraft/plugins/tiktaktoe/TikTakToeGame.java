package de.dreimu.minecraft.plugins.tiktaktoe;

import java.util.ArrayList;
import java.util.HashMap;

public class TikTakToeGame {

    private static HashMap<String,TikTakToeGame> IdToGame = new HashMap<String,TikTakToeGame>();

    private String gameID;

    private TikTakToePlayer player1;
    private TikTakToePlayer player2;

    private Boolean[][] verteilungsArray = {
        {null,null,null},
        {null,null,null},
        {null,null,null}
    };

    private HashMap<Integer,HashMap<Integer,Boolean>> verteilung = new HashMap<Integer,HashMap<Integer,Boolean>>();

    public TikTakToeGame(String gameID) {
        setGameID(gameID);
    }

    public void setPlayer1(TikTakToePlayer player1) {
        this.player1 = player1;
    } public TikTakToePlayer getPlayer1() {
        return player1;
    }

    public void setPlayer2(TikTakToePlayer player2) {
        this.player2 = player2;
    } public TikTakToePlayer getPlayer2() {
        return player2;
    }

    public Boolean[][] getVerteilung() {
        return hashMapToArray();
    } public Boolean[][] getVerteilungPlayer1() {
        return hashMapToArrayForPlayer1();
    } public Boolean[][] getVerteilungPlayer2() {
        return hashMapToArrayForPlayer2();
    } 

    
    private Boolean[][] hashMapToArray() {
        Boolean[][] returnArray = {
            {null,null,null},
            {null,null,null},
            {null,null,null}
        };
        for(int x:verteilung.keySet()) {
            for(int y:verteilung.get(x).keySet()) {
                System.out.print("x: \""+x+"\", y: \""+y+"\"");
                returnArray[x][y] = verteilung.get(x).get(y);
            }
        }
        return returnArray;
    } private Boolean[][] hashMapToArrayForPlayer1() {
        Boolean[][] returnArray = {
            {null,null,null},
            {null,null,null},
            {null,null,null}
        };
        for(int x:verteilung.keySet()) {
            for(int y:verteilung.get(x).keySet()) {
                System.out.print("x: \""+x+"\", y: \""+y+"\"");
                if(verteilung.get(x).get(y) == true)
                    returnArray[x][y] = true;
                else {
                    returnArray[x][y] = null;
                }
            }
        }
        return returnArray;
    } private Boolean[][] hashMapToArrayForPlayer2() {
        Boolean[][] returnArray = {
            {null,null,null},
            {null,null,null},
            {null,null,null}
        };

        for(int x:verteilung.keySet()) {
            for(int y:verteilung.get(x).keySet()) {
                System.out.print("x: \""+x+"\", y: \""+y+"\"");
                if(verteilung.get(x).get(y) == false)
                    returnArray[x][y] = false;
                else {
                    returnArray[x][y] = null;
                }
            }
        }
        return returnArray;
    }

    public void setGameID(String newGameID) {
        if(gameID == newGameID) {
            return;
        } else {
            if(IdToGame.containsKey(newGameID)) {
                return;
            } else {
                try {
                    IdToGame.remove(gameID);
                } catch(Exception e){}

                this.gameID = newGameID;
                IdToGame.put(newGameID, this);
            }
        }
    }
}
