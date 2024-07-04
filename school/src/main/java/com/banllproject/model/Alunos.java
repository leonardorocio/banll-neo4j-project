package com.banllproject.model;

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

public class Alunos {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idAluno;
    private String nome;
    private String sobrenome;
    private LocalDate dtNascimento;
    private String cpf;
    private String sexoBiologico;
    private LocalDate dtIngresso;
    private int fkCurso;
    private Cursos fkCursoObject;

    // N:N
    private int idTurmaDoAluno;

    public Alunos(int idAluno, String nome, String sobrenome, LocalDate dtNascimento, String sexoBiologico,
            LocalDate dtIngresso) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dtNascimento = dtNascimento;
        this.sexoBiologico = sexoBiologico;
        this.dtIngresso = dtIngresso;
    }

    public Alunos(String nome, String sobrenome, LocalDate dtNascimento, String cpf, String sexoBiologico, LocalDate dtIngresso,
            int fkCurso) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dtNascimento = dtNascimento;
        this.cpf = cpf;
        this.sexoBiologico = sexoBiologico;
        this.dtIngresso = dtIngresso;
        this.fkCurso = fkCurso;
    }

    public Alunos(int idAluno, String nome, String sobrenome, LocalDate dtNascimento, String cpf, String sexoBiologico,
            LocalDate dtIngresso, int fkCurso, Cursos fkCursoObject) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dtNascimento = dtNascimento;
        this.cpf = cpf;
        this.sexoBiologico = sexoBiologico;
        this.dtIngresso = dtIngresso;
        this.fkCurso = fkCurso;
        this.fkCursoObject = fkCursoObject;
    }

    public Alunos() {
    }

    public int getIdTurmaDoAluno() {
        return idTurmaDoAluno;
    }

    public void setIdTurmaDoAluno(int idTurmaDoAluno) {
        this.idTurmaDoAluno = idTurmaDoAluno;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexoBiologico() {
        return sexoBiologico;
    }

    public void setSexoBiologico(String sexoBiologico) {
        this.sexoBiologico = sexoBiologico;
    }

    public LocalDate getDtIngresso() {
        return dtIngresso;
    }

    public void setDtIngresso(LocalDate dtIngresso) {
        this.dtIngresso = dtIngresso;
    }

    public void imprimeAluno() {
        if (this.getIdAluno() == 0) {
            return;
        }
        System.out.println(
                String.format(
                        "\nInformações do aluno:\nID: %d\nNome: %s %s\nSexo: %s\nCPF: %s\nData de nascimento: %s\nData de Ingresso: %s",
                        this.getIdAluno(), this.getNome(), this.getSobrenome(), this.getSexoBiologico(), this.getCpf(),
                        this.getDtNascimento().toString(), this.getDtIngresso().toString()));
        if (this.getFkCursoObject() != null) {
            this.getFkCursoObject().imprimeCurso();
        } else {
            Menu.pausaMenu();
        }
    }

    public int getFkCurso() {
        return fkCurso;
    }

    public void setFkCurso(int fkCurso) {
        this.fkCurso = fkCurso;
    }

    public Cursos getFkCursoObject() {
        return fkCursoObject;
    }

    public void setFkCursoObject(Cursos fkCursoObject) {
        this.fkCursoObject = fkCursoObject;
    }

    public static Alunos getById(int idAluno)  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (a:Aluno {id_aluno: $idAluno})-[:ESTUDA_EM]->(c:Curso) " +
                           "RETURN a.id_aluno as idAluno, a.nome as nome, " +
                           "a.sobrenome as sobrenome, a.sexo_biologico as sexoBiologico, " +
                           "a.cpf as cpf, a.dt_nascimento as dtNascimento, a.dt_ingresso as dt_ingresso, " +
                           "c.id_curso as id_curso";
        
            Result result = tx.run(query, Values.parameters("idAluno", idAluno));
        
            if (result.hasNext()) {
                Record record = result.next();
                int fk = record.get("id_curso").asInt();
                Cursos cursos = null;
        
                if (fk != 0) {
                    cursos = Cursos.getById(fk);
                }
        
                return new Alunos(
                    record.get("idAluno").asInt(),
                    record.get("nome").asString(),
                    record.get("sobrenome").asString(),
                    record.get("dtNascimento").asLocalDate(),
                    record.get("cpf").asString(),
                    record.get("sexoBiologico").asString(),
                    record.get("dt_ingresso").asLocalDate(),
                    fk,
                    cursos
                );
            } else {
                System.out.println("Aluno não encontrado com esse ID!");
                return new Alunos();
            }
        });
    }

    public static List<Alunos> getAll()  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (a:Aluno)-[:ESTUDA_EM]->(c:Curso) " +
                           "RETURN a.id_aluno as idAluno, a.nome as nome, " +
                           "a.sobrenome as sobrenome, a.sexo_biologico as sexoBiologico, " +
                           "a.cpf as cpf, a.dt_nascimento as dtNascimento, a.dt_ingresso as dt_ingresso, " +
                           "c.id_curso as id_curso";
            
            
        
            Result result = tx.run(query);
            List<Alunos> alunos = new ArrayList<>();
            
            while (result.hasNext()) {
                Record record = result.next();
                int fk = record.get("id_curso").asInt();
                Cursos cursos = null;
        
                if (fk != 0) {
                    cursos = Cursos.getById(fk);
                }
        
                alunos.add(new Alunos(
                    record.get("idAluno").asInt(),
                    record.get("nome").asString(),
                    record.get("sobrenome").asString(),
                    record.get("dtNascimento").asLocalDate(),
                    record.get("cpf").asString(),
                    record.get("sexoBiologico").asString(),
                    record.get("dt_ingresso").asLocalDate(),
                    fk,
                    cursos
                ));
            }
            return alunos;
        });
    }

    public static int create(Alunos aluno)  {
        return conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Curso {id_curso: $fkCurso}) " +
                        "MERGE (p:Aluno {id_aluno: $idAluno, nome: $nome, sobrenome: $sobrenome, sexo_biologico: $sexo_biologico, cpf: $cpf, dt_nascimento: $dtNascimento, dt_ingresso: $dtIngresso}) " +
                        "CREATE (p)-[:ESTUDA_EM]->(c) " +
                        "RETURN p.id_aluno as id";
            
            Result result = tx.run(query, 
            Values.parameters(                  "idAluno", new Random().nextInt(0, 100000),
                                                "nome", aluno.getNome(),
                                                "sobrenome", aluno.getSobrenome(),
                                                "sexo_biologico", aluno.getSexoBiologico(),
                                                "cpf", aluno.getCpf(),
                                                "dtNascimento", aluno.getDtNascimento(),
                                                "dtIngresso", aluno.getDtIngresso(),
                                                "fkCurso", aluno.getFkCurso()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(List<String> updatedFields, Alunos aluno)  {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Aluno {id_aluno: $idAluno}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idAluno", aluno.getIdAluno());

            for (String field : updatedFields) {
                switch (field) {
                    case "nome":
                        params.put("nome", aluno.getNome());
                        break;
                    case "sobrenome":
                        params.put("sobrenome", aluno.getSobrenome());
                        break;
                    case "cpf":
                        params.put("cpf", aluno.getCpf());
                        break;
                    case "sexo_biologico":
                        params.put("sexo_biologico", aluno.getSexoBiologico());
                        break;
                    case "dt_nascimento":
                        params.put("dt_nascimento", aluno.getDtNascimento());
                        break;
                    case "dt_ingresso":
                        params.put("dt_ingresso", aluno.getDtIngresso());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idAluno)  {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (a:Aluno {id_aluno: $idAluno})-[r:ESTUDA_EM]->(c:Curso) " +
                           "DETACH DELETE a";

            Value params = Values.parameters("idAluno", idAluno);

            tx.run(query, params);
            return null;
        });
    }
}
