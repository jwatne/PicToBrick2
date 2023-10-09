package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Dialog with color mixer.
 *
 * @author Adrian Schuetz
 */
public class ColorObjectNewDialog extends JDialog
        implements PicToBrickDialog, ActionListener, ChangeListener {
    /** Initial x coordinate of dialog. */
    private static final int INITIAL_X = 600;
    /** Initial y coordinate. */
    private static final int INITIAL_Y = 100;
    /**
     * GridLayout with 4 rows, 2 columns, 5 pixel horizontal and vertical gap.
     */
    private static final GridLayout GRIDLAYOUT_4_2_5 = new GridLayout(4, 2, 5,
            5);
    /** Minimum and preferred preview panel width in pixels. */
    private static final int PREVIEW_WIDTH = 70;

    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Red value. */
    private final SpinnerNumberModel valueRed;
    /** Green value. */
    private final SpinnerNumberModel valueGreen;
    /** Blue value. */
    private final SpinnerNumberModel valueBlue;
    /** Color preview panel. */
    private final JPanel colorPreview;
    /** Selected color. */
    private Color selectedColor;
    /** Color name. */
    private final JTextField colorName;
    /** Error string. */
    private String error = "";
    /** Indicates whether the dialog has been canceled. */
    private boolean cancel = true;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     */
    public ColorObjectNewDialog(final JDialog owner) {
        super(owner, textbundle.getString("dialog_colorObjectNew_frame"), true);
        this.setLocation(INITIAL_X, INITIAL_Y);
        this.setResizable(false);
        final JPanel input = new JPanel();
        final TitledBorder inputBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_colorObjectNew_border"));
        input.setBorder(inputBorder);
        inputBorder.setTitleColor(PicToBrickDialog.GRANITE_GRAY);
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel left = new JPanel(GRIDLAYOUT_4_2_5);
        final JPanel right = new JPanel();
        colorPreview = new JPanel();
        final Border border = BorderFactory.createLineBorder(Color.BLACK);
        colorPreview.setBorder(border);
        right.add(colorPreview);
        final JPanel bottom = new JPanel();
        final JLabel name = new JLabel(
                textbundle.getString("dialog_colorObjectNew_label_name") + ":");
        colorName = new JTextField();
        final JLabel red = new JLabel(
                textbundle.getString("dialog_colorObjectNew_label_red")
                        + ":  ");
        final JLabel green = new JLabel(
                textbundle.getString("dialog_colorObjectNew_label_green")
                        + ":  ");
        final JLabel blue = new JLabel(
                textbundle.getString("dialog_colorObjectNew_label_blue")
                        + ":  ");
        // SpinnerNumberModel mit Wert, Minimum, Maximum und Schrittweite
        valueRed = new SpinnerNumberModel(0, 0, MAX_COLOR_VALUE, 1);
        valueGreen = new SpinnerNumberModel(0, 0, MAX_COLOR_VALUE, 1);
        valueBlue = new SpinnerNumberModel(0, 0, MAX_COLOR_VALUE, 1);
        final JSpinner spinnerRed = new JSpinner(valueRed);
        final JSpinner spinnerGreen = new JSpinner(valueGreen);
        final JSpinner spinnerBlue = new JSpinner(valueBlue);
        spinnerRed.addChangeListener(this);
        spinnerGreen.addChangeListener(this);
        spinnerBlue.addChangeListener(this);
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        final JButton cancelButton = new JButton(
                textbundle.getString("button_cancel"));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        left.add(name);
        left.add(colorName);
        left.add(red);
        left.add(spinnerRed);
        left.add(green);
        left.add(spinnerGreen);
        left.add(blue);
        left.add(spinnerBlue);
        // initialize color preview
        selectedColor = new Color(0, 0, 0);
        colorPreview.setBackground(selectedColor);
        bottom.add(ok);
        bottom.add(cancelButton);
        input.add(left);
        input.add(right);
        colorPreview.setMinimumSize(
                new Dimension(PREVIEW_WIDTH, left.getMinimumSize().height));
        colorPreview.setPreferredSize(
                new Dimension(PREVIEW_WIDTH, left.getMinimumSize().height));
        content.add(input, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns if the dialog is canceled.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    public boolean isCanceled() {
        return this.cancel;
    }

    /**
     * Returns the mixed color.
     *
     * @author Adrian Schuetz
     * @return color object
     */
    public Color getColorObject() {
        return this.selectedColor;
    }

    /**
     * Returns the name of the color.
     *
     * @author Adrian Schuetz
     * @return color name
     */
    public String getColorName() {
        return this.colorName.getText();
    }

    /**
     * Returns if the input is valid.
     *
     * @author Adrian Schuetz
     * @return true or false
     */
    private boolean validInput() {
        error = "";

        if (colorName.getText().length() == 0) {
            error = error.concat(
                    textbundle.getString("dialog_colorObjectNew_error") + "\n");
        }

        if (!(error.equals(""))) {
            return false;
        }

        return true;
    }

    /**
     * Shows the dialog.
     *
     * @author Adrian Schuetz
     */
    public void setVisible() {
        this.setVisible(true);
    }

    /**
     * ActionListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        if (event.getActionCommand().contains("cancel")) {
            this.cancel = true;
            this.setVisible(false);
        }

        if (event.getActionCommand().contains("ok")) {
            if (validInput()) {
                this.cancel = false;
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, error,
                        textbundle.getString("dialog_error_frame"),
                        JOptionPane.ERROR_MESSAGE);
                error = "";
            }
        }
    }

    /**
     * ChangeListener controls the color preview.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void stateChanged(final ChangeEvent event) {
        final int red = ((Integer) valueRed.getValue()).intValue();
        final int green = ((Integer) valueGreen.getValue()).intValue();
        final int blue = ((Integer) valueBlue.getValue()).intValue();
        this.selectedColor = new Color(red, green, blue);
        colorPreview.setBackground(this.selectedColor);
    }
}
