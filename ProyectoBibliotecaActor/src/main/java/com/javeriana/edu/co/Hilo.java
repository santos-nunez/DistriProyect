package com.javeriana.edu.co;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;

public class Hilo extends Thread {

    private ZMQ.Socket socket;

    public Hilo(String name, ZMQ.Socket socket) {
        super(name);
        this.socket = socket;
    }

    public void run() {
        String mensaje;
        SubHilo subh = new SubHilo("", "", "", "", "");
        SubHilo subh2 = new SubHilo("", "", "", "", "");
        String string, mensaje1 = "";
        StringTokenizer sscanf;
        while (!Thread.currentThread().isInterrupted()) {
            /**
             * SE RECIBE DEL OTRO ACTOR
             */
            string = socket.recvStr(0).trim();
            sscanf = new StringTokenizer(string, " ");
            int codigo = Integer.valueOf(sscanf.nextToken());
            mensaje1 = sscanf.nextToken().toString();
            if (!subh.isAlive()) {
                subh = new SubHilo("SubHiloDevolver", "DEVOLVER", mensaje1, "", "");
                subh.start();
                System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + "]");
            }
            

        }
    }

}
