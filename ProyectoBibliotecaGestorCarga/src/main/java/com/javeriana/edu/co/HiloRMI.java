/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.edu.co;

import com.javeriana.edu.co.RMI.RemoteInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.ZMQ;

/**
 *
 * @author PC
 */
public class HiloRMI extends Thread {

    public String solicitud;
    public RemoteInterface rem;
    public ZMQ.Socket socket;

    public HiloRMI(String name, String solicitud, RemoteInterface rem, ZMQ.Socket socket) {
        super(name);
        this.solicitud = solicitud;
        this.rem = rem;
        this.socket = socket; 
    }

    public void run() {
        try {
            Date dateS = new Date(), dateF = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateS);
            cal.add(Calendar.DATE, 7);
            dateF = cal.getTime();

            int year = 1900 + dateS.getYear(), year2 = 1900 + dateF.getYear();
            int mes = dateS.getMonth() + 1, mes2 = dateF.getMonth() + 1;

            String fecha = dateS.getDate() + "-" + mes + "-" + year;
            String fecha2 = dateF.getDate() + "-" + mes2 + "-" + year2;
            String enviar = fecha + " " + fecha2;
            solicitud = solicitud + " " + enviar;
            String mensaje = rem.solicitar(solicitud);
            socket.send(mensaje);
            
        } catch (Exception ex) {
            Logger.getLogger(HiloRMI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
