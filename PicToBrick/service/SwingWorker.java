package pictobrick.service;

import javax.swing.SwingUtilities;

/**
 * An abstract class to perform lengthy GUI-interaction tasks in a background
 * thread. This is quite different from the JDK's SwingWorker<T,V> class.
 * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
 *
 * @author Tobias Reichling / Adrian Schuetz
 */
public abstract class SwingWorker {
    /** The Object worked on by concrete implementations. */
    private Object value;

    /**
     * class: ThreadVar description: Class to maintain reference to current
     * worker thread under separate synchronization control.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    private static class ThreadVar {
        /** Current worker Thread. */
        private Thread thread;

        ThreadVar(final Thread t) {
            thread = t;
        }

        synchronized Thread get() {
            return thread;
        }

        synchronized void clear() {
            thread = null;
        }
    }

    /** Reference to current worker Thread. */
    private final ThreadVar threadVar;

    /**
     * method: getValue description: Get the value produced by the worker
     * thread, or null if it hasn't been constructed yet.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @return value produced by the worker thread (object)
     */
    protected synchronized Object getValue() {
        return value;
    }

    /**
     * Set the value produced by worker thread.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @param x produced by the worker thread (object)
     */
    private synchronized void setValue(final Object x) {
        value = x;
    }

    /**
     * method: construct description: Compute the value to be returned by the
     * get-method.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @return the value returned by the get-method.
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public abstract Object construct();

    /**
     * method: finished description: Called on the event dispatching thread (not
     * on the worker thread) after the construct-method has returned.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public void finished() {
    }

    /**
     * method: interrupt description: A new method that interrupts the worker
     * thread. Call this method to force the worker to stop what it's doing.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public void interrupt() {
        final Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
     * method: get description: Return the value created by the
     * construct-method. Returns null if either the constructing thread or the
     * current thread was interrupted before a value was produced.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     * @return value created by the construct-method
     */
    public Object get() {
        while (true) {
            final Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    /**
     * method: SwingWorker description: Start a thread that will call the
     * construct-method and then exit.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
            public void run() {
                finished();
            }
        };

        final Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        final Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
     * method: start description: Start the worker thread.
     * https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
     *
     * @author Tobias Reichling / Adrian Schuetz
     */
    public void start() {
        final Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
