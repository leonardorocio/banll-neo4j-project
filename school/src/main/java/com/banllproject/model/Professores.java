package com.banllproject.model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import com.banllproject.ConexaoNeo4j;
import com.banllproject.view.Menu;

public class Professores {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idProfessor;
    private String nome;
    private String sobrenome;
    private String sexoBiologico;
    private String cpf;
    private LocalDate dtNascimento;
    private int fkDepartamento;
    private Departamentos fkDepartamentoObject;

    public Professores() {
    };

    public Professores(int idProfessor, String nome, String sobrenome, String sexoBiologico, LocalDate dtNascimento) {
        this.idProfessor = idProfessor;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.sexoBiologico = sexoBiologico;
        this.dtNascimento = dtNascimento;
    }

    public Professores(String nome, String sobrenome, String sexoBiologico, String cpf,
            LocalDate dtNascimento, int fkDepartamento) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.sexoBiologico = sexoBiologico;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.fkDepartamento = fkDepartamento;
    }

    public Professores(int idProfessor, String nome, String sobrenome, String sexoBiologico, String cpf,
            LocalDate dtNascimento, int fkDepartamento, Departamentos fkDepartamentoObject) {
        this.idProfessor = idProfessor;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.sexoBiologico = sexoBiologico;
        this.cpf = cpf;
        this.dtNascimento = dtNascimento;
        this.fkDepartamento = fkDepartamento;
        this.fkDepartamentoObject = fkDepartamentoObject;
    }

    public String getSexoBiologico() {
        return sexoBiologico;
    }

    public void setSexoBiologico(String sexoBiologico) {
        this.sexoBiologico = sexoBiologico;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public LocalDate getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(LocalDate dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public int getFkDepartamento() {
        return fkDepartamento;
    }

    public void setFkDepartamento(int fkDepartamento) {
        this.fkDepartamento = fkDepartamento;
    }

    public Departamentos getFkDepartamentoObject() {
        return fkDepartamentoObject;
    }

    public void setFkDepartamentoObject(Departamentos fkDepartamentoObject) {
        this.fkDepartamentoObject = fkDepartamentoObject;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void imprimeProfessor() {
        if (this.getIdProfessor() == 0)
            return;
        System.out.println(
                String.format(
                        "\nInformações do professor:\nID: %d\nNome Completo: %s %s\nSexo: %s\nCPF: %s\nData de nascimento: %s",
                        this.getIdProfessor(), this.getNome(), this.getSobrenome(), this.getSexoBiologico(),
                        this.getCpf(),
                        this.getDtNascimento().toString()));
        if (this.getFkDepartamentoObject() != null) {
            this.getFkDepartamentoObject().imprimeDepartamento();
        } else {
            Menu.pausaMenu();
        }
    }

    public static Professores getById(int idProfessor) {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (p:Professor {id_professor: $idProfessor})-[:PERTENCE_A]->(d:Departamento) " +
                           "RETURN p.id_professor as idProfessor, p.nome as nome, " +
                           "p.sobrenome as sobrenome, p.sexo_biologico as sexoBiologico, " +
                           "p.cpf as cpf, p.dt_nascimento as dtNascimento, " +
                           "d.id_departamento as idDepartamento, d.departamento as nomeDepartamento";
        
            Result result = tx.run(query, Values.parameters("idProfessor", idProfessor));
        
            if (result.hasNext()) {
                Record record = result.next();
                int fk = record.get("idDepartamento").asInt();
                Departamentos departamento = null;
        
                if (fk != 0) {
                    departamento = Departamentos.getById(fk);
                }
        
                return new Professores(
                    record.get("idProfessor").asInt(),
                    record.get("nome").asString(),
                    record.get("sobrenome").asString(),
                    record.get("sexoBiologico").asString(),
                    record.get("cpf").asString(),
                    record.get("dtNascimento").asLocalDate(),
                    fk,
                    departamento
                );
            } else {
                System.out.println("Professor não encontrado com esse ID!");
                return new Professores();
            }
        });        

    }
    

    public static int create(Professores professores) {
        return conexao.writeTransaction(tx -> {
            String query = "MATCH (d:Departamento {id_departamento: $fkDepartamento}) " +
                   "MERGE (p:Professor {id_professor: $idProfessor, nome: $nome, sobrenome: $sobrenome, sexo_biologico: $sexo_biologico, cpf: $cpf, dt_nascimento: $dtNascimento}) " +
                   "CREATE (p)-[:PERTENCE_A]->(d) " +
                   "RETURN p.id_professor as id";
            
            Result result = tx.run(query, 
            Values.parameters(                  "idProfessor", new Random().nextInt(0, 100000),
                                                "nome", professores.getNome(),
                                                "sobrenome", professores.getSobrenome(),
                                                "sexo_biologico", professores.getSexoBiologico(),
                                                "cpf", professores.getCpf(),
                                                "dtNascimento", professores.getDtNascimento(),
                                                "fkDepartamento", professores.getFkDepartamento()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(List<String> updatedFields, Professores professores) throws SQLException {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Professor {id_professor: $idProfessor}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idProfessor", professores.getIdProfessor());

            for (String field : updatedFields) {
                switch (field) {
                    case "nome":
                        params.put("nome", professores.getNome());
                        break;
                    case "sobrenome":
                        params.put("sobrenome", professores.getSobrenome());
                        break;
                    case "cpf":
                        params.put("cpf", professores.getCpf());
                        break;
                    case "sexo_biologico":
                        params.put("sexo_biologico", professores.getSexoBiologico());
                        break;
                    case "dt_nascimento":
                        params.put("dt_nascimento", professores.getDtNascimento());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idProfessor) throws SQLException {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Professor {id_professor: $idProfessor})-[r:PERTENCE_A]->(d:Departamento) " +
                           "DETACH DELETE c";

            Value params = Values.parameters("idProfessor", idProfessor);

            tx.run(query, params);
            return null;
        });
    }

    public static List<Professores> getAll() throws SQLException {
        return conexao.readTransaction(tx -> {
            List<Professores> professores = new ArrayList<>();
            String query = "MATCH (p:Professor)-[:PERTENCE_A]->(d:Departamento)" +
                           "RETURN p.id_professor as idProfessor, p.nome as nome, " +
                           "p.sobrenome as sobrenome, p.sexo_biologico as sexoBiologico, " +
                           "p.cpf as cpf, p.dt_nascimento as dtNascimento, " +
                           "d.id_departamento as idDepartamento, d.departamento as nomeDepartamento";
            
            Result result = tx.run(query);
            while (result.hasNext()) {

                Record record = result.next();
                int fk = record.get("idDepartamento").asInt();
                Departamentos departamento = null;
        
                if (fk != 0) {
                    departamento = Departamentos.getById(fk);
                }

                professores.add(new Professores(
                    record.get("idProfessor").asInt(),
                    record.get("nome").asString(),
                    record.get("sobrenome").asString(),
                    record.get("sexoBiologico").asString(),
                    record.get("cpf").asString(),
                    record.get("dtNascimento").asLocalDate(),
                    fk,
                    departamento
                ));
            }

            return professores;
        });
    }

}
