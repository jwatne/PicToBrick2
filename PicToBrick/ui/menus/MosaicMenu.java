package pictobrick.ui.menus;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import pictobrick.ui.MainWindow;

/**
 * Mosaic menu for main window. Code moved from {@link pictobrick.ui.MainWindow}
 * by John Watne 09/2023.
 */
public class MosaicMenu extends JMenu {
    private ButtonGroup menuGroupQuantisation;
    private ButtonGroup menuGroupTiling;
    private JRadioButtonMenuItem menuAlgorithm11;
    private JRadioButtonMenuItem menuAlgorithm12;
    private JRadioButtonMenuItem menuAlgorithm13;
    private JRadioButtonMenuItem menuAlgorithm14;
    private JRadioButtonMenuItem menuAlgorithm15;
    private JRadioButtonMenuItem menuAlgorithm16;
    private JRadioButtonMenuItem menuAlgorithm17;
    private JRadioButtonMenuItem menuAlgorithm21;
    private JRadioButtonMenuItem menuAlgorithm22;
    private JRadioButtonMenuItem menuAlgorithm23;
    private JRadioButtonMenuItem menuAlgorithm24;
    private JRadioButtonMenuItem menuAlgorithm25;
    private JMenuItem menuMosaicGenerate;

    public JRadioButtonMenuItem getMenuAlgorithm11() {
        return menuAlgorithm11;
    }

    public JMenuItem getMenuMosaicGenerate() {
        return menuMosaicGenerate;
    }

    public JRadioButtonMenuItem getMenuAlgorithm12() {
        return menuAlgorithm12;
    }

    public JRadioButtonMenuItem getMenuAlgorithm13() {
        return menuAlgorithm13;
    }

    public JRadioButtonMenuItem getMenuAlgorithm14() {
        return menuAlgorithm14;
    }

    public JRadioButtonMenuItem getMenuAlgorithm15() {
        return menuAlgorithm15;
    }

    public JRadioButtonMenuItem getMenuAlgorithm16() {
        return menuAlgorithm16;
    }

    public JRadioButtonMenuItem getMenuAlgorithm17() {
        return menuAlgorithm17;
    }

    public JRadioButtonMenuItem getMenuAlgorithm25() {
        return menuAlgorithm25;
    }

    public JRadioButtonMenuItem getMenuAlgorithm21() {
        return menuAlgorithm21;
    }

    public JRadioButtonMenuItem getMenuAlgorithm22() {
        return menuAlgorithm22;
    }

    public JRadioButtonMenuItem getMenuAlgorithm23() {
        return menuAlgorithm23;
    }

    public JRadioButtonMenuItem getMenuAlgorithm24() {
        return menuAlgorithm24;
    }

