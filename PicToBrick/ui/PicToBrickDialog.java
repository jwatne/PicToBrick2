package pictobrick.ui;

import javax.swing.RootPaneContainer;

import java.awt.Color;

/**
 * Interface for PicToBrick dialogs, mostly for common constants.
 */
public interface PicToBrickDialog extends RootPaneContainer {
    /** Granite gray color. */
    Color GRANITE_GRAY = new Color(100, 100, 100);
    /** Maximum R, G, or B value in RGB color. */
    int MAX_COLOR_VALUE = 255;

    @Override
    String toString();

}
