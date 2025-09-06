package Entidades.Excecoes;

import java.rmi.RemoteException;

public class FamiliarJaExiste extends RemoteException {
    public FamiliarJaExiste(String mensagem) {
        super(mensagem);
    }
}
