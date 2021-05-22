package com.javeriana.edu.co.repository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.javeriana.edu.co.models.*;
import java.util.*;

public class DataBase {

    public DataBase() {

    }

    public List<Libro> leerFicheroLibro(String arg) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        List<Libro> libros = new ArrayList<>();
        Libro libroNuevo;
        try {
            archivo = new File(arg);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            String[] textElements;
            while ((linea = br.readLine()) != null) {
                textElements = linea.split(",");
                libroNuevo = new Libro(Integer.valueOf(textElements[0]), textElements[1].toString(),
                        Integer.valueOf(textElements[2]), textElements[3].toString(), textElements[4].toString());
                libros.add(libroNuevo);
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("Error en DataBase por " + e.getMessage());
        } finally {
            return libros;
        }
    }

    public List<Prestamo> leerFicheroPrestamo(String arg) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        List<Prestamo> prestamos = new ArrayList<>();
        Prestamo prestamo;
        try {
            archivo = new File(arg);
            if (archivo.exists()) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                String linea;
                String[] textElements;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy");
                Date dt_1;
                Date dt_2;
                while ((linea = br.readLine()) != null) {
                    textElements = linea.split(",");
                    dt_1 = objSDF.parse(textElements[1].toString());
                    dt_2 = objSDF.parse(textElements[2].toString());
                    prestamo = new Prestamo(Integer.valueOf(textElements[0]), dt_1, dt_2,
                            Integer.valueOf(textElements[3]), textElements[4].toString(),
                            Boolean.parseBoolean(textElements[5]));
                    prestamos.add(prestamo);
                }
                fr.close();
            }
        } catch (Exception e) {
            System.out.println("Error en DataBase leer fichero prestamo por " + e.getMessage());
        } finally {
            return prestamos;
        }
    }

    public Boolean modificarPrestamo(String arg, Prestamo prestamo) {
        String nFnuevo = "prestamosAux.txt";
        File fNuevo = new File(nFnuevo);
        File archivo = null;
        BufferedReader br;
        Boolean modifico = false;
        try {
            archivo = new File(arg);
            if (archivo.exists()) {
                br = new BufferedReader(new FileReader(archivo));

                Date fechaSolicitud = prestamo.getFechaSolicitud();
                Date fechaFinalizacion = prestamo.getFechaFinalizacion();

                int annioDate1 = fechaSolicitud.getYear() + 1900;
                int annioDate2 = fechaFinalizacion.getYear() + 1900;
                int mesDate1 = fechaSolicitud.getMonth() + 1;
                int mesDate2 = fechaFinalizacion.getMonth() + 1;

                String fechaInicial = fechaSolicitud.getDate() + "-" + mesDate1 + "-" + annioDate1;
                String fechaFinal = fechaFinalizacion.getDate() + "-" + mesDate2 + "-" + annioDate2;

                String insertar = prestamo.getIdSolicitud() + "," + fechaInicial + "," + fechaFinal + ","
                        + prestamo.getIdSolicitante() + "," + prestamo.getCodigoLibro() + ","
                        + prestamo.getFinalizado().toString();

                String[] textElements;
                String linea;
                while ((linea = br.readLine()) != null) {
                    textElements = linea.split(",");
                    if (Integer.parseInt(textElements[0]) == prestamo.getIdSolicitud()) {
                        Escribir(fNuevo, insertar);
                    } else {
                        Escribir(fNuevo, linea);
                    }
                }
                br.close();
                borrar(archivo);
                fNuevo.renameTo(archivo);
                modifico = true;

            } else {
                throw new Exception("Error en DataBase fichero prestamo por fichero no existe");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return modifico;
    }

    public boolean modificar(String arg, Prestamo prestamo) {
        return true;
    }

    public Boolean modificarLibro(String arg, Libro libro) {
        String nFnuevo = "librosAux.txt";
        File fNuevo = new File(nFnuevo);
        File archivo = null;
        BufferedReader br;
        Boolean modifico = false;
        try {
            archivo = new File(arg);
            if (archivo.exists()) {
                br = new BufferedReader(new FileReader(archivo));

                String insertar = libro.getID() + "," + libro.getCodigo() + "," + libro.getUnidades() + ","
                        + libro.getTitulo() + "," + libro.getAutor();
                String[] textElements;
                String linea;
                while ((linea = br.readLine()) != null) {
                    textElements = linea.split(",");
                    if (Integer.parseInt(textElements[0]) == libro.getID()) {
                        Escribir(fNuevo, insertar);

                    } else {
                        Escribir(fNuevo, linea);
                    }
                }
                br.close();
                borrar(archivo);
                fNuevo.renameTo(archivo);
                modifico = true;

            } else {
                throw new Exception("Error en DataBase fichero prestamo por fichero no existe");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return modifico;
    }

    /**
     * Puede retardar la escritura de un archivo
     * 
     * @param fFichero
     * @param cadena
     */
    public void Escribir(File fFichero, String cadena) {
        BufferedWriter bw;
        try {
            if (!fFichero.exists()) {
                fFichero.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(fFichero, true));
            bw.write(cadena + "\n");
            bw.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void borrar(File Ffichero) {
        try {
            if (Ffichero.exists()) {
                Ffichero.delete();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
