package com.javeriana.edu.co;

import java.util.List;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;

public class Ps {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            String[] servidor = { "tcp://localHost:6000", "tcp://25.14.192.153:6000" };
            int serverNbr = 0;
            System.out.println("contectado al servidor " + servidor[serverNbr]);
            ZMQ.Socket cliente = context.createSocket(SocketType.REQ);
            cliente.connect(servidor[serverNbr]);
            Poller poller = context.createPoller(1);
            poller.register(cliente, ZMQ.Poller.POLLIN);
            int nEnviar = 0;
            DataBase db = new DataBase();
            List<String> peticiones = db.leerFichero("peticiones.txt");
            String request = "";
            while (nEnviar < peticiones.size()) {
                request = peticiones.get(nEnviar);
                cliente.send(request.getBytes(ZMQ.CHARSET), 0);
                boolean esperandoRespuesta = true;
                while (esperandoRespuesta) {
                    // Poll socket for a reply, with timeout
                    int rc = poller.poll(2000);
                    if (rc == -1)
                        break;
                    if (poller.pollin(0)) {
                        byte[] reply = cliente.recv(0);
                        System.out.println("Received [" + new String(reply, ZMQ.CHARSET) + "]");
                        esperandoRespuesta = false;
                        nEnviar++;
                    } else {
                        System.out.println("No responde el servidor " + servidor[serverNbr]);
                        // Old socket is confused; close it and open a new one
                        poller.unregister(cliente);
                        context.destroySocket(cliente);
                        serverNbr = (serverNbr + 1) % 2;
                        Thread.sleep(100);
                        System.out.println("contectado al servidor " + servidor[serverNbr]);
                        cliente = context.createSocket(SocketType.REQ);
                        cliente.connect(servidor[serverNbr]);
                        poller.register(cliente, ZMQ.Poller.POLLIN);
                        cliente.send(request);
                    }
                }
            }
            context.destroy();
        } catch (Exception e) {
            System.out.println("Error en ps" + e.getMessage() + " " + e.getCause());
        }

    }
}