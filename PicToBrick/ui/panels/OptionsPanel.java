package pictobrick.ui.panels;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import pictobrick.ui.MainWindow;
import pictobrick.ui.PicToBrickDialog;

/**
 * Interface for common components of Options Panels. Code moved from MainWindow
 * by John Watne 09/2023.
 */
public interface OptionsPanel {
    /**
     * Returns the border used for all Options Panels.
     *
     * @return the border used for all Options Panels.
     */
    default TitledBorder getOptionAreaBorder() {
        final TitledBorder optionAreaBorder = BorderFactory
                .createTitledBorder(MainWindow.getTextBundle()
                        .getString("dialog_mainWindow_border_2"));
        optionAreaBorder.setTitleColor(PicToBrickDialog.GRANITE_GRAY);
        return optionAreaBorder;
    }
}
