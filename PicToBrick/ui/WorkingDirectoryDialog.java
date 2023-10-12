package pictobrick.ui;

import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Dialog for choosing the working directory.
 *
 * @author Adrian Schuetz
 */
public class WorkingDirectoryDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** 1 for choosing a new directory; 0 otherwise. */
    private int buttonNumber = 0;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     * @param workingDirectoryOld
     */
    public WorkingDirectoryDialog(final Frame owner,
            final File workingDirectoryOld) {
        super(owner, textbundle.getString("dialog_workingDirectory_frame"),
                true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        JPanel content = new JPanel(new BorderLayout());
        JPanel text = new JPanel();
        JPanel buttons = new JPanel();
        JLabel label = new JLabel();

        if (workingDirectoryOld == null) {
            label.setText(
                    textbundle.getString("dialog_workingDirectory_label"));
        } else {
            label.setText(workingDirectoryOld.getPath());
        }

        JButton newDirectory = new JButton(
                textbundle.getString("dialog_workingDirectory_button"));
        newDirectory.setActionCommand("ok");
        newDirectory.addActionListener(this);
        JButton cancel = new JButton(textbundle.getString("button_cancel"));
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);
        buttons.add(newDirectory);
        buttons.add(cancel);
        text.add(label);
        TitledBorder textBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_workingDirectory_border"));
        text.setBorder(textBorder);
        textBorder.setTitleColor(GRANITE_GRAY);
        content.add(text, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Returns the button number: 1 for choosing a new directory.
     *
     * @author Adrian Schuetz
     * @return 1 for choosing a new directory, 0 else
     */
    public int getButton() {
        return this.buttonNumber;
    }

    /**
     * ActionListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        if (event.getActionCommand().contains("ok")) {
            buttonNumber = 1;
            this.setVisible(false);
        }

        if (event.getActionCommand().contains("cancel")) {
            this.setVisible(false);
        }
    }
}
