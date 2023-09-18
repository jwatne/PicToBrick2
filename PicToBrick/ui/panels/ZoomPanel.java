package pictobrick.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import pictobrick.ui.MainWindow;

public class ZoomPanel extends JPanel {
    private final JLabel guiLabelZoom1;
    private final JLabel guiLabelZoom2;
    private JSlider guiZoomSlider1;
    private int guiZoomSlider1Value;
    private JSlider guiZoomSlider2;
    private int guiZoomSlider2Value;

    public ZoomPanel(final LayoutManager layout, final MainWindow mainWindow) {
        super(layout);
        final TitledBorder zoomAreaBorder = BorderFactory
                .createTitledBorder(MainWindow.getTextBundle().getString("dialog_mainWindow_border_3"));
        zoomAreaBorder.setTitleColor(new Color(100, 100, 100));
        this.setBorder(zoomAreaBorder);
        mainWindow.getGuiPanelRightArea().add(this, BorderLayout.SOUTH);
        guiLabelZoom1 = new JLabel(MainWindow.getTextBundle().getString("dialog_mainWindow_label_7"));
        guiLabelZoom2 = new JLabel(MainWindow.getTextBundle().getString("dialog_mainWindow_label_8"));
        guiZoomSlider1 = new JSlider(1, 7, 3);
        guiZoomSlider1Value = 3;
        guiZoomSlider1.setMinorTickSpacing(1);
        guiZoomSlider1.setPaintTicks(true);
        guiZoomSlider1.setSnapToTicks(true);
        guiZoomSlider1.addChangeListener(mainWindow);
        guiZoomSlider1.setFocusable(false);
        guiZoomSlider2 = new JSlider(1, 7, 3);
        guiZoomSlider2Value = 3;
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

    public JSlider getGuiZoomSlider1() {
        return guiZoomSlider1;
    }

    public void setGuiZoomSlider1(final JSlider guiZoomSlider1) {
        this.guiZoomSlider1 = guiZoomSlider1;
    }

    public int getGuiZoomSlider1Value() {
        return guiZoomSlider1Value;
    }

    public void setGuiZoomSlider1Value(final int guiZoomSlider1Value) {
        this.guiZoomSlider1Value = guiZoomSlider1Value;
    }

    public JSlider getGuiZoomSlider2() {
        return guiZoomSlider2;
    }

    public void setGuiZoomSlider2(final JSlider guiZoomSlider2) {
        this.guiZoomSlider2 = guiZoomSlider2;
    }

    public int getGuiZoomSlider2Value() {
        return guiZoomSlider2Value;
    }

    public void setGuiZoomSlider2Value(final int guiZoomSlider2Value) {
        this.guiZoomSlider2Value = guiZoomSlider2Value;
    }

}
