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
 * Shows 4 progress bars.
 *
 * @author Adrian Schuetz
 */
public class ProgressBarsAlgorithms extends JDialog {
    /** 100% as int value (for progress bar percentage). */
    public static final int ONE_HUNDRED_PERCENT = 100;
    /** Medium gray color. */
    public static final Color MEDIUM_GRAY = new Color(100, 100, 100);
    /** 25 pixels. */
    private static final int PX25 = 25;
    /** 300 pixels. */
    private static final int PX300 = 300;
    /** Dark blue color. */
    private static final Color DARK_BLUE = new Color(0, 0, 150);
    /** Count of 4. */
    private static final int COUNT4 = 4;
    /** 100 pixels (offset). */
    private static final int PX100 = 100;
    /** Number for quantization progress bar. */
    public static final int QUANTIZATION = 1;
    /** Number for tiling progress bar. */
    public static final int TILING = 2;
    /** Number for paint progress bar. */
    public static final int PAINT = 3;
    /** Number for statistics progress bar. */
    public static final int STATISTICS = 4;
    /** Text resource button. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Quantization progress bar. */
    private JProgressBar quantisation;
    /** Tiling progress bar. */
    private JProgressBar tiling;
    /** Statistics generation progress bar. */
    private JProgressBar statistic;
    /** Paint progress bar. */
    private JProgressBar paint;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     */
    public ProgressBarsAlgorithms(final Frame owner) {
        super(owner,
                textbundle.getString("dialog_progressBarsAlgorithms_frame"),
                false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setLocation(PX100, PX100);
        this.setResizable(false);
        JPanel content = new JPanel(new GridLayout(COUNT4, 1));
        JPanel quantisationPanel = new JPanel(new GridLayout(1, 1));
        quantisation = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                ONE_HUNDRED_PERCENT);
        quantisation.setStringPainted(true);
        quantisation.setValue(0);
        quantisation.setForeground(DARK_BLUE);
        Dimension d = new Dimension(PX300, PX25);
        quantisation.setMaximumSize(d);
        quantisation.setPreferredSize(d);
        quantisation.setMinimumSize(d);
        TitledBorder quantisationBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_progressBarsAlgorithms_border_1"));
        quantisationPanel.setBorder(quantisationBorder);
        quantisationBorder.setTitleColor(MEDIUM_GRAY);
        quantisationPanel.add(quantisation);
        JPanel tilingPanel = new JPanel(new GridLayout(1, 1));
        tiling = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                ONE_HUNDRED_PERCENT);
        tiling.setStringPainted(true);
        tiling.setValue(0);
        tiling.setForeground(DARK_BLUE);
        tiling.setMaximumSize(d);
        tiling.setPreferredSize(d);
        tiling.setMinimumSize(d);
        TitledBorder tilingBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_progressBarsAlgorithms_border_2"));
        tilingPanel.setBorder(tilingBorder);
        tilingBorder.setTitleColor(MEDIUM_GRAY);
        tilingPanel.add(tiling);
        JPanel statisticPanel = new JPanel(new GridLayout(1, 1));
        statistic = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                ONE_HUNDRED_PERCENT);
        statistic.setStringPainted(true);
        statistic.setValue(0);
        statistic.setForeground(DARK_BLUE);
        statistic.setMaximumSize(d);
        statistic.setPreferredSize(d);
        statistic.setMinimumSize(d);
        TitledBorder statisticBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_progressBarsAlgorithms_border_3"));
        statisticPanel.setBorder(statisticBorder);
        statisticBorder.setTitleColor(MEDIUM_GRAY);
        statisticPanel.add(statistic);
        JPanel paintPanel = new JPanel(new GridLayout(1, 1));
        paint = new JProgressBar(JProgressBar.HORIZONTAL, 0,
                ONE_HUNDRED_PERCENT);
        paint.setStringPainted(true);
        paint.setValue(0);
        paint.setForeground(DARK_BLUE);
        paint.setMaximumSize(d);
        paint.setPreferredSize(d);
        paint.setMinimumSize(d);
        TitledBorder paintBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_progressBarsAlgorithms_border_4"));
        paintPanel.setBorder(paintBorder);
        paintBorder.setTitleColor(MEDIUM_GRAY);
        paintPanel.add(paint);
        content.add(quantisationPanel);
        content.add(tilingPanel);
        content.add(statisticPanel);
        content.add(paintPanel);
        this.getContentPane().add(content);
        this.pack();
        this.setModal(false);
    }

    /**
     * Sets the status of the progress bars (enabled/disabled).
     *
     * @author Adrian Schuetz
     * @param active (false = disabled, true = enabled)
     */
    public void setStatus(final boolean active) {
        if (!active) {
            this.statistic.setString(textbundle
                    .getString("dialog_progressBarsAlgorithms_progressBar"));
        } else {
            this.statistic.setString(null);
        }
    }

    /**
     * Change the progress bar value.
     *
     * @author Adrian Schuetz
     * @param value
     * @param number = 1:quantisation, 2:tiling, 3: paint, 4: statistic
     */
    public void showValue(final int value, final int number) {
        if (number == QUANTIZATION) {
            quantisation.setValue(value);
        } else if (number == TILING) {
            tiling.setValue(value);
        } else if (number == PAINT) {
            paint.setValue(value);
        } else if (number == STATISTICS) {
            statistic.setValue(value);
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
        this.setLocation(PX100, PX100);
        this.setVisible(true);
    }

}
