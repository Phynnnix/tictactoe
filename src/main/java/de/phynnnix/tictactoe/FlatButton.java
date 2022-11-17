package de.phynnnix.tictactoe;

import javax.swing.JButton;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Eine Erweiterung des JButtons, für ein moderneres Design.
 * Erzeugt eine flache Button Komponente, die Farben für Standard, Mouseover, Mousedown sowie den Rahmen festlegen kann. 
 * Außerdem kann die Rahmendicke und -rundung bestimmt werden. Ist der Button deaktiviert wird stets die Standardfarbe angezeigt.
 */
public class FlatButton extends JButton{
    private Color main;
    private Color over;
    private Color down;
    private Color border;

    private int borderWidth;
    private int roundness;

    private MouseInputAdapter enabledActions;
    private MouseInputAdapter disabledActions;

    private ActionListener onClick;

    private boolean isOver;

    /**
     * Variante 1) Der vollständigste Konstruktor. Ermöglicht das händische Einstellen aller Parameter.
     * @param text Der auf dem Button angezeigte Text.
     * @param mainC Die Standardfarbe
     * @param overC Die Farbe für Mouseover.
     * @param downC Die Farbe für Mousedown.
     * @param borderC Die Rahmenfarbe.
     * @param borderWidthI Die Rahmenstärke.
     * @param roundnessI Die Rahmenrundung.
     */
    protected FlatButton(String text, Color mainC, Color overC, Color downC, Color borderC, int borderWidthI, int roundnessI){
        super(text);

        main = mainC;
        over = overC;
        down = downC;
        border = borderC;

        borderWidth = borderWidthI;
        roundness = roundnessI;

        isOver = false;

        setContentAreaFilled(false);
        setBorder(null);
        setBackground(main);

        /*
         * Verarbeitet die Inputevents um auf Mouseover und Mousedown zu reagieren.
         */
        enabledActions = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isOver = true;
                setBackground(over);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isOver = false;
                setBackground(main);
                super.mouseExited(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(down);
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(isOver){
                    setBackground(over);
                }else{
                    setBackground(main);
                }
                super.mouseReleased(e);
            }
        };
        
