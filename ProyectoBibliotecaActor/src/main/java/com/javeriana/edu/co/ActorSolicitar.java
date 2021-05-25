/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.edu.co;

import com.javeriana.edu.co.controllers.LibroController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

/**
 *
 * @author admin
 */
public class ActorSolicitar {

    public static void main(String[] args) {
        try (ZContext contextClient = new ZContext()) {
            System.out.println("ACTOR SOLICITAR START ------");
            ZMQ.Socket socket = contextClient.createSocket(SocketType.REP);
            socket.bind("tcp://*:3333");
            String mensaje, string, estado;
            LibroController libroController = new LibroController("libros.txt");
            ZMQ.Socket conectarBDActorSolicitar = contextClient.createSocket(SocketType.REP);
            conectarBDActorSolicitar.bind("tcp://*:6666");
            HiloSolicitar actualizarDB = new HiloSolicitar("act", conectarBDActorSolicitar);
            actualizarDB.start();
            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = socket.recv(0);

                System.out.println("Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]");
                mensaje = new String(reply, ZMQ.CHARSET);
                StringTokenizer sscanf = new StringTokenizer(mensaje, " ");
                String peticion = sscanf.nextToken();
                String codigoLibro = sscanf.nextToken().toString();
                int idSolicitante = Integer.valueOf(sscanf.nextToken().toString());
                String fechaSolicitud = sscanf.nextToken().toString();
                String fechaFinalizacion = sscanf.nextToken().toString();
                Date dat1, dat2;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                dat1 = objSDF.parse(fechaSolicitud);
                dat2 = objSDF.parse(fechaFinalizacion);
                /**
                 * SE PERMITE ACTUALIZAR
                 */
                ZMQ.Socket conectarHiloDevolver = contextClient.createSocket(SocketType.REQ);
                /**
                 * SE CONECTA CON EL HILO DEL ACTOR SOLICITAR
                 */
                conectarHiloDevolver.connect("tcp://10.0.4.87:6666");
                string = "VALIDAR" + " " + codigoLibro + " " + idSolicitante + " " + fechaSolicitud + " " + fechaFinalizacion;
                HiloSolicitar.sleep(100);
                conectarHiloDevolver.send(string);
                mensaje = conectarHiloDevolver.recvStr(0).trim();
                if (mensaje.equalsIgnoreCase("true")) {
                    estado = libroController.solicitarLibro(codigoLibro, idSolicitante, dat1, dat2);
                    socket.send(estado);
                } else {
                    Thread.sleep(100);
                    estado = libroController.solicitarLibro(codigoLibro, idSolicitante, dat1, dat2);
                    socket.send(estado);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ActorSolicitar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ActorSolicitar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
