package Entidades;

import java.io.Serializable;

public class ProfSaude extends Utilizador implements Serializable {

    public ProfSaude (int id, String nome, String dataNascimento, int contacto, String palavraPasse) { super( id , nome , dataNascimento , contacto, palavraPasse ); }

    public String toString() {

        return "Profissional de saúde { "
                    + super.getId() + ", "
                    + super.getNome() +", "
                    + super.getDataNascimento() + ", "
                    + super.getContacto() + " }";
    }

    public String getPalavraPasse(){return super.getPalavraPasse();}

}
