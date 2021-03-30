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
            // socket.connect("tcp://192.168.0.109:6000");
            socket.connect("tcp://localHost:6000");
            // socket.connect("tcp://25.67.209.173:6000");
            /**
             * for (int requestNbr = 0; requestNbr != 10; requestNbr++) { String request =
             * "RENOVAR 12 11-11-2011 11-12-2022"; System.out.println("Sending request " +
             * requestNbr); socket.send(request.getBytes(ZMQ.CHARSET), 0);
             * 
             * byte[] reply = socket.recv(0); System.out.println("Received " + new
             * String(reply, ZMQ.CHARSET) + " " + requestNbr); }
             */
            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                String request = "DEVOLVER 14 B12";
                System.out.println("Sending request " + requestNbr);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                System.out.println("Received " + new String(reply, ZMQ.CHARSET) + " " + requestNbr);
            }
        }
    }
}
