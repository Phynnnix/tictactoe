package de.phynnnix.tictactoe;

/**
 * Bildet ein Interface für Klassen, die Nachrichten an einen Zuhörer abschicken sollen. 
 * Es gibt nur einen Zuhörer (Unterschied zum Observable Pattern) und die Methoden müssen von den abstammenden Klassen selbst implementiert werden,
 * da diese Klassen u.a. von JComponents abgeleitet werden und somit keine weitere Klasse erweitern können.
 */
public interface Dispatcher {
    /**
     * Setzt den Zuhörer für die Klasse.
     * @param l Der zuhörer, an den die Nachrichten entsendet werden.
     */
    public void setListener(Listener l);

    /**
     * Schickt eine Nachricht an den Zuhörer.
     * Die Implementierung sollte dabei in der Regel sich selbst als Sender der Nachricht eintragen und die Parameter weitergeben.
     * Sollte in der Regel nur Klassenintern verwendet werden.
     * @param info Information über den Grund oder den Kontext der entsendeten Nachricht.
     * @param data Die Datenlast die der Nachricht angehängt werden soll.
     */
    public void dispatchMessage(String info, Object data);
}
