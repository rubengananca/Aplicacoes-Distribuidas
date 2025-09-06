package Entidades.UtenteInfo;

import Entidades.Utente;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prescricao extends Utente implements Serializable {

    private int prescricaoID;

    private String medicamento;

    private int dose;

    private int duracao;

    private String dataInicio;

    private String tomas;

    public Prescricao(int id, String nome, String dataNascimento, int contacto, String palavraPasse){
        super(id,nome,dataNascimento,contacto, palavraPasse);
        this.prescricaoID = 0;
        this.medicamento = null;
        this.dose = 0;
        this.duracao = 0;
        this.tomas = null;
        this.dataInicio = null;
    }

    public int getPrescricaoID(){ return prescricaoID;}

    public void setPrescricaoID(int prescricaoID){this.prescricaoID = prescricaoID ;}

    public String getMedicamento(){ return medicamento;}

    public void setMedicamento(String medicamento){this.medicamento = medicamento;}

    public int getDose() { return dose; }

    public void setDose(int dose) { this.dose = dose; }

    public int getDuracao(){ return duracao;}

    public void setDuracao(int duracao){ this.duracao = duracao;}

    public String getTomas(){ return tomas;}

    public void setTomas(String tomas){ this.tomas = tomas;}

    public String getDataInicio(){ return dataInicio;}

    public void setDataInicio(String dataInicio){ this.dataInicio = dataInicio;}
}
