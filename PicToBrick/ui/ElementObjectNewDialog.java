package PicToBrick.ui;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * class: ElementObjectNewDialog
 * layer: Gui (three tier architecture)
 * description: new element dialog
 *
 * @author Adrian Schuetz
 */
public class ElementObjectNewDialog
		extends JDialog
		implements ActionListener {
	private static ResourceBundle textbundle = ResourceBundle.getBundle("Resources.TextResource");
	private boolean cancel = true;
	private String error = "";
	private final JTextField elementName, inputStability, inputCosts;
	private boolean[][] elementMatrix;
	private final JCheckBox[][] checkboxMatrix = new JCheckBox[8][8];
	private final int[][] activationCounter = new int[8][8];
	private Vector<Integer> active = new Vector<>();
	private int top = 7;
	private int bottom = 0;
	private int left = 7;
	private int right = 0;
	private int basisElementWidth = 0;
	private int basisElementHeight = 0;
	private final JButton ok;

	/**
	 * method: ElementObjectNewDialog
	 * description: constructor
	 *
	 * @author Adrian Schuetz
	 * @param owner
	 */
	public ElementObjectNewDialog(final JDialog owner, final int ratioWidth, final int ratioHeight) {
		super(owner, textbundle.getString("dialog_elementObjectNew_frame"), true);
		final double width = ratioWidth;
		final double height = ratioHeight;

		if (width > height) {
			this.basisElementHeight = (int) ((height / height) * 15);
			this.basisElementWidth = (int) ((width / height) * 15);
		} else {
			this.basisElementWidth = (int) ((width / width) * 15);
			this.basisElementHeight = (int) ((height / width) * 15);
		}

		// Images for checkboxes
		final BufferedImage white = new BufferedImage((basisElementWidth - 2), (basisElementHeight - 2),
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2dWhite = white.createGraphics();
		g2dWhite.setColor(Color.WHITE);
		g2dWhite.fillRect(0, 0, (basisElementWidth - 2), (basisElementHeight - 2));
		final BufferedImage black = new BufferedImage((basisElementWidth - 2), (basisElementHeight - 2),
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2dBlack = black.createGraphics();
		g2dBlack.setColor(Color.BLACK);
		g2dBlack.fillRect(0, 0, (basisElementWidth - 2), (basisElementHeight - 2));
		final BufferedImage blue = new BufferedImage((basisElementWidth - 2), (basisElementHeight - 2),
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2dBlue = blue.createGraphics();
		g2dBlue.setColor(new Color(0, 0, 150));
		g2dBlue.fillRect(0, 0, (basisElementWidth - 2), (basisElementHeight - 2));
		final BufferedImage gray = new BufferedImage((basisElementWidth - 2), (basisElementHeight - 2),
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2dGray = gray.createGraphics();
		g2dGray.setColor(Color.LIGHT_GRAY);
		g2dGray.fillRect(0, 0, (basisElementWidth - 2), (basisElementHeight - 2));
		// Icons
		final Icon active = new ImageIcon(black);
		final Icon inactive = new ImageIcon(white);
		final Icon about = new ImageIcon(blue);
		final Icon off = new ImageIcon(gray);
		this.setLocation(600, 100);
		this.setResizable(false);
		final JPanel contentPanel = new JPanel(new BorderLayout());
		final JPanel inputPanel = new JPanel(new BorderLayout());
		final JPanel valuesPanel = new JPanel(new GridLayout(3, 2));
		final JPanel matrixPanel = new JPanel(new GridLayout(8, 8, 0, 0));
		final JPanel clearPanel = new JPanel();
		final JPanel matrix2Panel = new JPanel();
		final JPanel buttonsPanel = new JPanel();
		// Border for Panels
		final TitledBorder valuesBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_elementObjectNew_border_1"));
		valuesPanel.setBorder(valuesBorder);
		valuesBorder.setTitleColor(new Color(100, 100, 100));
		final TitledBorder matrixBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_elementObjectNew_border_2"));
		matrix2Panel.setBorder(matrixBorder);
		matrixBorder.setTitleColor(new Color(100, 100, 100));
		final TitledBorder clearBorder = BorderFactory
				.createTitledBorder(textbundle.getString("dialog_elementObjectNew_border_3"));
		clearPanel.setBorder(clearBorder);
		clearBorder.setTitleColor(new Color(100, 100, 100));
		// 8x8 checkboxMatrix
		for (int checkboxMatrixRow = 0; checkboxMatrixRow < 8; checkboxMatrixRow++) {
			for (int checkboxMatrixColumn = 0; checkboxMatrixColumn < 8; checkboxMatrixColumn++) {
				final JCheckBox checkbox = new JCheckBox("", false);
				checkbox.setMargin(new Insets(0, 0, 0, 0));
				checkbox.setMinimumSize(new Dimension(basisElementWidth, basisElementHeight));
				checkbox.setMaximumSize(new Dimension(basisElementWidth, basisElementHeight));
				checkbox.setPreferredSize(new Dimension(basisElementWidth, basisElementHeight));
				checkbox.setIcon(inactive);
				checkbox.setSelectedIcon(active);
				checkbox.setRolloverIcon(about);
				checkbox.setDisabledIcon(off);
				checkbox.addActionListener(this);
				checkbox.setActionCommand("element" + checkboxMatrixRow + "," + checkboxMatrixColumn);
				// ActionCommand with coordinates of the checkbox
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn] = checkbox;
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn] = 0;
				matrixPanel.add(checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn]);
			}
		}
		// contente der Panels erzeugen
		final JLabel name = new JLabel(textbundle.getString("dialog_elementObjectNew_label_1") + ": ");
		elementName = new JTextField();
		final JLabel stabilityLabel = new JLabel(textbundle.getString("dialog_elementObjectNew_label_2") + ": ");
		inputStability = new JTextField();
		final JLabel costsLabel = new JLabel(textbundle.getString("dialog_elementObjectNew_label_3") + ": ");
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
		final JButton cancelButton = new JButton(textbundle.getString("button_cancel"));
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		final JButton undoAllButton = new JButton(textbundle.getString("dialog_elementObjectNew_button_1"));
		undoAllButton.setActionCommand("clear");
		undoAllButton.addActionListener(this);
		final JButton undoButton = new JButton(textbundle.getString("dialog_elementObjectNew_button_2"));
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
	 * method: activateCheckboxMatrix
	 * description: Initialise the matrix (enabled = true / selected = false)
	 *
	 * @author Adrian Schuetz
	 */
	private void activateCheckboxMatrix() {
		for (int checkboxMatrixRow = 0; checkboxMatrixRow < 8; checkboxMatrixRow++) {
			for (int checkboxMatrixColumn = 0; checkboxMatrixColumn < 8; checkboxMatrixColumn++) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setEnabled(true);
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setSelected(false);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn] = 0;
			}
		}
	}

	/**
	 * method: deactivateCheckboxMatrix
	 * description: Deaktivate all Fields (Enabled = false)
	 *
	 * @author Adrian Schuetz
	 */
	private void deactivateCheckboxMatrix() {
		for (int checkboxMatrixRow = 0; checkboxMatrixRow < 8; checkboxMatrixRow++) {
			for (int checkboxMatrixColumn = 0; checkboxMatrixColumn < 8; checkboxMatrixColumn++) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setEnabled(false);
			}
		}
	}

	/**
	 * method: isCanceled
	 * description: Checks if dialog is canceled
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	public boolean isCanceled() {
		return this.cancel;
	}

	/**
	 * method: getElement
	 * description: returns the matrix of the element
	 *
	 * @author Adrian Schuetz
	 * @return elementMatrix
	 */
	public boolean[][] getElement() {
		return elementMatrix;
	}

	/**
	 * method: getElementName
	 * description: returns the element name
	 *
	 * @author Adrian Schuetz
	 * @return String elementName
	 */
	public String getElementName() {
		return this.elementName.getText();
	}

	/**
	 * method: getStability
	 * description: returns the stability
	 *
	 * @author Adrian Schuetz
	 * @return stability
	 */
	public int getStability() {
		return Integer.parseInt(this.inputStability.getText());
	}

	/**
	 * method: getCosts
	 * description: returns the costs
	 *
	 * @author Adrian Schuetz
	 * @return costs
	 */
	public int getCosts() {
		return Integer.parseInt(this.inputCosts.getText());
	}

	/**
	 * method: getElementWidth
	 * description: returns element width
	 *
	 * @author Adrian Schuetz
	 * @return width
	 */
	public int getElementWidth() {
		return (this.right - this.left) + 1;
	}

	/**
	 * method: getElementHeight
	 * description: returns element hieght
	 *
	 * @author Adrian Schuetz
	 * @return height
	 */
	public int getElementHeight() {
		return (this.bottom - this.top) + 1;
	}

	/**
	 * method: isInputValid
	 * description: checks if input is valid
	 *
	 * @author Adrian Schuetz
	 * @return true or false
	 */
	private boolean isInputValid() {
		error = new String("");

		if (elementName.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_elementObjectNew_error_1") + "\n");
		}

		if (inputStability.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_elementObjectNew_error_2") + "\n");
		} else {
			try {
				if (Integer.parseInt(inputStability.getText()) <= 0) {
					error = error.concat(textbundle.getString("dialog_elementObjectNew_error_3") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_elementObjectNew_error_4") + "\n");
			}
		}

		if (inputCosts.getText().length() == 0) {
			error = error.concat(textbundle.getString("dialog_elementObjectNew_error_5") + "\n");
		} else {
			try {
				if (Integer.parseInt(inputCosts.getText()) <= 0) {
					error = error.concat(textbundle.getString("dialog_elementObjectNew_error_6") + "\n");
				}
			} catch (final NumberFormatException e) {
				error = error.concat(textbundle.getString("dialog_elementObjectNew_error_7") + "\n");
			}
		}

		if (!(error.equals(""))) {
			return false;
		}
		return true;
	}

	/**
	 * method: setArea
	 * description: sets the area for the elementMatrix
	 *
	 * @author Adrian Schuetz
	 * @param row
	 * @param column
	 */
	private void setArea(final int row, final int column) {
		if (this.checkboxMatrix[row][column].isSelected() == true) {
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
			this.top = 7;
			this.bottom = 0;
			this.left = 7;
			this.right = 0;
			for (int rown = 0; rown < 8; rown++) {
				for (int columnn = 0; columnn < 8; columnn++) {
					if (this.checkboxMatrix[rown][columnn].isSelected() == true) {
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
	 * method: showDialog
	 * description: shows dialog
	 *
	 * @author Adrian Schuetz
	 */
	public void showDialog() {
		this.setVisible(true);
	}

	/**
	 * method: matrixCutout
	 * description: generates the elementMatrix from the checkboxMatrix
	 *
	 * @author Adrian Schuetz
	 */
	private void matrixCutout() {
		final int width = (this.right - this.left) + 1;
		final int height = (this.bottom - this.top) + 1;
		this.elementMatrix = new boolean[height][width];
		for (int row = this.top, h = 0; row <= this.bottom; row++, h++) {
			for (int column = this.left, b = 0; column <= this.right; column++, b++) {
				this.elementMatrix[h][b] = this.checkboxMatrix[row][column].isSelected();
			}
		}
	}

	/**
	 * method: activateCheckboxes
	 * description: checks the surrounding checkboxes and activates them
	 *
	 * @author Adrian Schuetz
	 * @param checkboxMatrixRow
	 * @param checkboxMatrixColumn
	 */
	private void activateCheckboxes(final int checkboxMatrixRow, final int checkboxMatrixColumn) {
		// left
		if (checkboxMatrixColumn == 0) {
			// top
			if (checkboxMatrixRow == 0) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
			}
		}
		// midle
		else if (checkboxMatrixColumn > 0 && checkboxMatrixColumn < 7) {
			// top
			if (checkboxMatrixRow == 0) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
			}
		}
		// right
		else if (checkboxMatrixColumn == 7) {
			// top
			if (checkboxMatrixRow == 0) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] + 1;
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(true);
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] + 1;
				checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(true);
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] + 1;
			}
		}
	}

	/**
	 * method: deactivateCheckboxes
	 * description: checks the surrounding checkboxes and deactivates them
	 *
	 * @author Adrian Schuetz
	 * @param checkboxMatrixRow
	 * @param checkboxMatrixColumn
	 */
	private void deactivateCheckboxes(final int checkboxMatrixRow, final int checkboxMatrixColumn) {
		// left
		if (checkboxMatrixColumn == 0) {
			// top
			if (checkboxMatrixRow == 0) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
		}
		// midle
		else if (checkboxMatrixColumn > 0 && checkboxMatrixColumn < 7) {
			// top
			if (checkboxMatrixRow == 0) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						+ 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn + 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn + 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
		}
		// right
		else if (checkboxMatrixColumn == 7) {
			// top
			if (checkboxMatrixRow == 0) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
			// midle
			else if (checkboxMatrixRow > 0 && checkboxMatrixRow < 7) {
				activationCounter[checkboxMatrixRow
						+ 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow + 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow + 1][checkboxMatrixColumn].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
			// bottom
			else if (checkboxMatrixRow == 7) {
				activationCounter[checkboxMatrixRow][checkboxMatrixColumn
						- 1] = activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] - 1;
				if (activationCounter[checkboxMatrixRow][checkboxMatrixColumn - 1] == 0) {
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn - 1].setEnabled(false);
				}
				activationCounter[checkboxMatrixRow
						- 1][checkboxMatrixColumn] = activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] - 1;
				if (activationCounter[checkboxMatrixRow - 1][checkboxMatrixColumn] == 0) {
					checkboxMatrix[checkboxMatrixRow - 1][checkboxMatrixColumn].setEnabled(false);
				}
			}
		}
	}

	/**
	 * method: actionPerformed
	 * description: ActionListener
	 *
	 * @author Adrian Schuetz
	 * @param event
	 */
	public void actionPerformed(final ActionEvent event) {
		if (event.getActionCommand().equals("cancel")) {
			this.cancel = true;
			this.setVisible(false);
		}
		if (event.getActionCommand().equals("ok")) {
			if (isInputValid()) {
				matrixCutout();
				this.cancel = false;
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, error, textbundle.getString("dialog_error_frame"),
						JOptionPane.ERROR_MESSAGE);
				error = "";
			}
		}
		if (event.getActionCommand().contains("element")) {
			// get coordinates
			final String[] coordinates = event.getActionCommand().substring(7).split(",");
			final int checkboxMatrixRow = Integer.parseInt(coordinates[0]);
			final int checkboxMatrixColumn = Integer.parseInt(coordinates[1]);

			if (checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].isSelected()) {
				if (active.size() == 0) {
					deactivateCheckboxMatrix();
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setEnabled(true);
					checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setSelected(true);
				}

				setArea(checkboxMatrixRow, checkboxMatrixColumn);
				activateCheckboxes(checkboxMatrixRow, checkboxMatrixColumn);
				active.addElement(checkboxMatrixRow);
				active.addElement(checkboxMatrixColumn);
			} else {
				checkboxMatrix[checkboxMatrixRow][checkboxMatrixColumn].setSelected(true);
			}

			if (active.size() == 4) {
				ok.setEnabled(true);
			}
		}

		if (event.getActionCommand().equals("undo")) {
			// last element
			if (active.size() == 2) {
				active = new Vector<>();
				activateCheckboxMatrix();
				// before last element
			} else if (active.size() == 4) {
				ok.setEnabled(false);
				final int column = (Integer) active.lastElement();
				active.removeElementAt(active.size() - 1);
				final int row = (Integer) active.lastElement();
				active.removeElementAt(active.size() - 1);
				checkboxMatrix[row][column].setSelected(false);
				deactivateCheckboxes(row, column);
				setArea(row, column);

				if (row < 7 && checkboxMatrix[row + 1][column].isSelected()) {
					checkboxMatrix[row + 1][column].setEnabled(true);
				} else if (row > 0 && checkboxMatrix[row - 1][column].isSelected()) {
					checkboxMatrix[row - 1][column].setEnabled(true);
				} else if (column < 7 && checkboxMatrix[row][column + 1].isSelected()) {
					checkboxMatrix[row][column + 1].setEnabled(true);
				} else if (column > 0 && checkboxMatrix[row][column - 1].isSelected()) {
					checkboxMatrix[row][column - 1].setEnabled(true);
				}
				// all other elements
			} else if (active.size() > 4) {
				final int column = (Integer) active.lastElement();
				active.removeElementAt(active.size() - 1);
				final int row = (Integer) active.lastElement();
				active.removeElementAt(active.size() - 1);
				checkboxMatrix[row][column].setSelected(false);
				deactivateCheckboxes(row, column);
				setArea(row, column);
			}
		}

		if (event.getActionCommand().equals("clear")) {
			ok.setEnabled(false);
			active = new Vector<>();
			activateCheckboxMatrix();
			// reset to start
			this.top = 7;
			this.bottom = 0;
			this.left = 7;
			this.right = 0;
		}
	}
}
