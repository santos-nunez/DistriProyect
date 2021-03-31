package com.javeriana.edu.co;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.javeriana.edu.co.controllers.PrestamoController;

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
        System.out.println("Recibiendo en hilo " + this.getName());
        string = socket.recvStr(0).trim();
        sscanf = new StringTokenizer(string, " ");
        codigo = Integer.valueOf(sscanf.nextToken());
        mensaje1 = sscanf.nextToken().toString();
        if (tipoSolicitud == "RENOVAR") {
            mensaje1 = sscanf.nextToken().toString();
            mensaje2 = sscanf.nextToken().toString();
            mensaje3 = sscanf.nextToken().toString();
            System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
            if (mensaje1.length() > 0 && mensaje2.length() > 0 && mensaje3.length() > 0) {
                PrestamoController prestamo = new PrestamoController("prestamos.txt");
                Date dat1, dat2;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    dat1 = objSDF.parse(mensaje2);
                    dat2 = objSDF.parse(mensaje3);
                    if (prestamo.renovarPrestamo(Integer.valueOf(mensaje1), dat1, dat2)) {
                        System.out.println("Se ha modificado la base de datos para el ID " + mensaje1);
                    } else {
                        System.out.println("No se renovo porque son iguales");
                    }
                } catch (ParseException e) {
                    System.err.println("Error en Hilo " + this.getName() + " por " + e.getMessage());
                }
            }
        }
        if (tipoSolicitud == "DEVOLVER") {
            System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + "]");
            if (mensaje1.length() > 0) {
                PrestamoController prestamo = new PrestamoController("prestamos.txt");
                if (prestamo.devolverPrestamo(Integer.valueOf(mensaje1))) {
                    System.out.println("Se ha modificado la base de datos prestamos para el ID " + mensaje1);
                } else {
                    System.out.println("No se pudo devolver el libro porque el libro ya fue devuelto");
                }
            }
        }

    }

}
