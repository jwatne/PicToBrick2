package PicToBrick.ui;

import java.awt.event.*;

/**
 * class:           WindowClosingAdapter
 * layer:           Gui (three tier architecture)
 * description:     creates a window closing adapter
 * @author          Tobias Reichling / Adrian Schuetz
 */

public class WindowClosingAdapter
extends WindowAdapter
{
	private boolean exitSystem;

	/**
	 * method:           WindowClosingAdapter
	 * description:      constructor
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            exitSystem
	 */
	public WindowClosingAdapter(boolean exitSystem)
	{
		this.exitSystem = exitSystem;
	}

	/**
	 * method:           WindowClosingAdapter
	 * description:      constructor
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
	public WindowClosingAdapter()
	{
		this(false);
	}

	/**
	 * method:           windowClosing
	 * description:      this method closes the window, disposes
	 *                   the window and exits the software
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            event
	 */
	public void windowClosing(WindowEvent event)
	{
		event.getWindow().setVisible(false);
		event.getWindow().dispose();
		if (exitSystem) {
			System.exit(0);
		}
	}
}
