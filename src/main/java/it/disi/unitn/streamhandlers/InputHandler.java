package it.disi.unitn.streamhandlers;

import java.io.InputStream;

public class InputHandler extends Thread {
    InputStream input_;

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
