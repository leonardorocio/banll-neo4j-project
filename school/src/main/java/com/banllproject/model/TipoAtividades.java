package com.banllproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.Values;

import com.banllproject.ConexaoNeo4j;
import com.banllproject.view.Menu;

public class TipoAtividades {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idTipoAtividade;
    private String descricaoTipoAtividade;

    public TipoAtividades(int idTipoAtividade, String descricaoTipoAtividade) {
        this.idTipoAtividade = idTipoAtividade;
        this.descricaoTipoAtividade = descricaoTipoAtividade;
    }

    public TipoAtividades(String descricaoTipoAtividade) {
        this.descricaoTipoAtividade = descricaoTipoAtividade;
    }

    public TipoAtividades() {
    }

    public int getIdTipoAtividade() {
        return idTipoAtividade;
    }

    public void setIdTipoAtividade(int idTipoAtividade) {
        this.idTipoAtividade = idTipoAtividade;
    }

    public String getDescricaoTipoAtividade() {
        return descricaoTipoAtividade;
    }

    public void setDescricaoTipoAtividade(String descricaoTipoAtividade) {
        this.descricaoTipoAtividade = descricaoTipoAtividade;
    }

    public void imprimeTipoAtividade() {
        if (this.getIdTipoAtividade() == 0)
            return;
        System.out.println(
                String.format("\nTipo de Atividade\nID: %d\nDescrição: %s", this.getIdTipoAtividade(), this.getDescricaoTipoAtividade()));
        Menu.pausaMenu();
    }

    public static TipoAtividades getById(int idTipoAtividade)  {
        return conexao.readTransaction(tx -> {
            String query = "MATCH (at:TipoAtividade {id_tipo_atividade: $idTipoAtividade}) " +
                           "RETURN at.id_tipo_atividade as id_tipo_atividade, at.descricao as descricao ";
        
            Result result = tx.run(query, Values.parameters("idTipoAtividade", idTipoAtividade));
        
            if (result.hasNext()) {
                Record record = result.next();
        
                return new TipoAtividades(
                    record.get("id_tipo_atividade").asInt(),
                    record.get("descricao").asString());
            } else {
                System.out.println("Turma não encontrada com esse ID!");
                return new TipoAtividades();
            }
        });   
    }

    public static List<TipoAtividades> getAll()  {
        return conexao.readTransaction(tx -> {
            List<TipoAtividades> tipoAtividades = new ArrayList<>();
            String query = "MATCH (at:TipoAtividade) " +
                           "RETURN at.id_tipo_atividade as id_tipo_atividade, at.descricao as descricao";
        
            Result result = tx.run(query);
        
            while (result.hasNext()) {
                Record record = result.next();
        
                tipoAtividades.add(new TipoAtividades(
                    record.get("id_tipo_atividade").asInt(),
                    record.get("descricao").asString()));
            }
            return tipoAtividades;
        });
    }

    public static int create(TipoAtividades tipoAtividades)  {
        return conexao.writeTransaction(tx -> {
            String query = "MERGE (ta:TipoAtividade {id_tipo_atividade: $idTipoAtividade, descricao: $descricao}) " + 
                            "RETURN ta.id_tipo_atividade as id";
            
            Result result = tx.run(query, 
            Values.parameters(                  "idTipoAtividade", new Random().nextInt(0, 100000),
                                                "descricao", tipoAtividades.getDescricaoTipoAtividade()));

            if (result.hasNext()) {
                return result.next().get("id").asInt();
            } else {
                return -1;
            }
        });
    }

    public static void update(TipoAtividades tipoAtividades)  {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:TipoAtividade {id_tipo_atividade: $idTipoAtividade}) " +
                           "SET c.descricao = $descricao";

            tx.run(query, Values.parameters("idTipoAtividade", tipoAtividades.getIdTipoAtividade(),
                                                              "descricao", tipoAtividades.getDescricaoTipoAtividade()));

            return null;
        });
    }

    public static void delete(int idTipoAtividade)  {
        conexao.writeTransaction(tx -> {
            String query = "MATCH (c:TipoAtividade {id_tipo_atividade: $idTipoAtividade}) " +
                           "DETACH DELETE c";

            Value params = Values.parameters("idTipoAtividade", idTipoAtividade);

            tx.run(query, params);
            return null;
        });
    }
}
