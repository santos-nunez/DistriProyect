package com.javeriana.edu.co;

import java.util.List;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;

public class Ps {
    public static void main(String[] args) {
        iniciar("C:\\Users\\admin\\Documents\\GitHub\\DistriProyect\\ProyectoBibliotecaPS\\peticiones.txt");
    }
    public static void iniciar(String ruta) {
        try (ZContext context = new ZContext()) {
            String[] servidor = { "tcp://10.0.4.89:7000", "tcp://localhost:7000" };
            int serverNbr = 0;
            System.out.println("contectado al servidor " + servidor[serverNbr]);
            ZMQ.Socket cliente = context.createSocket(SocketType.REQ);
            cliente.connect(servidor[serverNbr]);
            Poller poller = context.createPoller(1);
            poller.register(cliente, ZMQ.Poller.POLLIN);
            int nEnviar = 0;
            DataBase db = new DataBase();
            List<String> peticiones = db.leerFichero(ruta);
            String request = "";
            String tipo;
            while (nEnviar < peticiones.size()) {
                request = peticiones.get(nEnviar);
                cliente.send(request.getBytes(ZMQ.CHARSET), 0);
                boolean esperandoRespuesta = true;
                tipo = request.split(" ")[0];
                while (esperandoRespuesta) {
                    // Poll socket for a reply, with timeout
                    if(tipo.equalsIgnoreCase("SOLICITAR")){
                        Thread.sleep(1000);
                    }else{
                         Thread.sleep(500);
                    }
                    int rc = poller.poll(500);
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