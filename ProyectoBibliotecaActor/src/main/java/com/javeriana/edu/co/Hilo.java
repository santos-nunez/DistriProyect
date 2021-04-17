package com.javeriana.edu.co;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;

public class Hilo extends Thread {
    private ZMQ.Socket socket;
    private String tipoSolicitud;

    public Hilo(String name, ZMQ.Socket socket, String tipoSolicitud) {
        super(name);
        this.socket = socket;
        this.tipoSolicitud = tipoSolicitud;
    }

    public void run() {

        String string, mensaje1 = "", mensaje2 = "", mensaje3 = "";
        StringTokenizer sscanf;
        int codigo = -10;
        Queue<String> cola = new LinkedList<>();
        SubHilo subh = new SubHilo("", "", "", "", "");

        do {
            // System.out.println("Recibiendo en hilo " + this.getName());
            string = socket.recvStr(0).trim();
            cola.add(string);
            while (cola.size() > 0) {

                sscanf = new StringTokenizer(cola.peek(), " ");
                codigo = Integer.valueOf(sscanf.nextToken());
                mensaje1 = sscanf.nextToken().toString();
                if (tipoSolicitud == "RENOVAR" && !subh.isAlive()) {
                    mensaje2 = sscanf.nextToken().toString();
                    mensaje3 = sscanf.nextToken().toString();
                    subh = new SubHilo("SubHiloRenovar", tipoSolicitud, mensaje1, mensaje2, mensaje3);
                    subh.start();
                    System.out.println(
                            "Received " + " :  [" + codigo + " " + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
                    cola.poll();
                } else if (tipoSolicitud == "DEVOLVER" && !subh.isAlive()) {
                    subh = new SubHilo("SubHiloDevolver", tipoSolicitud, mensaje1, "", "");
                    subh.start();
                    System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + "]");
                    cola.poll();
                }
            }

        } while (subh.isAlive());

    }

}
