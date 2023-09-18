package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pictobrick.ui.MainWindow;

/**
 * Panel for the second set of options for the program. Code moved from
 * {@link MainWindow} by John Watne 09/2023.
 */
public class OptionsPanel2 extends JPanel implements OptionsPanel {
    private final JPanel guiPanelOptions2Bottom;
    private JPanel guiPanelOptions2Top;
    private final JButton buttonMosaicGenerate;
    private final JLabel guiLabelQuantisation;
    private final JLabel guiLabelTiling;
    private final JLabel guiLabelSeparator;
    private ButtonGroup guiGroupQuantisation;
    private ButtonGroup guiGroupTiling;
    private final JButton buttonOutput;
    private final JRadioButton guiRadioAlgorithm11;
    private final JRadioButton guiRadioAlgorithm12;
    private final JRadioButton guiRadioAlgorithm13;
    private final JRadioButton guiRadioAlgorithm14;
    private final JRadioButton guiRadioAlgorithm15;
    private final JRadioButton guiRadioAlgorithm16;
    private final JRadioButton guiRadioAlgorithm17;
    private final JRadioButton guiRadioAlgorithm21;
    private final JRadioButton guiRadioAlgorithm22;
    private final JRadioButton guiRadioAlgorithm23;
    private final JRadioButton guiRadioAlgorithm24;
    private final JRadioButton guiRadioAlgorithm25;
    private JComboBox<String> guiComboBoxInterpolation;
    private final JCheckBox guiThreeDEffect;
    private final JCheckBox guiStatistic;
    private final JPanel guiPanelThreeDEffectStatistic;

