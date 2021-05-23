package com.javeriana.edu.co;

import com.javeriana.edu.co.controllers.LibroController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;

public class HiloSolicitar extends Thread {

    private ZMQ.Socket socket;

    public HiloSolicitar(String name, ZMQ.Socket socket) {
        super(name);
        this.socket = socket;
    }

    public void run() {
        //SubHilo subh = new SubHilo("", "", "", "", "");
        String string;
        StringTokenizer sscanf;
        LibroController libroController = new LibroController("libros.txt");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                /**
                 * SE RECIBE DEL OTRO ACTOR
                 */
                string = socket.recvStr(0).trim();
                sscanf = new StringTokenizer(string, " ");
                String peticion = sscanf.nextToken();
                String codigoLibro = sscanf.nextToken().toString();
                int idSolicitante = Integer.valueOf(sscanf.nextToken().toString());
                String fechaSolicitud = sscanf.nextToken().toString();
                String fechaFinalizacion = sscanf.nextToken().toString();
                Date dat1, dat2;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                dat1 = objSDF.parse(fechaSolicitud);
                dat2 = objSDF.parse(fechaFinalizacion);
                if(peticion.equalsIgnoreCase("VALIDAR")){
                    /**
                     * VALIDAR Y ACTUALIZAR
                     */
                    if(libroController.validarSolicitudLibro(codigoLibro)){
                        socket.send("true");
                        libroController.solicitarLibro(codigoLibro, idSolicitante, dat1, dat2);
                    } else{
                        socket.send("false");
                    }
                } else{
                    /**
                     * ActualizaDB
                     */
                    libroController.solicitarLibro(codigoLibro, idSolicitante, dat1, dat2);
                }
            } catch (ParseException ex) {
                Logger.getLogger(HiloSolicitar.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
