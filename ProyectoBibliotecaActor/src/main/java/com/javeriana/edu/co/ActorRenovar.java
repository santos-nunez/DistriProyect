package com.javeriana.edu.co;

import java.util.StringTokenizer;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorRenovar {

    public static void main(String[] args) {

        Boolean conect = false;

        System.out.println("ACTOR RENOVAR START ------");
        try (ZContext context = new ZContext(); ZContext context2 = new ZContext()) {

            ZMQ.Socket suscriberGC1 = context.createSocket(SocketType.SUB);
            while (!Thread.currentThread().isInterrupted()) {

                //if (suscriberGC1.connect("tcp://localHost:5557") ) {
                if (suscriberGC1.connect("tcp://10.0.4.87:5557")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10000 ";
                    suscriberGC1.subscribe(filter.getBytes(ZMQ.CHARSET));
                    SubHilo subh = new SubHilo("", "", "", "", "");
                    String string, mensaje1 = "", mensaje2 = "", mensaje3 = "";
                    StringTokenizer sscanf;
                    string = suscriberGC1.recvStr(0).trim();
                    sscanf = new StringTokenizer(string, " ");
                    int codigo = Integer.valueOf(sscanf.nextToken());
                    mensaje1 = sscanf.nextToken().toString();
                    if (!subh.isAlive()) {
                        mensaje2 = sscanf.nextToken().toString();
                        mensaje3 = sscanf.nextToken().toString();
                        subh = new SubHilo("SubHiloRenovar", "RENOVAR", mensaje1, mensaje2, mensaje3);
                        subh.start();
                        System.out.println(
                                "Received " + " :  [" + codigo + " " + mensaje1 + " " + mensaje2 + " " + mensaje3 + "]");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR EN ACTOR RENOVAR " + e.getMessage());
        }
    }
}
