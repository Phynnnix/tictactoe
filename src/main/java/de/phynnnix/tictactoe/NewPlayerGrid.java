package de.phynnnix.tictactoe;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Dimension;

/**
 * Eine Erweiterung des JPanel um die Darstellung mehrer, dynamisch hinzugefügter, Zeilen für neue Spieler festzulegen.
 */
public class NewPlayerGrid extends JPanel implements Dispatcher{
    private int lineCount;
    private int lineHeight;
    private int regPlayers;

    private Listener listener;

    /**
     * Standardkonstruktor, legt die Rahmenbedingungen des <code>NewPlayerGrid</code>s fest. Einziger Konstruktor.
     */
    public NewPlayerGrid(){
        GridLayout grid = new GridLayout(0,1);
        this.setLayout(grid);
        lineCount = 0;
        lineHeight = 24;
        regPlayers = 0;
        appendNewLine();
    }

    /**
     * Fügt eine neue Zeile zum Hinzufügen eines weiteren Spielers an die bereits bestehenden Zeilen an 
     * und passt die Dimensionen des JPanels entsprechend an.
     * Zählt außerdem mit, wie viele Spieler bereits Registriert sind.
     * Sind breits 6 Spieler registriert wird keine weitere Zeile hinzugefügt.
     */
    protected void appendNewLine(){
        System.out.println("Append Line");
        if(lineCount >= 6){
            return;
        }
        lineCount += 1;
        NewPlayerPanel npp = new NewPlayerPanel(lineCount);
        npp.setListener(new Listener(){
            public void receive(Message m){
                if(m.isFrom(npp)){
                    String name = m.getData(String.class);
                    System.out.println(name);
                    regPlayers ++;
                    appendNewLine();
                    dispatchMessage("New Player", name);
                    if(regPlayers == 2){
                        dispatchMessage("Enough Player", null);
                    }
                }
            }
        });
        this.add(npp);
        this.adjustHeight();
    }

    /**
     * Setzt die neue Höhe des <code>JPanels</code>.
     */
    private void adjustHeight(){
        this.setPreferredSize(new Dimension(600, lineCount * lineHeight));
    }

    /**
     * Gibt die Anzahl der bisher registrierten Spieler zurück.
     * @return int Anzahl der bisher rigistrierten Spieler.
     */
    protected int registeredPlayers(){
        return regPlayers;
    }

    public void setListener(Listener l){
        listener = l;
    }

    public void dispatchMessage(String info, Object data){
        listener.receive(new Message(this, info, data));
    }
}
