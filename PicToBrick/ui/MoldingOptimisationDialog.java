
package pictobrick.ui;

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
import javax.swing.border.TitledBorder;

/**
 * Dialog for choosing parameter for molding optimisation.
 *
 * @author Tobias Reichling
 */
public class MoldingOptimisationDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** GridLayout, 6 rows x 1 column. */
    private static final GridLayout GRIDLAYOUT_6X1 = new GridLayout(6, 1, 0, 0);
    /** GridLayout, 12 rows x 1 column. */
    private static final GridLayout GRIDLAYOUT_12X1 = new GridLayout(12, 1, 0,
            0);
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Optimization button group. */
    private final ButtonGroup optimisation = new ButtonGroup();

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param owner              the Frame from which the dialog is displayed.
     * @param quantisationNumber number indicating optimization choice.
     * @param quantisation       algorithm description.
     */
    public MoldingOptimisationDialog(final Frame owner,
            final int quantisationNumber, final String quantisation) {
        super(owner, textbundle.getString("dialog_moldingOptimisation_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        JPanel radio;
        final JPanel content = new JPanel(new BorderLayout());

        // if quantisation = error diffusino (2color) the radio panel
        // must be greater because it must show more information
        if (quantisationNumber == 2) {
            radio = new JPanel(GRIDLAYOUT_12X1);
        } else {
            radio = new JPanel(GRIDLAYOUT_6X1);
        }

        final JPanel buttons = new JPanel();
        // Label
        JLabel text1;
        JLabel text2;
        JLabel text3;
        JLabel text4;
        JLabel text5;
        JLabel text6;
        JLabel text7;
        JLabel text8;
        JLabel text9;
        JLabel text10;
        text1 = new JLabel(
                textbundle.getString("dialog_moldingOptimisation_label_1") + " "
                        + quantisation + ".");
        text2 = new JLabel(
                textbundle.getString("dialog_moldingOptimisation_label_2"));
        text3 = new JLabel(
                textbundle.getString("dialog_moldingOptimisation_label_3"));
        text4 = new JLabel("");
        // BUTTON
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        // Radio Buttons
        final JRadioButton a = new JRadioButton();
        a.setText(textbundle.getString("dialog_moldingOptimisation_radio_1"));
        a.setActionCommand("no");
        a.setSelected(true);
        final JRadioButton b = new JRadioButton();
        b.setText(textbundle.getString("dialog_moldingOptimisation_radio_2"));
        b.setActionCommand("yes");
        optimisation.add(a);
        optimisation.add(b);
        radio.add(text1);
        radio.add(text2);
        radio.add(text3);
        radio.add(text4);
        radio.add(a);
        radio.add(b);
        // if quantisation = error diffusion (2color) the radio panel
        // must be greater because it must show more information
        if (quantisationNumber == 2) {
            text5 = new JLabel("");
            text6 = new JLabel(
                    textbundle.getString("dialog_moldingOptimisation_label_6a")
                            + " " + quantisation + " " + textbundle.getString(
                                    "dialog_moldingOptimisation_label_6b"));
            text7 = new JLabel(
                    textbundle.getString("dialog_moldingOptimisation_label_7"));
            text8 = new JLabel(
                    textbundle.getString("dialog_moldingOptimisation_label_8"));
            text9 = new JLabel(
                    textbundle.getString("dialog_moldingOptimisation_label_9"));
            text10 = new JLabel(textbundle
                    .getString("dialog_moldingOptimisation_label_10"));
            radio.add(text5);
            radio.add(text6);
            radio.add(text7);
            radio.add(text8);
            radio.add(text9);
            radio.add(text10);
        }
        buttons.add(ok);
        final TitledBorder optimierungBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_moldingOptimisation_border"));
        radio.setBorder(optimierungBorder);
        optimierungBorder.setTitleColor(GRANITE_GRAY);
        content.add(radio, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns the method.
     *
     * @author Tobias Reichling
     * @return action command (vector)
     */
    public Vector<Object> getMethod() {
        final Vector<Object> vector = new Vector<>();
        vector.add(optimisation.getSelection().getActionCommand());
        return vector;
    }

    /**
     * ActionListener.
     *
     * @author Tobias Reichling
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        if (event.getActionCommand().contains("ok")) {
            this.setVisible(false);
        }
    }
}
