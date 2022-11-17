package de.phynnnix.tictactoe;

import java.util.Arrays;


/**
 * Repräsentiert das Spielbrett des Tic Tac Toe Spiels.
 * Dazu wird jedem Feld ein <code>Player</code> zugeordnet.
 * Das Brett kann mit einer bestimmten Größe initialisiert werden.
 * Innerhalb des Pakets kann jedem Feld ein <code>Player</code> zugeordnet werden oder der aktuelle <code>Player</code> eines
 *  Feldes abgerufen werden.
 * Das Brett kann auf Sieglinien geprüft werden.
 * 
 * @author Fynn Nix
 * @version 0.1.0
 */
public class Board {
    private int size;
    private int height;
    private int length;
    private int winningLength;
    private Player[] fields;
    private boolean[] winningFields;
    
    /**
     * Erzeugt das <code>Board</code>, dies ist der einzige zulässige Konstruktor und legt die Größe des Feldes fest.
     * @param s Bestimmt die Größe des <code>Board</code>. Dabei ist s eine Seitenlänge, das <code>Board</code> hat dann
     *  die Dimensionen <code>s * s</code>.
     * @param len Legt die Länge der Linie fest, die ein Spieler besetzen muss um zu gewinnen.
     */
    protected Board(int s, int len){
        size = s;
        height = this.size;
        length = this.size;
        fields = new Player[this.height * this.length];
        winningFields = new boolean[this.height * this.length];
        winningLength = len;
        for(int i = 0; i < this.fields.length; i++){
            this.fields[i] = Player.none();
        }
        for(int i = 0; i < this.winningFields.length; i++){
            this.winningFields[i] = false;
        }
    }


    /**
     * Gibt die Größe (Seitenlänge) des Bretts zurück.
     * @return int Die Seitenlänge des Bretts. Hat das Brett die Dimension s * s, so wird s zurückgegeben.
     */
    protected int getSize(){
        return size;
    }


    /** 
     * Prüft, ob das Feld am übergebenen Index leer ist.
     * @param index Der lineare (1D) Index des Bretts.
     * @return boolean <code>true</code> wenn das Feld am <code>index</code> leer ist, <code>false</code> sonst.
     */
    protected boolean isEmpty(int index){
        return this.fields[index].isNone();
    }

        
    /** 
     * Liefert den Spieler, der an der angegebenen Position gesetzt hat.
     * Hat niemand an der gegebenen Position (2D) gesetzt, wird der Spieler zurückgegeben,
     *  für den <code>.isNone()</code> mit <code>true</code> evaluiert.
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     * @return Player Der Spieler, der an Position (x,y) gesetzt hat oder die Standardbelegung <code>Player.none()</code>
     */
    protected Player getPlayerAt(int x, int y){
        assert this.isValid(x, y);
        return this.fields[this.getIndex(x, y)];
    }

    
    /** 
     * Versucht einen Spieler an der gegebenen Position (2D) zu platzieren.
     * Dies ist nicht zulässig, wenn das Feld an (x,y) nicht leer ist.
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     * @param p Der zu platzierende Spieler.
     * @return boolean <code>true</code>, wenn das Platzieren zulässig ist, <code>false</code> sonst.
     */
    protected boolean setPlayerAt(int x, int y, Player p){
        int index = this.getIndex(x, y);
        if(!this.isEmpty(index)){
            return false;
        }
        this.fields[index] = p;
        return true;
    }


    /**
     * Markiert das Feld an der Position (x,y) als Siegfeld. Ein Siegfeld ist jedes Feld, dass Teil einer Linie ist, die den Sieg für einen Spieler herbeigeführt hat.
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     */
    private void setWinningField(int x, int y){
        winningFields[getIndex(x, y)] = true;
    }


    /**
     * Prüft ob das Feld an der Position (x,y) als Siegfeld markiert wurde. Ein Siegfeld ist jedes Feld, dass Teil einer Linie ist, die den Sieg für einen Spieler herbeigeführt hat.
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     * @return boolean <code>true</code> wenn das Feld an der Position (x,y) als Siegfeld markiert ist, <code>false</code> sonst.
     */
    protected boolean isWinningField(int x, int y){
        return winningFields[getIndex(x, y)];
    }


