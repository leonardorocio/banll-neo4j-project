package com.banllproject.model;

import java.sql.SQLException;
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

public class Departamentos {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idDepartamento;
    private String nome;
    private String sigla;

    public Departamentos() {
    }

    public Departamentos(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public Departamentos(int idDepartamento, String nome, String sigla) {
        this.idDepartamento = idDepartamento;
        this.nome = nome;
        this.sigla = sigla;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public void imprimeDepartamento() {
        if (this.getIdDepartamento() == 0)
            return;
        System.out.println(
                String.format("\nInformações do departamento:\nID: %d\nNome: %s\nSigla: %s", this.getIdDepartamento(),
                        this.getNome(), this.getSigla()));
        Menu.pausaMenu();
    }

    public static Departamentos getById(int idDepartamento) {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (c:Departamento {id_departamento: $idDepartamento}) " +
                            "RETURN c.id_departamento AS id_departamento, c.departamento AS departamento, c.sigla AS sigla";
            Result result = tx.run(query, Values.parameters("idDepartamento", idDepartamento));
            if (result.hasNext()) {
                Record record = result.next();
                return new Departamentos(record.get("id_departamento").asInt(),
                                    record.get("departamento").asString(),
                                    record.get("sigla").asString());
            } else {
                System.out.println("Departamento não encontrado com esse ID!");
                return new Departamentos();
            }
        });
    }

    public static int create(Departamentos departamento) {
        return conexao.writeTransaction(tx -> {
            String query = "MERGE (c:Departamento {id_departamento: $idDepartamento, departamento: $departamento, sigla: $sigla}) " +
                           "RETURN c.id_departamento AS id";
            Result result = tx.run(query, 
                Values.parameters(                 "idDepartamento", new Random().nextInt(0, 10000000),
                                                   "departamento", departamento.getNome(),
                                                   "sigla", departamento.getSigla()));
            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(List<String> updatedFields, Departamentos departamento) {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Departamento {id_departamento: $idDepartamento}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idDepartamento", departamento.getIdDepartamento());

            for (String field : updatedFields) {
                switch (field) {
                    case "departamento":
                        params.put("departamento", departamento.getNome());
                        break;
                    case "sigla":
                        params.put("sigla", departamento.getSigla());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idDepartamento) throws SQLException {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Departamento {id_departamento: $idDepartamento}) " +
                           "DELETE c";

            Value params = Values.parameters("idDepartamento", idDepartamento);

            tx.run(query, params);
            return null;
        });
    }

    public static List<Departamentos> getAll() throws SQLException {
        return conexao.readTransaction(tx -> {
            List<Departamentos> departamentos = new ArrayList<>();
            String query = "MATCH (c:Departamento) " +
                           "RETURN c.id_departamento AS id_departamento, c.departamento AS departamento, c.sigla AS sigla";
            
            Result result = tx.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                departamentos.add(new Departamentos(record.get("id_departamento").asInt(),
                                      record.get("departamento").asString(),
                                      record.get("sigla").asString()));
            }

            return departamentos;
        });
    }

}
