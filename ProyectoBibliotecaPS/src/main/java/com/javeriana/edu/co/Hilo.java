package com.javeriana.edu.co;

import org.zeromq.ZMQ;

/**
 * Hello world!
 *
 */
public class Hilo extends Thread {
    private ZMQ.Socket socket;
    private String update;

    public Hilo(String name, ZMQ.Socket socket, String update) {
        super(name);
        this.update = update;
        this.socket = socket;
    }

    public void run() {

        socket.send(update.getBytes(ZMQ.CHARSET), 0);

        byte[] reply = socket.recv(0);
        System.out.println("Received " + new String(reply, ZMQ.CHARSET) + " ");
    }

}