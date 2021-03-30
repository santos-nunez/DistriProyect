package com.javeriana.edu.co;

import org.zeromq.ZMQ;

/**
 * Hello world!
 *
 */
public class Hilo extends Thread {
    public Hilo(String name) {
        super(name);
    }

    public void run(ZMQ.Socket publisherRenovar, String update) {

        for (int i = 0; i < 10; i++) {
            System.out.println("Send " + ": [" + update + "]");
            publisherRenovar.send(update, 0);
        }
    }

}
