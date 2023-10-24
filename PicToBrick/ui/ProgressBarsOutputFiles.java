package pictobrick.ui;

import javax.swing.border.TitledBorder;

import java.util.ResourceBundle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Shows 6 progress bars.
 *
 * @author Adrian Schuetz
 */
public class ProgressBarsOutputFiles extends JDialog {
    /** Number for miscellaneous status bar. */
    public static final int MISCELLANEOUS = 6;
    /** Number for XML status bar. */
    public static final int XML = 5;
    /** Number for instruction status bar. */
    private static final int INSTRUCTION = 4;
    /** Number for material progress bar. */
    public static final int MATERIAL = 3;
    /** Number for configuration progress bar. */
    public static final int CONFIGURATION = 2;
    /** Number for graphic progress bar. */
    public static final int GRAPHIC = 1;
    // Color names determined from https://chir.ag/projects/name-that-color/
    /** Dove gray color. */
    private static final Color DOVE_GRAY = new Color(100, 100, 100);
    /** Navy blue color. */
    private static final Color NAVY_BLUE = new Color(0, 0, 150);
    /** Max value for progress bars. */
    private static final int MAX_PROGRESS_VALUE = 100;
    /**
     * Number of pixels between top/left of screen and initial left position of
     * window.
     */
    private static final int DEFAULT_PIXELS = 100;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Graphic progress bar. */
    private final JProgressBar graphic;
    /** Configuration progress bar. */
    private final JProgressBar configuration;
    /** Material progress bar. */
    private final JProgressBar material;
    /** Instructions progress bar. */
    private final JProgressBar instruction;
    /** XML progress bar. */
    private final JProgressBar xml;
    /** Miscellaneous progress bar. */
    private final JProgressBar miscellaneous;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     */
    public ProgressBarsOutputFiles(final Frame owner) {
        super(owner,
                textbundle.getString("dialog_progressBarsOutputFiles_frame"),
                false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel content = new JPanel(new GridLayout(6, 1));
        final JPanel graphicPanel = new JPanel(new GridLayout(1, 1));
        graphic = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                MAX_PROGRESS_VALUE);
        graphic.setStringPainted(true);
        graphic.setValue(0);
        graphic.setForeground(NAVY_BLUE);
        final Dimension d = new Dimension(300, 25);
        graphic.setMaximumSize(d);
        graphic.setPreferredSize(d);
        graphic.setMinimumSize(d);
        final TitledBorder graphicBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_1"));
        graphicPanel.setBorder(graphicBorder);
        graphicBorder.setTitleColor(DOVE_GRAY);
        graphicPanel.add(graphic);
        final JPanel konfiPanel = new JPanel(new GridLayout(1, 1));
        configuration = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                MAX_PROGRESS_VALUE);
        configuration.setStringPainted(true);
        configuration.setValue(0);
        configuration.setForeground(NAVY_BLUE);
        configuration.setMaximumSize(d);
        configuration.setPreferredSize(d);
        configuration.setMinimumSize(d);
        final TitledBorder konfiBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_2"));
        konfiPanel.setBorder(konfiBorder);
        konfiBorder.setTitleColor(DOVE_GRAY);
        konfiPanel.add(configuration);
        final JPanel materialPanel = new JPanel(new GridLayout(1, 1));
        material = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                MAX_PROGRESS_VALUE);
        material.setStringPainted(true);
        material.setValue(0);
        material.setForeground(NAVY_BLUE);
        material.setMaximumSize(d);
        material.setPreferredSize(d);
        material.setMinimumSize(d);
        final TitledBorder materialBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_3"));
        materialPanel.setBorder(materialBorder);
        materialBorder.setTitleColor(DOVE_GRAY);
        materialPanel.add(material);
        final JPanel instructionPanel = new JPanel(new GridLayout(1, 1));
        instruction = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                MAX_PROGRESS_VALUE);
        instruction.setStringPainted(true);
        instruction.setValue(0);
        instruction.setForeground(NAVY_BLUE);
        instruction.setMaximumSize(d);
        instruction.setPreferredSize(d);
        instruction.setMinimumSize(d);
        final TitledBorder instructionBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_4"));
        instructionPanel.setBorder(instructionBorder);
        instructionBorder.setTitleColor(DOVE_GRAY);
        instructionPanel.add(instruction);
        final JPanel xmlPanel = new JPanel(new GridLayout(1, 1));
        xml = new JProgressBar(JProgressBar.HORIZONTAL, 0, MAX_PROGRESS_VALUE);
        xml.setStringPainted(true);
        xml.setValue(0);
        xml.setForeground(NAVY_BLUE);
        xml.setMaximumSize(d);
        xml.setPreferredSize(d);
        xml.setMinimumSize(d);
        final TitledBorder xmlBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_5"));
        xmlPanel.setBorder(xmlBorder);
        xmlBorder.setTitleColor(DOVE_GRAY);
        xmlPanel.add(xml);
        final JPanel miscellaneousPanel = new JPanel(new GridLayout(1, 1));
        miscellaneous = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                MAX_PROGRESS_VALUE);
        miscellaneous.setStringPainted(true);
        miscellaneous.setValue(0);
        miscellaneous.setForeground(NAVY_BLUE);
        miscellaneous.setMaximumSize(d);
        miscellaneous.setPreferredSize(d);
        miscellaneous.setMinimumSize(d);
        final TitledBorder miscellaneousBorder = BorderFactory
                .createTitledBorder(textbundle
                        .getString("dialog_progressBarsOutputFiles_border_6"));
        miscellaneousPanel.setBorder(miscellaneousBorder);
        miscellaneousBorder.setTitleColor(DOVE_GRAY);
        miscellaneousPanel.add(miscellaneous);
        content.add(graphicPanel);
        content.add(konfiPanel);
        content.add(materialPanel);
        content.add(instructionPanel);
        content.add(xmlPanel);
        content.add(miscellaneousPanel);
        this.getContentPane().add(content);
        this.pack();
        this.setModal(false);
    }

    /**
     * Sets the progress bar status (enabled/disabled).
     *
     * @author Adrian Schuetz
     * @param graphicEnabled       <code>true</code> if enabled.
     * @param configurationEnabled <code>true</code> if enabled.
     * @param materialEnabled      <code>true</code> if enabled.
     * @param instructionEnabled   <code>true</code> if enabled.
     * @param xmlEnabled           <code>true</code> if enabled.
     */
    public void setStatus(final boolean graphicEnabled,
            final boolean configurationEnabled, final boolean materialEnabled,
            final boolean instructionEnabled, final boolean xmlEnabled) {
        final boolean miscellaneousEnabled = true; // Always true.

        if (!graphicEnabled) {
            this.graphic.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.graphic.setString(null);
        }

        if (!configurationEnabled) {
            this.configuration.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.configuration.setString(null);
        }

        if (!materialEnabled) {
            this.material.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.material.setString(null);
        }

        if (!instructionEnabled) {
            this.instruction.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.instruction.setString(null);
        }

        if (!xmlEnabled) {
            this.xml.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.xml.setString(null);
        }

        if (!miscellaneousEnabled) {
            this.miscellaneous.setString(textbundle
                    .getString("dialog_progressBarsOutputFiles_progressBar_1"));
        } else {
            this.miscellaneous.setString(null);
        }
    }

    /**
     * Changes progress bar value.
     *
     * @author Adrian Schuetz
     * @param value
     * @param number
     */
    public void showValue(final int value, final int number) {
        switch (number) {
        case GRAPHIC:
            if (value == 0) {
                graphic.setString(textbundle.getString(
                        "dialog_progressBarsOutputFiles_progressBar_2"));
            } else if (value == MAX_PROGRESS_VALUE) {
                graphic.setString(null);
            }

            graphic.setValue(value);
            break;
        case CONFIGURATION:
            configuration.setValue(value);
            break;
        case MATERIAL:
            material.setValue(value);
            break;
        case INSTRUCTION:
            instruction.setValue(value);
            break;
        case XML:
            xml.setValue(value);
            break;
        case MISCELLANEOUS:
            miscellaneous.setValue(value);
            break;
        default:
            break;
        }
    }

    /**
     * Hides the dialog.
     *
     * @author Adrian Schuetz
     */
    public void hideDialog() {
        this.setVisible(false);
    }

    /**
     * Shows the dialog.
     *
     * @author Adrian Schuetz
     */
    public void showDialog() {
        this.setLocation(DEFAULT_PIXELS, DEFAULT_PIXELS);
        this.setVisible(true);
    }

    /**
     * Animates the graphic progress bar while saving the image file.
     *
     * @author Adrian Schuetz
     * @param active (true/false)
     */
    public void animateGraphic(final boolean active) {
        this.graphic.setIndeterminate(active);
    }
}
