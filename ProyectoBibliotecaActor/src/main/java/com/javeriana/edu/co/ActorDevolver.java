
package com.javeriana.edu.co;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorDevolver {

    public static void main(String[] args) {
        Boolean conect = false;
        System.out.println("Actor devolver Start");
        try (ZContext context = new ZContext(); ZContext context2 = new ZContext()) {

            ZMQ.Socket suscriberGC1 = context.createSocket(SocketType.SUB);
            ZMQ.Socket suscriberGC2 = context2.createSocket(SocketType.SUB);
            Hilo h = new Hilo("conectServer1", suscriberGC1, "");
            Hilo h2 = new Hilo("conectServer2", suscriberGC2, "");
            while (!Thread.currentThread().isInterrupted()) {

                if (suscriberGC1.connect("tcp://localHost:5557") && !h.isAlive()) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10001 ";
                    suscriberGC1.subscribe(filter.getBytes(ZMQ.CHARSET));
                    h = new Hilo("conectServer1", suscriberGC1, "DEVOLVER");
                    h.start();
                }
                if (suscriberGC2.connect("tcp://localHost:5556") && !h2.isAlive()) {
                    // if (suscriber.connect("tcp://25.67.209.173:5556")) {
                    // if (suscriber.connect("tcp://192.168.0.109:5556")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10001 ";
                    suscriberGC2.subscribe(filter.getBytes(ZMQ.CHARSET));
                    h2 = new Hilo("conectServer2", suscriberGC2, "DEVOLVER");
                    h2.start();
                } else {
                    System.out.println("ESPERANDO ...");
                }
                if (!conect) {
                    Thread.sleep(1000);
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
