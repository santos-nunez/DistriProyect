package com.javeriana.edu.co;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Ps {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            System.out.println("Connecting to hello world server");

            // Socket to talk to server
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            ZMQ.Socket socketServer2 = context.createSocket(SocketType.REQ);
            // socket.connect("tcp://192.168.0.109:6000");
            socket.connect("tcp://localHost:6000");
            socketServer2.connect("tcp://localHost:6000");
            // socket.connect("tcp://25.67.209.173:6000");
            Hilo h = new Hilo("requestServer1-" + "", socket, "");
            Hilo h2 = new Hilo("resquestServer2" + "", socketServer2, "");
            int requestNbr = 0;
            do {

                if (!h.isAlive()) {
                    String request = "RENOVAR 12 11-11-2011 11-12-2022";
                    System.out.println("Sending request renovar " + requestNbr);
                    h = new Hilo("requestServer1-" + requestNbr, socket, request);
                    h.start();
                    requestNbr++;
                }
                if (!h2.isAlive()) {
                    String request = "DEVOLVER 14 B12";
                    System.out.println("Sending request devolver" + requestNbr);
                    h2 = new Hilo("resquestServer2" + requestNbr, socketServer2, request);
                    h2.start();
                    requestNbr++;
                }
                if (!h.isAlive() || !h2.isAlive()) {
                    Thread.sleep(100000);
                }

            } while (h.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}