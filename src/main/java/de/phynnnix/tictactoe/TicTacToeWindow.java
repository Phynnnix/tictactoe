package de.phynnnix.tictactoe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import java.awt.Dimension;

/**
 * Diese Erweiterung des JFrames ist das Hauptfenster des Spiels.
 * Es verwaltet drei Bereiche: Header, Hauptbereich und Footer.
 * Diese werden je nach Phase des Spiels mit unterschiedlichen Elementen belegt.
 */
public class TicTacToeWindow extends JFrame implements Dispatcher{
    private JPanel header;
    private JPanel main;
    private JPanel footer;

    private LabeledSlider winLenSlider;
    private LabeledSlider boardSizeSlider;
    private BoardGrid boardGrid;

    private Listener listener;

    /**
     * Einziger Konstruktor, initialisiert ein leeres Fenster mit den Bereichen Header, Hauptbereich und Footer.
     * Legt auch die Größe des Fensters fest, sowie das Layout und den Titel.
     */
    protected TicTacToeWindow(){
        super("Tic Tac Toe");
        listener = null;
        this.setPreferredSize(new Dimension(600, 800));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        header = new JPanel();
        main = new JPanel();
        footer = new JPanel();
        header.setBackground(new ColorUIResource(200, 200, 200));
        main.setBackground(new ColorUIResource(255, 255, 255));
        footer.setBackground(new ColorUIResource(200, 200, 200));
        main.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        //main.setAlignmentX(CENTER_ALIGNMENT);
        this.add(header, BorderLayout.PAGE_START);
        this.add(main, BorderLayout.CENTER);
        this.add(footer, BorderLayout.PAGE_END);

        this.pack();
        this.setVisible(true);
    }

    /**
     * Definiert die Standardanzeige der Headeranzeigen für reguläre Züge.
     * @param round Die aktuelle Runde.
     * @param turn Der aktuelle Zug.
     * @param p Der Spieler, der am Zug ist.
     */
    protected void setHeaderForPlayer(int round, int turn, Player p){
        setHeaderText("Round "+round+" Turn "+turn+":It's your turn, "+p.getName()+" (Player "+p.getId()+")");
    }

    /**
     * Legt den Headertext fest.
     * Zeichnet das Fenster neu.
     * @param text Headertext.
     */
    protected void setHeaderText(String text){
        header.removeAll();
        header.add(new JLabel(text));

        redraw();
    }

    /**
     * Legt den Footertext fest.
     * Zeichnet das Fenster neu.
     * @param text Footertext.
     */
    protected void setFooterText(String text){
        footer.removeAll();
        footer.setLayout(new BorderLayout());
        footer.add(new JLabel(text));

        redraw();
    }

    /**
     * Legt den Footertext in drei Spalten fest.
     * So kann links, zentriert und rechts bündig Text angebracht werden.
     * Zeichnet das Fenster neu.
     * @param textL Linksbündiger Text
     * @param textC Zentrierter text
     * @param textR Rechtsbündiger Text
     */
    protected void setFooterText(String textL, String textC, String textR){
        footer.removeAll();
        footer.setLayout(new GridLayout(0,3));
        footer.add(new JLabel(textL, JLabel.LEFT));
        footer.add(new JLabel(textC, JLabel.CENTER));
        footer.add(new JLabel(textR, JLabel.RIGHT));

        redraw();
    }

    /**
     * Initialisieren des Spiels.
     * Setzt den Standard Footer Text.
     * Zeichnet das Fenster neu.
     */
    protected void run(){
        setFooterText("Phynnnix", "Multiplayer Tic Tac Toe Game", "2022");

        redraw();
    }