         /*
         * Verarbeitet die Inputevents um auf Mouseover und Mousedown zu reagieren, setzt hierbei jedoch stets die Standardfarbe. 
         * Dies ist das Eventhandling für deaktivierte Buttons.
         */
        disabledActions = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isOver = true;
                setBackground(main);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isOver = false;
                setBackground(main);
                super.mouseExited(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(main);
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(isOver){
                    setBackground(main);
                }else{
                    setBackground(main);
                }
                super.mouseReleased(e);
            }
        };

        addMouseListener(enabledActions);
    }

    /**
     * Variante 5) Wie Variante 1, allerdings mit den Defaults
     * Rahmenstärke = 1
     * Rahmenrundung = 0
     * Rahmenfarbe = Schwarz
     * Mouseoverfarbe = Hellgrau
     * Mousedownfarbe = Grau
     * Standardfarbe = Weiß
     * @param text Der auf dem Button angezeigte Text.
     */
    protected FlatButton(String text){
        this(text, Color.WHITE);
    }

    /**
     * Variante 4) Wie Variante 1, allerdings mit den Defaults
     * Rahmenstärke = 1
     * Rahmenrundung = 0
     * Rahmenfarbe = Schwarz
     * Mouseoverfarbe = Hellgrau
     * Mousedownfarbe = Grau
     * @param text Der auf dem Button angezeigte Text.
     * @param mainC Die Standardfarbe
     */
    protected FlatButton(String text, Color mainC){
        this(text, mainC, Color.LIGHT_GRAY, Color.GRAY);
    }

    /**
     * Variante 3) Wie Variante 1, allerdings mit den Defaults
     * Rahmenstärke = 1
     * Rahmenrundung = 0
     * Rahmenfarbe = Schwarz
     * @param text Der auf dem Button angezeigte Text.
     * @param mainC Die Standardfarbe
     * @param overC Die Farbe für Mouseover.
     * @param downC Die Farbe für Mousedown.
     */
    protected FlatButton(String text, Color mainC, Color overC, Color downC){
        this(text, mainC, overC, downC, Color.BLACK);
    }

    /**
     * Variante 2) Wie Variante 1, allerdings mit den Defaults
     * Rahmenstärke = 1
     * Rahmenrundung = 0
     * @param text Der auf dem Button angezeigte Text.
     * @param mainC Die Standardfarbe
     * @param overC Die Farbe für Mouseover.
     * @param downC Die Farbe für Mousedown.
     * @param borderC Die Rahmenfarbe.
     */
    protected FlatButton(String text, Color mainC, Color overC, Color downC, Color borderC){
        this(text, mainC, overC, downC, borderC, 1, 0);
    }

    /**
     * Legt die Standardfarbe fest.
     * @param c Die Standardfarbe.
     */
    protected void setMain(Color c){
        main = c;
        if(!isOver){
            setBackground(main);
        }
    }

    /**
     * Legt die Hintergrundfarben automatisch anhand der Standardfarbe fest. 
     * Dabei sind die Farbe für Mouseover und Mousedown jeweils dunklere Versionen der Standardfarbe.
     * @param mainC Die Standardfarbe, von der die weiteren Farben abgeleitet werden.
     */
    protected void setMainAutomateVariations(Color mainC){
        setMain(mainC);
        setOver(mainC.darker());
        setDown(mainC.darker().darker());
    }
    
    /**
     * Legt die Hintergrundfarben alle auf die Standardfarbe fest.
     * Achtung: Ein so festgelegter Button erscheint nicht mehr interaktiv, ist aber ggfs. noch klickbar!
     * Vielleicht ist es vorzuziehen, den Button stattdessen zu deaktivieren.
     * @param mainC Die Standardfarbe, auf die auch die Variationen festgelegt werden.
     */
    protected void setMainAndVariations(Color mainC){
        setMain(mainC);
        setOver(mainC);
        setDown(mainC);
    }


    /**
     * Legt die Farbe für Mouseover fest.
     * @param c Die Farbe für Mouseover.
     */
    protected void setOver(Color c){
        over = c;
        if(isOver){
            setBackground(over);
        }
    }


    /**
     * Legt die Farbe für Mousedown fest.
     * @param c Die Farbe für Mousedown.
     */
    protected void setDown(Color c){
        down = c;
    }

    /**
     * Shorthand zur schnellen Festlegung des Rahmens. 
     * Vereint <code>setBorderColor</code> und <code>setBorderWidth</code>.
     * @param c Die Farbe des Rahmens.
     * @param w Die Stärke des Rahmens.
     */
    protected void setBorder(Color c, int w){
        setBorderColor(c);
        setBorderWidth(w);
    }


    /**
     * Shorthand zur schnellen Festlegung des Rahmens. 
     * Vereint <code>setBorderColor</code>, <code>setBorderWidth</code> sowie <code>setRoundness</code>.
     * @param c Die Farbe des Rahmens.
     * @param w Die Stärke des Rahmens.
     * @param r Die Rundung des Rahmens.
     */
    protected void setBorder(Color c, int w, int r){
        setBorderColor(c);
        setBorderWidth(w);
        setRoundness(r);
    }

    /**
     * Legt die Farbe des Rahmens fest.
     * @param c Die Rahmenfarbe.
     */
    protected void setBorderColor(Color c){
        border = c;
    }

    /**
     * Legt die Stärke des Rahmens fest.
     * @param w Die Rahmenstärke.
     */
    protected void setBorderWidth(int w){
        borderWidth = w;
    }

    /**
     * Legt die Rundung des Rahmens fest.
     * @param r Die Rahmenrundung.
     */
    protected void setRoundness(int r){
        roundness = r;
    }

    @Override
    public void setEnabled(boolean e) {
        if(e){
            removeMouseListener(enabledActions);
            removeMouseListener(disabledActions);
            addMouseListener(enabledActions);
        }else{
            removeMouseListener(disabledActions);
            removeMouseListener(enabledActions);
            addMouseListener(disabledActions);
        }
        setActionListenerActive(e);
        super.setEnabled(e);
    }

    @Override
    public void addActionListener(ActionListener l) {
        if(onClick != null){
            return;
        }
        onClick = l;
        super.addActionListener(l);
    }

    /**
     * Legt fest ob der gesetzte ActionListener aktuell aktiv sein soll.
     * @param active Wahrheitswert, ob der ActionListener Aktiv sein soll.
     */
    public void setActionListenerActive(boolean active) {
        if(onClick == null){
            return;
        }
        if(active){
            super.addActionListener(onClick);
        }else{
            super.removeActionListener(onClick);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(border);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), roundness, roundness);

        g2.setColor(getBackground());
        g2.fillRoundRect(borderWidth, borderWidth, getWidth() - 2 * borderWidth, getHeight() - 2 * borderWidth, roundness, roundness);

        super.paintComponent(g);
    }
}
