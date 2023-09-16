package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pictobrick.ui.MainWindow;

public class OptionsPanel3 extends JPanel implements OptionsPanel {
    private JPanel guiPanelOptions3Top;
    private JPanel guiPanelOptions3Empty;
    private JButton buttonDocumentsGenerate;
    private JLabel guiLabelOutput;
    private JCheckBox guiOutputGraphic;
    private JCheckBox guiOutputXml;
    private JCheckBox guiOutputConfiguration;
    private JCheckBox guiOutputMaterial;
    private JCheckBox guiOutputBuildingInstruction;
    private JButton buttonMosaicNew;

    /**
     * Constructs the panel for the main window with the given layout manager.
     *
     * @param layout     the LayoutManager to use.
     * @param mainWindow the main window of the application that is the action
     *                   listener for items on this panel.
     */
    public OptionsPanel3(LayoutManager layout, MainWindow mainWindow) {
        super(layout);
        setBorder(getOptionAreaBorder());
        guiPanelOptions3Empty = new JPanel();
        guiPanelOptions3Top = new JPanel(new GridLayout(8, 1));
        buttonDocumentsGenerate = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_8"));
        buttonDocumentsGenerate.addActionListener(mainWindow);
        buttonDocumentsGenerate.setActionCommand("documentgenerate");
        guiLabelOutput = new JLabel(MainWindow.textbundle.getString("dialog_mainWindow_label_6") + ":");
        guiOutputGraphic = new JCheckBox(MainWindow.textbundle.getString("dialog_mainWindow_check_3"));
        guiOutputXml = new JCheckBox(MainWindow.textbundle.getString("dialog_mainWindow_check_4"));
        guiOutputConfiguration = new JCheckBox(MainWindow.textbundle.getString("dialog_mainWindow_check_5"));
        guiOutputMaterial = new JCheckBox(MainWindow.textbundle.getString("dialog_mainWindow_check_6"));
        guiOutputBuildingInstruction = new JCheckBox(MainWindow.textbundle.getString("dialog_mainWindow_check_7"));
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
        buttonMosaicNew = new JButton(MainWindow.textbundle.getString("dialog_mainWindow_button_1"));
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

    public JPanel getGuiPanelOptions3Top() {
        return guiPanelOptions3Top;
    }

    public JPanel getGuiPanelOptions3Empty() {
        return guiPanelOptions3Empty;
    }

    public JButton getButtonDocumentsGenerate() {
        return buttonDocumentsGenerate;
    }

    public JLabel getGuiLabelOutput() {
        return guiLabelOutput;
    }

    public JCheckBox getGuiOutputGraphic() {
        return guiOutputGraphic;
    }

    public JCheckBox getGuiOutputXml() {
        return guiOutputXml;
    }

    public JCheckBox getGuiOutputConfiguration() {
        return guiOutputConfiguration;
    }

    public JCheckBox getGuiOutputMaterial() {
        return guiOutputMaterial;
    }

    public JCheckBox getGuiOutputBuildingInstruction() {
        return guiOutputBuildingInstruction;
    }

    public JButton getButtonMosaicNew() {
        return buttonMosaicNew;
    }

}
