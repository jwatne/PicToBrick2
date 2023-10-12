
package pictobrick.ui;

import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Dialog for choosing optimisation method.
 *
 * @author Adrian Schuetz
 */
public class StabilityOptimisationDialog extends JDialog
        implements ActionListener, ChangeListener, PicToBrickDialog {
    /** SpinnerNumberModel for gap height. */
    private static final SpinnerNumberModel MODEL = new SpinnerNumberModel(10,
            6, 30, 1);
    /** Default maximum gap height. */
    private static final int DEFAULT_MAX_GAP_HEIGHT = 10;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Optimization button group. */
    private final ButtonGroup optimisation = new ButtonGroup();
    /** Gap height spinner number model. */
    private final SpinnerNumberModel valueGapHeight;
    /** Max gap height. */
    private int maxGapHeight = DEFAULT_MAX_GAP_HEIGHT;
    /** Gap spinner. */
    private final JSpinner spinnerGap;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     */
    public StabilityOptimisationDialog(final Frame owner) {
        super(owner, textbundle.getString("dialog_stabilityOptimisation_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel radio = new JPanel(new GridLayout(6, 1, 0, 0));
        final JPanel gap = new JPanel(new BorderLayout());
        final JPanel selection = new JPanel(new BorderLayout());
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel buttons = new JPanel();
        // Label
        JLabel text1;
        JLabel text2;
        JLabel text3;
        JLabel text4;
        JLabel text5;
        text1 = new JLabel(
                textbundle.getString("dialog_stabilityOptimisation_label_1"));
        text2 = new JLabel(
                textbundle.getString("dialog_stabilityOptimisation_label_2"));
        text3 = new JLabel(
                textbundle.getString("dialog_stabilityOptimisation_label_3"));
        text4 = new JLabel(
                textbundle.getString("dialog_stabilityOptimisation_label_4"));
        text5 = new JLabel(
                textbundle.getString("dialog_stabilityOptimisation_label_5"));
        valueGapHeight = MODEL;
        spinnerGap = new JSpinner(valueGapHeight);
        spinnerGap.addChangeListener(this);
        spinnerGap.setEnabled(false);
        // BUTTON
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        // Radio Buttons
        final JRadioButton a = new JRadioButton();
        a.setText(textbundle.getString("dialog_stabilityOptimisation_radio_1"));
        a.setActionCommand("normal");
        a.setSelected(true);
        a.addActionListener(this);
        final JRadioButton b = new JRadioButton();
        b.setText(textbundle.getString("dialog_stabilityOptimisation_radio_2"));
        b.setActionCommand("border");
        b.addActionListener(this);
        final JRadioButton c = new JRadioButton();
        c.setText(textbundle.getString("dialog_stabilityOptimisation_radio_3"));
        c.setActionCommand("complete");
        c.addActionListener(this);
        optimisation.add(a);
        optimisation.add(b);
        optimisation.add(c);
        radio.add(text1);
        radio.add(text2);
        radio.add(text3);
        radio.add(a);
        radio.add(b);
        radio.add(c);
        gap.add(text4, BorderLayout.WEST);
        gap.add(spinnerGap, BorderLayout.CENTER);
        gap.add(text5, BorderLayout.EAST);
        buttons.add(ok);
        selection.add(radio, BorderLayout.CENTER);
        selection.add(gap, BorderLayout.SOUTH);
        final TitledBorder optimierungBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_stabilityOptimisation_border"));
        selection.setBorder(optimierungBorder);
        optimierungBorder.setTitleColor(GRANITE_GRAY);
        content.add(selection, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Return optimisation method.
     *
     * @author Adrian Schuetz
     * @return optimisation method (vector)
     */
    public Vector<Object> getMethod() {
        final Vector<Object> vector = new Vector<>();

        // otimisation yes/no
        // only border or complete
        // max. gap height
        if (optimisation.getSelection().getActionCommand() == "normal") {
            vector.add(false);
            vector.add(true);
            vector.add(maxGapHeight);
        } else if (optimisation.getSelection().getActionCommand() == "border") {
            vector.add(true);
            vector.add(true);
            vector.add(maxGapHeight);
        } else {
            // "complete"
            vector.add(true);
            vector.add(false);
            vector.add(maxGapHeight);
        }

        return vector;
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

        if (event.getActionCommand().contains("complete")) {
            spinnerGap.setEnabled(true);
        } else {
            spinnerGap.setEnabled(false);
        }
    }

    /**
     * ChangeListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void stateChanged(final ChangeEvent event) {
        this.maxGapHeight = ((Integer) valueGapHeight.getValue()).intValue();
    }
}
