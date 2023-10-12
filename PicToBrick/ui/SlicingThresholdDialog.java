package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pictobrick.model.ColorObject;
import pictobrick.service.Calculator;

/**
 * Dialog for choosing colors and thresholds.
 *
 * @author Tobias Reichling
 */
public class SlicingThresholdDialog extends JDialog
        implements ActionListener, ChangeListener, PicToBrickDialog {
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Colors. */
    private final JComboBox<String>[] colors;
    /** "From" threshold values. */
    private final JSpinner[] thresholdFrom;
    /** "To" threshold values. */
    private final JSpinner[] thresholdTo;
    /** Number of colors. */
    private final int colorCount;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param owner
     * @param count
     * @param colorEnum
     */
    @SuppressWarnings("unchecked")
    public SlicingThresholdDialog(final Frame owner, final int count,
            final Enumeration<ColorObject> colorEnum) {
        super(owner, textbundle.getString("dialog_slicingThreshold_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        this.colorCount = count;
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel selection = new JPanel(new GridLayout(count, 1, 0, 0));
        final JPanel buttons = new JPanel();
        // BUTTON
        final JButton ok = new JButton("ok");
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        // COLOR FOR DROPDOWNBOX
        final Vector<String> colorSelection = new Vector<>();

        while (colorEnum.hasMoreElements()) {
            final ColorObject farbe = colorEnum.nextElement();
            colorSelection.add(farbe.getName());
        }

        // Array init
        colors = new JComboBox[count];
        thresholdFrom = new JSpinner[count];
        thresholdTo = new JSpinner[count];
        // Gui Block for every color
        int suggestionValueTo = 0;
        int suggestionValueFrom = 0;

        for (int i = 0; i < count; i++) {
            JSpinner fromValue;
            JSpinner toValue;
            suggestionValueTo = (int) ((Calculator.ONE_HUNDRED_PERCENT / count)
                    * (i + 1));
            suggestionValueFrom = (int) ((Calculator.ONE_HUNDRED_PERCENT
                    / count) * (i));
            // GUI Elements
            final JComboBox<String> colorBox = new JComboBox<>(colorSelection);
            colorBox.setEditable(false);
            colorBox.setEnabled(true);
            final SpinnerNumberModel fromModel = new SpinnerNumberModel(
                    suggestionValueFrom, i, 100 - (count - i), 1);
            fromValue = new JSpinner(fromModel);
            fromValue.setEnabled(true);
            fromValue.addChangeListener(this);

            if (i == 0) {
                fromValue.setEnabled(false);
            }

            final SpinnerNumberModel toModel = new SpinnerNumberModel(
                    suggestionValueTo, i + 1, 100 - (count - i - 1), 1);
            toValue = new JSpinner(toModel);
            toValue.setEnabled(true);
            toValue.addChangeListener(this);

            if (i == (count - 1)) {
                toValue.setEnabled(false);
            }

            final JLabel from = new JLabel(
                    textbundle.getString("dialog_slicingThreshold_label_1")
                            + ":");
            final JLabel to = new JLabel(
                    textbundle.getString("dialog_slicingThreshold_label_2")
                            + ":");
            final JPanel selectionColor = new JPanel();
            final JPanel selectionThreshold = new JPanel(
                    new GridLayout(1, 4, 10, 10));
            final JPanel selectionElement = new JPanel(new BorderLayout());
            // Elements
            selectionColor.add(colorBox);
            selectionThreshold.add(from);
            selectionThreshold.add(fromValue);
            selectionThreshold.add(to);
            selectionThreshold.add(toValue);
            final JPanel selectionThresholdum = new JPanel();
            selectionThresholdum.add(selectionThreshold);
            selectionElement.add(selectionColor, BorderLayout.WEST);
            selectionElement.add(selectionThresholdum, BorderLayout.EAST);
            // Border
            final TitledBorder border = BorderFactory.createTitledBorder(
                    textbundle.getString("dialog_slicingThreshold_border") + " "
                            + (i + 1) + ":");
            selectionElement.setBorder(border);
            border.setTitleColor(GRANITE_GRAY);
            colors[i] = colorBox;
            thresholdFrom[i] = fromValue;
            thresholdTo[i] = toValue;
            selection.add(selectionElement);
        }

        // Build dialog
        buttons.add(ok);
        content.add(selection, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns a vector (color/threshold).
     *
     * @author Tobias Reichling
     * @return vector
     */
    public Vector<Object> getSelection() {
        final Vector<Object> result = new Vector<>();

        for (int i = 0; i < colorCount; i++) {
            result.add(colors[i].getSelectedItem());
            result.add(thresholdFrom[i].getValue());
        }

        return result;
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

    /**
     * ChangeListener for JSpinner.
     *
     * @author Tobias Reichling
     * @param event
     */
    public void stateChanged(final ChangeEvent event) {
        int row = -1;
        boolean from = false;
        boolean increment = false;

        // Determine which JSpinner has sent the event.
        for (int i = 0; i < colorCount; i++) {
            if ((JSpinner) event.getSource() == thresholdFrom[i]) {
                // FROM-JSpinner
                row = i;
                from = true;

                if (((Integer) (thresholdFrom[i]
                        .getValue()) < (Integer) (thresholdTo[i - 1]
                                .getValue()))) {
                    increment = false;
                } else {
                    increment = true;
                }

                if (!(thresholdTo[i - 1].getValue() == thresholdFrom[i]
                        .getValue())) {
                    // change associated JSpinner
                    thresholdTo[i - 1].setValue(thresholdFrom[i].getValue());

                    // check that from an to values are not the same!
                    if (increment) {
                        if (!from) {
                            row = row + 1;
                        }

                        if (thresholdFrom[row].getValue()
                                .equals(thresholdTo[row].getValue())) {
                            thresholdTo[row].setValue(
                                    ((Integer) thresholdTo[row].getValue())
                                            + 1);
                        }
                    } else {
                        if (from) {
                            row = row - 1;
                        }

                        if (thresholdFrom[row].getValue()
                                .equals(thresholdTo[row].getValue())) {
                            thresholdFrom[row].setValue(
                                    ((Integer) thresholdFrom[row].getValue())
                                            - 1);
                        }
                    }
                }
            }

            if ((JSpinner) event.getSource() == thresholdTo[i]) {
                // TO-JSpinner
                row = i;
                from = false;

                if (((Integer) (thresholdFrom[i + 1]
                        .getValue()) < (Integer) (thresholdTo[i].getValue()))) {
                    increment = true;
                } else {
                    increment = false;
                }

                // change associated JSpinner
                if (!(thresholdFrom[i + 1].getValue() == thresholdTo[i]
                        .getValue())) {
                    // change associated JSpinner
                    thresholdFrom[i + 1].setValue(thresholdTo[i].getValue());

                    // check that from an to values are not the same!
                    if (increment) {
                        if (!from) {
                            row = row + 1;
                        }

                        if (thresholdFrom[row].getValue()
                                .equals(thresholdTo[row].getValue())) {
                            thresholdTo[row].setValue(
                                    ((Integer) thresholdTo[row].getValue())
                                            + 1);
                        }
                    } else {
                        if (from) {
                            row = row - 1;
                        }

                        if (thresholdFrom[row].getValue()
                                .equals(thresholdTo[row].getValue())) {
                            thresholdFrom[row].setValue(
                                    ((Integer) thresholdFrom[row].getValue())
                                            - 1);
                        }
                    }
                }
            }
        }
    }
}
