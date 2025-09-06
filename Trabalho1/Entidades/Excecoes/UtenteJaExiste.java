package Entidades.Excecoes;

import java.rmi.RemoteException;

public class UtenteJaExiste extends RemoteException {
    public UtenteJaExiste(String mensagem) {
        super(mensagem);
    }
}
