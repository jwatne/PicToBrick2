package pictobrick.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * New element dialog.
 *
 * @author Adrian Schuetz
 */
public class ElementObjectNewDialog extends JDialog
        implements ActionListener, PicToBrickDialog {
    /** Maximum number active items. */
    private static final int MAX_ACTIVE = 4;
    /** "Elementnnn" ActionCommand substring begin index. */
    private static final int ELEMENT_BEGIN_INDEX = 7;
    /** Initial X position for dialog. */
    private static final int INITIAL_X = 600;
    /**
     * Multiplier of basis element width and height measures (pixels per tile
     * height?).
     */
    private static final int BASIS_ELEMENT_SIZE_MULTIPLIER = 15;
    /** Default left position. */
    private static final int DEFAULT_LEFT = 7;
    /** Default top position. */
    private static final int DEFAULT_TOP = 7;
    /** Number of elements across dimension for matrix array. */
    private static final int M_SIZE = 8;
    /** Text resource bundle. */
    private static ResourceBundle textbundle = ResourceBundle
            .getBundle("Resources.TextResource");
    /** Indicates whether user canceled the dialog. */
    private boolean cancel = true;
    /** Error message. */
    private String error = "";
    /** Element name field. */
    private final JTextField elementName;
    /** Stability input field. */
    private final JTextField inputStability;
    /** Costs input field. */
    private final JTextField inputCosts;
    /** Element matrix. */
    private boolean[][] elementMatrix;
    /** Checkbox matrix. */
    private final JCheckBox[][] checkboxMatrix = new JCheckBox[M_SIZE][M_SIZE];
    /** Activation counters. */
    private final int[][] activationCounter = new int[M_SIZE][M_SIZE];
    /** Active. */
    private Vector<Integer> active = new Vector<>();
    /** Top of dialog. */
    private int top = DEFAULT_TOP;
    /** Bottom of dialog. */
    private int bottom = 0;
    /** Left of dialog. */
    private int left = DEFAULT_LEFT;
    /** Right of dialog. */
    private int right = 0;
    /** Basis element width. */
    private int basisElementWidth = 0;
    /** Basis element height. */
    private int basisElementHeight = 0;
    /** OK button. */
    private final JButton ok;

    /**
     * Constructor.
     *
     * @author Adrian Schuetz
     * @param owner
     * @param ratioWidth
     * @param ratioHeight
     */
    public ElementObjectNewDialog(final JDialog owner, final int ratioWidth,
            final int ratioHeight) {
        super(owner, textbundle.getString("dialog_elementObjectNew_frame"),
                true);
        final double width = ratioWidth;
        final double height = ratioHeight;

        if (width > height) {
            this.basisElementHeight = (int) ((height / height)
                    * BASIS_ELEMENT_SIZE_MULTIPLIER);
            this.basisElementWidth = (int) ((width / height)
                    * BASIS_ELEMENT_SIZE_MULTIPLIER);
        } else {
            this.basisElementWidth = (int) ((width / width)
                    * BASIS_ELEMENT_SIZE_MULTIPLIER);
            this.basisElementHeight = (int) ((height / width)
                    * BASIS_ELEMENT_SIZE_MULTIPLIER);
        }

        // Images for checkboxes
        final BufferedImage white = new BufferedImage((basisElementWidth - 2),
                (basisElementHeight - 2), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2dWhite = white.createGraphics();
        g2dWhite.setColor(Color.WHITE);
        g2dWhite.fillRect(0, 0, (basisElementWidth - 2),
                (basisElementHeight - 2));
        final BufferedImage black = new BufferedImage((basisElementWidth - 2),
                (basisElementHeight - 2), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2dBlack = black.createGraphics();
        g2dBlack.setColor(Color.BLACK);
        g2dBlack.fillRect(0, 0, (basisElementWidth - 2),
                (basisElementHeight - 2));
        final BufferedImage blue = new BufferedImage((basisElementWidth - 2),
                (basisElementHeight - 2), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2dBlue = blue.createGraphics();
        g2dBlue.setColor(DUKE_BLUE);
        g2dBlue.fillRect(0, 0, (basisElementWidth - 2),
                (basisElementHeight - 2));
        final BufferedImage gray = new BufferedImage((basisElementWidth - 2),
                (basisElementHeight - 2), BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2dGray = gray.createGraphics();
        g2dGray.setColor(Color.LIGHT_GRAY);
        g2dGray.fillRect(0, 0, (basisElementWidth - 2),
                (basisElementHeight - 2));
        // Icons
        final Icon activeIcon = new ImageIcon(black);
        final Icon inactive = new ImageIcon(white);
        final Icon about = new ImageIcon(blue);
        final Icon off = new ImageIcon(gray);
        this.setLocation(INITIAL_X, DEFAULT_PIXELS);
        this.setResizable(false);
        final JPanel contentPanel = new JPanel(new BorderLayout());
        final JPanel inputPanel = new JPanel(new BorderLayout());
        final JPanel valuesPanel = new JPanel(new GridLayout(3, 2));
        final JPanel matrixPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        final JPanel clearPanel = new JPanel();
        final JPanel matrix2Panel = new JPanel();
        final JPanel buttonsPanel = new JPanel();
        // Border for Panels
        final TitledBorder valuesBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_elementObjectNew_border_1"));
        valuesPanel.setBorder(valuesBorder);
        valuesBorder.setTitleColor(GRANITE_GRAY);
        final TitledBorder matrixBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_elementObjectNew_border_2"));
        matrix2Panel.setBorder(matrixBorder);
        matrixBorder.setTitleColor(GRANITE_GRAY);
        final TitledBorder clearBorder = BorderFactory.createTitledBorder(
                textbundle.getString("dialog_elementObjectNew_border_3"));
        clearPanel.setBorder(clearBorder);
        clearBorder.setTitleColor(GRANITE_GRAY);
        // 8x8 checkboxMatrix
        for (int cbMatrixRow = 0; cbMatrixRow < M_SIZE; cbMatrixRow++) {
            for (int cbMatrixCol = 0; cbMatrixCol < M_SIZE; cbMatrixCol++) {
                final JCheckBox checkbox = new JCheckBox("", false);
                checkbox.setMargin(new Insets(0, 0, 0, 0));
                checkbox.setMinimumSize(
                        new Dimension(basisElementWidth, basisElementHeight));
                checkbox.setMaximumSize(
                        new Dimension(basisElementWidth, basisElementHeight));
                checkbox.setPreferredSize(
                        new Dimension(basisElementWidth, basisElementHeight));
                checkbox.setIcon(inactive);
                checkbox.setSelectedIcon(activeIcon);
                checkbox.setRolloverIcon(about);
                checkbox.setDisabledIcon(off);
                checkbox.addActionListener(this);
                checkbox.setActionCommand(
                        "element" + cbMatrixRow + "," + cbMatrixCol);
                // ActionCommand with coordinates of the checkbox
                checkboxMatrix[cbMatrixRow][cbMatrixCol] = checkbox;
                activationCounter[cbMatrixRow][cbMatrixCol] = 0;
                matrixPanel.add(checkboxMatrix[cbMatrixRow][cbMatrixCol]);
            }
        }
        // contente der Panels erzeugen
        final JLabel name = new JLabel(
                textbundle.getString("dialog_elementObjectNew_label_1") + ": ");
        elementName = new JTextField();
        final JLabel stabilityLabel = new JLabel(
                textbundle.getString("dialog_elementObjectNew_label_2") + ": ");
        inputStability = new JTextField();
        final JLabel costsLabel = new JLabel(
                textbundle.getString("dialog_elementObjectNew_label_3") + ": ");
        inputCosts = new JTextField();
        valuesPanel.add(name);
        valuesPanel.add(elementName);
        valuesPanel.add(stabilityLabel);
        valuesPanel.add(inputStability);
        valuesPanel.add(costsLabel);
        valuesPanel.add(inputCosts);
        ok = new JButton(textbundle.getString("button_ok"));
        ok.setEnabled(false);
        ok.setActionCommand("ok");
        ok.addActionListener(this);
        final JButton cancelButton = new JButton(
                textbundle.getString("button_cancel"));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        final JButton undoAllButton = new JButton(
                textbundle.getString("dialog_elementObjectNew_button_1"));
        undoAllButton.setActionCommand("clear");
        undoAllButton.addActionListener(this);
        final JButton undoButton = new JButton(
                textbundle.getString("dialog_elementObjectNew_button_2"));
        undoButton.setActionCommand("undo");
        undoButton.addActionListener(this);
        activateCheckboxMatrix();
        // dialog
        buttonsPanel.add(ok);
        buttonsPanel.add(cancelButton);
        clearPanel.add(undoAllButton);
        clearPanel.add(undoButton);
        inputPanel.add(valuesPanel, BorderLayout.NORTH);
        inputPanel.add(matrix2Panel, BorderLayout.CENTER);
        inputPanel.add(clearPanel, BorderLayout.SOUTH);
        matrix2Panel.add(matrixPanel);
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(contentPanel);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Initialise the matrix (enabled = true / selected = false).
     *
     * @author Adrian Schuetz
     */
    private void activateCheckboxMatrix() {
        for (int cbMatrixRow = 0; cbMatrixRow < M_SIZE; cbMatrixRow++) {
            for (int cbMatrixCol = 0; cbMatrixCol < M_SIZE; cbMatrixCol++) {
                checkboxMatrix[cbMatrixRow][cbMatrixCol].setEnabled(true);
                checkboxMatrix[cbMatrixRow][cbMatrixCol].setSelected(false);
                activationCounter[cbMatrixRow][cbMatrixCol] = 0;
            }
        }
    }

    /**
     * Deactivate all fields (Enabled = false).
     *
     * @author Adrian Schuetz
     */
    private void deactivateCheckboxMatrix() {
        for (int cbMatrixRow = 0; cbMatrixRow < M_SIZE; cbMatrixRow++) {
            for (int cbMatrixCol = 0; cbMatrixCol < M_SIZE; cbMatrixCol++) {
                checkboxMatrix[cbMatrixRow][cbMatrixCol].setEnabled(false);
            }
        }
    }

    /**
     * Checks if dialog is canceled.
     *
     * @author Adrian Schuetz
     * @return <code>true</code> if dialog is canceled.
     */
    public boolean isCanceled() {
        return this.cancel;
    }

    /**
     * Returns the matrix of the element.
     *
     * @author Adrian Schuetz
     * @return elementMatrix
     */
    public boolean[][] getElement() {
        return elementMatrix;
    }

    /**
     * Returns the element name.
     *
     * @author Adrian Schuetz
     * @return String elementName
     */
    public String getElementName() {
        return this.elementName.getText();
    }

    /**
     * Returns the stability.
     *
     * @author Adrian Schuetz
     * @return stability
     */
    public int getStability() {
        return Integer.parseInt(this.inputStability.getText());
    }

    /**
     * Returns the costs.
     *
     * @author Adrian Schuetz
     * @return costs
     */
    public int getCosts() {
        return Integer.parseInt(this.inputCosts.getText());
    }

    /**
     * Returns element width.
     *
     * @author Adrian Schuetz
     * @return width
     */
    public int getElementWidth() {
        return (this.right - this.left) + 1;
    }

    /**
     * Returns element hieght.
     *
     * @author Adrian Schuetz
     * @return height
     */
    public int getElementHeight() {
        return (this.bottom - this.top) + 1;
    }

    /**
     * Checks if input is valid.
     *
     * @author Adrian Schuetz
     * @return <code>true</code> if input is valid.
     */
    private boolean isInputValid() {
        error = "";

        if (elementName.getText().length() == 0) {
            error = error.concat(
                    textbundle.getString("dialog_elementObjectNew_error_1")
                            + "\n");
        }

        if (inputStability.getText().length() == 0) {
            error = error.concat(
                    textbundle.getString("dialog_elementObjectNew_error_2")
                            + "\n");
        } else {
            try {
                if (Integer.parseInt(inputStability.getText()) <= 0) {
                    error = error.concat(textbundle.getString(
                            "dialog_elementObjectNew_error_3") + "\n");
                }
            } catch (final NumberFormatException e) {
                error = error.concat(
                        textbundle.getString("dialog_elementObjectNew_error_4")
                                + "\n");
            }
        }

        if (inputCosts.getText().length() == 0) {
            error = error.concat(
                    textbundle.getString("dialog_elementObjectNew_error_5")
                            + "\n");
        } else {
            try {
                if (Integer.parseInt(inputCosts.getText()) <= 0) {
                    error = error.concat(textbundle.getString(
                            "dialog_elementObjectNew_error_6") + "\n");
                }
            } catch (final NumberFormatException e) {
                error = error.concat(
                        textbundle.getString("dialog_elementObjectNew_error_7")
                                + "\n");
            }
        }

        if (!(error.equals(""))) {
            return false;
        }

        return true;
    }

    /**
     * Sets the area for the elementMatrix.
     *
     * @author Adrian Schuetz
     * @param row
     * @param column
     */
    private void setArea(final int row, final int column) {
        if (this.checkboxMatrix[row][column].isSelected()) {
            if (row < this.top) {
                this.top = row;
            }

            if (row > this.bottom) {
                this.bottom = row;
            }

            if (column < this.left) {
                this.left = column;
            }

            if (column > this.right) {
                this.right = column;
            }
        } else {
            // reset values
            this.top = DEFAULT_TOP;
            this.bottom = 0;
            this.left = DEFAULT_LEFT;
            this.right = 0;

            for (int rown = 0; rown < M_SIZE; rown++) {
                for (int columnn = 0; columnn < M_SIZE; columnn++) {
                    if (this.checkboxMatrix[rown][columnn].isSelected()) {
                        // if true; set values
                        if (columnn < this.left) {
                            this.left = columnn;
                        }

                        if (columnn > this.right) {
                            this.right = columnn;
                        }

                        if (rown < this.top) {
                            this.top = rown;
                        }

                        if (rown > this.bottom) {
                            this.bottom = rown;
                        }
                    }
                }
            }
        }
    }

    /**
     * Shows dialog.
     *
     * @author Adrian Schuetz
     */
    public void showDialog() {
        this.setVisible(true);
    }

    /**
     * Generates the elementMatrix from the checkboxMatrix.
     *
     * @author Adrian Schuetz
     */
    private void matrixCutout() {
        final int width = (this.right - this.left) + 1;
        final int height = (this.bottom - this.top) + 1;
        this.elementMatrix = new boolean[height][width];

        for (int row = this.top, h = 0; row <= this.bottom; row++, h++) {
            for (int column = this.left,
                    b = 0; column <= this.right; column++, b++) {
                this.elementMatrix[h][b] = this.checkboxMatrix[row][column]
                        .isSelected();
            }
        }
    }

    /**
     * Checks the surrounding checkboxes and activates them.
     *
     * @author Adrian Schuetz
     * @param cbMatrixRow
     * @param cbMatrixCol
     */
    private void activateCheckboxes(final int cbMatrixRow,
            final int cbMatrixCol) {
        // left
        if (cbMatrixCol == 0) {
            if (cbMatrixRow == 0) {
                // top
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_TOP) {
                // midle
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow == DEFAULT_TOP) {
                // bottom
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
            }
        } else if (cbMatrixCol > 0 && cbMatrixCol < DEFAULT_LEFT) {
            // midle
            if (cbMatrixRow == 0) {
                // top
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_LEFT) {
                // midle
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow == DEFAULT_TOP) {
                // bottom
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow][cbMatrixCol + 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
            }
        } else if (cbMatrixCol == DEFAULT_LEFT) {
            // right
            if (cbMatrixRow == 0) {
                // top
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_LEFT) {
                // midle
                checkboxMatrix[cbMatrixRow + 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] + 1;
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
            } else if (cbMatrixRow == DEFAULT_TOP) {
                // bottom
                checkboxMatrix[cbMatrixRow][cbMatrixCol - 1].setEnabled(true);
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                + 1;
                checkboxMatrix[cbMatrixRow - 1][cbMatrixCol].setEnabled(true);
                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] + 1;
            }
        }
    }

    /**
     * Xhecks the surrounding checkboxes and deactivates them.
     *
     * @author Adrian Schuetz
     * @param cbMatrixRow
     * @param cbMatrixCol
     */
    private void deactivateCheckboxes(final int cbMatrixRow,
            final int cbMatrixCol) {
        // left
        if (cbMatrixCol == 0) {
            // top
            if (cbMatrixRow == 0) {
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_LEFT) {
                // midle
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow == DEFAULT_LEFT) {
                // bottom
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }
            }
        } else if (cbMatrixCol > 0 && cbMatrixCol < DEFAULT_LEFT) {
            // midle
            if (cbMatrixRow == 0) {
                // top
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_TOP) {
                // midle
                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow == DEFAULT_TOP) {
                // bottom
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow][cbMatrixCol
                        + 1] = activationCounter[cbMatrixRow][cbMatrixCol + 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol + 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol + 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;
                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }
            }
        } else if (cbMatrixCol == DEFAULT_LEFT) {
            // right
            if (cbMatrixRow == 0) {
                // top
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow > 0 && cbMatrixRow < DEFAULT_TOP) {
                // midle
                activationCounter[cbMatrixRow
                        + 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                + 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow + 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow + 1][cbMatrixCol]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }
            } else if (cbMatrixRow == DEFAULT_TOP) {
                // bottom
                activationCounter[cbMatrixRow][cbMatrixCol
                        - 1] = activationCounter[cbMatrixRow][cbMatrixCol - 1]
                                - 1;

                if (activationCounter[cbMatrixRow][cbMatrixCol - 1] == 0) {
                    checkboxMatrix[cbMatrixRow][cbMatrixCol - 1]
                            .setEnabled(false);
                }

                activationCounter[cbMatrixRow
                        - 1][cbMatrixCol] = activationCounter[cbMatrixRow
                                - 1][cbMatrixCol] - 1;

                if (activationCounter[cbMatrixRow - 1][cbMatrixCol] == 0) {
                    checkboxMatrix[cbMatrixRow - 1][cbMatrixCol]
                            .setEnabled(false);
                }
            }
        }
    }

    /**
     * ActionListener.
     *
     * @author Adrian Schuetz
     * @param event
     */
    public void actionPerformed(final ActionEvent event) {
        final String actionCommand = event.getActionCommand();

        if (actionCommand.equals("cancel")) {
            this.cancel = true;
            this.setVisible(false);
        }

        if (actionCommand.equals("ok")) {
            if (isInputValid()) {
                matrixCutout();
                this.cancel = false;
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, error,
                        textbundle.getString("dialog_error_frame"),
                        JOptionPane.ERROR_MESSAGE);
                error = "";
            }
        }

        if (actionCommand.contains("element")) {
            // get coordinates
            final String[] coordinates = actionCommand
                    .substring(ELEMENT_BEGIN_INDEX).split(",");
            final int checkboxMatrixRow = Integer.parseInt(coordinates[0]);
            final int checkboxMatrixColumn = Integer.parseInt(coordinates[1]);

            if (checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn]
                    .isSelected()) {
                if (active.size() == 0) {
                    deactivateCheckboxMatrix();
                    checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn]
                            .setEnabled(true);
                    checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn]
                            .setSelected(true);
                }

                setArea(checkboxMatrixRow, checkboxMatrixColumn);
                activateCheckboxes(checkboxMatrixRow, checkboxMatrixColumn);
                active.addElement(checkboxMatrixRow);
                active.addElement(checkboxMatrixColumn);
            } else {
                checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn]
                        .setSelected(true);
            }

            if (active.size() == MAX_ACTIVE) {
                ok.setEnabled(true);
            }
        }

        if (actionCommand.equals("undo")) {
            // last element
            if (active.size() == 2) {
                active = new Vector<>();
                activateCheckboxMatrix();
                // before last element
            } else if (active.size() == MAX_ACTIVE) {
                ok.setEnabled(false);
                final int column = (Integer) active.lastElement();
                active.removeElementAt(active.size() - 1);
                final int row = (Integer) active.lastElement();
                active.removeElementAt(active.size() - 1);
                checkboxMatrix[row][column].setSelected(false);
                deactivateCheckboxes(row, column);
                setArea(row, column);

                if (row < DEFAULT_TOP
                        && checkboxMatrix[row + 1][column].isSelected()) {
                    checkboxMatrix[row + 1][column].setEnabled(true);
                } else if (row > 0
                        && checkboxMatrix[row - 1][column].isSelected()) {
                    checkboxMatrix[row - 1][column].setEnabled(true);
                } else if (column < DEFAULT_LEFT
                        && checkboxMatrix[row][column + 1].isSelected()) {
                    checkboxMatrix[row][column + 1].setEnabled(true);
                } else if (column > 0
                        && checkboxMatrix[row][column - 1].isSelected()) {
                    checkboxMatrix[row][column - 1].setEnabled(true);
                }
                // all other elements
            } else if (active.size() > MAX_ACTIVE) {
                final int column = (Integer) active.lastElement();
                active.removeElementAt(active.size() - 1);
                final int row = (Integer) active.lastElement();
                active.removeElementAt(active.size() - 1);
                checkboxMatrix[row][column].setSelected(false);
                deactivateCheckboxes(row, column);
                setArea(row, column);
            }
        }

        if (actionCommand.equals("clear")) {
            ok.setEnabled(false);
            active = new Vector<>();
            activateCheckboxMatrix();
            // reset to start
            this.top = DEFAULT_TOP;
            this.bottom = 0;
            this.left = DEFAULT_LEFT;
            this.right = 0;
        }
    }
}