    /**
     * Constructs the panel for the main window with the given layout manager.
     *
     * @param layout     the LayoutManager to use.
     * @param mainWindow the main window of the application that is the action
     *                   listener for items on this panel.
     */
    public OptionsPanel2(final LayoutManager layout, final MainWindow mainWindow) {
        super(layout);
        setBorder(getOptionAreaBorder());

        if ((System.getProperty("os.name").startsWith("Mac"))) {
            guiPanelOptions2Top = new JPanel(new GridLayout(14, 1, 0, 4));
        } else {
            guiPanelOptions2Top = new JPanel(new GridLayout(14, 1, 0, 0));
        }

        guiPanelOptions2Bottom = new JPanel(new GridLayout(5, 1, 0, 0));
        buttonMosaicGenerate = new JButton(MainWindow.getTextBundle().getString("dialog_mainWindow_button_6"));
        buttonMosaicGenerate.setActionCommand("mosaicgenerate");
        buttonMosaicGenerate.addActionListener(mainWindow);
        guiLabelQuantisation = new JLabel(MainWindow.getTextBundle().getString("dialog_mainWindow_label_4") + ":");
        guiLabelQuantisation.setFont(new Font(guiLabelQuantisation.getFont().getFontName(), Font.BOLD,
                guiLabelQuantisation.getFont().getSize()));
        guiLabelTiling = new JLabel(MainWindow.getTextBundle().getString("dialog_mainWindow_label_5") + ":");
        guiLabelTiling.setFont(
                new Font(guiLabelTiling.getFont().getFontName(), Font.BOLD, guiLabelTiling.getFont().getSize()));
        guiLabelSeparator = new JLabel("");
        guiGroupQuantisation = new ButtonGroup();
        guiGroupTiling = new ButtonGroup();
        buttonOutput = new JButton(MainWindow.getTextBundle().getString("dialog_mainWindow_button_7"));
        buttonOutput.setActionCommand("output");
        buttonOutput.addActionListener(mainWindow);
        guiRadioAlgorithm11 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_naiveQuantisationRgb"));
        guiRadioAlgorithm12 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_errorDiffusion"));
        guiRadioAlgorithm13 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_vectorErrorDiffusion"));
        guiRadioAlgorithm14 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_patternDithering"));
        guiRadioAlgorithm15 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_solidRegions"));
        guiRadioAlgorithm16 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_slicing"));
        guiRadioAlgorithm17 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_naiveQuantisationLab"));
        guiRadioAlgorithm21 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_elementSizeOptimisation"));
        guiRadioAlgorithm22 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_moldingOptimisation"));
        guiRadioAlgorithm23 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_costsOptimisation"));
        guiRadioAlgorithm24 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_stabilityOptimisation"));
        guiRadioAlgorithm25 = new JRadioButton(MainWindow.getTextBundle().getString("algorithm_basicElementsOnly"));
        guiRadioAlgorithm11.addActionListener(mainWindow);
        guiRadioAlgorithm12.addActionListener(mainWindow);
        guiRadioAlgorithm13.addActionListener(mainWindow);
        guiRadioAlgorithm14.addActionListener(mainWindow);
        guiRadioAlgorithm15.addActionListener(mainWindow);
        guiRadioAlgorithm16.addActionListener(mainWindow);
        guiRadioAlgorithm17.addActionListener(mainWindow);
        guiRadioAlgorithm21.addActionListener(mainWindow);
        guiRadioAlgorithm22.addActionListener(mainWindow);
        guiRadioAlgorithm23.addActionListener(mainWindow);
        guiRadioAlgorithm24.addActionListener(mainWindow);
        guiRadioAlgorithm25.addActionListener(mainWindow);
        guiRadioAlgorithm11.setActionCommand("algorithm11");
        guiRadioAlgorithm11.setSelected(true);
        guiRadioAlgorithm12.setActionCommand("algorithm12");
        guiRadioAlgorithm13.setActionCommand("algorithm13");
        guiRadioAlgorithm14.setActionCommand("algorithm14");
        guiRadioAlgorithm15.setActionCommand("algorithm15");
        guiRadioAlgorithm16.setActionCommand("algorithm16");
        guiRadioAlgorithm17.setActionCommand("algorithm17");
        guiRadioAlgorithm21.setActionCommand("algorithm21");
        guiRadioAlgorithm22.setActionCommand("algorithm22");
        guiRadioAlgorithm23.setActionCommand("algorithm23");
        guiRadioAlgorithm24.setActionCommand("algorithm24");
        guiRadioAlgorithm25.setActionCommand("algorithm25");
        guiRadioAlgorithm25.setSelected(true);
        final Vector<String> guiComboBoxInterpolationsVerfahren = new Vector<>();
        guiComboBoxInterpolationsVerfahren.add(MainWindow.getTextBundle().getString("dialog_mainWindow_combo_1"));
        guiComboBoxInterpolationsVerfahren.add(MainWindow.getTextBundle().getString("dialog_mainWindow_combo_2"));
        guiComboBoxInterpolationsVerfahren.add(MainWindow.getTextBundle().getString("dialog_mainWindow_combo_3"));
        guiComboBoxInterpolation = new JComboBox<>(guiComboBoxInterpolationsVerfahren);
        guiComboBoxInterpolation.setEditable(false);
        guiComboBoxInterpolation.setEnabled(true);
        guiThreeDEffect = new JCheckBox(MainWindow.getTextBundle().getString("dialog_mainWindow_check_1"));
        guiThreeDEffect.setEnabled(true);
        guiThreeDEffect.setSelected(true);
        guiStatistic = new JCheckBox(MainWindow.getTextBundle().getString("dialog_mainWindow_check_2"));
        guiStatistic.setEnabled(true);
        guiStatistic.setSelected(false);
        guiPanelThreeDEffectStatistic = new JPanel(new BorderLayout());
        guiPanelThreeDEffectStatistic.add(guiThreeDEffect, BorderLayout.WEST);
        guiPanelThreeDEffectStatistic.add(guiStatistic, BorderLayout.CENTER);
        guiGroupQuantisation.add(guiRadioAlgorithm11);
        guiGroupQuantisation.add(guiRadioAlgorithm17);
        guiGroupQuantisation.add(guiRadioAlgorithm12);
        guiGroupQuantisation.add(guiRadioAlgorithm13);
        guiGroupQuantisation.add(guiRadioAlgorithm14);
        guiGroupQuantisation.add(guiRadioAlgorithm15);
        guiGroupQuantisation.add(guiRadioAlgorithm16);
        guiGroupTiling.add(guiRadioAlgorithm21);
        guiGroupTiling.add(guiRadioAlgorithm22);
        guiGroupTiling.add(guiRadioAlgorithm23);
        guiGroupTiling.add(guiRadioAlgorithm24);
        guiGroupTiling.add(guiRadioAlgorithm25);
        guiPanelOptions2Top.add(guiLabelQuantisation);
        guiPanelOptions2Top.add(guiRadioAlgorithm11);
        guiPanelOptions2Top.add(guiRadioAlgorithm17);
        guiPanelOptions2Top.add(guiRadioAlgorithm12);
        guiPanelOptions2Top.add(guiRadioAlgorithm13);
        guiPanelOptions2Top.add(guiRadioAlgorithm14);
        guiPanelOptions2Top.add(guiRadioAlgorithm15);
        guiPanelOptions2Top.add(guiRadioAlgorithm16);
        guiPanelOptions2Top.add(guiLabelTiling);
        guiPanelOptions2Top.add(guiRadioAlgorithm25);
        guiPanelOptions2Top.add(guiRadioAlgorithm21);
        guiPanelOptions2Top.add(guiRadioAlgorithm22);
        guiPanelOptions2Top.add(guiRadioAlgorithm23);
        guiPanelOptions2Top.add(guiRadioAlgorithm24);
        guiPanelOptions2Bottom.add(guiPanelThreeDEffectStatistic);
        guiPanelOptions2Bottom.add(guiComboBoxInterpolation);
        guiPanelOptions2Bottom.add(buttonMosaicGenerate);
        guiPanelOptions2Bottom.add(guiLabelSeparator);
        guiPanelOptions2Bottom.add(buttonOutput);
        add(guiPanelOptions2Top, BorderLayout.NORTH);
        add(guiPanelOptions2Bottom, BorderLayout.SOUTH);
    }

