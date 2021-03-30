package com.javeriana.edu.co;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class GestorCarga {

    public static void main(String[] args) throws InterruptedException {
        String tipoSolicitud = "";
        String[] mensajes;
        String mensaje;
        int id = 0;
        String arg1 = "";
        String arg2 = "";
        try (ZContext contextClient = new ZContext()) {
            System.out.println("Gestor start");
            ZMQ.Socket socket = contextClient.createSocket(SocketType.REP);
            socket.bind("tcp://*:6000");

            ZMQ.Socket publisher = contextClient.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5556");
            publisher.bind("ipc://PROCESO");

            while (!Thread.currentThread().isInterrupted()) {
                /**
                 * Conectar con cliente
                 */
                tipoSolicitud = "NINGUNA";
                byte[] reply = socket.recv(0);
                System.out.println("Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]");
                mensaje = new String(reply, ZMQ.CHARSET);
                mensajes = mensaje.split(" ");
                if (mensajes.length == 4) {
                    tipoSolicitud = mensajes[0];
                    id = Integer.valueOf(mensajes[1]);
                    arg1 = mensajes[2];
                    arg2 = mensajes[3];
                } else if (mensajes.length == 3) {
                    tipoSolicitud = mensajes[0];
                    id = Integer.valueOf(mensajes[1]);
                    arg1 = mensajes[2];
                }
                if (tipoSolicitud != "SOLICITAR") {
                    String response = "PETICION DE " + tipoSolicitud + " ACEPTADA";
                    socket.send(response.getBytes(ZMQ.CHARSET), 0);
                }

                /**
                 * Conectar con Actor
                 */
                if (tipoSolicitud.equals("RENOVAR")) {

                    int codigoTopico = 10000;
                    String enviar = arg1 + " " + arg2;
                    String update = String.format("%d %d %s", codigoTopico, id, enviar);
                    Hilo h = new Hilo("renovar");
                    h.run(publisher, update);

                } else if (tipoSolicitud.equals("DEVOLVER")) {

                    int codigoTopico = 10001;
                    String enviar = arg1;
                    String update = String.format("%d %d %s", codigoTopico, id, enviar);
                    Hilo h = new Hilo("devolver");
                    h.run(publisher, update);

                } else if (tipoSolicitud.equals("SOLICITAR")) {
                    int codigoTopico = 10002;
                    String enviar = arg1;
                    String update = String.format("%d %d %s", codigoTopico, id, enviar);
                    Hilo h = new Hilo("solicitar");
                    h.run(publisher, update);
                } else {
                    Thread.sleep(1000);
                }

            }

        }

    }
}