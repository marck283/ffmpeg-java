package it.disi.unitn.streamhandlers;

import java.io.InputStream;

/**
 * This class can handle any InputStream.
 */
public class InputHandler extends Thread {
    InputStream input_;

    /**
     * The class's constructor.
     * @param input The InputStream on which to build this class
     * @param name The name of the new Thread
     */
    public InputHandler(InputStream input, String name) {
        super(name);
        input_ = input;
    }

    public void run() {
        try {
            int c;
            while ((c = input_.read()) != -1) {
                System.out.write(c);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
