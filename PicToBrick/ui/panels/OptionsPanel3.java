package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pictobrick.ui.MainWindow;

public class OptionsPanel3 extends JPanel implements OptionsPanel {
    /** Top panel. */
    private JPanel guiPanelOptions3Top;
    /** Empty panel. */
    private JPanel guiPanelOptions3Empty;
    /** Generate documents button. */
    private JButton buttonDocumentsGenerate;
    /** Output label. */
    private JLabel guiLabelOutput;
    /** Graphics output. */
    private JCheckBox guiOutputGraphic;
    /** XML output. */
    private JCheckBox guiOutputXml;
    /** Configuration output. */
    private JCheckBox guiOutputConfiguration;
    /** Materials output. */
    private JCheckBox guiOutputMaterial;
    /** Building instructions output. */
    private JCheckBox guiOutputBuildingInstruction;
    /** New mosaic button. */
    private JButton buttonMosaicNew;

    /**
     * Constructs the panel for the main window with the given layout manager.
     *
     * @param layout     the LayoutManager to use.
     * @param mainWindow the main window of the application that is the action
     *                   listener for items on this panel.
     */
    public OptionsPanel3(final LayoutManager layout,
            final MainWindow mainWindow) {
        super(layout);
        setBorder(getOptionAreaBorder());
        guiPanelOptions3Empty = new JPanel();
        guiPanelOptions3Top = new JPanel(GRIDLAYOUT_8_1);
        buttonDocumentsGenerate = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_8"));
        buttonDocumentsGenerate.addActionListener(mainWindow);
        buttonDocumentsGenerate.setActionCommand("documentgenerate");
        guiLabelOutput = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_6") + ":");
        guiOutputGraphic = new JCheckBox(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_check_3"));
        guiOutputXml = new JCheckBox(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_check_4"));
        guiOutputConfiguration = new JCheckBox(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_check_5"));
        guiOutputMaterial = new JCheckBox(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_check_6"));
        guiOutputBuildingInstruction = new JCheckBox(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_check_7"));
        guiOutputGraphic.addActionListener(mainWindow);
        guiOutputGraphic.setActionCommand("guigrafic");
        guiOutputXml.addActionListener(mainWindow);
        guiOutputXml.setActionCommand("guixml");
        guiOutputConfiguration.addActionListener(mainWindow);
        guiOutputConfiguration.setActionCommand("guiconfiguration");
        guiOutputMaterial.addActionListener(mainWindow);
        guiOutputMaterial.setActionCommand("guimaterialliste");
        guiOutputBuildingInstruction.addActionListener(mainWindow);
        guiOutputBuildingInstruction.setActionCommand("guibuildinginstruction");
        buttonMosaicNew = new JButton(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_button_1"));
        buttonMosaicNew.addActionListener(mainWindow);
        buttonMosaicNew.setActionCommand("mosaicnew");
        this.add(guiPanelOptions3Top, BorderLayout.NORTH);
        this.add(guiPanelOptions3Empty, BorderLayout.CENTER);
        this.add(buttonMosaicNew, BorderLayout.SOUTH);
        guiPanelOptions3Top.add(guiLabelOutput);
        guiPanelOptions3Top.add(guiOutputGraphic);
        guiPanelOptions3Top.add(guiOutputConfiguration);
        guiPanelOptions3Top.add(guiOutputMaterial);
        guiPanelOptions3Top.add(guiOutputBuildingInstruction);
        guiPanelOptions3Top.add(guiOutputXml);
        guiPanelOptions3Top.add(buttonDocumentsGenerate);
    }

    /**
     * Returns top panel.
     *
     * @return top panel.
     */
    public final JPanel getGuiPanelOptions3Top() {
        return guiPanelOptions3Top;
    }

    /**
     * Returns Empty panel.
     *
     * @return Empty panel.
     */
    public final JPanel getGuiPanelOptions3Empty() {
        return guiPanelOptions3Empty;
    }

    /**
     * Returns Generate documents button.
     *
     * @return Generate documents button.
     */
    public final JButton getButtonDocumentsGenerate() {
        return buttonDocumentsGenerate;
    }

    /**
     * Returns Output label.
     *
     * @return Output label.
     */
    public final JLabel getGuiLabelOutput() {
        return guiLabelOutput;
    }

    /**
     * Returns Graphics output.
     *
     * @return Graphics output.
     */
    public final JCheckBox getGuiOutputGraphic() {
        return guiOutputGraphic;
    }

    /**
     * Returns XML output.
     *
     * @return XML output.
     */
    public final JCheckBox getGuiOutputXml() {
        return guiOutputXml;
    }

    /**
     * Returns Configuration output.
     *
     * @return Configuration output.
     */
    public final JCheckBox getGuiOutputConfiguration() {
        return guiOutputConfiguration;
    }

    /**
     * Returns Materials output.
     *
     * @return Materials output.
     */
    public final JCheckBox getGuiOutputMaterial() {
        return guiOutputMaterial;
    }

    /**
     * Returns Building instructions output.
     *
     * @return Building instructions output.
     */
    public final JCheckBox getGuiOutputBuildingInstruction() {
        return guiOutputBuildingInstruction;
    }

    /**
     * Returns New mosaic button.
     *
     * @return New mosaic button.
     */
    public final JButton getButtonMosaicNew() {
        return buttonMosaicNew;
    }
}
