package PicToBrick.service;

import javax.swing.SwingUtilities;

/**
 * class:            SwingWorker
 * layer:            Data processing (three tier architecture)
 * description:      http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 * @author           Tobias Reichling / Adrian Schuetz
 */
public abstract class SwingWorker {
    private Object value;

	/**
	 * class:            ThreadVar
	 * description:      Class to maintain reference to current worker thread
     *                   under separate synchronization control.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }


    private ThreadVar threadVar;
	/**
	 * method:           getValue
	 * description:      Get the value produced by the worker thread, or null if it
     *                   hasn't been constructed yet.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @return           value produced by the worker thread (object)
	 */
    protected synchronized Object getValue() {
        return value;
    }


    /**
	 * method:           setValue
	 * description:      Set the value produced by worker thread
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 * @param            value produced by the worker thread (object)
	 */
    private synchronized void setValue(Object x) {
        value = x;
    }


    /**
	 * method:           construct
	 * description:      Compute the value to be returned by the get-method.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    public abstract Object construct();


    /**
	 * method:           finished
	 * description:      Called on the event dispatching thread (not on the worker thread)
     *                   after the construct-method has returned.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    public void finished() {
    }


    /**
	 * method:           interrupt
	 * description:      A new method that interrupts the worker thread. Call this method
     *                   to force the worker to stop what it's doing.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }


    /**
	 * method:           get
	 * description:      Return the value created by the construct-method.
     *                   Returns null if either the constructing thread or the current
     *                   thread was interrupted before a value was produced.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
     * @return           value created by the construct-method
     */
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }


    /**
	 * method:           SwingWorker
	 * description:      Start a thread that will call the construct-method and then exit.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };


        Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }


    /**
	 * method:           start
	 * description:      Start the worker thread.
     *                   http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
	 * @author           Tobias Reichling / Adrian Schuetz
	 */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
