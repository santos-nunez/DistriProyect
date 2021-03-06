package com.javeriana.edu.co;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorRenovar {

    public static void main(String[] args) {

        Boolean conect = false;

        System.out.println("Actor renovar Start");
        try (ZContext context = new ZContext(); ZContext context2 = new ZContext()) {

            ZMQ.Socket suscriberGC1 = context.createSocket(SocketType.SUB);
            ZMQ.Socket suscriberGC2 = context2.createSocket(SocketType.SUB);
            Hilo h = new Hilo("conectServer1", suscriberGC1, "");
            Hilo h2 = new Hilo("conectServer2", suscriberGC2, "");
            while (!Thread.currentThread().isInterrupted()) {

                if (suscriberGC1.connect("tcp://localhost:5556") && !h.isAlive()) {
                    // if (suscriberGC1.connect("tcp://25.87.209.187:5556") && !h.isAlive()) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10000 ";
                    suscriberGC1.subscribe(filter.getBytes(ZMQ.CHARSET));
                    System.out.println("Primer hilo");
                    h = new Hilo("conectServer1", suscriberGC1, "RENOVAR");
                    h.start();
                }
               /* if (suscriberGC2.connect("tcp://localhost:5557") && !h2.isAlive()) {
                    // if (suscriber.connect("tcp://25.67.209.173:5556")) {
                    // if (suscriber.connect("tcp://192.168.0.109:5556")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10000 ";
                    suscriberGC2.subscribe(filter.getBytes(ZMQ.CHARSET));
                    System.out.println("Segundo hilo");
                    h2 = new Hilo("conectServer2", suscriberGC2, "RENOVAR");
                    h2.start();
                } */

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
