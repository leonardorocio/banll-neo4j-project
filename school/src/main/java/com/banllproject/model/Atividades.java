package com.banllproject.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.Session;

import com.banllproject.ConexaoNeo4j;
import com.banllproject.view.Menu;

public class Atividades {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();
    private int idAtividade;
    private String descricaoAtividade;
    private Date dtEntrega;

    private int fkTipoAtividade;
    private TipoAtividades fkTipoAtividadesObject;
    private int fkProfessores;
    private Professores fkProfessoresObject;
    private int fkTurma;
    private Turmas fkTurmasObject;

    // N:N
    private int idAlunoAtividade;

    public Atividades(int idAtividade, String descricaoAtividade, Date dtEntrega) {
        this.idAtividade = idAtividade;
        this.descricaoAtividade = descricaoAtividade;
        this.dtEntrega = dtEntrega;
    }

    public Atividades(String descricaoAtividade, Date dtEntrega, int fkProfessores, int fkTurma,
            int fkTipoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
        this.dtEntrega = dtEntrega;
        this.fkProfessores = fkProfessores;
        this.fkTurma = fkTurma;
        this.fkTipoAtividade = fkTipoAtividade;
    }

    public Atividades(int idAtividade, String descricaoAtividade, Date dtEntrega,
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

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtEntrega) {
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
        String sql = "SELECT * FROM atividades WHERE id_atividade = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setInt(1, idAtividade);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            int fk_professor = result.getInt("fk_professor");
            Professores professores = new Professores();
            if (fk_professor != 0) {
                professores = Professores.getById(fk_professor);
            }
            int fk_turma = result.getInt("fk_turma");
            Turmas turmas = new Turmas();
            if (fk_turma != 0) {
                turmas = Turmas.getById(fk_turma);
            }
            int fk_tipo = result.getInt("fk_tipo_atividade");
            TipoAtividades tipo = new TipoAtividades();
            if (fk_tipo != 0) {
                tipo = TipoAtividades.getById(fk_tipo);
            }
            return new Atividades(
                    result.getInt("id_atividade"),
                    result.getString("descricao_atividade"),
                    result.getDate("dt_entrega"),
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
    }

    public static int create(Atividades atividade)  {
        atividade.imprimeAtividade();
        String sql = "INSERT INTO atividades (descricao_atividade, dt_entrega, fk_professor, fk_turma, fk_tipo_atividade) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, atividade.getDescricaoAtividade());
        preparedStatement.setDate(2, atividade.getDtEntrega());
        preparedStatement.setInt(3, atividade.getFkProfessores());
        preparedStatement.setInt(4, atividade.getFkTurma());
        preparedStatement.setInt(5, atividade.getFkTipoAtividade());
        preparedStatement.execute();
        ResultSet keys = preparedStatement.getGeneratedKeys();
        int keyValue = -1;
        if (keys.next()) {
            keyValue = keys.getInt(1);
            return keyValue;
        }
        preparedStatement.close();
        return keyValue;
    }

    public static void update(List<String> updatedFields, Atividades atividade)  {
        String setFields = "SET ";
        for (int i = 0; i < updatedFields.size(); i++) {
            if (i < updatedFields.size() - 1)
                setFields += updatedFields.get(i) + " = ?,";
            else
                setFields += updatedFields.get(i) + " = ?";
        }
        String sql = "UPDATE atividades " + setFields + " WHERE id_atividade = ?";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        int i;
        for (i = 1; i <= updatedFields.size(); i++) {
            if (updatedFields.get(i - 1).equals("descricao_atividade")) {
                preparedStatement.setString(i, atividade.getDescricaoAtividade());
            }
            if (updatedFields.get(i - 1).equals("dt_entrega")) {
                preparedStatement.setDate(i, atividade.getDtEntrega());
            }
        }
        preparedStatement.setInt(i, atividade.getIdAtividade());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static void delete(int idAtividade)  {
        String sql = "DELETE FROM atividades WHERE id_atividade = ?";
        PreparedStatement preparedStatement = conexao.prepareStatement(sql);
        preparedStatement.setInt(1, idAtividade);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static List<Atividades> getAll()  {
        String sql = "SELECT * FROM atividades";
        Statement statement = conexao.createStatement();
        ResultSet resultList = statement.executeQuery(sql);
        List<Atividades> atividades = new ArrayList<>();
        while (resultList.next()) {
            int fk_professor = resultList.getInt("fk_professor");
            Professores professores = new Professores();
            if (fk_professor != 0) {
                professores = Professores.getById(fk_professor);
            }
            int fk_turma = resultList.getInt("fk_turma");
            Turmas turmas = new Turmas();
            if (fk_turma != 0) {
                turmas = Turmas.getById(fk_turma);
            }
            int fk_tipo = resultList.getInt("fk_tipo_atividade");
            TipoAtividades tipo = new TipoAtividades();
            if (fk_tipo != 0) {
                tipo = TipoAtividades.getById(fk_tipo);
            }
            atividades.add(new Atividades(
                    resultList.getInt("id_atividade"),
                    resultList.getString("descricao_atividade"),
                    resultList.getDate("dt_entrega"),
                    fk_professor,
                    professores,
                    fk_turma,
                    turmas,
                    fk_tipo,
                    tipo));
        }
        return atividades;
    }

}
