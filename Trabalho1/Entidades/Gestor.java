package Entidades;

import Entidades.Excecoes.FamiliarJaExiste;
import Entidades.Excecoes.ProfSaudeJaExiste;
import Entidades.Excecoes.UtenteJaExiste;
import Entidades.UtenteInfo.Consulta;
import Entidades.UtenteInfo.Exame;
import Entidades.UtenteInfo.ParamMedico;
import Entidades.UtenteInfo.Prescricao;
import Interfaces.GestorInterface;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Gestor implements Serializable, GestorInterface {

    private Map<Integer, Utente> utente;
    private Map<Integer, ProfSaude> medico;
    private Map<Integer, Familiar> familiar;
    private Map<Integer, List<Consulta>> consulta;
    private Map<Integer, List<Prescricao>> prescricao;
    private Map<Integer, List<Exame>> exame;
    private Map<Integer, ParamMedico> parametro;

    public enum TipoUtilizador {
        UTENTE, MEDICO, FAMILIAR
    }

    private int regLog;

    public Gestor(){
        this.utente = new HashMap<>();
        this.medico = new HashMap<>();
        this.familiar = new HashMap<>();
        this.consulta = new HashMap<>();
        this.prescricao = new HashMap<>();
        this.exame = new HashMap<>();
        this.parametro = new HashMap<>();
    }

    /*          ----------------------------- Cria --------------------------------     */

    public void criarUtente(int id, String nome, String dataNascimento, int contacto, String palavraPasse){
        Utente utentes = new Utente(id,nome,dataNascimento,contacto, palavraPasse);
        this.utente.put(id,utentes);
        gerarCSV();
    }

    public void criarProfSaude(int id, String nome, String dataNascimento, int contacto, String palavraPasse){
        ProfSaude medicos = new ProfSaude(id,nome,dataNascimento,contacto, palavraPasse);
        this.medico.put(id,medicos);
        gerarCSV();
    }

    public void criarFamiliar(int id, String nome, String dataNascimento, int contacto, int idAssociado, String palavraPasse){
        Familiar familiares = new Familiar(id,nome,dataNascimento,contacto,idAssociado, palavraPasse);
        this.familiar.put(id,familiares);
        gerarCSV();
    }

    public void criarConsulta(int utenteID,int medicoID, String medico, String dataHora){
        if (!consulta.containsKey(utenteID)) {
            consulta.put(utenteID, new ArrayList<>());
        }

        int consultaID = gerarIdConsulta()+1;
        Utente utenteAtual = utente.get(utenteID);
        String nomeUtente = utenteAtual.getNome();
        String dataNascimentoUtente = utenteAtual.getDataNascimento();
        int contactoUtente = utenteAtual.getContacto();
        String passeUtente = utenteAtual.getPalavraPasse();

        Consulta consultas = new Consulta(utenteID,nomeUtente,dataNascimentoUtente,contactoUtente,passeUtente);
        consultas.setConsultaID(consultaID);
        consultas.setMedicoID(medicoID);
        consultas.setMedico(medico);
        consultas.setDataHora(dataHora);

        this.consulta.get(utenteID).add(consultas);

    }

    public void criarPrescricao(int utenteID, String medicamento, int dose, int duracao, String tomas, String dataInicio){

        if (!prescricao.containsKey(utenteID)) {
            prescricao.put(utenteID, new ArrayList<>());
        }

        Utente utenteAtual = utente.get(utenteID);
        String nomeUtente = utenteAtual.getNome();
        String dataNascimentoUtente = utenteAtual.getDataNascimento();
        int contactoUtente = utenteAtual.getContacto();
        String passeUtente = utenteAtual.getPalavraPasse();

        int prescricaoID = gerarIdPrescricao()+1;

        Prescricao prescricoes = new Prescricao(utenteID,nomeUtente,dataNascimentoUtente, contactoUtente, passeUtente);
        prescricoes.setPrescricaoID(prescricaoID);
        prescricoes.setMedicamento(medicamento);
        prescricoes.setDose(dose);
        prescricoes.setDuracao(duracao);
        prescricoes.setTomas(tomas);
        prescricoes.setDataInicio(dataInicio);

        this.prescricao.get(utenteID).add(prescricoes);
    }

    public void criarParametro(int utenteID,double peso, double colesterol, double bpm, double pressao, double altura, double temperatura){

        // Utente já existe e vai substituir pelos novos valores
        if (parametro.containsKey(utenteID)) {
            ParamMedico parametros = parametro.get(utenteID);
            parametros.setPeso(peso);
            parametros.setColesterol(colesterol);
            parametros.setBpm(bpm);
            parametros.setPressao(pressao);
            parametros.setAltura(altura);
            parametros.setTemperatura(temperatura);
        }

        Utente utenteAtual = utente.get(utenteID);
        String nomeUtente = utenteAtual.getNome();
        String dataNascimentoUtente = utenteAtual.getDataNascimento();
        int contactoUtente = utenteAtual.getContacto();
        String passeUtente = utenteAtual.getPalavraPasse();

        ParamMedico parametros = new ParamMedico(utenteID,nomeUtente,dataNascimentoUtente,contactoUtente,passeUtente);
        parametros.setPeso(peso);
        parametros.setColesterol(colesterol);
        parametros.setBpm(bpm);
        parametros.setPressao(pressao);
        parametros.setAltura(altura);
        parametros.setTemperatura(temperatura);
        this.parametro.put(utenteID,parametros);
    }

    public void criarExame(int utenteID, Exame.tipoExame tipo, String dataHora){

        if (!exame.containsKey(utenteID)) {
            exame.put(utenteID, new ArrayList<>());
        }

        Utente utenteAtual = utente.get(utenteID);
        String nomeUtente = utenteAtual.getNome();
        String dataNascimentoUtente = utenteAtual.getDataNascimento();
        int contactoUtente = utenteAtual.getContacto();
        String passeUtente = utenteAtual.getPalavraPasse();

        int exameID = gerarIdExame(utenteID)+1;
        Exame exames = new Exame(utenteID,nomeUtente,dataNascimentoUtente, contactoUtente, passeUtente);

        exames.setExameID(exameID);
        exames.setTipo(tipo);
        exames.setDataHora(dataHora);

        this.exame.get(utenteID).add(exames);
    }


    /*          ----------------------------- Históricos -> Listar  --------------------------------     */

    // Histórico de Exames
    public void listarExames(int utenteID) {
        StringBuilder informacoes = new StringBuilder();
        informacoes.append("\nInformações dos exames do utente: \n");

        if (!exame.containsKey(utenteID) || exame.get(utenteID) == null) {
            System.out.println("\nAinda não tem exames marcados.\n");
            return; // Sair do método, pois não há exames para listar
        }

        List<Exame> listaExames = exame.get(utenteID);

        for (Exame exame : listaExames) {
            informacoes.append("ID do Exame: ").append(exame.getExameID()).append("\n");
            informacoes.append("Tipo de Exame: ").append(exame.getTipo()).append("\n");
            informacoes.append("Data e hora do Exame: ").append(exame.getDataHora()).append("\n");
        }

        System.out.println(informacoes.toString());
    }

    // Histórico de Consultas
    public void listarConsultas(int utenteID){

        StringBuilder informacoes = new StringBuilder();
        informacoes.append("Informações sobre as consultas do utente: \n");

        if (!consulta.containsKey(utenteID) || consulta.get(utenteID) == null) {
            System.out.println("\nAinda não tem consultas marcadas.\n");
            return; // Sair do método, pois não há exames para listar
        }

        List<Consulta> listaConsulta = consulta.get(utenteID);


        for (Consulta consulta : listaConsulta){
            informacoes.append("ID da consulta: ").append(consulta.getConsultaID()).append("\n");
            informacoes.append("Nome do médico: ").append(consulta.getMedico()).append(" e ID: ").append(consulta.getMedicoID()).append("\n");
            informacoes.append("Data e hora da consulta: ").append(consulta.getDataHora()).append("\n");
            informacoes.append("\n");
        }

        System.out.println(informacoes.toString());

    }

    // Dados fisicos do utente
    public void listarParametros(int utenteID){
        StringBuilder informacoes = new StringBuilder();
        informacoes.append("Dados Fisiológicos mais atualizados do Utente: \n");

        if (!parametro.containsKey(utenteID) || parametro.get(utenteID) == null) {
            System.out.println("\nAinda não parâmetros físicos marcados.\n");
            return;
        }

        ParamMedico parametros = parametro.get(utenteID);

        informacoes.append("Peso: ").append(parametros.getPeso()).append("\n");
        informacoes.append("Colesterol: ").append(parametros.getColesterol()).append("\n");
        informacoes.append("Frequência cardíaca: ").append(parametros.getBpm()).append("\n");
        informacoes.append("Pressão arterial: ").append(parametros.getPressao()).append("\n");
        informacoes.append("Altura: ").append(parametros.getAltura()).append("\n");
        informacoes.append("Temperatura corporal: ").append(parametros.getTemperatura()).append("\n");

        System.out.println(informacoes);
    }

    public void listarPrescricoes(int utenteID){

        StringBuilder informacoes = new StringBuilder();
        informacoes.append("Informações sobre as prescrições do utente: \n");

        if (!prescricao.containsKey(utenteID) || prescricao.get(utenteID) == null) {
            System.out.println("\nAinda não prescrições médicas marcadas.\n");
            return;
        }

        List<Prescricao> listaPrescricao = prescricao.get(utenteID);

        for (Prescricao prescricao : listaPrescricao){
            informacoes.append("ID da Prescrição: ").append(prescricao.getPrescricaoID()).append("\n");
            informacoes.append("Nome do Medicamento: ").append(prescricao.getMedicamento()).append("\n");
            informacoes.append("Dose do Medicamento: ").append(prescricao.getDose()).append("\n");
            informacoes.append("Duração do Tratamento: ").append(prescricao.getDuracao()).append("\n");
            informacoes.append("Tomas do medicamento: ").append(prescricao.getTomas()).append("\n");
            informacoes.append("Data e hora da Prescrição: ").append(prescricao.getDataInicio()).append("\n");
            informacoes.append("\n");
        }

        System.out.println(informacoes);
    }


    /*          ----------------------------- Notificações - criação e gestão  --------------------------------     */


    public void criarNotificacao(int utenteID, String mensagem) {
        if (utente.containsKey(utenteID)) {
            Utente utenteAtual = utente.get(utenteID);
            utenteAtual.adicionarNotificacao(mensagem);
        }
    }
    public void listarNotificacoes(int utenteID) {
        Utente utenteAtual = utente.get(utenteID);
        List<String> notificacoes = utenteAtual.getNotificacoes();

        if(notificacoes.isEmpty()){
            System.out.println("\nNão tem nenhuma notificação.\n");
        }else{
            for (String notificacao : notificacoes) {
                System.out.println(notificacao);
            }
        }
    }

    public void verificarUltimaConsulta(int utenteID) {
        List<Consulta> consultas = consulta.get(utenteID);

        if (consultas != null && !consultas.isEmpty()) {
            Consulta ultimaConsulta = consultas.get(consultas.size() - 1);
            String dataConsultaStr = ultimaConsulta.getDataHora();

            // Converter a string da data para o formato LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDate dataConsulta = LocalDate.parse(dataConsultaStr, formatter);

            // Verificar se a última consulta foi há mais de 6 meses
            LocalDate dataAtual = LocalDate.now();
            LocalDate dataLimite = dataAtual.minusMonths(6);

            if (dataConsulta.isBefore(dataLimite)) {
                criarNotificacao(utenteID,"Você deve marcar uma nova consulta, pois a última ocorreu há mais de 6 meses e foi a "+dataConsulta +".");
            }
        }
    }


    /*          ----------------------------- Guarda e Carrega --------------------------------     */

    public void guardarUtentes() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("utentes.ser"))) {
            outputStream.writeObject(utente);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarUtentes() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("utentes.ser"))) {
            utente = (Map<Integer, Utente>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void guardarProfSaude() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("medicos.ser"))) {
            outputStream.writeObject(medico);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarProfSaude() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("medicos.ser"))) {
            medico = (Map<Integer, ProfSaude>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void guardarFamiliar() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("familiares.ser"))) {
            outputStream.writeObject(familiar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarFamiliar() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("familiares.ser"))) {
            familiar = (Map<Integer, Familiar>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void guardarExames() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("exames.ser"))) {
            outputStream.writeObject(exame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarExames() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("exames.ser"))) {
            exame = (Map<Integer, List<Exame>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void guardarConsultas() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("consultas.ser"))) {
            outputStream.writeObject(consulta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarConsultas() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("consultas.ser"))) {
            consulta = (Map<Integer, List<Consulta>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void guardarParametros() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("parametros.ser"))) {
            outputStream.writeObject(parametro);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarParametros() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("parametros.ser"))) {
            parametro = (Map<Integer, ParamMedico>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {;}
    }

    public void guardarPrescricoes() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("prescricoes.ser"))) {
            outputStream.writeObject(prescricao);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarPrescricoes() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("prescricoes.ser"))) {
            prescricao = (Map<Integer, List<Prescricao>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {}
    }

    public void gerarCSV() {
        try (FileWriter writer = new FileWriter("utilizadores.csv")) {

            // Escrever cabeçalho
            writer.append("ID, Nome, Data de Nascimento, Contacto, Tipo\n");

            // Escrever informações dos utentes
            escreverCSV(writer, utente, TipoUtilizador.UTENTE);

            // Escrever informações dos médicos
            escreverCSV(writer, medico, TipoUtilizador.MEDICO);

            // Escrever informações dos familiares
            escreverCSV(writer, familiar, TipoUtilizador.FAMILIAR);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escreverCSV(FileWriter writer, Map<Integer, ? extends Utilizador> map, TipoUtilizador tipo) throws IOException {
        for (Utilizador utilizador : map.values()) {
            writer.append(String.format("%d, %s, %s, %d, %s\n",
                    utilizador.getId(), utilizador.getNome(), utilizador.getDataNascimento(), utilizador.getContacto(), tipo.name()));
        }
    }

    public void carregarFicheiros(){
        carregarUtentes();
        carregarProfSaude();
        carregarFamiliar();
        carregarExames();
        carregarConsultas();
        carregarParametros();
        carregarPrescricoes();
    }

    public void guardarFicheiros(){
        guardarUtentes();
        guardarProfSaude();
        guardarFamiliar();
        guardarExames();
        guardarConsultas();
        guardarParametros();
        guardarPrescricoes();
    }


    /*          ----------------------------- Gestores de IDs --------------------------------     */


    public int gerarIdConsulta(){

        List<Integer> listadeIds = new ArrayList<>();

        for (List<Consulta> consultas : consulta.values()){
            if(consultas != null){
                for (Consulta consulta : consultas){
                    listadeIds.add(consulta.getConsultaID());
                }
            }
        }

        return listadeIds.size();
    }

    public int gerarIdExame(int utenteID){

        List<Integer> listadeIds = new ArrayList<>();
        List<Exame> examesUtente = exame.get(utenteID);
        if(examesUtente != null && !examesUtente.isEmpty()){
            for (Exame exame : examesUtente){
                listadeIds.add(exame.getExameID());
            }
        }
        return listadeIds.size();
    }

    public int gerarIdPrescricao(){

        List<Integer> listadeIds = new ArrayList<>();

        for (List<Prescricao> prescricoes : prescricao.values()){
            if(prescricoes != null){
                for (Prescricao prescricao : prescricoes){
                    listadeIds.add(prescricao.getPrescricaoID());
                }
            }
        }

        return listadeIds.size();
    }

    public boolean existeUtente(int utenteID) {
        return utente.containsKey(utenteID);
    }

    public boolean existeProfSaude(int medicoID) {
        return medico.containsKey(medicoID);
    }

    public boolean existeFamiliar(int familiarID) {
        return familiar.containsKey(familiarID);
    }

    public boolean existeID(int id){
        return (medico != null && medico.containsKey(id)) || (utente != null && utente.containsKey(id) || (familiar != null && familiar.containsKey(id)));
    }

    public boolean verificarPasse(int id, TipoUtilizador tipo, String passeTentada){

        if(tipo == TipoUtilizador.UTENTE){
            Utente utenteAtual = utente.get(id);
            String palavraPasse = utenteAtual.getPalavraPasse();
            return(palavraPasse.equals(passeTentada));

        }else if(tipo == TipoUtilizador.MEDICO){
            ProfSaude profissionalAtual = medico.get(id);
            String palavraPasse = profissionalAtual.getPalavraPasse();
            return(palavraPasse.equals(passeTentada));

        }else if(tipo == TipoUtilizador.FAMILIAR){
            Familiar familiarAtual = familiar.get(id);
            String palavraPasse = familiarAtual.getPalavraPasse();
            return(palavraPasse.equals(passeTentada));
        }
        return false;
    }


    /*          ----------------------------- Estatística --------------------------------     */

    public int numeroConsultas(int medicoID) {
        // Inicializa a variável que armazenará o total de consultas
        int totalConsultas = 0;

        // Itera sobre os valores do mapa 'consulta' (cada valor é uma lista de consultas)
        for (List<Consulta> consultasMedico : consulta.values()) {
            // Verifica se a lista de consultas não é nula
            if (consultasMedico != null) {
                // Itera sobre as consultas na lista
                for (Consulta consulta : consultasMedico) {
                    // Verifica se o ID do médico associado à consulta é igual ao 'medicoID' fornecido
                    if (consulta.getMedicoID() == medicoID) {
                        // Incrementa o contador de consultas
                        totalConsultas++;
                    }
                }
            }
        }

        // Retorna o total de consultas associadas ao médico com o 'medicoID'
        return totalConsultas;
    }


    /*          ----------------------------- Main --------------------------------     */


    public void inicializar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Indique o que pretende:\n1 - Registar-se\n2 - Fazer log-in");
        regLog = scanner.nextInt();
    }

    public void processarEscolhaUtilizador() throws UtenteJaExiste, ProfSaudeJaExiste, FamiliarJaExiste {
        switch (regLog) {
            case 1:
                registarUtilizador();
                break;
            case 2:
                loginUtilizador();
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    public TipoUtilizador getTipoUtilizador() {
        System.out.println("Indique que tipo de utilizador é: ");
        for (TipoUtilizador tipo : TipoUtilizador.values()) {
            System.out.println(tipo.ordinal() + 1 + " - " + tipo);
        }
        int escolha = new Scanner(System.in).nextInt();
        if (escolha < 1 || escolha > TipoUtilizador.values().length) {
            System.out.println("Operação inválida!");
            return null;
        }
        return TipoUtilizador.values()[escolha - 1];
    }

    public void registarUtilizador() throws UtenteJaExiste, ProfSaudeJaExiste, FamiliarJaExiste {
        TipoUtilizador tipo = getTipoUtilizador();
        System.out.println("Indique o número de saúde: ");
        int ID = new Scanner(System.in).nextInt();

        if (tipo == TipoUtilizador.UTENTE) {
            registarUtente(ID);
        } else if (tipo == TipoUtilizador.MEDICO) {
            registarProfSaude(ID);
        }else if(tipo == TipoUtilizador.FAMILIAR){
            registarFamiliar(ID);
        } else {
            System.out.println("Operação inválida");
        }
    }

    public void registarUtente(int ID) {
        try {
            if (existeUtente(ID)) {
                throw new UtenteJaExiste("Utente já está registado!");
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Indique o nome: ");
                String nome = scanner.nextLine();
                System.out.println("Indique a sua data de nascimento (formato - yyyy/MM/dd): ");
                String dataNascimento = scanner.nextLine();
                System.out.println("Indique o contacto telefónico: ");
                int contacto = scanner.nextInt();
                System.out.println("Indique a sua palavra-passe: ");
                scanner.nextLine();
                String palavraPasse = scanner.nextLine();
                criarUtente(ID, nome, dataNascimento, contacto, palavraPasse);
            }
        } catch (UtenteJaExiste e) {
            System.out.println(e.getMessage()); // Imprime apenas a mensagem da exceção
        }
    }

    public void registarProfSaude(int ID) throws ProfSaudeJaExiste {
        try{
            if (existeProfSaude(ID)) {
                throw new ProfSaudeJaExiste("Profissional de saúde já está registado!");
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Indique o nome: ");
                String nome = scanner.nextLine();
                System.out.println("Indique a sua data de nascimento (formato - yyyy/MM/dd): ");
                String dataNascimento = scanner.nextLine();
                System.out.println("Indique o contacto telefónico: ");
                int contacto = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Indique a sua palavra-passe: ");
                String palavraPasse = scanner.nextLine();
                criarProfSaude(ID, nome, dataNascimento, contacto,palavraPasse);
            }
        }catch(ProfSaudeJaExiste e){
            System.out.println(e.getMessage());
        }
    }

    public void registarFamiliar(int ID) throws FamiliarJaExiste {

        try{
            if (existeFamiliar(ID)) {
                throw new FamiliarJaExiste("Familiar já está registado!");
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Indique o nome: ");
                String nome = scanner.nextLine();
                System.out.println("Indique a sua data de nascimento (formato - yyyy/MM/dd): ");
                String dataNascimento = scanner.nextLine();
                System.out.println("Indique o contacto telefónico: ");
                int contacto = scanner.nextInt();
                System.out.println("Indique o nº de saúde do seu familiar: ");
                int numeroEmergencia = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Indique a sua palavra-passe: ");
                String palavraPasse = scanner.nextLine();

                if(existeUtente(numeroEmergencia)){
                    criarFamiliar(ID,nome,dataNascimento,contacto,numeroEmergencia,palavraPasse);
                }else {
                    System.out.println("Você está a tentar associar-se a um utente que não existe.");
                }
            }
        }catch(FamiliarJaExiste e){
            System.out.println(e.getMessage());
        }
    }

    public void loginUtilizador() {
        TipoUtilizador tipo = getTipoUtilizador();
        System.out.println("Indique o seu número de saúde: ");
        int ID = new Scanner(System.in).nextInt();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Palavra-Passe: ");
        String passeTentada = scanner.nextLine();

        if (existeID(ID)) {
            if (tipo == TipoUtilizador.UTENTE && existeUtente(ID) && verificarPasse(ID,tipo,passeTentada)) {
                Utente utenteInfo = utente.get(ID);
                String nomeUtente = utenteInfo.getNome();
                System.out.println("\nBem-vindo "+nomeUtente+"!");
                processosUtente(ID);
            } else if (tipo == TipoUtilizador.MEDICO && existeProfSaude(ID) && verificarPasse(ID,tipo,passeTentada)) {
                ProfSaude profissionalInfo = medico.get(ID);
                String nomeProfissional = profissionalInfo.getNome();
                System.out.println("\nBem-vindo "+nomeProfissional+"!");
                processosMedico(ID,nomeProfissional);
            } else if (tipo == TipoUtilizador.FAMILIAR && existeFamiliar(ID) && verificarPasse(ID,tipo,passeTentada)) {
                Familiar familiarID = familiar.get(ID);
                String nomeFamiliar = familiarID.getNome();
                System.out.println("\nBem-vindo "+nomeFamiliar+"!");
                processosFamiliar(familiarID.getIdAssociado());
            } else {
                System.out.println("\nOperação Inválida!");
            }
        } else {
            System.out.println("Nº de saúde não encontrado na base de dados. Necessário efetuar registo!");
        }
    }

    public void processosUtente(int utenteID) {

        verificarUltimaConsulta(utenteID);
        Scanner scanner = new Scanner(System.in);
        int operacao;

        do{
            System.out.println("Indique o que pretende: \n1 - Aceder às suas consultas \n2 - Aceder aos seus dados fisiológicos \n3 - Aceder às suas prescrições \n4 - Aceder aos seus exames \n5 - Caixa de Mensagens \n6 - Sair ");
            operacao = scanner.nextInt();
            if(operacao == 1){
                listarConsultas(utenteID);
            }else if(operacao == 2){
                listarParametros(utenteID);
            }else if(operacao == 3){
                listarPrescricoes(utenteID);
            }else if(operacao == 4){
                listarExames(utenteID);
            }else if(operacao == 5){
                listarNotificacoes(utenteID);

                scanner.nextLine();
                System.out.println("Quer limpar a sua caixa de mensagens?[y/n]");
                String decisao=scanner.nextLine();

                Utente utenteAtual = utente.get(utenteID);
                if ("y".equalsIgnoreCase(decisao)) {
                    utenteAtual.limparNotificacoes();
                    System.out.println("Caixa de mensagens limpa.");
                }

            }else if(operacao == 6){
                System.out.println("A sair...");
            }else{
                System.out.println("Opção inválida!");
            }
        }while(operacao != 6);
    }

    public void processosMedico(int medicoID, String nome) {
        Scanner scanner = new Scanner(System.in);

        int operacao;

        do{
            System.out.println("\nIndique o que pretende: \n1 - Agendar uma nova consulta \n2 - Registar uma nova prescrição médica \n3 - Registar um novo exame \n4 - Atualizar os dados fisiológicos de um utente \n5 - Consultar os dados de um utente \n6 - Estatística \n7 - Sair");
            operacao = scanner.nextInt();

            if(operacao == 7){
                System.out.println("A sair...");
                break;
            }

            if(operacao ==6){
                System.out.println("Você já deu um total de "+numeroConsultas(medicoID) +" consultas.");
            }else{
                System.out.println("Indique o número de saúde do utente: ");
                int utenteID = scanner.nextInt();

                if(existeUtente(utenteID)){
                    if(operacao == 1){

                        scanner.nextLine();
                        System.out.println("Indique a data e a hora para a qual quer agendar a consulta (formato - yyyy/MM/dd HH:mm:ss): ");
                        String dataHora = scanner.nextLine();
                        criarConsulta(utenteID,medicoID,nome,dataHora);
                        criarNotificacao(utenteID,"Você tem uma nova consulta com o médico "+ nome +" na data "+dataHora+"\n");
                    }else if(operacao == 2){

                        scanner.nextLine();
                        System.out.println("Indique o nome do medicamento: ");
                        String medicamento = scanner.nextLine();

                        System.out.println("Indique a dose (em mg): ");
                        int dose = scanner.nextInt();

                        System.out.println("Indique a duração do tratamento (em dias): ");
                        int duracao = scanner.nextInt();

                        scanner.nextLine();
                        System.out.println("Quantas vezes ao dia deverá ser tomado: ");
                        String tomas = scanner.nextLine();

                        System.out.println("Indique a data de início da toma (formato - yyyy/MM/dd HH:mm:ss): ");
                        String dataInicio = scanner.nextLine();

                        criarPrescricao(utenteID,medicamento, dose, duracao, tomas, dataInicio);
                        criarNotificacao(utenteID,"Você tem uma nova prescrição médica para o medicamento "+ medicamento +" a partir do dia "+dataInicio+"\n");
                    }else if(operacao == 3){

                        System.out.println("Indique o tipo de exame: ");

                        // Exibir opções de tipo de exame
                        for (Exame.tipoExame tipo : Exame.tipoExame.values()) {
                            System.out.println(tipo.ordinal() + 1 + ". " + tipo);
                        }

                        // Obter a escolha do usuário
                        int tipo = scanner.nextInt();

                        // Verificar se a escolha é válida
                        if (tipo < 1 || tipo > Exame.tipoExame.values().length) {
                            System.out.println("Operação Inválida!");
                            return;
                        }

                        // Converter a escolha do usuário para o tipo de exame correspondente
                        Exame.tipoExame tipoExameEscolhido = Exame.tipoExame.values()[tipo - 1];

                        scanner.nextLine();
                        System.out.print("Indique a data e hora do exame (formato - yyyy/MM/dd HH:mm:ss):  ");
                        String dataHora = scanner.nextLine();

                        criarExame(utenteID, tipoExameEscolhido, dataHora);
                        criarNotificacao(utenteID,"Você tem um novo exame de "+ tipoExameEscolhido +" na data "+dataHora+"\n");
                    }else if(operacao == 4){

                        System.out.print("Indique o peso (kg):  ");
                        double peso = scanner.nextDouble();

                        System.out.print("Indique o colesterol (mg/L):  ");
                        double colesterol = scanner.nextDouble();

                        System.out.print("Indique os batimentos por minuto:  ");
                        double bpm = scanner.nextDouble();

                        System.out.print("Indique a pressão sanguínea:  ");
                        double pressao = scanner.nextDouble();

                        System.out.print("Indique a altura (em metros):  ");
                        double altura = scanner.nextDouble();

                        System.out.print("Indique a temperatura corporal:  ");
                        double temperatura = scanner.nextDouble();

                        criarParametro(utenteID,peso,colesterol,bpm,pressao,altura,temperatura);
                    }else if(operacao ==5) {

                        System.out.println("Indique o que pretende: \n1 - Aceder às consultas \n2 - Aceder aos dados fisiológicos \n3 - Aceder às prescrições \n4 - Aceder aos exames");
                        int aceder = scanner.nextInt();

                        if (aceder == 1) {
                            listarConsultas(utenteID);
                        } else if (aceder == 2) {
                            listarParametros(utenteID);
                        } else if (aceder == 3) {
                            listarPrescricoes(utenteID);
                        } else if (aceder == 4) {
                            listarExames(utenteID);
                        }
                    } else{
                        System.out.println("Operação Inválida!");
                    }
                }else{
                    System.out.println("Esse utente não existe.");
                }
            }


        }while (operacao !=7);
    }

    public void processosFamiliar(int utenteID) {

        Utente utenteInfo = utente.get(utenteID);
        String nomeUtente = utenteInfo.getNome();

        System.out.println("Você acedeu aos dados do "+nomeUtente+"\n");
        int operacao;

        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Indique o que pretende: \n1 - Aceder às consultas \n2 - Aceder aos dados fisiológicos \n3 - Aceder às prescrições \n4 - Aceder aos exames \n5 - Caixa de Mensagens \n6 - Sair");
            operacao = scanner.nextInt();
            if(operacao == 1){
                listarConsultas(utenteID);
            }else if(operacao == 2){
                listarParametros(utenteID);
            }else if(operacao == 3){
                listarPrescricoes(utenteID);
            }else if(operacao == 4){
                listarExames(utenteID);
            }else if(operacao ==5){
                System.out.println("\nCaixa de Mensagens de "+nomeUtente+"\n");
                listarNotificacoes(utenteID);
            }else if(operacao == 6){
                System.out.println("A sair...");
            }else{
                System.out.println("Operação Inválida!");
            }
        }while(operacao != 6);
    }
}
