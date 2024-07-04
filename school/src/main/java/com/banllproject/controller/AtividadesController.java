package com.banllproject.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banllproject.model.Atividades;
import com.banllproject.view.Menu;

public class AtividadesController extends Controller {

    @Override
    public void getById() throws SQLException {
        int idAtividade = Menu.buscaOpcaoInteira("Digite o id da atividade: ");
        Atividades.getById(idAtividade).imprimeAtividade();
    }

    @Override
    public void getAll() throws SQLException {
        Atividades.getAll().forEach(Atividades::imprimeAtividade);
    }

    @Override
    public void create() throws SQLException {
        Atividades novaAtividade = new Atividades(
                Menu.buscaDadoString("Digite a descrição da nova atividade: "),
                convertStringToSQLDate(Menu.buscaDadoString("Digite a data de entrega da nova atividade (dd/MM/aaaa): ")),
                Menu.buscaOpcaoInteira("Digite o id do professor que aplicou a atividade: "),
                Menu.buscaOpcaoInteira("Digite o id da turma a qual foi aplicada a atividade: "),
                Menu.buscaOpcaoInteira("Digite o id do tipo da atividade: "));
        int tableKey = Atividades.create(novaAtividade);
        System.out.println("Atividade " + tableKey + " criada com sucesso!");
    }

    @Override
    public void update() throws SQLException {
        int idAtividade = Menu.buscaOpcaoInteira("Digite o id da atividade: ");
        Atividades atividadeAtualizada = new Atividades(
                idAtividade,
                Menu.buscaDadoString("Digite a descrição da atividade (Digite . para manter o atual): "),
                convertStringToSQLDate(Menu
                        .buscaDadoString("Digite a data de entrega da atividade (dd/MM/aaaa) (Digite . para manter o atual): ")));

        List<String> updatedFieldNames = new ArrayList<>();

        if (!atividadeAtualizada.getDescricaoAtividade().equals(".")) {
            updatedFieldNames.add("descricao_atividade");
        }

        if (!atividadeAtualizada.getDtEntrega().toString().equals("1969-12-31")) {
            updatedFieldNames.add("dt_entrega");
        }

        Atividades.update(updatedFieldNames, atividadeAtualizada);
        System.out.println("Atividade atualizada com sucesso!");
    }

    @Override
    public void delete() throws SQLException {
        int idAtividade = Menu.buscaOpcaoInteira("Digite o id da atividade: ");
        Atividades.delete(idAtividade);
        System.out.println("Atividade deletada com sucesso!");
    }

}
