package com.javeriana.edu.co.RMI;

import java.rmi.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gustavo Rivera
 */
public interface RemoteInterface extends Remote{
    public String solicitar(String solicitud) throws Exception;
}
