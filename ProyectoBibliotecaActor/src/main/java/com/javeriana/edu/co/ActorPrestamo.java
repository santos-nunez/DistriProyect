/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeriana.edu.co;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class ActorPrestamo {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) throws AlreadyBoundException {
        // TODO code application logic here
        Registry reg;
        
        try {
            reg = LocateRegistry.createRegistry(3333);
            reg.bind("SOLICITAR", new ServerImplementsActor());
        } catch (RemoteException ex) {
            Logger.getLogger(ActorPrestamo.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        System.out.println("SERVER ON");
    }
    
}
