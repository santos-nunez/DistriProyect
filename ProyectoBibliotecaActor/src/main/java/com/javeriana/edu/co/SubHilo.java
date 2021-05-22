package com.javeriana.edu.co;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.javeriana.edu.co.controllers.PrestamoController;

public class SubHilo extends Thread {
    private String tipoSolicitud;
    private String mensaje1;
    private String mensaje2;
    private String mensaje3;

    public SubHilo(String name, String tipoSolicitud, String mensaje1, String mensaje2, String mensaje3) {
        super(name);
        this.mensaje1 = mensaje1;
        this.mensaje2 = mensaje2;
        this.mensaje3 = mensaje3;
        this.tipoSolicitud = tipoSolicitud;
    }

    public void run() {
        if (tipoSolicitud == "RENOVAR") {
            if (mensaje1.length() > 0 && mensaje2.length() > 0 && mensaje3.length() > 0) {
                PrestamoController prestamo = new PrestamoController("prestamos.txt");
                Date dat1, dat2;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    dat1 = objSDF.parse(mensaje2);
                    dat2 = objSDF.parse(mensaje3);
                    if (/**prestamo.renovarPrestamo(Integer.valueOf(mensaje1), dat1, dat2)*/true) {
                        System.out.println("Se ha modificado la base de datos para el ID " + mensaje1);
                    } else {
                        System.out.println("No se renovo");
                    }
                } catch (ParseException e) {
                    System.out.println("Error en Hilo " + this.getName() + " por " + e.getMessage());
                }
                catch (Exception e) {
                    System.out.println("Error en Hilo " + this.getName() + " por " + e.getMessage());
                }
            }
        } else if (tipoSolicitud == "DEVOLVER") {
            if (mensaje1.length() > 0) {
                PrestamoController prestamo = new PrestamoController("prestamos.txt");
                if (/**prestamo.devolverPrestamo(Integer.valueOf(mensaje1))*/true) {
                    System.out.println("Se ha modificado la base de datos prestamos para el ID " + mensaje1);
                } else {
                    System.out.println("No se pudo devolver el libro porque el libro ya fue devuelto");
                }
            }
        }

    }
    
}
