package pictobrick.ui;

import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog for choosing color quantity for slicing.
 *
 * @author Tobias Reichling
 */
public class SlicingColorNumberDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Number of colors. */
    private final JComboBox<Integer> quantity;

    /**
     * Constructor.
     *
     * @author Tobias Reichling
     * @param owner
     * @param start
     * @param end
     */
    public SlicingColorNumberDialog(final Frame owner, final int start,
            final int end) {
        super(owner, textbundle.getString("dialog_slicingColorNumber_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel content = new JPanel(new BorderLayout());
        final JPanel dropdown = new JPanel(new GridLayout(1, 2, 10, 10));
        final JPanel buttons = new JPanel();
        // BUTTON
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        // DROPDOWN LISTS
        final Vector<Integer> colorQuantity = new Vector<>();

        for (int i = start; i <= end; i++) {
            colorQuantity.add(i);
        }

        quantity = new JComboBox<>(colorQuantity);
        quantity.setEditable(false);
        quantity.setEnabled(true);
        // LABEL
        final JLabel quantityText = new JLabel(
                textbundle.getString("dialog_slicingColorNumber_label") + ":");
        dropdown.add(quantityText);
        dropdown.add(quantity);
        buttons.add(ok);
        final TitledBorder dropdownBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_slicingColorNumber_border"));
        dropdown.setBorder(dropdownBorder);
        dropdownBorder.setTitleColor(GRANITE_GRAY);
        content.add(dropdown, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns color quantity.
     *
     * @author Tobias Reichling
     * @return quantity
     */
    public int getQuantity() {
        return (Integer) quantity.getSelectedItem();
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
