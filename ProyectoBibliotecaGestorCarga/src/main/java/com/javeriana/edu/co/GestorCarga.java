package com.javeriana.edu.co;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class GestorCarga {

    public static void main(String[] args) {
        String tipoSolicitud = "VACIO";
        String[] mensajes;
        String mensaje;
        int id = 0;
        String arg1 = "";
        try (ZContext contextClient = new ZContext(); ZContext contextActor = new ZContext()) {
            System.out.println("GESTOR DE CARGA START ------");
            ZMQ.Socket socket = contextClient.createSocket(SocketType.REP);
            socket.bind("tcp://*:7000");
            ZMQ.Socket publisher = contextClient.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5557");
            publisher.bind("ipc://PROCESO");
            Hilo h = new Hilo("", publisher, "");
            Hilo h2 = new Hilo("", publisher, "");
            Queue<String> cola = new LinkedList<>();
            while (!Thread.currentThread().isInterrupted()) {
                /**
                 * Conectar con cliente
                 */
                byte[] reply = socket.recv(0);
                System.out.println("Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]");
                mensaje = new String(reply, ZMQ.CHARSET);
                mensajes = mensaje.split(" ");
                if (mensajes.length > 1) {
                    tipoSolicitud = mensajes[0];
                }
                if (!tipoSolicitud.equalsIgnoreCase("SOLICITAR") && !tipoSolicitud.equalsIgnoreCase("SOLICITAR")) {
                    String response = "PETICION DE " + tipoSolicitud + " ACEPTADA";
                    socket.send(response.getBytes(ZMQ.CHARSET), 0);
                    cola.add(mensaje);
                }
                if (tipoSolicitud.equalsIgnoreCase("SOLICITAR") && mensaje.length() > 0) {
                    ZMQ.Socket cliente = contextActor.createSocket(SocketType.REQ);
                    /**
                     * SE CONECTA AL ACTOR SOLICITAR
                     */
                    cliente.connect("tcp://localhost:3333");
                    Date dateS = new Date(), dateF = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateS);
                    cal.add(Calendar.DATE, 7);
                    dateF = cal.getTime();

                    int year = 1900 + dateS.getYear(), year2 = 1900 + dateF.getYear();
                    int mes = dateS.getMonth() + 1, mes2 = dateF.getMonth() + 1;

                    String fecha = dateS.getDate() + "-" + mes + "-" + year;
                    String fecha2 = dateF.getDate() + "-" + mes2 + "-" + year2;
                    String enviar = fecha + " " + fecha2;
                    mensaje = mensaje + " " + enviar;
                    cliente.send(mensaje.getBytes(ZMQ.CHARSET), 0);
                    byte[] replyActor = cliente.recv(0);
                    System.out.println("Received [" + new String(reply, ZMQ.CHARSET) + "]");
                    socket.send(new String(replyActor, ZMQ.CHARSET));
                }

                if (tipoSolicitud.equals("RENOVAR") && !h.isAlive()) {

                    int codigoTopico = 10000;

                    Date dateS = new Date(), dateF = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateS);
                    cal.add(Calendar.DATE, 7);
                    dateF = cal.getTime();

                    int year = 1900 + dateS.getYear(), year2 = 1900 + dateF.getYear();
                    int mes = dateS.getMonth() + 1, mes2 = dateF.getMonth() + 1;

                    String fecha = dateS.getDate() + "-" + mes + "-" + year;
                    String fecha2 = dateF.getDate() + "-" + mes2 + "-" + year2;
                    String enviar = fecha + " " + fecha2;
                    mensajes = mensaje.split(" ");
                    if (mensajes.length == 2) {
                        id = Integer.parseInt(mensajes[1]);
                        String update = String.format("%d %d %s", codigoTopico, id, enviar);
                        h = new Hilo("renovar", publisher, update);
                        h.start();
                    }

                } else if (tipoSolicitud.equals("DEVOLVER") && !h2.isAlive()) {

                    int codigoTopico = 10001;
                    if (mensajes.length == 2) {
                        id = Integer.parseInt(mensajes[1]);
                        String update = String.format("%d %d", codigoTopico, id);
                        h2 = new Hilo("devolver", publisher, update);
                        h2.start();
                    }
                }

            }
            contextClient.destroy();
        }
    }
}
