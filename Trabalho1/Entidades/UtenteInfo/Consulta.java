package Entidades.UtenteInfo;
import Entidades.Utente;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Consulta extends Utente implements Serializable {

    private int consultaID;

    private int medicoID;

    private String medico;

    private String dataHora;

    public Consulta(int id, String nome, String dataNascimento, int contacto, String palavraPasse){
        super(id,nome,dataNascimento,contacto,palavraPasse);
        this.consultaID = 0;
        this.medicoID = 0;
        this.medico = null;
        this.dataHora = null;

    }

    public int getConsultaID(){ return consultaID;}
    public void setConsultaID(int consultaID){ this.consultaID = consultaID;}

    public int getMedicoID() {
        return medicoID;
    }
    public void setMedicoID(int medicoID){
        this.medicoID = medicoID;
    }

    public String getMedico() {
        return medico;
    }
    public void setMedico(String medico){
        this.medico = medico;
    }

    public String getDataHora(){
        return dataHora;
    }
    public void setDataHora(String dataHora){
        this.dataHora = dataHora;
    }
}
