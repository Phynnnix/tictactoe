package de.phynnnix.tictactoe;

/**
 * Klasse zum Erstellen von Nachrichten, die von <code>Dispatcher</code>n an <code>Listener</code> geschickt werden.
 * Sie übermitteln den Sender, einen Kontext der Nachricht sowie ggfs. eine Datenlast.
 */
public class Message {
    private Object sender;
    private Object data;
    private String info;

    /**
     * Eine <code>Message</code> muss immer mit Absender, Kontext und Datenlast erzeugt werden.
     * die Datenlast darf explizit <code>null</code> sein, muss dann aber explizit als solche angegeben werden.
     * @param sender Der Absender (<code>Dispatcher</code>) der Nachricht.
     * @param info Der Kontext, in dem die Nachricht steht.
     * @param data Die Datenlast.
     */
    protected Message(Dispatcher sender, String info, Object data){
        this.sender = sender;
        this.info = info;
        this.data = data;
    }

    /**
     * Prüft, ob der angegebene Kontext dem Kontext der Nachricht entspricht.
     * @param info Der Kontext auf den die Nachricht geprüft wird.
     * @return <code>true</code> wenn der übergebene Kontext dem der Nachricht entspricht, <code>false</code> sonst.
     */
    public boolean isAbout(String info){
        return this.info.equals(info);
    }

    /**
     * Prüft, ob der angegebene Absender dem Absender der Nachricht entspricht.
     * Hierbei wird die Gleichheit der Referenz geprüft.
     * @param o Der Absender auf den die Nachricht geprüft wird.
     * @return <code>true</code> wenn der übergebene Absender das identische Objekt zum Absender der Nachricht ist, <code>false</code> sonst.
     */
    public boolean isFrom(Object o){
        return this.sender == o;
    }
    
    /**
     * Prüft, ob der angegebene Absender dem Absender der Nachricht entspricht.
     * Unter Angabe des Typs wird hier die <code>.equals</code> Methode zur prüfung angewandt.
     * @param cls die Klasse, der der Absender entsprechen soll.
     * @param o Der Absender auf den die Nachricht geprüft wird.
     * @return <code>true</code> wenn der übergebene Absender nach einem Cast auf <code>cls</code> dem Absender der Nachricht entspricht, <code>false</code> sonst.
     */
    public <T> boolean isFrom(Class<T> cls, Object o){
        return cls.cast(this.sender).equals(cls.cast(o));
    }

    /**
     * Gibt die Datenlast zurück, gecastet auf den angegebenen Typ.
     * @param cls Die Klasse, auf die die Datenlast gecastet werden soll.
     * @return Die gecastete Datenlast.
     */
    public <T> T getData(Class<T> cls){
        return cls.cast(this.data);
    }

    @Override
    public String toString(){
        String d = data == null ? "null" : data.toString();
        return "{sender: "+sender.toString()+", data: "+d+"}";
    }
}
