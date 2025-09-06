package Entidades.Excecoes;

import java.rmi.RemoteException;

public class ProfSaudeJaExiste extends RemoteException {
    public ProfSaudeJaExiste(String mensagem) {
        super(mensagem);
    }
}