package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Font;
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
import javax.swing.border.TitledBorder;

/**
 * Dialog with information about pictobrick.
 *
 * @author Adrian Schuetz
 */
public class AboutDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** Number of rows in information panels. */
    private static final int INFORMATION_PANEL_ROWS = 4;
    /** Number of pixels horizontal and vertical gap in information panel. */
    private static final int INFORMATION_GAP_PIXELS = 10;
    /** Number of pixels horizontal and vertical gap in information2 panel. */
    private static final int INFORMATION2_GAP_PIXELS = 3;

    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner - window which owns this dialog
     */
    public AboutDialog(final Frame owner) {
        super(owner, textbundle.getString("dialog_about_frame"), true);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel content = new JPanel();
        final JPanel information = new JPanel(
                new GridLayout(INFORMATION_PANEL_ROWS, 1,
                        INFORMATION_GAP_PIXELS, INFORMATION_GAP_PIXELS));
        final JPanel information2 = new JPanel(
                new GridLayout(INFORMATION_PANEL_ROWS, 1,
                        INFORMATION2_GAP_PIXELS, INFORMATION2_GAP_PIXELS));
        final JLabel name = new JLabel("pictobrick 2.0 - 2023-09-01");
        name.setFont(new Font(name.getFont().getFontName(), Font.BOLD,
                name.getFont().getSize()));
        final JLabel homepage = new JLabel("http://www.pictobrick.de ("
                + textbundle.getString("dialog_about_label_5") + ")");
        final JLabel author1 = new JLabel(
                "Tobias Reichling - pictobrick@t-reichling.de");
        final JLabel author2 = new JLabel(
                "Adrian Schütz - pictobrick@basezero.net");
        final JLabel author3 = new JLabel(
                "John Watne (v2.0) - john.watne@gmail.com");
        final JButton ok = new JButton(textbundle.getString("button_ok"));
        ok.setActionCommand("ok");
        final JPanel okPanel = new JPanel();
        okPanel.add(ok);
        ok.addActionListener(this);
        final TitledBorder informationBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_about_border_1"));
        information.setBorder(informationBorder);
        informationBorder.setTitleColor(GRANITE_GRAY);
        content.setLayout(new BorderLayout());
        information.add(name);
        information.add(homepage);
        information.add(author1);
        information.add(author2);
        information.add(author3);
        final JLabel lego1 = new JLabel(
                textbundle.getString("dialog_about_label_1"));
        final JLabel lego2 = new JLabel(
                textbundle.getString("dialog_about_label_2"));
        final JLabel ministeck1 = new JLabel(
                textbundle.getString("dialog_about_label_3"));
        final JLabel ministeck2 = new JLabel(
                textbundle.getString("dialog_about_label_4"));
        information2.add(lego1);
        information2.add(lego2);
        information2.add(ministeck1);
        information2.add(ministeck2);
        final TitledBorder information2Border = BorderFactory
                .createTitledBorder(
                        textbundle.getString("dialog_about_border_2"));
        information2.setBorder(information2Border);
        information2Border.setTitleColor(GRANITE_GRAY);
        content.add(information, BorderLayout.NORTH);
        content.add(information2, BorderLayout.CENTER);
        content.add(okPanel, BorderLayout.SOUTH);
        this.getContentPane().add(content);
        this.pack();
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
