package pictobrick.ui.panels;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import pictobrick.ui.MainWindow;
import pictobrick.ui.PicToBrickDialog;

/**
 * Interface for common components of Options Panels. Code moved from MainWindow
 * by John Watne 09/2023.
 */
public interface OptionsPanel {
    /** Quantization group. */
    int QUANTIZATION_GROUP = 1;
    /** Optimization group. */
    int OPTIMIZATION_GROUP = 2;
    /** 8x1 GridLayout. */
    GridLayout GRIDLAYOUT_8_1 = new GridLayout(8, 1);

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
