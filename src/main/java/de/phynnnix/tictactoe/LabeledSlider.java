package de.phynnnix.tictactoe;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridLayout;
import java.awt.Dimension;

/**
 * Eine Erweiterung des JPanel um eine Slider + Label Kombination zu wrappen. 
 */
public class LabeledSlider extends JPanel implements Dispatcher{
    private Listener listener;
    private int min;
    private int max;
    private int value;

    private JLabel label;
    private JSlider slider;


    /**
     * Konstruktor mit dem der Text des Labels sowie die Grenzwerte des Sliders definiert werden.
     * @param labelText Benennung des Sliders, wird im Label angezeigt.
     * @param mn Minimal zulässiger Wert des Sliders.
     * @param mx Maximal zulässiger Wert des Sliders.
     */
    protected LabeledSlider(String labelText, int mn, int mx){
        min = mn;
        max = mx;
        value = min;

        setLayout(new GridLayout(1,2));

        label = new JLabel(labelText);
        this.add(label);

        slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.setEnabled(min < max);
        this.add(slider);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e){
                if(!slider.getValueIsAdjusting()){
                    value = slider.getValue();
                    dispatchMessage("Changed Value", value);
                }
            }
        });

        this.setPreferredSize(new Dimension(560, 72));
    }

    /**
     * Gibt den aktuell (final) eingestellten Wert des Sliders zurück.
     * "final" bedeutet hierbei, dass eine mögliche Sliderbewegung abgeschlossen sein muss, damit der Wert angepasst wird.
     * @return Der Wert, an dem der Slider zuletzt abgelegt wurde.
     */
    protected int getValue(){
        return value;
    }
    
    /**
     * Passe den minimal zulässigen Wert des Sliders an.
     * @param mn Neuer minimal zulässiger Wert.
     */
    protected void setMin(int mn){
        min = mn;
        slider.setMinimum(min);
        if(value < min){
            value = min;
            slider.setValue(value);
        }
        slider.setEnabled(min < max);
    }

    /**
     * Passe den maximal zulässigen Wert des Sliders an.
     * @param mx Neuer maximal zulässiger Wert.
     */
    protected void setMax(int mx){
        max = mx;
        slider.setMaximum(max);
        if(value > max){
            value = max;
            slider.setValue(value);
        }
        slider.setEnabled(min < max);
    }

    /**
     * Shorthand zum Festlegen der Grenzwerte. 
     * Vereint <code>setMin</code> und <code>setMax</code>.
     * @param mn Neuer minimal zulässiger Wert.
     * @param mx Neuer maximal zulässiger Wert.
     */
    protected void setMinMax(int mn, int mx){
        if(min > max){
            return;
        }
        setMin(mn);
        setMax(mx);
    }

    public void setListener(Listener l){
        listener = l;
    }

    public void dispatchMessage(String info, Object data){
        if(listener != null) listener.receive(new Message(this, info, data));
    }
}
