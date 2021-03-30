package com.javeriana.edu.co;

/**
 * Borrar
 */
import java.util.Date;
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

    Boolean conect = false;
    String mensaje1, mensaje2, mensaje3;
    String mensajeR1, mensajeR2, mensajeR3;
    mensajeR1 = "";
    mensajeR2 = "";
    mensajeR3 = "";
    System.out.println("Actor renovar Start");
    try (ZContext context = new ZContext(); ZContext context2 = new ZContext()) {

      ZMQ.Socket suscriberGC1 = context.createSocket(SocketType.SUB);
      ZMQ.Socket suscriberGC2 = context2.createSocket(SocketType.SUB);
      Hilo h = new Hilo("conectServer1", suscriberGC1, mensajeR1, mensajeR2, mensajeR3);
      Hilo h2 = new Hilo("conectServer2", suscriberGC2, mensajeR1, mensajeR2, mensajeR3);
      while (!Thread.currentThread().isInterrupted()) {
        conect = false;
        mensaje1 = "";
        mensaje2 = "";
        mensaje3 = "";

        if (suscriberGC1.connect("tcp://localHost:5557") && !h.isAlive()) {
          conect = true;
          String filter = (args.length > 0) ? args[0] : "10000 ";
          suscriberGC1.subscribe(filter.getBytes(ZMQ.CHARSET));
          System.out.println("Primer hilo");
          h = new Hilo("conectServer1", suscriberGC1, mensajeR1, mensajeR2, mensajeR3);
          h.start();
        }
        if (suscriberGC2.connect("tcp://localHost:5556") && !h2.isAlive()) {
          // if (suscriber.connect("tcp://25.67.209.173:5556")) {
          // if (suscriber.connect("tcp://192.168.0.109:5556")) {
          conect = true;
          String filter = (args.length > 0) ? args[0] : "10000 ";
          suscriberGC2.subscribe(filter.getBytes(ZMQ.CHARSET));
          System.out.println("Segundo hilo");
          h2 = new Hilo("conectServer2", suscriberGC2, mensajeR1, mensajeR2, mensajeR3);
          h2.start();
        } else {
          System.out.println("NO SE CONECTO");
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
