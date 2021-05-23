package com.javeriana.edu.co;

import java.util.StringTokenizer;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

/**
 * Subsriptor
 */
public class ActorDevolver {

    public static void main(String[] args) {
        Boolean conect = false;
        System.out.println("ACTOR DEVOLVER START ------");
        try (ZContext context = new ZContext()) {

            ZMQ.Socket suscriberGC1 = context.createSocket(SocketType.SUB);

            ZMQ.Socket conectarBDActorDevolver = context.createSocket(SocketType.REP);
            conectarBDActorDevolver.bind("tcp://*:4444");
            Hilo actualizarDB = new Hilo("act", conectarBDActorDevolver);
            actualizarDB.start();
            while (!Thread.currentThread().isInterrupted()) {
                if (suscriberGC1.connect("tcp://localHost:5557")) {
                    //if (suscriberGC1.connect("tcp://10.0.4.87:5557")) {
                    conect = true;
                    String filter = (args.length > 0) ? args[0] : "10001 ";
                    suscriberGC1.subscribe(filter.getBytes(ZMQ.CHARSET));
                    SubHilo subh = new SubHilo("", "", "", "", "");
                    String string, mensaje1 = "";
                    StringTokenizer sscanf;
                    /**
                     * RECIBE DEL GESTOR
                     */
                    string = suscriberGC1.recvStr(0).trim();
                    sscanf = new StringTokenizer(string, " ");
                    int codigo = Integer.valueOf(sscanf.nextToken());
                    mensaje1 = sscanf.nextToken().toString();
                    if (!subh.isAlive()) {
                        subh = new SubHilo("SubHiloDevolver", "DEVOLVER", mensaje1, "", "");
                        subh.start();
                        System.out.println("Received " + " :  [" + codigo + " " + mensaje1 + "]");
                        ZMQ.Socket conectarHiloDevolver = context.createSocket(SocketType.REQ);
                        /**
                         * SE CONECTA CON EL HILO DEL ACTOR DEVOLVER
                         */
                        conectarHiloDevolver.connect("tcp://10.0.4.87:4444");
                        conectarHiloDevolver.send(string);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR EN DEVOLVER " + e.getMessage());
        }
    }
}
