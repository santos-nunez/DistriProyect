package com.javeriana.edu.co;

import com.javeriana.edu.co.RMI.RemoteInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class GestorCarga {

    public static void main(String[] args) {
        String tipoSolicitud = "";
        String[] mensajes;
        String mensaje;
        int id = 0;
        String arg1 = "";
        try (ZContext contextClient = new ZContext()) {
            System.out.println("Gestor start");
            ZMQ.Socket socket = contextClient.createSocket(SocketType.REP);
            socket.bind("tcp://*:6000");

            ZMQ.Socket publisher = contextClient.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5556");
            publisher.bind("ipc://PROCESO");
            Hilo h = new Hilo("", publisher, "");
            Hilo h2 = new Hilo("", publisher, "");
            Registry reg = LocateRegistry.getRegistry("Localhost", 3333);
            RemoteInterface remote = (RemoteInterface) reg.lookup("SOLICITAR"); 
            HiloRMI h3 = new HiloRMI("","", remote);

            Queue<String> cola = new LinkedList<>();
            while (!Thread.currentThread().isInterrupted()) {
                /**
                 * Conectar con cliente
                 */
                byte[] reply = socket.recv(0);
                System.out.println("Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]");
                mensaje = new String(reply, ZMQ.CHARSET);
                cola.add(mensaje);

                if (tipoSolicitud != "SOLICITAR" && tipoSolicitud != "") {
                    String response = "PETICION DE " + tipoSolicitud + " ACEPTADA";
                    socket.send(response.getBytes(ZMQ.CHARSET), 0);
                }
                while (cola.size() > 0) {
                    /**
                     * Conectar con Actor
                     */
                    tipoSolicitud = "NINGUNA";
                    String enCola = cola.peek();
                    mensajes = enCola.split(" ");
                    if (mensajes.length == 2) {
                        tipoSolicitud = mensajes[0];
                        id = Integer.valueOf(mensajes[1]);
                    }
                    if (mensajes.length == 3) {
                        tipoSolicitud = mensajes[0];
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
                        String update = String.format("%d %d %s", codigoTopico, id, enviar);
                        h = new Hilo("renovar", publisher, update);
                        h.start();
                        cola.poll();

                    } else if (tipoSolicitud.equals("DEVOLVER") && !h2.isAlive()) {

                        int codigoTopico = 10001;
                        String update = String.format("%d %d", codigoTopico, id);
                        h2 = new Hilo("devolver", publisher, update);
                        h2.start();
                        cola.poll();
                    } else if (tipoSolicitud.equals("SOLICITAR")) {
                        h3 = new HiloRMI("solicitar", enCola, remote);
                        h3.start();
                        cola.poll();
                        /* int codigoTopico = 10002;
                        String enviar = arg1;
                        String update = String.format("%d %d %s", codigoTopico, id, enviar);
                        h = new Hilo("solicitar", publisher, update);
                        h.start(); */
                    }
                }

            }
            contextClient.destroy();
        } catch (RemoteException ex) {
            Logger.getLogger(GestorCarga.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(HiloRMI.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
}
