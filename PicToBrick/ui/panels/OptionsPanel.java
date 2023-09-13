package pictobrick.ui.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import pictobrick.ui.MainWindow;

/**
 * Interface for common components of Options Panels. Code moved from MainWindow
 * by John Watne 09/2023.
 */
public interface OptionsPanel {
    /**
     * Returns the border used for all Options Panels
     *
     * @return
     */
    default TitledBorder getOptionAreaBorder() {
        final TitledBorder optionAreaBorder = BorderFactory
                .createTitledBorder(MainWindow.textbundle.getString("dialog_mainWindow_border_2"));
        optionAreaBorder.setTitleColor(new Color(100, 100, 100));
        return optionAreaBorder;
    }
}
