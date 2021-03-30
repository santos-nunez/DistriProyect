package com.javeriana.edu.co;

/**
 * Borrar
 */
import java.util.Date;
import java.util.StringTokenizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.javeriana.edu.co.controllers.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorRenovar {

  public static void main(String[] args) {
    // if (args.length != 0) {
    /* args[0] */
    /**
     * 
     */
    int cont = 0;
    Boolean conect = false;
    String mensaje1, mensaje2, mensaje3;
    String mensajeR1, mensajeR2, mensajeR3;
    mensajeR1 = "";
    mensajeR2 = "";
    mensajeR3 = "";
    System.out.println("Actor renovar Start");
    try (ZContext context = new ZContext()) {
      while (!Thread.currentThread().isInterrupted()) {
        conect = false;
        mensaje1 = "";
        mensaje2 = "";
        mensaje3 = "";

        ZMQ.Socket suscriber = context.createSocket(SocketType.SUB);
        if (suscriber.connect("tcp://localHost:5556")) {
          // if (suscriber.connect("tcp://25.67.209.173:5556")) {
          // if (suscriber.connect("tcp://192.168.0.109:5556")) {
          conect = true;
          String filter = (args.length > 0) ? args[0] : "10000 ";
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
          mensaje3 = sscanf.nextToken().toString();
          System.out.println("Received " + cont + " :  [" + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
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
          mensaje3 = sscanf.nextToken().toString();
          System.out.println("Received " + ": [" + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
        } else {
          System.out.println("NO SE CONECTO");
        }
        if (conect && mensaje1.length() > 0 && mensaje2.length() > 0 && mensaje3.length() > 0) {
          if (!mensaje1.equals(mensajeR1) && !mensaje2.equals(mensajeR2) && !mensaje3.equals(mensajeR3)) {
            mensajeR1 = mensaje1;
            mensajeR2 = mensaje2;
            mensajeR3 = mensaje3;
            PrestamoController prestamo = new PrestamoController("prestamos.txt");
            Date dat1, dat2;
            SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
            dat1 = objSDF.parse(mensaje2);
            dat2 = objSDF.parse(mensaje3);
            prestamo.renovarPrestamo(Integer.valueOf(mensaje1), dat1, dat2);
            System.err.println("Se ha modificado la base de datos para el ID " + mensaje1);
          }
        }
        if (!conect) {
          Thread.sleep(1000);
        }
      }
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }
}
