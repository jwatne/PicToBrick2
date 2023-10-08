package pictobrick.ui.menus;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pictobrick.ui.MainWindow;

/**
 * Output menu for main window. Code moved from {@link pictobrick.ui.MainWindow}
 * by John Watne 09/2023.
 */
public class OutputMenu extends JMenu {
    /** Graphic item. */
    private final JCheckBoxMenuItem menuGraphic;
    /** XML item. */
    private final JCheckBoxMenuItem menuXml;
    /** Builiding instructions item. */
    private final JCheckBoxMenuItem menuBuildingInstruction;
    /** Materials item. */
    private final JCheckBoxMenuItem menuMaterial;
    /** Configuration item. */
    private final JCheckBoxMenuItem menuConfiguration;
    /** Generate document item. */
    private final JMenuItem menuDocumentGenerate;

    /**
     * Returns generate document item.
     *
     * @return generate document item.
     */
    public final JMenuItem getMenuDocumentGenerate() {
        return menuDocumentGenerate;
    }

    /**
     * Returns graphic item.
     *
     * @return graphic item.
     */
    public final JCheckBoxMenuItem getMenuGraphic() {
        return menuGraphic;
    }

    /**
     * Returns XML item.
     *
     * @return XML item.
     */
    public final JCheckBoxMenuItem getMenuXml() {
        return menuXml;
    }

    /**
     * Returns configuration item.
     *
     * @return configuration item.
     */
    public final JCheckBoxMenuItem getMenuConfiguration() {
        return menuConfiguration;
    }

    /**
     * Returns building instructions item.
     *
     * @return building instructions item.
     */
    public final JCheckBoxMenuItem getMenuBuildingInstruction() {
        return menuBuildingInstruction;
    }

    /**
     * Returns materials item.
     *
     * @return materials item.
     */
    public final JCheckBoxMenuItem getMenuMaterial() {
        return menuMaterial;
    }

    /**
     * Constructs an output menu for the specified main window.
     *
     * @param s          the label for the menu.
     * @param mainWindow the main window, which implements the action listener
     *                   for the menu items.
     */
    public OutputMenu(final String s, final MainWindow mainWindow) {
        super(s);
        menuGraphic = new JCheckBoxMenuItem(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_menu_41"));
        menuGraphic.addActionListener(mainWindow);
        menuGraphic.setActionCommand("menugrafic");
        menuXml = new JCheckBoxMenuItem(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_menu_42"));
        menuXml.addActionListener(mainWindow);
        menuXml.setActionCommand("menuxml");
        menuBuildingInstruction = new JCheckBoxMenuItem(MainWindow
                .getTextBundle().getString("dialog_mainWindow_menu_43"));
        menuBuildingInstruction.addActionListener(mainWindow);
        menuBuildingInstruction.setActionCommand("menubuildinginstruction");
        menuMaterial = new JCheckBoxMenuItem(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_menu_44"));
        menuMaterial.addActionListener(mainWindow);
        menuMaterial.setActionCommand("menumaterial");
        menuConfiguration = new JCheckBoxMenuItem(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_menu_45"));
        menuConfiguration.addActionListener(mainWindow);
        menuConfiguration.setActionCommand("menuconfiguration");
        add(menuGraphic);
        add(menuConfiguration);
        add(menuMaterial);
        add(menuBuildingInstruction);
        add(menuXml);
        addSeparator();
        menuDocumentGenerate = new JMenuItem(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_menu_46"));
        menuDocumentGenerate.addActionListener(mainWindow);
        menuDocumentGenerate.setActionCommand("documentgenerate");
        add(menuDocumentGenerate);
    }

    /**
     * Enable Output menu gui elements after generating output.
     */
    public void enableGuiAfterGeneratingOutput() {
        getMenuGraphic().setEnabled(true);
        getMenuXml().setEnabled(true);
        getMenuBuildingInstruction().setEnabled(true);
        getMenuMaterial().setEnabled(true);
        getMenuConfiguration().setEnabled(true);
        getMenuDocumentGenerate().setEnabled(true);
    }

    /**
     * Process disable output menu portion of GUI while geneating output files
     * state.
     */
    public void processDisableGuiWhileGeneratingOutput() {
        getMenuGraphic().setEnabled(false);
        getMenuXml().setEnabled(false);
        getMenuBuildingInstruction().setEnabled(false);
        getMenuMaterial().setEnabled(false);
        getMenuConfiguration().setEnabled(false);
        getMenuDocumentGenerate().setEnabled(false);
    }

    /**
     * Process output menu portion of output (GUI) state.
     */
    public void processOutput() {
        getMenuGraphic().setEnabled(true);
        getMenuXml().setEnabled(true);
        getMenuBuildingInstruction().setEnabled(true);
        getMenuMaterial().setEnabled(true);
        getMenuConfiguration().setEnabled(true);
        getMenuDocumentGenerate().setEnabled(true);
    }

    /**
     * Process output menu portion of generate mosaic state.
     */
    public void processGenerateMosaic() {
        getMenuGraphic().setEnabled(false);
        getMenuXml().setEnabled(false);
        getMenuBuildingInstruction().setEnabled(false);
        getMenuMaterial().setEnabled(false);
        getMenuConfiguration().setEnabled(false);
        getMenuDocumentGenerate().setEnabled(false);
    }

    /**
     * Process output menu portion of GUI start state.
     */
    public void processGuiStart() {
        getMenuGraphic().setEnabled(false);
        getMenuXml().setEnabled(false);
        getMenuBuildingInstruction().setEnabled(false);
        getMenuMaterial().setEnabled(false);
        getMenuConfiguration().setEnabled(false);
        getMenuDocumentGenerate().setEnabled(false);
    }
}
