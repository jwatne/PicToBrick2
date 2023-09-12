package pictobrick.ui.menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pictobrick.ui.MainWindow;

/**
 * File menu for main window. Code moved from {@link pictobrick.ui.MainWindow}
 * by John Watne 09/2023.
 */
public class FileMenu extends JMenu {
    private JMenuItem menuNewMosaic;
    private JMenuItem menuImageLoad;

    public JMenuItem getMenuImageLoad() {
        return menuImageLoad;
    }

    private JMenuItem menuConfigurationLoad;
    private JMenuItem menuSettings;
    private JMenuItem menuEnd;

    public JMenuItem getMenuConfigurationLoad() {
        return menuConfigurationLoad;
    }

    public JMenuItem getMenuSettings() {
        return menuSettings;
    }

    public JMenuItem getMenuEnd() {
        return menuEnd;
    }

    /**
     * Constructs the File menu for the main window.
     *
     * @param s          the label for the menu.
     * @param mainWindow the main window, which implements the ActionListener for
     *                   the menu items.
     */
    public FileMenu(final String s, final MainWindow mainWindow) {
        super(s);
        menuNewMosaic = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_11"));
        menuNewMosaic.addActionListener(mainWindow);
        menuNewMosaic.setActionCommand("mosaicnew");
        this.add(menuNewMosaic);
        menuImageLoad = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_12"));
        menuImageLoad.addActionListener(mainWindow);
        menuImageLoad.setActionCommand("imageload");
        this.add(menuImageLoad);
        menuConfigurationLoad = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_13"));
        menuConfigurationLoad.addActionListener(mainWindow);
        menuConfigurationLoad.setActionCommand("configurationload");
        this.add(menuConfigurationLoad);
        menuSettings = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_14"));
        menuSettings.addActionListener(mainWindow);
        menuSettings.setActionCommand("settings");
        this.add(menuSettings);
        menuEnd = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_15"));
        menuEnd.addActionListener(mainWindow);
        menuEnd.setActionCommand("exit");
        this.add(menuEnd);
    }

    /**
     * Disable select submenus when processing a generate mosaic request.
     */
    public void processGenerateMosaic() {
        getMenuImageLoad().setEnabled(false);
        getMenuConfigurationLoad().setEnabled(false);
    }

    /**
     * Disable select submenus when processing a process cutout state request.
     */
    public void processCutoutState() {
        getMenuConfigurationLoad().setEnabled(false);
        getMenuImageLoad().setEnabled(false);
        getMenuSettings().setEnabled(false);
    }

    /**
     * Disable select submenus when processing a process output request.
     */
    public void processOutput() {
        getMenuImageLoad().setEnabled(false);
        getMenuConfigurationLoad().setEnabled(false);
    }

    /**
     * Enable submenus on processing GUI start.
     */
    public void processGuiStart() {
        getMenuImageLoad().setEnabled(true);
        getMenuConfigurationLoad().setEnabled(true);
        getMenuSettings().setEnabled(true);
    }
}
