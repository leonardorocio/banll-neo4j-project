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

public class Atividades {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idAtividade;
    private String descricaoAtividade;
    private LocalDate dtEntrega;

    private int fkTipoAtividade;
    private TipoAtividades fkTipoAtividadesObject;
    private int fkProfessores;
    private Professores fkProfessoresObject;
    private int fkTurma;
    private Turmas fkTurmasObject;

    // N:N
    private int idAlunoAtividade;

    public Atividades(int idAtividade, String descricaoAtividade, LocalDate dtEntrega) {
        this.idAtividade = idAtividade;
        this.descricaoAtividade = descricaoAtividade;
        this.dtEntrega = dtEntrega;
    }

    public Atividades(String descricaoAtividade, LocalDate dtEntrega, int fkProfessores, int fkTurma,
            int fkTipoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
        this.dtEntrega = dtEntrega;
        this.fkProfessores = fkProfessores;
        this.fkTurma = fkTurma;
        this.fkTipoAtividade = fkTipoAtividade;
    }

    public Atividades(int idAtividade, String descricaoAtividade, LocalDate dtEntrega,
            int fkProfessores, Professores fkProfessoresObject, int fkTurma, Turmas fkTurmasObject, int fkTipoAtividade,
            TipoAtividades fkTipoAtividadesObject) {
        this.idAtividade = idAtividade;
        this.descricaoAtividade = descricaoAtividade;
        this.dtEntrega = dtEntrega;
        this.fkProfessores = fkProfessores;
        this.fkProfessoresObject = fkProfessoresObject;
        this.fkTurma = fkTurma;
        this.fkTurmasObject = fkTurmasObject;
        this.fkTipoAtividade = fkTipoAtividade;
        this.fkTipoAtividadesObject = fkTipoAtividadesObject;
    }

