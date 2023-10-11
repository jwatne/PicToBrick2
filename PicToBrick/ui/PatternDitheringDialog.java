package pictobrick.ui;

import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * Dialog for luminance value distance.
 *
 * @author Adrian Schuetz
 */
public class PatternDitheringDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** Major tick spacing for distance slider. */
    private static final int MAJOR_TICK_SPACING = 10;
    /** Default distance slider value. */
    private static final int DEFAULT_DISTANCE = 75;
    /** Maximum distance slider value. */
    private static final int MAX_DISTANCE = 100;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Distance slider. */
    private final JSlider distance;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     */
    public PatternDitheringDialog(final Frame owner) {
        super(owner, textbundle.getString("dialog_patternDithering_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel textSlider = new JPanel(new BorderLayout());
        final JPanel text = new JPanel(new GridLayout(2, 1, 5, 5));
        final JPanel slider = new JPanel(new GridLayout(1, 1));
        final JPanel buttons = new JPanel();
        // BUTTON
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        // LABEL
        final JLabel text1 = new JLabel(
                textbundle.getString("dialog_patternDithering_label_1"));
        final JLabel text2 = new JLabel(
                textbundle.getString("dialog_patternDithering_label_2"));
        // Slider
        distance = new JSlider(0, MAX_DISTANCE, DEFAULT_DISTANCE);
        distance.setMinorTickSpacing(1);
        distance.setMajorTickSpacing(MAJOR_TICK_SPACING);
        distance.setPaintLabels(true);
        distance.setPaintTicks(true);
        distance.setSnapToTicks(true);
        distance.setFocusable(false);
        slider.add(distance);
        text.add(text1);
        text.add(text2);
        textSlider.add(text, BorderLayout.NORTH);
        textSlider.add(slider, BorderLayout.SOUTH);
        final TitledBorder textSliderBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_patternDithering_border"));
        textSlider.setBorder(textSliderBorder);
        textSliderBorder.setTitleColor(GRANITE_GRAY);
        buttons.add(ok);
        content.add(textSlider, BorderLayout.NORTH);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns the maximum distance of the luminance values.
     *
     * @author Adrian Schuetz
     * @return distance
     */
    public int getDistance() {
        return distance.getValue();
    }

    /**
     * ActionListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        if (event.getActionCommand().contains("ok")) {
            this.setVisible(false);
        }
    }
}
