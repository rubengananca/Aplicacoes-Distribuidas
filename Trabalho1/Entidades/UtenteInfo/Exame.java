package Entidades.UtenteInfo;

import Entidades.Utente;

import java.io.Serializable;

public class Exame extends Utente implements Serializable {

    private int exameID;

    private String dataHora;

    public enum tipoExame {
        ECG, RaioX, Hemograma, Tomografia, Ressonância, Ecografia, Outros
    }

    private tipoExame tipo;

    // Construtor da classe
    public Exame(int id, String nome, String dataNascimento, int contacto, String palavraPasse) {
        super(id,nome,dataNascimento,contacto, palavraPasse);
        this.exameID = 0;
        this.tipo = null;
        this.dataHora = null;
    }


    public tipoExame getTipo() {
        return tipo;
    }
    public void setTipo(tipoExame tipo) {
        this.tipo = tipo;
    }

    public int getExameID() {
        return exameID;
    }
    public void setExameID(int exameID) {
        this.exameID = exameID;
    }

    public String getDataHora(){
        return dataHora;
    }

    public void setDataHora(String dataHora){
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "ID do Exame: " + exameID + "\n" +
                "Tipo de Exame: " + tipo + "\n";
    }

}
