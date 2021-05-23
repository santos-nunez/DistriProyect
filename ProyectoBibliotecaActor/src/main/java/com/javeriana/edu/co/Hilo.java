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
        SubHilo subh = new SubHilo("", "", "", "", "");
        String string, mensaje1 = "", mensaje2 = "", mensaje3 = "";
        StringTokenizer sscanf;
        while (!Thread.currentThread().isInterrupted()) {
            /**
             * SE RECIBE DEL OTRO ACTOR
             */
            string = socket.recvStr(0).trim();
            sscanf = new StringTokenizer(string, " ");
            int codigo = Integer.valueOf(sscanf.nextToken());
            if (codigo == 10001) {
                if (!subh.isAlive()) {
                    mensaje1 = sscanf.nextToken().toString();
                    subh = new SubHilo("SubHiloDevolver", "DEVOLVER", mensaje1, "", "");
                    subh.start();
                    System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + "]");
                }
            } else if (codigo == 10000) {
                if (!subh.isAlive()) {
                    mensaje1 = sscanf.nextToken().toString();
                    mensaje2 = sscanf.nextToken().toString();
                    mensaje3 = sscanf.nextToken().toString();
                    subh = new SubHilo("SubHiloRenovar", "RENOVAR", mensaje1, mensaje2, mensaje3);
                    subh.start();
                    System.out.println("Received " + " :  [" + codigo + " " + mensaje1 +  " " + mensaje2 +  " " + mensaje3 +"]");
                }

            }

        }
    }

}
