package pictobrick.ui.handlers;

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileFilter;

import pictobrick.service.DataProcessor;
import pictobrick.ui.MainWindow;
import pictobrick.ui.PictureElement;
import pictobrick.ui.panels.ZoomPanel;

/**
 * Image loader for main window. Code moved from
 * {@link pictobrick.ui.MainWindow} by John Watne 09/2023.
 */
public final class ImageLoader {
    /** Private constructor for utility class. */
    private ImageLoader() {

    }

    /**
     * System dialog for image loading.
     *
     * @param mainWindow the main window for the application.
     * @author Tobias Reichling
     * @exception IOExcepion
     */
    public static void imageLoad(final MainWindow mainWindow) {
        // system dialog
        final JFileChooser d = new JFileChooser();
        d.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".jpg")
                        || f.getName().toLowerCase().endsWith(".jpeg")
                        || f.getName().toLowerCase().endsWith(".gif")
                        || f.getName().toLowerCase().endsWith(".png");
            }

            public String getDescription() {
                return "*.jpg;*.gif;*.png";
            }
        });

        d.showOpenDialog(mainWindow);
        final File selectedFile = d.getSelectedFile();
        final DataProcessor dataProcessing = mainWindow.getDataProcessing();
        final ResourceBundle textbundle = MainWindow.getTextBundle();

        if (dataProcessing.imageLoad(selectedFile)) {
            final ZoomPanel guiPanelZoom = mainWindow.getGuiPanelZoom();
            final JSlider guiZoomSlider1 = guiPanelZoom.getGuiZoomSlider1();
            guiZoomSlider1.setEnabled(true);
            final JScrollPane guiScrollPaneTop = mainWindow
                    .getGuiScrollPaneTop();
            final JSplitPane guiSplitPane = mainWindow.getGuiSplitPane();
            dataProcessing.computeScaleFactor(false,
                    (double) (guiScrollPaneTop.getWidth()
                            - MainWindow.PANEL_HORIZONTAL_PADDING),
                    (double) ((guiSplitPane.getHeight()
                            - guiSplitPane.getDividerLocation()
                            - MainWindow.PANEL_VERTICAL_PADDING)));
            guiZoomSlider1.setValue(MainWindow.MINIMUM_ZOOM_SLIDER_VALUE);
            final PictureElement guiPictureElementTop = mainWindow
                    .getGuiPictureElementTop();
            guiPictureElementTop.setImage(dataProcessing.getScaledImage(false,
                    guiZoomSlider1.getValue()));
            guiPictureElementTop.updateUI();
            mainWindow.getGuiPanelOptions1()
                    .showImageInfo(selectedFile.getName());
            mainWindow.showInfo(textbundle.getString("output_mainWindow_20")
                    + " " + selectedFile.getName() + " "
                    + textbundle.getString("output_mainWindow_18") + ".");
        } else {
            mainWindow
                    .errorDialog(textbundle.getString("output_mainWindow_21"));
        }
    }

}
