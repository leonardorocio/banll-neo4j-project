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

public class Turmas {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idTurma;
    private String anoSemestre;
    private String localAula;
    private int fkDisciplina;
    private Disciplinas fkDisciplinaObject;

    // N:N
    private int idProfessorDaTurma;

    public Turmas() {
    }

    public Turmas(int idTurma, String anoSemestre, String localAula) {
        this.idTurma = idTurma;
        this.anoSemestre = anoSemestre;
        this.localAula = localAula;
    }

    public Turmas(int idTurma, String anoSemestre, String localAula, int fkDisciplina, Disciplinas fkDisciplinaObject) {
        this.idTurma = idTurma;
        this.anoSemestre = anoSemestre;
        this.localAula = localAula;
        this.fkDisciplina = fkDisciplina;
        this.fkDisciplinaObject = fkDisciplinaObject;
    }

    public Turmas(String anoSemestre, String localAula, int fkDisciplina, int idProfessorDaTurma) {
        this.anoSemestre = anoSemestre;
        this.localAula = localAula;
        this.fkDisciplina = fkDisciplina;
        this.idProfessorDaTurma = idProfessorDaTurma;
    }

    public int getIdProfessorDaTurma() {
        return idProfessorDaTurma;
    }

    public void setIdProfessorDaTurma(int idProfessorDaTurma) {
        this.idProfessorDaTurma = idProfessorDaTurma;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public String getAnoSemestre() {
        return anoSemestre;
    }

    public void setAnoSemestre(String anoSemestre) {
        this.anoSemestre = anoSemestre;
    }

    public String getLocalAula() {
        return localAula;
    }

    public void setLocalAula(String localAula) {
        this.localAula = localAula;
    }

    public int getFkDisciplina() {
        return fkDisciplina;
    }

    public void setFkDisciplina(int fkDisciplina) {
        this.fkDisciplina = fkDisciplina;
    }

    public Disciplinas getFkDisciplinaObject() {
        return fkDisciplinaObject;
    }

    public void setFkDisciplinaObject(Disciplinas fkDisciplinaObject) {
        this.fkDisciplinaObject = fkDisciplinaObject;
    }

    public void imprimeTurma() {
        if (this.getIdTurma() == 0)
            return;
        System.out.println(
                String.format("\nInformações da turma:\nID: %d\nAno/Semestre: %s\nLocal de aula: %s", this.getIdTurma(),
                        this.getAnoSemestre(), this.getLocalAula()));
        if (this.getFkDisciplinaObject() != null) {
            this.getFkDisciplinaObject().imprimeDisciplina();
        } else {
            Menu.pausaMenu();
        }
    }

    public static Turmas getById(int idTurma)  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (t:Turma {id_turma: $idTurma})-[r:LECIONA]->(d:Disciplina) " +
                           "RETURN t.id_turma as idTurma, t.ano_semestre as ano_semestre, t.local_aula as local_aula, " +
                           "d.id_disciplina as id_disciplina";
        
            Result result = tx.run(query, Values.parameters("idTurma", idTurma));
        
            if (result.hasNext()) {
                Record record = result.next();
                int fk = record.get("id_disciplina").asInt();
                Disciplinas disciplina = null;
        
                if (fk != 0) {
                    disciplina = Disciplinas.getById(fk);
                }
        
                return new Turmas(
                    record.get("idTurma").asInt(),
                    record.get("ano_semestre").asString(),
                    record.get("local_aula").asString(),
                    fk,
                    disciplina);
            } else {
                System.out.println("Turma não encontrada com esse ID!");
                return new Turmas();
            }
        });    
    }

    public static List<Turmas> getAll()  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (t:Turma)-[r:LECIONA]->(d:Disciplina) " +
                            "RETURN t.id_turma as idTurma, t.ano_semestre as ano_semestre, t.local_aula as local_aula, " +
                            "d.id_disciplina as id_disciplina";

            Result result = tx.run(query);
            List<Turmas> turmas = new ArrayList<>();
            while (result.hasNext()) {

                Record record = result.next();
                int fk = record.get("id_disciplina").asInt();
                Disciplinas disciplinas = new Disciplinas();
                if (fk != 0) {
                    disciplinas = Disciplinas.getById(fk);
                }
                turmas.add(
                        new Turmas(
                            record.get("idTurma").asInt(),
                            record.get("ano_semestre").asString(),
                            record.get("local_aula").asString(),
                                fk,
                                disciplinas));
            }
            return turmas;
        });
        
    }

    public static int create(Turmas turmas)  {
        return conexao.writeTransaction(tx -> {
            String query =  "MATCH (d:Disciplina {id_disciplina: $idDisciplina}) " +
                            "MATCH (t:Professor {id_professor: $idProfessor}) " +
                            "MERGE (p:Turma {id_turma: $id_turma, ano_semestre: $ano_semestre, local_aula: $local_aula}) " +
                            "CREATE (p)-[:LECIONA]->(d) " +
                            "CREATE (p)-[:ENSINADA_POR]->(t) " +
                            "RETURN p.id_turma as id";
            
            Result result = tx.run(query, 
            Values.parameters(                  "id_turma", new Random().nextInt(0, 100000),
                                                "ano_semestre", turmas.getAnoSemestre(),
                                                "local_aula", turmas.getLocalAula(),
                                                "idDisciplina", turmas.getFkDisciplina(),
                                                "idProfessor", turmas.getIdProfessorDaTurma()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });

    }

    public static void update(List<String> updatedFields, Turmas turmas)  {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Turma {id_turma: $idTurma}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idTurma", turmas.getIdTurma());

            for (String field : updatedFields) {
                switch (field) {
                    case "ano_semestre":
                        params.put("ano_semestre", turmas.getAnoSemestre());
                        break;
                    case "local_aula":
                        params.put("local_aula", turmas.getLocalAula());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idTurma)  {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:Turma {id_turma: $idTurma})-[r:LECIONA]->(d:Disciplina) " +
                           "DETACH DELETE c";

            Value params = Values.parameters("idTurma", idTurma);

            tx.run(query, params);
            return null;
        });
    }

}
