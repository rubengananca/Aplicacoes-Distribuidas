package Interfaces;

import Entidades.Gestor;
import Entidades.UtenteInfo.Exame;

import java.io.IOException;
import java.rmi.Remote;

public interface GestorInterface extends Remote {
    public void criarUtente(int id, String nome, String dataNascimento, int contacto, String palavraPasse) throws IOException;

    public void criarProfSaude(int id, String nome, String dataNascimento, int contacto, String palavraPasse) throws IOException;

    public void criarFamiliar(int id, String nome, String dataNascimento, int contacto, int idAssociado, String palavraPasse) throws IOException;

    public void criarConsulta(int utenteID,int medicoID, String medico, String dataHora) throws IOException;

    public void criarPrescricao(int utenteID, String medicamento, int dose, int duracao, String tomas, String dataInicio) throws IOException;

    public void criarParametro(int utenteID,double peso, double colesterol, double bpm, double pressao, double altura, double temperatura) throws IOException;

    public void criarExame(int utenteID, Exame.tipoExame tipo, String dataHora) throws IOException;

    public void criarNotificacao(int utenteID, String mensagem) throws IOException;

    public void listarExames(int utenteID) throws IOException;

    public void listarConsultas(int utenteID) throws IOException;

    public void listarParametros(int utenteID) throws IOException;

    public void listarPrescricoes(int utenteID) throws IOException;

    public void listarNotificacoes(int utenteID) throws IOException;

    public void verificarUltimaConsulta(int utenteID) throws IOException;

    public void carregarFicheiros() throws IOException;

    public void guardarFicheiros() throws IOException;

    public boolean verificarPasse(int id, Gestor.TipoUtilizador tipo, String passeTentada) throws IOException;

    public int numeroConsultas(int medicoID) throws IOException;

    public void inicializar() throws IOException;

    public void processarEscolhaUtilizador() throws IOException;
}