    public JButton getButtonMosaicGenerate() {
        return buttonMosaicGenerate;
    }

    public JButton getButtonOutput() {
        return buttonOutput;
    }

    public JRadioButton getGuiRadioAlgorithm11() {
        return guiRadioAlgorithm11;
    }

    public JRadioButton getGuiRadioAlgorithm12() {
        return guiRadioAlgorithm12;
    }

    public JRadioButton getGuiRadioAlgorithm13() {
        return guiRadioAlgorithm13;
    }

    public JRadioButton getGuiRadioAlgorithm14() {
        return guiRadioAlgorithm14;
    }

    public JRadioButton getGuiRadioAlgorithm15() {
        return guiRadioAlgorithm15;
    }

    public JRadioButton getGuiRadioAlgorithm16() {
        return guiRadioAlgorithm16;
    }

    public JRadioButton getGuiRadioAlgorithm17() {
        return guiRadioAlgorithm17;
    }

    public JRadioButton getGuiRadioAlgorithm21() {
        return guiRadioAlgorithm21;
    }

    public JRadioButton getGuiRadioAlgorithm22() {
        return guiRadioAlgorithm22;
    }

    public JRadioButton getGuiRadioAlgorithm23() {
        return guiRadioAlgorithm23;
    }

    public JRadioButton getGuiRadioAlgorithm24() {
        return guiRadioAlgorithm24;
    }

    public JRadioButton getGuiRadioAlgorithm25() {
        return guiRadioAlgorithm25;
    }

    public JComboBox<String> getGuiComboBoxInterpolation() {
        return guiComboBoxInterpolation;
    }

    public void setGuiComboBoxInterpolation(final JComboBox<String> guiComboBoxInterpolation) {
        this.guiComboBoxInterpolation = guiComboBoxInterpolation;
    }

    public ButtonGroup getGuiGroupQuantisation() {
        return guiGroupQuantisation;
    }

    public void setGuiGroupQuantisation(final ButtonGroup guiGroupQuantisation) {
        this.guiGroupQuantisation = guiGroupQuantisation;
    }

    public ButtonGroup getGuiGroupTiling() {
        return guiGroupTiling;
    }

    public void setGuiGroupTiling(final ButtonGroup guiGroupTiling) {
        this.guiGroupTiling = guiGroupTiling;
    }

    public JCheckBox getGuiThreeDEffect() {
        return guiThreeDEffect;
    }

    public JCheckBox getGuiStatistic() {
        return guiStatistic;
    }

    /**
     * method: disableStatisticButton
     * description: disables statistic if not possible
     *
     * @author Tobias Reichling
     */
    public void disableStatisticButton() {
        guiStatistic.setSelected(false);
    }

