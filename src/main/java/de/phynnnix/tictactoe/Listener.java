package de.phynnnix.tictactoe;

/**
 * Ein Zuhörer Interface, das von Dispatchern gesendete Nachrichten empfangen kann.
 */
public interface Listener {
    /**
     * Empfängt eine Nachricht und reagiert ggfs. auf sie.
     * @param m Die empfangene Nachricht.
     */
    public void receive(Message m);
}
