package pictobrick.ui.menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pictobrick.ui.MainWindow;

/**
 * Preprocessing menu for main window. Code moved from
 * {@link pictobrick.ui.MainWindow} by John Watne 09/2023.
 */
public class PreprocessingMenu extends JMenu {
    private JMenuItem menuMosaicDimension;

    public JMenuItem getMenuMosaicDimension() {
        return menuMosaicDimension;
    }

    /**
     * Constructs a Preprocessing menu for the specified main window.
     *
     * @param s          the label for the menu.
     * @param mainWindow the main window, which implements the action listener for
     *                   the menu items.
     */
    public PreprocessingMenu(final String s, final MainWindow mainWindow) {
        super(s);
        menuMosaicDimension = new JMenuItem(MainWindow.getTextBundle().getString("dialog_mainWindow_menu_21"));
        menuMosaicDimension.addActionListener(mainWindow);
        menuMosaicDimension.setActionCommand("mosaicdimension");
        this.add(menuMosaicDimension);
    }

}
