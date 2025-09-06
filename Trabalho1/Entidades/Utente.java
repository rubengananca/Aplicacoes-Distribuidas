package Entidades;

import Entidades.UtenteInfo.Consulta;
import Entidades.UtenteInfo.Exame;
import Entidades.UtenteInfo.ParamMedico;
import Entidades.UtenteInfo.Prescricao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utente extends Utilizador implements Serializable {

    private ArrayList<ParamMedico> dadosFisiologicos;

    private ArrayList<String> notificacao;

    private Map<Integer, List<Consulta>> consulta;

    private Map<Integer, List<Prescricao>> prescricao;

    private Map<Integer, List<Exame>> exame;

    public Utente (int id, String nome, String dataNascimento, int contacto, String palavraPasse) {
        super( id , nome , dataNascimento , contacto, palavraPasse);
        this.dadosFisiologicos = new ArrayList<>();
        this.notificacao = new ArrayList<>();
        this.consulta = new HashMap<>();
        this.prescricao = new HashMap<>();
        this.exame = new HashMap<>();
    }

    public String toString() {
        return "Utente { "
                + super.getId() + ", "
                + super.getNome() +", "
                + super.getDataNascimento() + ", "
                + super.getContacto() + " }";
    }


    public String getPalavraPasse(){return super.getPalavraPasse();}

    public void adicionarNotificacao(String mensagem) {
        notificacao.add(mensagem);
    }

    public List<String> getNotificacoes(){return notificacao;}

    public void limparNotificacoes() {
        this.notificacao.clear();
    }
}
