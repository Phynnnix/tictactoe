package de.phynnnix.tictactoe;

import java.awt.Color;

/**
 * Klasse die die Spieler repräsentiert. 
 * Nutzt das Singleton Pattern, um je Id stets die identische Objektreferenz zu liefern.
 */
public class Player {
    private static Player[] players = {new Player(0, null), null, null, null, null, null, null};

    private int id;
    private String name;

    /**
     * Erstellt einen neuen Spieler mit Id und Name.
     * Um das Singleton Pattern zu wahren ist dieser Konstruktor nicht öffentlich zugänglich. 
     * Zum Erzeugen und Erhalten von Instanzen muss <code>.get</code> verwendet werden.
     * @param id Die Id des Spielers.
     * @param name Der Name des Spielers.
     */
    private Player(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Gibt den Spieler mit der angegebenen Spieler Id zurück.
     * Der zurückgegebene Spieler kann auch <code>null</code> sein, sofern die Id nie mit <code>.get(int, String)</code> erzeugt wurde.
     * @param playerId Die Spieler Id zu der der passende Spieler zurückgegeben werden soll.
     * @return Der Spieler mit der Id <code>playerId</code>. <code>null</code> wenn diese Id nie erzeugt wurde.
     */
    protected static Player get(int playerId){
        return players[playerId];
    }

    /**
     * Erzeugt neue Spieler wenn nötig, bzw gibt diese zurück.
     * Ist der Spieler bereits erzeugt wird ggfs. dessen Name angepasst.
     * Ist dies unerwünscht sollte stattdessen <code>.get(int)</code> verwendet werden.
     * @param playerId  Die Id des Spielers, der erzeugt/zurückgegeben werden soll.
     * @param playerName Der Name des Spielers. Wird ggfs. am bestehenden Objekt angepasst.
     * @return Der angeforderte Spieler.
     */
    protected static Player get(int playerId, String playerName){
        if(players[playerId] == null){
            players[playerId] = new Player(playerId, playerName);
        }
        players[playerId].name = playerName;
        return players[playerId];
    }

    /**
     * Gibt den Spieler zurück, der die Bedingung <code>.isNone</code> erfüllt.
     * Diesem Spieler wird kein realer Spieler zugeordnet, dieser Spieler erhält keinen Namen.
     * @return Der Spieler, der <code>.isNone</code> erfüllt.
     */
    protected static Player none(){
        return Player.get(0);
    }

    public boolean equals(Player p){
        return p.id == this.id;
    }

    /**
     * Prüft, ob es sich bei diesem Spieler um den None Spieler handelt (Id = 0).
     * @return <code>true</code>, wenn dieser Spieler der None Spieler ist, <code>false</code> sonst.
     */
    protected boolean isNone(){
        return this.id == 0;
    }

    @Override
    public String toString(){
        return this.isNone() ? "None" : "Player " + this.id + ": "+ this.name;
    }

    /**
     * Gibt den Namen des Spielers zurück.
     * @return String Der Name des Spielers.
     */
    protected String getName(){
        return name;
    }

    /**
     * Gibt die id des Spielers zurück.
     * @return int Die Id des Spielers.
     */
    protected int getId(){
        return id;
    }

    /**
     * Erzeugt eine Spielerfarbe basierend auf der Id.
     * @return Die dem Spieler per Id zugeordnete Farbe.
     */
    protected Color getColor(){
        int r = 130;
        int g = 130;
        int b = 130;
        switch(id){
            case 1:
                b = 200;
                break;
            case 2:
                r = 200;
                g = 200;
                break;
            case 3:
                r = 200;
                break;
            case 4:
                b = 200;
                g = 200;
                break;
            case 5:
                g = 200;
                break;
            case 6:
                r = 200;
                b = 200;
                break;
        }
        return new Color(r, g, b);
    }
}
