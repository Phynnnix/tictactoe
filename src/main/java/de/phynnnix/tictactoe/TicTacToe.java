package de.phynnnix.tictactoe;

import java.util.ArrayList;

/**
 * Die Einstigsklasse des Spiels.
 * Verwaltet den Spielstatus und die Spielphasen.
 * Es verwaltet auch Brett und die Spieler indem sie diese Aufgaben an die entsprechenden Komponenten delegiert und deren Nachrichten empfängt.
 */
public class TicTacToe {
    /**
     * Die vier Phasen des Spiels.
     */
    private enum State{
        UNINITIALIZED,
        SETTINGS,
        INGAME,
        WINNER
    }
    Board board;
    ArrayList<Player> joinedPlayers;
    TicTacToeWindow window;
    State state;
    int onTurn;
    int playedTurns;
    int playedRounds;

    /**
     * Einstiegsmethode. Erzeugt ein neues TicTacToe Objekt und startet das Spiel.
     * @param args Für Kompatibilität. Wird nicht verwendet.
     */
    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe();
        ttt.run();
        
        return;
    }

    /**
     * Konstruktor für ein leeres Spiel. Erzeugt ein Fenster.
     */
    public TicTacToe(){
        joinedPlayers = new ArrayList<Player>(2);
        window = new TicTacToeWindow();
        state = State.UNINITIALIZED;
    }

    /**
     * Startet das Spiel. Erzeugt erste Komponenten im Fenster. 
     * Kann nur ein mal aufgerufen werden, da danach die Phase nicht wieder zu <code>UNINITIALIZED</code> wechselt.
     */
    public void run(){
        if(state == State.UNINITIALIZED){
            window.run();
            gameSettings();
        }
    }

    /**
     * Zeigt die Spieleinstellungen im Fenster und wartet auf deren Bestätigung.
     */
    public void gameSettings(){
        if(state == State.UNINITIALIZED){
            state = State.SETTINGS;
            window.gameSettings();
            window.setListener(new Listener() {
                public void receive(Message m){
                    if(m.isAbout("New Player")){
                        joinedPlayers.add(Player.get(joinedPlayers.size()+1, m.getData(String.class)));
                    }else if(m.isAbout("Play")){
                        start(window.getChosenSize(), window.getChosenWinLen());
                    }
                }
            });
            window.setHeaderText("Spieler registrieren, Brettgröße und zum Sieg benötigte Linienlänge wählen.");
        }
    }

    /**
     * Startet das eigentliche Spiel, mit dem im Einstellungsfenster eingestellten Parametern, sowie den dort registrierten Spielern.
     * Erzeugt das Spielbrett im Fenster.
     * @param size Seitenlänge des Spielbretts.
     * @param winLen Benötigte Linienlänge zum Sieg.
     */
    public void start(int size, int winLen){
        if(state == State.SETTINGS){
            state = State.INGAME;
            board = new Board(size, winLen);
            onTurn = 1;
            playedRounds = 0;
            playedTurns = 0;
            window.setHeaderForPlayer(1,1,Player.get(onTurn));
            window.start(board, new Listener(){
                public void receive(Message m){
                    if(m.isAbout("Played At")){
                        int[] pos = m.getData(int[].class);
                        board.setPlayerAt(pos[0], pos[1], Player.get(onTurn));
                    }else if(m.isAbout("Take Turn")){
                        takeTurn();
                    }
                }
            });
            window.setListener(null);
        }
    }

    /**
     * Beendet den aktuellen Zug indem das Brett auf einen Sieg überprüft wird.
     * Leitet zum Sieg oder Unentschieden Bildschirm weiter, wenn das Spiel vorbei ist.
     * Startet andernfalls den nächsten Zug.
     */
    private void takeTurn(){
        Player winner = board.checkWinner();
        if(!winner.isNone()){
            win(winner);
            return;
        }
        onTurn = (onTurn % joinedPlayers.size()) + 1;
        playedTurns++;
        if(playedTurns == board.getSize()*board.getSize()){
            draw();
            return;
        }
        if(onTurn == 1){
            playedRounds++;
        }
        window.setHeaderForPlayer(playedRounds+1, playedTurns+1, Player.get(onTurn));
    }

    /**
     * Zeigt an, dass das Spiel unentschieden geendet hat.
     */
    public void draw(){
        if(state == State.INGAME){
            state = State.WINNER;
            window.setHeaderText("DRAW          DRAW          DRAW          DRAW");
            window.showWinner(Player.none(), board);
            window.setListener(new Listener() {
                public void receive(Message m){
                    if(m.isAbout("Replay")){
                        joinedPlayers = new ArrayList<Player>(2);
                        board = null;
                        state = State.UNINITIALIZED;
                        gameSettings();
                    }
                } 
            });
        }
    }

    /**
     * Zeigt an, dass das Spiel beendet wurde und welcher Spieler gewonnen hat.
     * Hebt zudem alle Sieglinien auf dem Brett hervor.
     * @param winner Der zum Sieger gekürte Spieler.
     */
    public void win(Player winner){
        if(state == State.INGAME){
            state = State.WINNER;
            window.setHeaderText("WIN   WIN   WIN   WIN   WIN   WIN   WIN");
            window.showWinner(winner, board);
            window.setListener(new Listener() {
                public void receive(Message m){
                    if(m.isAbout("Replay")){
                        joinedPlayers = new ArrayList<Player>(2);
                        board = null;
                        state = State.UNINITIALIZED;
                        gameSettings();
                    }
                } 
            });
        }
    }
}