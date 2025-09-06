package Entidades;

import java.io.Serializable;

public class Familiar extends Utilizador implements Serializable {

    private int idAssociado;
    public Familiar(int id, String nome, String dataNascimento, int contacto, int idAssociado,String palavraPasse) {
        super(id, nome, dataNascimento, contacto, palavraPasse);
        this.idAssociado = idAssociado;
    }

    public String toString() {


        return "Familiar { "
                + super.getId() + ", "
                + super.getNome() + ", "
                + super.getDataNascimento() + ", "
                + super.getContacto() + " }"
                + idAssociado;
    }

    public int getIdAssociado(){ return idAssociado;}

    public String getPalavraPasse(){return super.getPalavraPasse();}

}
