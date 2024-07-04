package com.banllproject.controller;

import java.sql.SQLException;

import com.banllproject.model.TipoAtividades;
import com.banllproject.view.Menu;

public class TipoAtividadesController extends Controller {

    @Override
    public void create() throws SQLException {
        TipoAtividades novoTipoAtividades = new TipoAtividades(
            Menu.buscaDadoString("Digite a descrição dessa atividade avaliativa: ")
        );
        int tableKey = TipoAtividades.create(novoTipoAtividades);
        System.out.println("Tipo de atividade " + tableKey + " criado com sucesso!");
    }

    @Override
    public void delete() throws SQLException {
        int idTipoAtividade = Menu.buscaOpcaoInteira("Digite o id do tipo de atividade: ");
        TipoAtividades.delete(idTipoAtividade);
    }

    @Override
    public void getAll() throws SQLException {
        TipoAtividades.getAll().forEach(TipoAtividades::imprimeTipoAtividade);
    }

    @Override
    public void getById() throws SQLException {
        int idTipoAtividade = Menu.buscaOpcaoInteira("Digite o id do tipo de atividade: ");
        TipoAtividades.getById(idTipoAtividade).imprimeTipoAtividade();
    }

    @Override
    public void update() throws SQLException {
        TipoAtividades tipoAtividadesAtualizado = new TipoAtividades(
            Menu.buscaOpcaoInteira("Digite o id do tipo de atividade: "),
            Menu.buscaDadoString("Digite a descrição do tipo de atividade: ")
        );
        TipoAtividades.update(tipoAtividadesAtualizado);
        System.out.println("Tipo de atividade atualizado com sucesso!");
    }
    
}
