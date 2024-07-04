package com.banllproject.model;

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

public class Cursos {

    private int idCurso;
    private String nome;
    private int duracaoMinima;
    private int duracaoMaxima;
    private String sigla;

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();

    public Cursos(String nome, int duracaoMinima, int duracaoMaxima, String sigla) {
        this.nome = nome;
        this.duracaoMinima = duracaoMinima;
        this.duracaoMaxima = duracaoMaxima;
        this.sigla = sigla;
    }

    public Cursos(int idCurso, String nome, int duracaoMinima, int duracaoMaxima, String sigla) {
        this.idCurso = idCurso;
        this.nome = nome;
        this.duracaoMinima = duracaoMinima;
        this.duracaoMaxima = duracaoMaxima;
        this.sigla = sigla;
    }

    public Cursos() {
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracaoMinima() {
        return duracaoMinima;
    }

    public void setDuracaoMinima(int duracaoMinima) {
        this.duracaoMinima = duracaoMinima;
    }

    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }

    public void setDuracaoMaxima(int duracaoMaxima) {
        this.duracaoMaxima = duracaoMaxima;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public void imprimeCurso() {
        if (this.getIdCurso() == 0)
            return;
        System.out.println(
                String.format(
                        "\nInformações do curso:\nID: %d\nNome: %s\nSigla: %s\nDuração Mínima: %d semestres\nDuração Máxima: %d semestres",
                        this.getIdCurso(), this.getNome(), this.getSigla(), this.getDuracaoMinima(),
                        this.getDuracaoMaxima()));
        Menu.pausaMenu();
    }

    public static Cursos getById(int idCurso) {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (c:Curso {id_curso: $idCurso}) " +
                            "RETURN c.id_curso AS id_curso, c.nome AS nome, c.duracao_minima AS duracao_minima, c.duracao_maxima AS duracao_maxima, c.sigla AS sigla";
            Result result = tx.run(query, org.neo4j.driver.Values.parameters("idCurso", idCurso));
            if (result.hasNext()) {
                Record record = result.next();
                return new Cursos(record.get("id_curso").asInt(),
                                    record.get("nome").asString(),
                                    record.get("duracao_minima").asInt(),
                                    record.get("duracao_maxima").asInt(),
                                    record.get("sigla").asString());
            } else {
                System.out.println("Curso não encontrado com esse ID!");
                return new Cursos();
            }
        });
    }

    public static int create(Cursos curso) {
        return conexao.writeTransaction(tx -> {
            String query = "MERGE (c:Curso {id_curso: $idCurso, nome: $nome, duracao_minima: $duracaoMinima, duracao_maxima: $duracaoMaxima, sigla: $sigla}) " +
                           "RETURN c.id_curso as id";
            Result result = tx.run(query, 
                Values.parameters(              "idCurso", new Random().nextInt(0, 10000000),
                                                   "nome", curso.getNome(),
                                                   "duracaoMinima", curso.getDuracaoMinima(),
                                                   "duracaoMaxima", curso.getDuracaoMaxima(),
                                                   "sigla", curso.getSigla()));
            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(List<String> updatedFields, Cursos curso) {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Curso {id_curso: $idCurso}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idCurso", curso.getIdCurso());

            for (String field : updatedFields) {
                switch (field) {
                    case "nome":
                        params.put("nome", curso.getNome());
                        break;
                    case "duracao_minima":
                        params.put("duracao_minima", curso.getDuracaoMinima());
                        break;
                    case "duracao_maxima":
                        params.put("duracao_maxima", curso.getDuracaoMaxima());
                        break;
                    case "sigla":
                        params.put("sigla", curso.getSigla());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idCurso) {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Curso {id_curso: $idCurso}) " +
                           "DELETE c";

            Value params = Values.parameters("idCurso", idCurso);

            tx.run(query, params);
            return null; // Método não retorna nada
        });
    }

    public static List<Cursos> getAll() {
        return conexao.readTransaction(tx -> {
            List<Cursos> cursos = new ArrayList<>();
            String query = "MATCH (c:Curso) " +
                           "RETURN c.id_curso AS id_curso, c.nome AS nome, c.duracao_minima AS duracao_minima, c.duracao_maxima AS duracao_maxima, c.sigla AS sigla";
            
            Result result = tx.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                cursos.add(new Cursos(record.get("id_curso").asInt(),
                                      record.get("nome").asString(),
                                      record.get("duracao_minima").asInt(),
                                      record.get("duracao_maxima").asInt(),
                                      record.get("sigla").asString()));
            }

            return cursos;
        });
    }

}