    public Atividades() {
    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public LocalDate getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(LocalDate dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public int getFkProfessores() {
        return fkProfessores;
    }

    public void setFkProfessores(int fkProfessores) {
        this.fkProfessores = fkProfessores;
    }

    public Professores getFkProfessoresObject() {
        return fkProfessoresObject;
    }

    public void setFkProfessoresObject(Professores fkProfessoresObject) {
        this.fkProfessoresObject = fkProfessoresObject;
    }

    public int getIdAlunoAtividade() {
        return idAlunoAtividade;
    }

    public void setIdAlunoAtividade(int idAlunoAtividade) {
        this.idAlunoAtividade = idAlunoAtividade;
    }

    public int getFkTurma() {
        return fkTurma;
    }

    public void setFkTurma(int fkTurma) {
        this.fkTurma = fkTurma;
    }

    public Turmas getFkTurmasObject() {
        return fkTurmasObject;
    }

    public void setFkTurmasObject(Turmas fkTurmasObject) {
        this.fkTurmasObject = fkTurmasObject;
    }

    public int getFkTipoAtividade() {
        return fkTipoAtividade;
    }

    public void setFkTipoAtividade(int fkTipoAtividade) {
        this.fkTipoAtividade = fkTipoAtividade;
    }

    public TipoAtividades getFkTipoAtividadesObject() {
        return fkTipoAtividadesObject;
    }

    public void setFkTipoAtividadesObject(TipoAtividades fkTipoAtividadesObject) {
        this.fkTipoAtividadesObject = fkTipoAtividadesObject;
    }

    public void imprimeAtividade() {
        if (this.getIdAtividade() == 0)
            return;
        System.out.println(
                String.format(
                        "\nInformações da atividade:\nID: %d\nDescrição: %s\nData de Entrega: %s",
                        this.getIdAtividade(), this.getDescricaoAtividade(),
                        this.getDtEntrega().toString()));
                        
        if (this.getFkTipoAtividadesObject() != null) {
            this.getFkTipoAtividadesObject().imprimeTipoAtividade();
        }
        if (this.getFkTipoAtividadesObject() == null) {
            Menu.pausaMenu();
        }
    }

    public static Atividades getById(int idAtividade)  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (t:Atividade {id_atividade: $idAtividade})-[r:APLICADA_POR]->(p:Professor) " +
                           "MATCH (t:Atividade {id_atividade: $idAtividade})-[c:APLICADA_EM]->(b:Turma) " + 
                           "MATCH (t:Atividade {id_atividade: $idAtividade})-[a:TIPO_DE]->(ta:TipoAtividade) " + 
                           "RETURN t.id_atividade as idAtividade, t.descricao_atividade as descricao, t.dt_entrega as dt_entrega, " +
                           "ta.id_tipo_atividade as fk_tipo_atividade, " +
                           "b.id_turma as fk_turma, " +
                           "p.id_professor as fk_professor";
        
            Result result = tx.run(query, Values.parameters("idAtividade", idAtividade));
        
            if (result.hasNext()) {
                Record record = result.next();

                int fk_professor = record.get("fk_professor").asInt();
                Professores professores = new Professores();
                if (fk_professor != 0) {
                    professores = Professores.getById(fk_professor);
                }
                int fk_turma = record.get("fk_turma").asInt();
                Turmas turmas = new Turmas();
                if (fk_turma != 0) {
                    turmas = Turmas.getById(fk_turma);
                }
                int fk_tipo = record.get("fk_tipo_atividade").asInt();
                TipoAtividades tipo = new TipoAtividades();
                if (fk_tipo != 0) {
                    tipo = TipoAtividades.getById(fk_tipo);
                }
        
                return new Atividades(
                    record.get("idAtividade").asInt(),
                    record.get("descricao").asString(),
                    record.get("dt_entrega").asLocalDate(),
                    fk_professor,
                    professores,
                    fk_turma,
                    turmas,
                    fk_tipo,
                    tipo);
            } else {
                System.out.println("Atividade não encontrada com esse ID!");
                return new Atividades();
            }
        });  
    }

    public static List<Atividades> getAll()  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (t:Atividade)-[r:APLICADA_POR]->(p:Professor) " +
                           "MATCH (t:Atividade)-[c:APLICADA_EM]->(b:Turma) " + 
                           "MATCH (t:Atividade)-[a:TIPO_DE]->(ta:TipoAtividade) " + 
                           "RETURN t.id_atividade as idAtividade, t.descricao_atividade as descricao, t.dt_entrega as dt_entrega, " +
                           "ta.id_tipo_atividade as fk_tipo_atividade, " +
                           "b.id_turma as fk_turma, " +
                           "p.id_professor as fk_professor";
        
            Result result = tx.run(query);
            List<Atividades> atividades = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();

                int fk_professor = record.get("fk_professor").asInt();
                Professores professores = new Professores();
                if (fk_professor != 0) {
                    professores = Professores.getById(fk_professor);
                }
                int fk_turma = record.get("fk_turma").asInt();
                Turmas turmas = new Turmas();
                if (fk_turma != 0) {
                    turmas = Turmas.getById(fk_turma);
                }
                int fk_tipo = record.get("fk_tipo_atividade").asInt();
                TipoAtividades tipo = new TipoAtividades();
                if (fk_tipo != 0) {
                    tipo = TipoAtividades.getById(fk_tipo);
                }
        
                atividades.add(new Atividades(
                    record.get("idAtividade").asInt(),
                    record.get("descricao").asString(),
                    record.get("dt_entrega").asLocalDate(),
                    fk_professor,
                    professores,
                    fk_turma,
                    turmas,
                    fk_tipo,
                    tipo));
            }
            return atividades;
        });  
    }

    public static int create(Atividades atividade)  {
        return conexao.writeTransaction(tx -> {
            String query = "MATCH (p:Professor {id_professor: $fkProfessor}) " +
                        "MATCH (ta:TipoAtividade {id_tipo_atividade: $fkTipoAtividade}) " +
                        "MATCH (t:Turma {id_turma: $fkTurma}) " +
                        "MERGE (a:Atividade {id_atividade: $idAtividade, descricao_atividade: $descricao, dt_entrega: $dt_entrega}) " +
                        "CREATE (a)-[:APLICADA_POR]->(p) " +
                        "CREATE (a)-[:APLICADA_EM]->(t) " +
                        "CREATE (a)-[:TIPO_DE]->(ta) " +
                        "RETURN a.id_atividade as id";
            
            Result result = tx.run(query, 
            Values.parameters(                  "idAtividade", new Random().nextInt(0, 100000),
                                                "descricao", atividade.getDescricaoAtividade(),
                                                "dt_entrega", atividade.getDtEntrega(),
                                                "fkProfessor", atividade.getFkProfessores(),
                                                "fkTipoAtividade", atividade.getFkTipoAtividade(),
                                                "fkTurma", atividade.getFkTurma()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(List<String> updatedFields, Atividades atividade)  {
        conexao.writeTransaction(tx -> {
            StringBuilder setFields = new StringBuilder();
            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1)
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i)).append(", ");
                else
                    setFields.append("c.").append(updatedFields.get(i)).append(" = $").append(updatedFields.get(i));
            }

            String query = "MATCH (c:Atividade {id_atividade: $idAtividade}) " + 
                           "SET " + setFields.toString();

            Map<String, Object> params = new HashMap<>();
            params.put("idAtividade", atividade.getIdAtividade());

            for (String field : updatedFields) {
                switch (field) {
                    case "descricao_atividade":
                        params.put("descricao_atividade", atividade.getDescricaoAtividade());
                        break;
                    case "dt_entrega":
                        params.put("dt_entrega", atividade.getDtEntrega());
                        break;
                }
            }

            tx.run(query, params);
            return null;
        });
    }

    public static void delete(int idAtividade)  {
        conexao.writeTransaction(tx -> {
            String query =  "MATCH (t:Atividade {id_atividade: $idAtividade})-[r:APLICADA_POR]->(p:Professor) " +
                            "MATCH (t:Atividade {id_atividade: $idAtividade})-[b:APLICADA_EM]->(c:Turma) " + 
                            "MATCH (t:Atividade {id_atividade: $idAtividade})-[a:TIPO_DE]->(ta:TipoAtividade) " +
                            "DETACH DELETE t";

            Value params = Values.parameters("idAtividade", idAtividade);

            tx.run(query, params);
            return null;
        });
    }

}
