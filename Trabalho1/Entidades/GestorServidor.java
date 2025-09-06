/*package Entidades;


import Interfaces.GestorInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class GestorServer {
    public static void main(String[] args) {
        try {
            GestorInterface server = new Gestor();
            Naming.rebind("rmi://localhost:50000/GM", server);
            System.out.println("Running");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("1");
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("2");
            throw new RuntimeException(e);
        }
    }
}*/

package Entidades;

import Interfaces.GestorInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class GestorServidor {
    public static void main(String[] args) {
        try {
            GestorInterface server = new Gestor();
            Naming.rebind("rmi://localhost:50000/GM", server);
            System.out.println("Running");

            System.out.println("Pressione Enter para encerrar o servidor.");
            System.in.read();

        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Erro de RemoteException: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Erro de MalformedURLException: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro inesperado: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


