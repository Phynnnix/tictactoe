package de.phynnnix.tictactoe;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Ergänzung des JPanel zur Darstellung einer einzelnen Zeile zum Hinzufügen eines neuen Spielers.
 * Fasst ein Label, ein Textfeld und einen Button zum Hinzufügen zusammen.
 */
public class NewPlayerPanel extends JPanel implements Dispatcher{
    Listener listener;
    int playerId;
    JLabel playerLabel;
    JTextField nameField;
    FlatButton accept;

    /**
     * Erzeugt die neue Zeile zum Hinzufügen eines Spielers. Dabei wird im Label die zu benennende Id des Spielers angezeigt.
     * Der Button ermöglicht das Beitreten zum Spiel.
     * Der Spieler kann nur Beitreten, wenn das Textfeld nicht leer ist.
     * @param pid Die Spieler Id, mit der der neue Spieler registriert wird.
     */
    protected NewPlayerPanel(int pid){
        super();
        this.setLayout(new GridLayout());
        listener = null;
        playerId = pid;
        playerLabel = new JLabel("Player "+playerId+":", JLabel.RIGHT);
        playerLabel.setPreferredSize(new Dimension(50, 10));
        nameField = new JTextField(24);
        accept = new FlatButton("Beitreten");
        accept.setMainAutomateVariations(new Color(233,250,233));
        /*
         * Behandelt das Klick Event. 
         * Der Spielername darf nicht leer sein, sonst wird der Textfeld Rahmen rot und die Aktion nicht ausgeführt.
         * Ist der Name nicht leer wird der Spieler registriert.
         * Der Name wird eingeloggt, Textfeld sowie Button werden inaktiv geschaltet.
         */
        accept.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                String playerName = nameField.getText().strip();
                if(!playerName.equals("")){
                    accept.setEnabled(false);
                    accept.setMainAndVariations(new Color(210,210,210));
                    nameField.setEditable(false);
                    dispatchMessage("New Player", playerName);
                }else{
                    nameField.setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.RED));
                }
            }
        });
        this.add(playerLabel);
        this.add(nameField);
        this.add(accept);
    }

    public void setListener(Listener l){
        listener = l;
    }

    public void dispatchMessage(String info, Object data){
        if(listener != null){
            listener.receive(new Message(this, info, data));
        }
    }
}
