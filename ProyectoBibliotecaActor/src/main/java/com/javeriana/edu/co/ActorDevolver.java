
package com.javeriana.edu.co;

import java.util.StringTokenizer;

import com.javeriana.edu.co.controllers.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorDevolver {

    public static void main(String[] args) {
        // if (args.length != 0) {
        /* args[0] */
        /**
         * 
         */
        int cont = 0;
        Boolean conect = false;
        String mensaje1, mensaje2;
        String mensajeR1, mensajeR2;
        mensajeR1 = "";
        mensajeR2 = "";
        System.out.println("Actor devolver Start");
        try (ZContext context = new ZContext()) {
            while (!Thread.currentThread().isInterrupted()) {
                conect = false;
                mensaje1 = "";
                mensaje2 = "";

                ZMQ.Socket suscriber = context.createSocket(SocketType.SUB);
                if (suscriber.connect("tcp://localHost:5557")) {
                    // if (suscriber.connect("tcp://25.67.209.173:5556")) {
                    // if (suscriber.connect("tcp://192.168.0.109:5556")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10001 ";
                    suscriber.subscribe(filter.getBytes(ZMQ.CHARSET));
                    String string;
                    StringTokenizer sscanf;
                    int codigo = -10;
                    System.out.println("Recibiendo ... " + cont);
                    string = suscriber.recvStr(0).trim();
                    sscanf = new StringTokenizer(string, " ");
                    codigo = Integer.valueOf(sscanf.nextToken());
                    mensaje1 = sscanf.nextToken().toString();
                    mensaje2 = sscanf.nextToken().toString();
                    // mensaje3 = sscanf.nextToken().toString();
                    System.out.println("Received " + cont + " :  [" + mensaje1 + " " + mensaje2 + " " + "]");
                    cont++;
                } else if (suscriber.connect("tcp://localHost:5557")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10000 ";
                    suscriber.subscribe(filter.getBytes(ZMQ.CHARSET));
                    String string;
                    StringTokenizer sscanf;
                    int codigo = -10;
                    string = suscriber.recvStr(0).trim();
                    sscanf = new StringTokenizer(string, " ");
                    codigo = Integer.valueOf(sscanf.nextToken());
                    mensaje1 = sscanf.nextToken().toString();
                    mensaje2 = sscanf.nextToken().toString();
                    // mensaje3 = sscanf.nextToken().toString();
                    System.out.println("Received " + ": [" + mensaje1 + " " + mensaje2 + "]");
                } else {
                    System.out.println("NO SE CONECTO");
                }
                if (conect && mensaje1.length() > 0 && mensaje2.length() > 0) {
                    if (!mensaje1.equals(mensajeR1) && !mensaje2.equals(mensajeR2)) {
                        mensajeR1 = mensaje1;
                        mensajeR2 = mensaje2;
                        // mensajeR3 = mensaje3;
                        PrestamoController prestamo = new PrestamoController("prestamos.txt");
                        LibroController libro = new LibroController("libros.txt");

                        if (prestamo.devolverPrestamo(Integer.valueOf(mensaje1), mensaje2)) {
                            System.out.println("Se ha modificado la base de datos prestamos para el ID " + mensaje1);
                            if (libro.devolverLibro(mensaje2)) {
                                System.out
                                        .println("Se ha modificado la base de datos libros para el Codigo " + mensaje2);
                            }
                        }
                    }
                }
                if (!conect) {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
