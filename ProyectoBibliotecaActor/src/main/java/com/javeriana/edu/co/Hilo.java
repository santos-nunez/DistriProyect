package com.javeriana.edu.co;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.sound.sampled.SourceDataLine;

import com.javeriana.edu.co.controllers.PrestamoController;

import org.zeromq.ZMQ;

public class Hilo extends Thread {
    private ZMQ.Socket socket;
    private String mensajeR1;
    private String mensajeR2;
    private String mensajeR3;

    public Hilo(String name, ZMQ.Socket socket, String mensaje1, String mensaje2, String mensaje3) {
        super(name);
        this.socket = socket;
        this.mensajeR1 = mensaje1;
        this.mensajeR2 = mensaje2;
        this.mensajeR3 = mensaje3;
    }

    public void run() {

        String string, mensaje1, mensaje2, mensaje3;
        StringTokenizer sscanf;
        int codigo = -10;
        System.out.println("Recibiendo en hilo " + this.getName());
        string = socket.recvStr(0).trim();
        sscanf = new StringTokenizer(string, " ");
        codigo = Integer.valueOf(sscanf.nextToken());
        mensaje1 = sscanf.nextToken().toString();
        mensaje2 = sscanf.nextToken().toString();
        mensaje3 = sscanf.nextToken().toString();
        System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
        if (!mensaje1.equals(mensajeR1) && !mensaje2.equals(mensajeR2) && !mensaje3.equals(mensajeR3)) {
            mensajeR1 = mensaje1;
            mensajeR2 = mensaje2;
            mensajeR3 = mensaje3;
            PrestamoController prestamo = new PrestamoController("prestamos.txt");
            Date dat1, dat2;
            SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
            try {
                dat1 = objSDF.parse(mensaje2);
                dat2 = objSDF.parse(mensaje3);
                prestamo.renovarPrestamo(Integer.valueOf(mensaje1), dat1, dat2);
                System.out.println("Se ha modificado la base de datos para el ID " + mensaje1);
            } catch (ParseException e) {
                System.err.println("Error en Hilo " + this.getName() + " por " + e.getMessage());
            }

        }
    }

}
