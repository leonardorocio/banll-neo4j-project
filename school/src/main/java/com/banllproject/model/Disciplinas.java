package com.banllproject.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;

import com.banllproject.ConexaoNeo4j;
import com.banllproject.view.Menu;

public class Disciplinas {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idDisciplina;
    private String nome;
    private int cargaHoraria;

    // Atributo para criação de tabela N:N
    private int idCursoDaDisciplina;

    public Disciplinas() {
    }

    public Disciplinas(String nome, int cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public Disciplinas(String nome, int cargaHoraria, int idCursoDaDisciplina) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.idCursoDaDisciplina = idCursoDaDisciplina;
    }

    public Disciplinas(int idDisciplina, String nome, int cargaHoraria) {
        this(nome, cargaHoraria);
        this.idDisciplina = idDisciplina;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public void imprimeDisciplina() {
        if (this.getIdDisciplina() == 0)
            return;
        System.out.println(
                String.format("\nInformações da disciplina:\nID: %d\nNome: %s\nCarga Horária: %d",
                        this.getIdDisciplina(),
                        this.getNome(), this.getCargaHoraria()));
        Menu.pausaMenu();
    }

    public int getIdCursoDaDisciplina() {
        return idCursoDaDisciplina;
    }

    public void setIdCursoDaDisciplina(int idCursoDaDisciplina) {
        this.idCursoDaDisciplina = idCursoDaDisciplina;
    }

    public static Disciplinas getById(int idDisciplina) {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (d:Disciplina {id_disciplina: $idDisciplina}) " +
                           "RETURN d.id_disciplina as id_disciplina, d.nome as nome, d.carga_horaria as carga_horaria";

            Result result = tx.run(query, Values.parameters("idDisciplina", idDisciplina));
            if (result.hasNext()) {
                Record record = result.next();
                return new Disciplinas(record.get("id_disciplina").asInt(),
                                    record.get("nome").asString(),
                                    record.get("carga_horaria").asInt());
            } else {
                System.out.println("Disciplina não encontrada com esse ID!");
                return new Disciplinas();
            }
        });
    }

    public static List<Disciplinas> getAll()  {
        return conexao.readTransaction(tx -> {
            List<Disciplinas> disciplinas = new ArrayList<>();
            String query = "MATCH (d:Disciplina) " +
                           "RETURN d.id_disciplina as id_disciplina, d.nome as nome, d.carga_horaria as carga_horaria";

            Result result = tx.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                disciplinas.add(new Disciplinas(record.get("id_disciplina").asInt(),
                                    record.get("nome").asString(),
                                    record.get("carga_horaria").asInt()));
            }
            return disciplinas;
        });
    }

    public static int create(Disciplinas disciplina) {
        return conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Curso {id_curso: $idCurso}) " +
                   "MERGE (d:Disciplina {id_disciplina: $id_disciplina, nome: $nome, carga_horaria: $carga_horaria}) " +
                   "CREATE (p)-[:PERTENCE_A]->(d) " +
                   "RETURN d.id_disciplina as id";
            
            Result result = tx.run(query, 
            Values.parameters("id_disciplina", new Random().nextInt(0, 100000),
                                                "nome", disciplina.getNome(),
                                                "carga_horaria", disciplina.getCargaHoraria(),
                                                "idCurso", disciplina.getIdCursoDaDisciplina()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });

    }

    public static void update(List<String> updatedFields, Disciplinas disciplina)  {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Disciplina {id_disciplina: $idDisciplina}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idDisciplina", disciplina.getIdDisciplina());

            for (String field : updatedFields) {
                switch (field) {
                    case "nome":
                        params.put("nome", disciplina.getNome());
                        break;
                    case "carga_horaria":
                        params.put("carga_horaria", disciplina.getCargaHoraria());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idDisciplina)  {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Disciplina {id_disciplina: $idDisciplina}) " +
                           "DETACH DELETE c";

            Value params = Values.parameters("idDisciplina", idDisciplina);

            tx.run(query, params);
            return null;
        });
    }

}