    /**
     * Constructs a mosaic menu for the specified main window.
     *
     * @param s          the label for the menu.
     * @param mainWindow the main window, which implements the action listener for
     *                   the menu items.
     */
    public MosaicMenu(final String s, final MainWindow mainWindow) {
        super(s);
        menuGroupQuantisation = new ButtonGroup();
        menuAlgorithm11 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_naiveQuantisationRgb"),
                true);
        menuAlgorithm11.addActionListener(mainWindow);
        menuAlgorithm11.setActionCommand("algorithm11");
        menuGroupQuantisation.add(menuAlgorithm11);
        add(menuAlgorithm11);
        menuAlgorithm17 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_naiveQuantisationLab"));
        menuAlgorithm17.addActionListener(mainWindow);
        menuAlgorithm17.setActionCommand("algorithm17");
        menuGroupQuantisation.add(menuAlgorithm17);
        add(menuAlgorithm17);
        menuAlgorithm12 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_errorDiffusion"));
        menuAlgorithm12.addActionListener(mainWindow);
        menuAlgorithm12.setActionCommand("algorithm12");
        menuGroupQuantisation.add(menuAlgorithm12);
        add(menuAlgorithm12);
        menuAlgorithm13 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_vectorErrorDiffusion"));
        menuAlgorithm13.addActionListener(mainWindow);
        menuAlgorithm13.setActionCommand("algorithm13");
        menuGroupQuantisation.add(menuAlgorithm13);
        add(menuAlgorithm13);
        menuAlgorithm14 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_patternDithering"));
        menuAlgorithm14.addActionListener(mainWindow);
        menuAlgorithm14.setActionCommand("algorithm14");
        menuGroupQuantisation.add(menuAlgorithm14);
        add(menuAlgorithm14);
        menuAlgorithm15 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_solidRegions"));
        menuAlgorithm15.addActionListener(mainWindow);
        menuAlgorithm15.setActionCommand("algorithm15");
        menuGroupQuantisation.add(menuAlgorithm15);
        add(menuAlgorithm15);
        menuAlgorithm16 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_slicing"));
        menuAlgorithm16.addActionListener(mainWindow);
        menuAlgorithm16.setActionCommand("algorithm16");
        menuGroupQuantisation.add(menuAlgorithm16);
        add(menuAlgorithm16);
        addSeparator();
        menuGroupTiling = new ButtonGroup();
        menuAlgorithm25 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_basicElementsOnly"),
                true);
        menuAlgorithm25.addActionListener(mainWindow);
        menuAlgorithm25.setActionCommand("algorithm25");
        menuGroupTiling.add(menuAlgorithm25);
        add(menuAlgorithm25);
        menuAlgorithm21 = new JRadioButtonMenuItem(
                MainWindow.textbundle.getString("algorithm_elementSizeOptimisation"));
        menuAlgorithm21.addActionListener(mainWindow);
        menuAlgorithm21.setActionCommand("algorithm21");
        menuGroupTiling.add(menuAlgorithm21);
        add(menuAlgorithm21);
        menuAlgorithm22 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_moldingOptimisation"));
        menuAlgorithm22.addActionListener(mainWindow);
        menuAlgorithm22.setActionCommand("algorithm22");
        menuGroupTiling.add(menuAlgorithm22);
        add(menuAlgorithm22);
        menuAlgorithm23 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_costsOptimisation"));
        menuAlgorithm23.addActionListener(mainWindow);
        menuAlgorithm23.setActionCommand("algorithm23");
        menuGroupTiling.add(menuAlgorithm23);
        add(menuAlgorithm23);
        menuAlgorithm24 = new JRadioButtonMenuItem(MainWindow.textbundle.getString("algorithm_stabilityOptimisation"));
        menuAlgorithm24.addActionListener(mainWindow);
        menuAlgorithm24.setActionCommand("algorithm24");
        menuGroupTiling.add(menuAlgorithm24);
        add(menuAlgorithm24);
        addSeparator();
        menuMosaicGenerate = new JMenuItem(MainWindow.textbundle.getString("dialog_mainWindow_menu_31"));
        menuMosaicGenerate.addActionListener(mainWindow);
        menuMosaicGenerate.setActionCommand("mosaicgenerate");
        add(menuMosaicGenerate);
    }

    /**
     * Set mosaic menu radio button selections to values set for the corresponding
     * GUI checkboxes.
     *
     * @param groupNumber  the number of the group to which the radio button
     *                     belongs.
     * @param buttonNumber the number of the radio button within the group.
     */
    public void radioButtonStatus(final int groupNumber, final int buttonNumber) {
        switch (groupNumber) {
            case 1:
                switch (buttonNumber) {
                    case 1:
                        menuAlgorithm11.setSelected(true);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 2:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(true);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 3:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(true);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 4:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(true);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 5:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(true);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 6:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(true);
                        menuAlgorithm17.setSelected(false);
                        break;
                    case 7:
                        menuAlgorithm11.setSelected(false);
                        menuAlgorithm12.setSelected(false);
                        menuAlgorithm13.setSelected(false);
                        menuAlgorithm14.setSelected(false);
                        menuAlgorithm15.setSelected(false);
                        menuAlgorithm16.setSelected(false);
                        menuAlgorithm17.setSelected(true);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (buttonNumber) {
                    case 1:
                        menuAlgorithm21.setSelected(true);
                        menuAlgorithm22.setSelected(false);
                        menuAlgorithm23.setSelected(false);
                        menuAlgorithm24.setSelected(false);
                        menuAlgorithm25.setSelected(false);
                        break;
                    case 2:
                        menuAlgorithm21.setSelected(false);
                        menuAlgorithm22.setSelected(true);
                        menuAlgorithm23.setSelected(false);
                        menuAlgorithm24.setSelected(false);
                        menuAlgorithm25.setSelected(false);
                        break;
                    case 3:
                        menuAlgorithm21.setSelected(false);
                        menuAlgorithm22.setSelected(false);
                        menuAlgorithm23.setSelected(true);
                        menuAlgorithm24.setSelected(false);
                        menuAlgorithm25.setSelected(false);
                        break;
                    case 4:
                        menuAlgorithm21.setSelected(false);
                        menuAlgorithm22.setSelected(false);
                        menuAlgorithm23.setSelected(false);
                        menuAlgorithm24.setSelected(true);
                        menuAlgorithm25.setSelected(false);
                        break;
                    case 5:
                        menuAlgorithm21.setSelected(false);
                        menuAlgorithm22.setSelected(false);
                        menuAlgorithm23.setSelected(false);
                        menuAlgorithm24.setSelected(false);
                        menuAlgorithm25.setSelected(true);
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }

    /**
     * Process mosaic menu portion of output (GUI) state.
     */
    public void processOutput() {
        getMenuAlgorithm11().setEnabled(false);
        getMenuAlgorithm12().setEnabled(false);
        getMenuAlgorithm13().setEnabled(false);
        getMenuAlgorithm14().setEnabled(false);
        getMenuAlgorithm15().setEnabled(false);
        getMenuAlgorithm16().setEnabled(false);
        getMenuAlgorithm17().setEnabled(false);
        getMenuAlgorithm21().setEnabled(false);
        getMenuAlgorithm22().setEnabled(false);
        getMenuAlgorithm23().setEnabled(false);
        getMenuAlgorithm24().setEnabled(false);
        getMenuAlgorithm25().setEnabled(false);
        getMenuMosaicGenerate().setEnabled(false);
    }

    /**
     * Process enabling mosaic menu portion of GUI after generating mosaic state.
     */
    public void processEnableGuiAfterGeneratingMosaic() {
        getMenuAlgorithm11().setEnabled(true);
        getMenuAlgorithm12().setEnabled(true);
        getMenuAlgorithm13().setEnabled(true);
        getMenuAlgorithm14().setEnabled(true);
        getMenuAlgorithm15().setEnabled(true);
        getMenuAlgorithm16().setEnabled(true);
        getMenuAlgorithm17().setEnabled(true);
        getMenuAlgorithm21().setEnabled(true);
        getMenuAlgorithm22().setEnabled(true);
        getMenuAlgorithm23().setEnabled(true);
        getMenuAlgorithm24().setEnabled(true);
        getMenuAlgorithm25().setEnabled(true);
        getMenuMosaicGenerate().setEnabled(true);
    }

    /**
     * Process disable mosaic menu portion of GUI while generating mosaic state.
     */
    public void processDisableGuiWhileGeneratingMosaic() {
        getMenuAlgorithm11().setEnabled(false);
        getMenuAlgorithm12().setEnabled(false);
        getMenuAlgorithm13().setEnabled(false);
        getMenuAlgorithm14().setEnabled(false);
        getMenuAlgorithm15().setEnabled(false);
        getMenuAlgorithm16().setEnabled(false);
        getMenuAlgorithm17().setEnabled(false);
        getMenuAlgorithm21().setEnabled(false);
        getMenuAlgorithm22().setEnabled(false);
        getMenuAlgorithm23().setEnabled(false);
        getMenuAlgorithm24().setEnabled(false);
        getMenuAlgorithm25().setEnabled(false);
        getMenuMosaicGenerate().setEnabled(false);
    }

    /**
     * Process mosaic menu portion of generating mosaic state.
     */
    public void processGenerateMosaic() {
        getMenuAlgorithm11().setEnabled(true);
        getMenuAlgorithm12().setEnabled(true);
        getMenuAlgorithm13().setEnabled(true);
        getMenuAlgorithm14().setEnabled(true);
        getMenuAlgorithm15().setEnabled(true);
        getMenuAlgorithm16().setEnabled(true);
        getMenuAlgorithm17().setEnabled(true);
        getMenuAlgorithm21().setEnabled(true);
        getMenuAlgorithm22().setEnabled(true);
        getMenuAlgorithm23().setEnabled(true);
        getMenuAlgorithm24().setEnabled(true);
        getMenuAlgorithm25().setEnabled(true);
        getMenuMosaicGenerate().setEnabled(true);
    }

    /**
     * Process mosaic menu portion of GUI start state.
     */
    public void processGuiStart() {
        getMenuAlgorithm11().setEnabled(false);
        getMenuAlgorithm12().setEnabled(false);
        getMenuAlgorithm13().setEnabled(false);
        getMenuAlgorithm14().setEnabled(false);
        getMenuAlgorithm15().setEnabled(false);
        getMenuAlgorithm16().setEnabled(false);
        getMenuAlgorithm17().setEnabled(false);
        getMenuAlgorithm21().setEnabled(false);
        getMenuAlgorithm22().setEnabled(false);
        getMenuAlgorithm23().setEnabled(false);
        getMenuAlgorithm24().setEnabled(false);
        getMenuAlgorithm25().setEnabled(false);
        getMenuMosaicGenerate().setEnabled(false);
    }
}
