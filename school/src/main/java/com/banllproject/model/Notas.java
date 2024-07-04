package com.banllproject.model;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import com.banllproject.ConexaoNeo4j;
import com.banllproject.view.Menu;

public class Notas {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idAluno;
    private int idAtividade;
    private double nota;

    public Notas(int idAluno, int idAtividade, double nota) {
        this.idAluno = idAluno;
        this.idAtividade = idAtividade;
        this.nota = nota;
    }

    public Notas() {
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public void imprimeNota() {
        if (this.getIdAluno() == 0 || this.getIdAtividade() == 0)
            return;
        try {
            Alunos aluno = Alunos.getById(this.idAluno);
            Atividades atividade = Atividades.getById(this.idAtividade);
    
            System.out.println(
                    String.format("\nAluno: %s\nAtividade: %s\nNota: %.2f",
                            String.format("%s %s", aluno.getNome(), aluno.getSobrenome()),
                                    atividade.getDescricaoAtividade(), this.nota));
            Menu.pausaMenu();
        } catch (Exception e) {
            System.out.println("Erro ao buscar aluno ou atividade!");
            e.printStackTrace();
        }
    }

    public static Notas getById(int idAluno, int idAtividade) {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (a:Aluno {id_aluno: $idAluno})-[r:PARTICIPA_EM]->(at:Atividade {id_atividade: $idAtividade}) " +
                           "MATCH (n:Nota)-[c:NOTA_ALUNO]->(a2:Aluno {id_aluno: a.idAluno}) " + 
                           "MATCH (n:Nota)-[d:NOTA_ATIVIDADE]->(at2:Atividade {id_atividade: at.idAtividade}) " + 
                           "RETURN at.id_atividade as id_atividade, a.id_aluno as id_aluno, n.nota as nota";
        
            Result result = tx.run(query, Values.parameters(
                                            "idAtividade", idAtividade,
                                            "idAluno", idAluno));
        
            if (result.hasNext()) {
                Record record = result.next();
        
                return new Notas(
                    record.get("id_atividade").asInt(),
                    record.get("id_aluno").asInt(),
                    record.get("nota").asDouble()
                    );
            } else {
                System.out.println("Nota não encontrada com esse ID!");
                return new Notas();
            }
        });  
    }

    public static List<Notas> getAll() {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (a:Aluno)-[r:PARTICIPA_EM]->(at:Atividade) " +
                           "MATCH (n:Nota)-[c:NOTA_ALUNO]->(a2:Aluno {id_aluno: a.id_aluno}) " + 
                           "MATCH (n:Nota)-[d:NOTA_ATIVIDADE]->(at2:Atividade {id_atividade: at.id_atividade}) " + 
                           "RETURN at.id_atividade as id_atividade, a.id_aluno as id_aluno, n.nota as nota";
        
            Result result = tx.run(query);
            List<Notas> notas = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
        
                notas.add(new Notas(
                    record.get("id_atividade").asInt(),
                    record.get("id_aluno").asInt(),
                    record.get("nota").asDouble()
                ));
            }
            return notas; 
        }); 
    }

    public static void create(Notas nota) {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (a:Aluno {id_aluno: $fkAluno}) " +
                        "MATCH (at:Atividade {id_atividade: $fkAtividade}) " +
                        "MERGE (n:Nota {nota: $nota}) " +
                        "CREATE (a)-[:NOTA_ALUNO]->(n) " +
                        "CREATE (at)-[:NOTA_ATIVIDADE]->(n) " +
                        "CREATE (a)-[:PARTICIPA_EM]->(at) " +
                        "RETURN a.id_atividade as id";
            
            tx.run(query, 
            Values.parameters(                  "fkAtividade", nota.getIdAtividade(),
                                                "fkAluno", nota.getIdAluno(),
                                                "nota", nota.nota));
            return null;
        });
    }

    public static void update(Notas nota) {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (a:Aluno {id_aluno: $idAluno})-[r:PARTICIPA_EM]->(at:Atividade {id_atividade: $idAtividade}) " +
                        "MATCH (n:Nota)-[c:NOTA_ALUNO]->(a2:Aluno {id_aluno: a.id_aluno}) " + 
                        "MATCH (n:Nota)-[d:NOTA_ATIVIDADE]->(at2:Atividade {id_atividade: at.id_atividade}) " + 
                        "SET n.nota = $nota";
            
            tx.run(query, 
            Values.parameters(                  "idAtividade", nota.getIdAtividade(),
                                                "idAluno", nota.getIdAluno(),
                                                "nota", nota.nota));
            return null;
        });
    }

    public static void delete(int idAluno, int idAtividade) {
        conexao.writeTransaction(tx -> {
            String query =  "MATCH (a:Aluno {id_aluno: $idAluno})-[r:PARTICIPA_EM]->(at:Atividade {id_atividade: $idAtividade}) " +
                            "MATCH (n:Nota)-[c:NOTA_ALUNO]->(a2:Aluno {id_aluno: a.id_aluno}) " + 
                            "MATCH (n:Nota)-[d:NOTA_ATIVIDADE]->(at2:Atividade {id_atividade: at.id_atividade}) " + 
                            "DETACH DELETE n";

            Value params = Values.parameters("idAtividade", idAtividade, "idAluno", idAluno);

            tx.run(query, params);
            return null;
        });
    }
}
