package com.javeriana.edu.co;

import org.zeromq.ZMQ;

/**
 * Hello world!
 *
 */
public class Hilo extends Thread {
    private ZMQ.Socket publisher;
    private String update;

    public Hilo(String name, ZMQ.Socket publisher, String update) {
        super(name);
        this.publisher = publisher;
        this.update = update;
    }

    public void run() {

        for (int i = 0; i < 10; i++) {
            System.out.println("Send " + ": [" + update + "]");
            publisher.send(update, 0);
        }
    }

}
