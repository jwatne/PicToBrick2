package pictobrick.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Creates a window closing adapter.
 *
 * @author Tobias Reichling / Adrian Schuetz
 */

public class WindowClosingAdapter extends WindowAdapter {
    /** Indicates whether to do a system exit. */
    private boolean exitSystem;

    /**
     * Constructor.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param exit
     */
    public WindowClosingAdapter(final boolean exit) {
        this.exitSystem = exit;
    }

    /**
     * Constructor.
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public WindowClosingAdapter() {
        this(false);
    }

    /**
     * This method closes the window, disposes the window and exits the
     * software.
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param event
     */
    public void windowClosing(final WindowEvent event) {
        event.getWindow().setVisible(false);
        event.getWindow().dispose();
        if (exitSystem) {
            System.exit(0);
        }
    }
}
