package de.phynnnix.tictactoe;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.plaf.FontUIResource;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;


/**
 * Eine Ergänzung des JPanel um die Felder des Spielbretts in einem Grid anzuzeigen. 
 */
public class BoardGrid extends JPanel implements Dispatcher{
    private int boardSize;

    private Listener listener;
    private FlatButton[] fields;

    private boolean gameOver;


    /**
     * Erzeugt das Panel anhand des übergebenen Boards. Dies ist der einzige zulässige Konstruktor.
     * @param board Das Board auf dessen Dimensionen das Panel ausgelegt wird.
     */
    public BoardGrid(Board board){
        gameOver = false;
        int gridSize = 560;
        boardSize = board.getSize();
        int fieldCount = boardSize * boardSize;
        fields = new FlatButton[fieldCount];
        GridLayout grid = new GridLayout(boardSize, boardSize);
        this.setLayout(grid);
        this.setPreferredSize(new Dimension(gridSize, gridSize));
        /*
         * Die einzelnen Felder als FlatButtons. Diese haben einen ActionListener, der die Klickaktion an den <code>listener</code> weiterleitet.
         * Der Button wird dann deaktiviert und die Spieler Id als Text eingefügt.
         * Der <code>listener</code> wird informiert, dass der Zug vorbei ist und der nächste erfolgen kann.
         */
        for(int i = 0; i < fieldCount; i++){
            FlatButton button = new FlatButton("-");
            button.setRoundness(4);
            fields[i] = button;
            button.setMainAutomateVariations(new Color(150,175,150));
            int[] position = board.getCoords(i);
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ev){
                    dispatchMessage("Played At", position);
                    Player hasPlayed = board.getPlayerAt(position[0], position[1]);
                    button.setMainAndVariations(hasPlayed.getColor());
                    button.setText(""+hasPlayed.getId());
                    button.setEnabled(false);
                    dispatchMessage("Take Turn", null);
                }
            });
            this.add(button);
            button.setFont(new FontUIResource("Arial", 1, 24));

        }
    }

    /**
     * Registriert, dass das Spiel vorbei ist und macht dazu alle noch nicht belegten Felder inaktiv. Außerdem hebt es die Siegfelder hervor.
     * @param board Das abgeschlossene Brett, um die Siegfelder auszulesen.
     */
    protected void gameOverState(Board board){
        if(gameOver) return;
        gameOver = true;
        for(FlatButton button: fields){
            if(button.getText().equals("-")){
                button.setMainAndVariations(new Color(100,100,100));
                button.setText("");
                button.setEnabled(false);
            }
        }
        for(int x = 0; x < board.getSize(); x++){
            for(int y = 0; y < board.getSize(); y++){
                if(board.isWinningField(x, y)){
                    fields[board.getIndex(x, y)].setBorder(Color.YELLOW, 2);
                }
            }
        }
    }

    public void setListener(Listener l){
        listener = l;
    }

    public void dispatchMessage(String info, Object data){
        if(listener != null) listener.receive(new Message(this, info, data));
    }
}

