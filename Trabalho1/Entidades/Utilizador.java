package Entidades;

import java.io.Serializable;

public class Utilizador implements Serializable {

    private int id;

    private String nome;

    private String dataNascimento;

    private int contacto;

    private String palavraPasse;

    public Utilizador(int id, String nome, String dataNascimento, int contacto, String palavraPasse) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.contacto = contacto;
        this.palavraPasse = palavraPasse;
    }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public int getContacto() { return contacto; }

    public void setContacto(int contacto) { this.contacto = contacto; }

    public String getPalavraPasse() {return palavraPasse;}

    public void setPalavraPasse(String palavraPasse){this.palavraPasse = palavraPasse;}


    @Override
    public String toString() {
        return "Utilizador{" + id + ", " + nome +", " + dataNascimento + ", " + contacto + " }";
    }
}
