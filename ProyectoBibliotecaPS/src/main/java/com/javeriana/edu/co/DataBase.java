package com.javeriana.edu.co;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class DataBase {

    public DataBase() {

    }

    public List<String> leerFichero(String arg) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        List<String> peticiones = new ArrayList<>();
        String peticion;
        try {
            archivo = new File(arg);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            String[] textElements;
            while ((linea = br.readLine()) != null) {
                textElements = linea.split(",");
                peticion = textElements[0].toString() + " " + textElements[1].toString();
                peticiones.add(peticion);
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("Error en DataBase por " + e.getMessage());
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                System.out.println("Error en DataBase Leer Fichero libro por " + e2.getMessage());
            }
            return peticiones;
        }
    }
    /** 
    public Boolean modificarLibro(String arg, Libro libro) {
        String nFnuevo = "peticionesAux.txt";
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
    }*/
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