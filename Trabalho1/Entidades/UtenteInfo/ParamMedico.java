package Entidades.UtenteInfo;

import Entidades.Utente;

import java.io.Serializable;

public class ParamMedico extends Utente implements Serializable {

    private double peso;

    private double colesterol;

    private double bpm;

    private double pressao;

    private double altura;

    private double temperatura;

    public ParamMedico(int id, String nome, String dataNascimento, int contacto,String palavraPasse){
        super(id,nome,dataNascimento,contacto, palavraPasse);
        this.peso= 0;
        this.colesterol = 0;
        this.bpm = 0;
        this.pressao = 0;
        this.altura = 0;
        this.temperatura = 0;
    }


    public double getPeso() { return peso; }

    public void setPeso(double peso){
        this.peso = peso;
    }

    public double getColesterol(){
        return colesterol;
    }

    public void setColesterol(double colesterol){ this.colesterol = colesterol; }

    public double getBpm(){ return bpm; }

    public void setBpm(double bpm){
        this.bpm = bpm;
    }

    public double getPressao(){
        return pressao;
    }

    public void setPressao(double pressao){
        this.pressao = pressao;
    }

    public double getAltura(){ return altura; }

    public void setAltura(double altura){
        this.altura = altura;
    }

    public double getTemperatura(){
        return temperatura;
    }

    public void setTemperatura(double temperatura){
        this.temperatura = temperatura;
    }
}