    /**
     * Legt die Elemente für die Spieleinstellungen aufs Fenster.
     * Zeichnet das Fenster neu.
     */
    protected void gameSettings(){
        main.removeAll();
        NewPlayerGrid newPlayers = new NewPlayerGrid();
        
        winLenSlider = new LabeledSlider("Linienlänge zum Sieg: ", 3, 3);

        boardSizeSlider = new LabeledSlider("Größe des Spielfelds: ", 3, 10);
        boardSizeSlider.setListener(new Listener() {
            public void receive(Message m){
                if(m.isAbout("Changed Value")){
                    int value = boardSizeSlider.getValue();
                    winLenSlider.setMax(value);
                }
                redraw();
            } 
        });

        newPlayers.setListener(new Listener() {
            public void receive(Message m){
                if(m.isFrom(newPlayers)){
                    if(m.isAbout("New Player")){
                        String name = m.getData(String.class);
                        int pCount = newPlayers.registeredPlayers();
                        if(pCount > 2){
                            boardSizeSlider.setMinMax(1+pCount, 8 + 2 * pCount);
                        }
                        if(pCount == 4 || pCount == 5){
                            winLenSlider.setMin(4);
                        }else if(pCount == 6){
                            winLenSlider.setMin(5);
                        }
                        dispatchMessage("New Player", name);
                    }else if(m.isAbout("Enough Player")){
                        FlatButton play = new FlatButton("Play! >>");
                        play.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent ev){
                                dispatchMessage("Play", null);
                            }
                        });
                        play.setPreferredSize(new Dimension(100, 25));
                        main.add(play);
                    }
                    redraw();
                }
            }
        });
        main.add(newPlayers);
        main.add(boardSizeSlider);
        main.add(winLenSlider);

        redraw();
    }

    /**
     * Zeichnet das Spielbrett aufs Fenster.
     * Zeichnet das Fenster neu.
     * @param board Das Brettobjekt, anhand dessen das Brett UI erzeugt wird.
     * @param boardListener Ein Zuhörer, der auf die Nachrichten des Brett UI reagiert.
     */
    protected void start(Board board, Listener boardListener){
        main.removeAll();
        boardGrid = new BoardGrid(board);
        boardGrid.setListener(boardListener);
        main.add(boardGrid);

        redraw();
    }

    /**
     * Zeigt den Sieger bzw. Unentschieden Bildschirm
     * @param winner Der Sieger. Erfüllt der Spieler <code>.isNone</code> endete das Spiel unentschieden.
     * @param board Das Endgültige Brett, anhanddessen wird die Sieglinie im Brett UI hervorgehoben.
     */
    protected void showWinner(Player winner, Board board){
        //main.removeAll();
        boardGrid.setPreferredSize(new Dimension(480,480));
        boardGrid.gameOverState(board);
        String colorHex = Integer.toHexString(winner.getColor().getRGB() & 0xffffff);
        String winnerText = "<html><div style='text-align:center;width:max-content;margin-left:auto;margin-right:auto'>"
                            +"<h1>Sieg!</h1>"
                            +"<br/>"
                            +"<p>Gewonnen hat dieses Spiel:</p>"
                            +"<p color='"+colorHex+"'><b>"+winner.getName()+"</b></p>"
                            +"<p color='"+colorHex+"'>(Player "+winner.getId()+")</p>"
                            +"</div></html>";
        if(winner.isNone()){
            colorHex = "#222222";
            winnerText = "<html><div style='text-align:center;width:max-content;margin-left:auto;margin-right:auto'>"
                        +"<h1>Spiel vorbei!</h1>"
                        +"<br/>"
                        +"<p>Gewonnen hat dieses Spiel:</p>"
                        +"<p color='"+colorHex+"'><b>niemand</b></p>"
                        +"<p color='"+colorHex+"'>Denn das Spiel endete unentschieden.</p>"
                        +"</div></html>";
        }
        JLabel label = new JLabel(winnerText);
        label.setFont(new FontUIResource("Verdana", 0, 14));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(560, 175));
        main.add(label);

        FlatButton replay = new FlatButton("Neues Spiel");
        replay.setPreferredSize(new Dimension(100, 24));
        replay.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispatchMessage("Replay", null);
            }
        });
        main.add(replay);

        redraw();
    }

    public void setListener(Listener l){
        listener = l;
    }

    public void dispatchMessage(String info, Object data){
        if(listener != null){
            listener.receive(new Message(this, info, data));
        }
    }

    /**
     * Weist das Fenster nach grafischen Anpassungen an, das Fesnter neu zu packen und zu zeichnen.
     * So werden grafische Fragmente ggfs. gelöschter Komponenten verhindert.
     */
    protected void redraw(){
        this.pack();
        this.repaint();
    }

    /**
     * Macht den Wert des Sliders für die Brettgröße nach außen verfügbar.
     * @return Eingestellter Wert für die Brettgröße.
     */
    protected int getChosenSize(){
        return boardSizeSlider.getValue();
    }

    /**
     * Macht den Wert des Sliders für die Sieglinienlänge nach außen verfügbar.
     * @return Eingestellter Wert für die Sieglinienlänge.
     */
    protected int getChosenWinLen(){
        return winLenSlider.getValue();
    }
}
