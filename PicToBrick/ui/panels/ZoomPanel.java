package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import pictobrick.ui.MainWindow;
import pictobrick.ui.PicToBrickDialog;

public class ZoomPanel extends JPanel {
    /** Maximum value for zoom sliders. */
    private static final int MAX_ZOOM_VALUE = 7;
    /** Initial value for zoom sliders. */
    private static final int INITIAL_ZOOM_VALUE = 3;

    /** Zoom original image label. */
    private final JLabel guiLabelZoom1;
    /** Zoom preview image label. */
    private final JLabel guiLabelZoom2;
    /** Zoom original image slider. */
    private JSlider guiZoomSlider1;
    /** Zoom original image slider value. */
    private int guiZoomSlider1Value;
    /** Zoom preview image slider. */
    private JSlider guiZoomSlider2;
    /** Zoom preview image slider value. */
    private int guiZoomSlider2Value;

    /**
     * Constructor.
     *
     * @param layout     the LayoutManager applied to the panel.
     * @param mainWindow the parent main window.
     */
    public ZoomPanel(final LayoutManager layout, final MainWindow mainWindow) {
        super(layout);
        final TitledBorder zoomAreaBorder = BorderFactory
                .createTitledBorder(MainWindow.getTextBundle()
                        .getString("dialog_mainWindow_border_3"));
        zoomAreaBorder.setTitleColor(PicToBrickDialog.GRANITE_GRAY);
        this.setBorder(zoomAreaBorder);
        mainWindow.getGuiPanelRightArea().add(this, BorderLayout.SOUTH);
        guiLabelZoom1 = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_7"));
        guiLabelZoom2 = new JLabel(MainWindow.getTextBundle()
                .getString("dialog_mainWindow_label_8"));
        guiZoomSlider1 = new JSlider(1, MAX_ZOOM_VALUE, INITIAL_ZOOM_VALUE);
        guiZoomSlider1Value = INITIAL_ZOOM_VALUE;
        guiZoomSlider1.setMinorTickSpacing(1);
        guiZoomSlider1.setPaintTicks(true);
        guiZoomSlider1.setSnapToTicks(true);
        guiZoomSlider1.addChangeListener(mainWindow);
        guiZoomSlider1.setFocusable(false);
        guiZoomSlider2 = new JSlider(1, MAX_ZOOM_VALUE, INITIAL_ZOOM_VALUE);
        guiZoomSlider2Value = INITIAL_ZOOM_VALUE;
        guiZoomSlider2.setMinorTickSpacing(1);
        guiZoomSlider2.setPaintTicks(true);
        guiZoomSlider2.setSnapToTicks(true);
        guiZoomSlider2.addChangeListener(mainWindow);
        guiZoomSlider2.setFocusable(false);
        final Dimension d = new Dimension(210, guiLabelZoom1.getHeight());
        guiLabelZoom1.setPreferredSize(d);
        guiLabelZoom2.setPreferredSize(d);
        this.add(guiLabelZoom1);
        this.add(guiZoomSlider1);
        this.add(guiLabelZoom2);
        this.add(guiZoomSlider2);
    }

    /**
     * Returns Zoom original image slider.
     *
     * @return Zoom original image slider.
     */
    public final JSlider getGuiZoomSlider1() {
        return guiZoomSlider1;
    }

    /**
     * Sets Zoom original image slider.
     *
     * @param slider1 Zoom original image slider.
     */
    public final void setGuiZoomSlider1(final JSlider slider1) {
        this.guiZoomSlider1 = slider1;
    }

    /**
     * Returns Zoom original image slider value.
     *
     * @return Zoom original image slider value.
     */
    public final int getGuiZoomSlider1Value() {
        return guiZoomSlider1Value;
    }

    /**
     * Sets Zoom original image slider value.
     *
     * @param slider1Value Zoom original image slider value.
     */
    public final void setGuiZoomSlider1Value(final int slider1Value) {
        this.guiZoomSlider1Value = slider1Value;
    }

    /**
     * Returns Zoom preview image slider.
     *
     * @return Zoom preview image slider.
     */
    public final JSlider getGuiZoomSlider2() {
        return guiZoomSlider2;
    }

    /**
     * Sets Zoom preview image slider.
     *
     * @param slider2 Zoom preview image slider.
     */
    public final void setGuiZoomSlider2(final JSlider slider2) {
        this.guiZoomSlider2 = slider2;
    }

    /**
     * Returns Zoom preview image slider value.
     *
     * @return Zoom preview image slider value.
     */
    public final int getGuiZoomSlider2Value() {
        return guiZoomSlider2Value;
    }

    /**
     * Sets Zoom preview image slider value.
     *
     * @param slider2Value Zoom preview image slider value.
     */
    public final void setGuiZoomSlider2Value(final int slider2Value) {
        this.guiZoomSlider2Value = slider2Value;
    }

}