    /**
     * Set GUI radio buttons to values of corresponding menu checkboxes.
     *
     * @param groupNumber  the group to which the radio button belongs.
     * @param buttonNumber the number of the radio button within the group.
     */
    public void radioButtonStatus(final int groupNumber, final int buttonNumber) {
        switch (groupNumber) {
            case 1:
                switch (buttonNumber) {
                    case 1:
                        guiRadioAlgorithm11.setSelected(true);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 2:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(true);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 3:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(true);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 4:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(true);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 5:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(true);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 6:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(true);
                        guiRadioAlgorithm17.setSelected(false);
                        break;
                    case 7:
                        guiRadioAlgorithm11.setSelected(false);
                        guiRadioAlgorithm12.setSelected(false);
                        guiRadioAlgorithm13.setSelected(false);
                        guiRadioAlgorithm14.setSelected(false);
                        guiRadioAlgorithm15.setSelected(false);
                        guiRadioAlgorithm16.setSelected(false);
                        guiRadioAlgorithm17.setSelected(true);
                        break;
                    default:
                        break;
                }

                break;
            case 2:
                switch (buttonNumber) {
                    case 1:
                        guiRadioAlgorithm21.setSelected(true);
                        guiRadioAlgorithm22.setSelected(false);
                        guiRadioAlgorithm23.setSelected(false);
                        guiRadioAlgorithm24.setSelected(false);
                        guiRadioAlgorithm25.setSelected(false);
                        guiStatistic.setEnabled(true);
                        break;
                    case 2:
                        guiRadioAlgorithm21.setSelected(false);
                        guiRadioAlgorithm22.setSelected(true);
                        guiRadioAlgorithm23.setSelected(false);
                        guiRadioAlgorithm24.setSelected(false);
                        guiRadioAlgorithm25.setSelected(false);
                        guiStatistic.setEnabled(true);
                        break;
                    case 3:
                        guiRadioAlgorithm21.setSelected(false);
                        guiRadioAlgorithm22.setSelected(false);
                        guiRadioAlgorithm23.setSelected(true);
                        guiRadioAlgorithm24.setSelected(false);
                        guiRadioAlgorithm25.setSelected(false);
                        guiStatistic.setEnabled(true);
                        break;
                    case 4:
                        guiRadioAlgorithm21.setSelected(false);
                        guiRadioAlgorithm22.setSelected(false);
                        guiRadioAlgorithm23.setSelected(false);
                        guiRadioAlgorithm24.setSelected(true);
                        guiRadioAlgorithm25.setSelected(false);
                        guiStatistic.setEnabled(true);
                        break;
                    case 5:
                        guiRadioAlgorithm21.setSelected(false);
                        guiRadioAlgorithm22.setSelected(false);
                        guiRadioAlgorithm23.setSelected(false);
                        guiRadioAlgorithm24.setSelected(false);
                        guiRadioAlgorithm25.setSelected(true);
                        guiStatistic.setEnabled(false);
                        guiStatistic.setSelected(false);
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }

    /**
     * Process options panel 2 portion of output (GUI) state.
     */
    public void processOutput() {
        getGuiComboBoxInterpolation().setEnabled(false);
        getGuiThreeDEffect().setEnabled(false);
        getGuiStatistic().setEnabled(false);
    }

    /**
     * Process enable options panel 2 portion of GUI after generating mosaic state.
     */
    public void processEnableGuiAfterGeneratingMosaic() {
        getButtonOutput().setEnabled(true);
        getGuiRadioAlgorithm11().setEnabled(true);
        getGuiRadioAlgorithm12().setEnabled(true);
        getGuiRadioAlgorithm13().setEnabled(true);
        getGuiRadioAlgorithm14().setEnabled(true);
        getGuiRadioAlgorithm15().setEnabled(true);
        getGuiRadioAlgorithm16().setEnabled(true);
        getGuiRadioAlgorithm17().setEnabled(true);
        getGuiRadioAlgorithm21().setEnabled(true);
        getGuiRadioAlgorithm22().setEnabled(true);
        getGuiRadioAlgorithm23().setEnabled(true);
        getGuiRadioAlgorithm24().setEnabled(true);
        getGuiRadioAlgorithm25().setEnabled(true);
        getGuiComboBoxInterpolation().setEnabled(true);
        getButtonMosaicGenerate().setEnabled(true);
        getGuiThreeDEffect().setEnabled(true);

        if (!guiRadioAlgorithm25.isSelected()) {
            getGuiStatistic().setEnabled(true);
        }
    }

    /**
     * Process disable options panel 2 portion of GUI while generating mosaic state.
     */
    public void processDisableGuiWhileGeneratingMosaic() {
        getGuiRadioAlgorithm11().setEnabled(false);
        getGuiRadioAlgorithm12().setEnabled(false);
        getGuiRadioAlgorithm13().setEnabled(false);
        getGuiRadioAlgorithm14().setEnabled(false);
        getGuiRadioAlgorithm15().setEnabled(false);
        getGuiRadioAlgorithm16().setEnabled(false);
        getGuiRadioAlgorithm17().setEnabled(false);
        getGuiRadioAlgorithm21().setEnabled(false);
        getGuiRadioAlgorithm22().setEnabled(false);
        getGuiRadioAlgorithm23().setEnabled(false);
        getGuiRadioAlgorithm24().setEnabled(false);
        getGuiRadioAlgorithm25().setEnabled(false);
        getGuiComboBoxInterpolation().setEnabled(false);
        getButtonOutput().setEnabled(false);
        getButtonMosaicGenerate().setEnabled(false);
        getGuiThreeDEffect().setEnabled(false);
        getGuiStatistic().setEnabled(false);
    }

    /**
     * Process options panel 2 portion of generate mosaic state.
     */
    public void processGenerateMosaic() {
        getGuiComboBoxInterpolation().setEnabled(true);
        getButtonOutput().setEnabled(false);
        guiThreeDEffect.setEnabled(true);
        guiThreeDEffect.setSelected(true);
        guiStatistic.setSelected(false);
        guiStatistic.setEnabled(false);
    }

    /**
     * Process options panel 2 portion of GUI start state.
     */
    public void processGuiStart() {
        guiComboBoxInterpolation.setEnabled(false);
        guiComboBoxInterpolation.setSelectedIndex(0);
    }

}
