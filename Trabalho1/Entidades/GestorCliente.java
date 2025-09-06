package Entidades;

// C:\Users\ruben\.jdks\openjdk-21\bin\rmiregistry.exe 50000 -J-D"sun.rmi.registry.registryFilter=*"
import Interfaces.GestorInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class GestorCliente {

    protected GestorCliente() throws RemoteException{
        super();
    }

    public static void main(String[] args) throws IOException {

        GestorInterface gm = null;
        try {
            gm = (GestorInterface) Naming.lookup("rmi://localhost:50000/GM");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            System.err.println("Erro ao conectar ao servidor RMI: " + e.getMessage());
            e.printStackTrace();

        }

        gm.carregarFicheiros();
        gm.inicializar();
        gm.processarEscolhaUtilizador();
        gm.guardarFicheiros();

    }
}