    /**
     * Prüft für das gesamte Brett, ob bereits ein Sieger aussteht und gibt diesen zurück. Gibt es einen Gewinner, so werden auch die Siegfelder markiert.
     * @return Player Der Sieger, wenn einer feststeht, <code>Playar.none()</code> sonst
     */
    protected Player checkWinner(){
        Player winner = Player.none();
        for(int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                Player candidate;
                if(x <= size - winningLength){ // Wenn nach rechts genug Platz ist: teste Horizontale.
                    candidate = winningLine(x, y, 1, 0);
                    if(!candidate.isNone()){
                        winner = candidate;
                    }
                }
                if(y <= size - winningLength){ // Wenn nach unten genug Platz ist: teste Vertikale.
                    candidate = winningLine(x, y, 0, 1);
                    if(!candidate.isNone()){
                        winner = candidate;
                    }

                    if(x <= size - winningLength){ // Wenn nach unten und rechts genug Platz ist: teste Hauptdiagonale.
                        candidate = winningLine(x, y, 1, 1);
                        if(!candidate.isNone()){
                            winner = candidate;
                        }
                    }
                    if(x >= winningLength-1){ // Wenn nach unten und links genug Platz ist: Teste Nebendiagonale.
                        candidate = winningLine(x, y, -1, 1);
                        if(!candidate.isNone()){
                            winner = candidate;
                        }
                    }
                }
            }
        }

        return winner;
    }

    /**
     * Prüft von der Position (x,y) aus eine Linie (Richtungsvektor (dx,dy) ) der Länge <code>winningLength</code> darauf, 
     * ob sie vollständig von einem Spieler belegt ist (Siegbedingung).
     * Ist dies der Fall, wird dieser Spieler zurückgegeben und alle Felder dieser Linie als Siegfelder markiert.
     * Andernfalls wird <code>Player.none()</code> zurückgegeben.
     * @param x x-Position des Startfelds.
     * @param y y-Position des Startfelds.
     * @param dx x-Komponente der Richtung.
     * @param dy y-Komponente der Richtung.
     * @return Den Spieler, der auf dieser Linie gewonnen hat, andernfalls <code>Player.none()</code>
     */
    private Player winningLine(int x, int y, int dx, int dy){
        Player candidate = getPlayerAt(x, y);
        for(int d = 1; d < winningLength; d++){
            if(candidate.isNone()) break;
            if(!candidate.equals(getPlayerAt(x + dx * d, y + dy * d))) candidate = Player.none();
        }
        if(!candidate.isNone()){
            for(int d = 0; d < winningLength; d++){
                setWinningField(x + dx * d, y + dy * d);
            }
        }
        return candidate;
    }

    /**
     * Gibt eine Reräsentation des Bretts auf die Konsole aus. Für Debugging.
     */
    protected void log(){
        System.out.println(Arrays.deepToString(this.fields));
    }

    
    /** 
     * Prüft, ob eine Position (2D) valide ist, also auf dem Brett existiert. 
     * @hidden
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     * @return boolean <code>true</code> wenn sich die (x,y) Position innehalb der Grenzen des Bretts befindet, <code>false</code> sonst.
     */
    private boolean isValid(int x, int y){
        return x >= 0 && x < this.length && y >= 0 && y < this.height;
    }

    
    /** 
     * Ermittelt aus einer Position (2D) des Bretts den linearen Index (1D) im <code>fields</code> Array.
     * @hidden
     * @param x Die x-Koordinate der Position. Die x-Achse verläuft horizontal.
     * @param y Die y-Koordinate der Position. Die y-Achse verläuft vertikal.
     * @return int Der lineare Index (1D) im Array, der das Feld an der Stelle (x,y) auf dem Brett repräsentiert.
     */
    protected int getIndex(int x, int y){
        return x + y * this.length;
    }

    
    /** 
     * Ermittelt aus dem linearen Index (1D) des <code>fields</code> Arrays die repräsentierte Position (2D) auf dem Spielbrett.
     * @hidden
     * @param ind Der lineare Index (1D) im Array, der das Feld an der Stelle (x,y) auf dem Brett repräsentiert.
     * @return int[] Die x- und y-Koordinaten, die von <code>ind</code> repräsentiert werden. Das Array hat die Form <code>{x,y}</code>.
     */
    protected int[] getCoords(int ind){
        int[] coords = {ind % this.size, ind / this.size};
        return coords;
    }

}
