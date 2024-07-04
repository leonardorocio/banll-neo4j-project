package com.banllproject.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banllproject.model.Departamentos;
import com.banllproject.view.Menu;

public class DepartamentosController extends Controller {

    @Override
    public void getById() throws SQLException {
        int idDepartamento = Menu.buscaOpcaoInteira("Digite o id do departamento: ");
        Departamentos.getById(idDepartamento).imprimeDepartamento();
    }

    @Override
    public void getAll() throws SQLException {
        Departamentos.getAll().forEach(Departamentos::imprimeDepartamento);
    }

    @Override
    public void create() throws SQLException {
        Departamentos novoDepartamento = new Departamentos(
                Menu.buscaDadoString("Digite o nome do novo departamento: "),
                Menu.buscaDadoString("Digite a sigla do novo departamento: "));
        int tableKey = Departamentos.create(novoDepartamento);
        System.out.println("Departamento " + tableKey + " criado com sucesso!");
    }

    @Override
    public void update() throws SQLException {
        int idDepartamento = Menu.buscaOpcaoInteira("Digite o id do departamento");
        Departamentos departamentoAtualizado = new Departamentos(
                idDepartamento,
                Menu.buscaDadoString("Digite o nome do departamento: "),
                Menu.buscaDadoString("Digite a sigla do departamento: "));
        List<String> updatedFieldNames = new ArrayList<>();

        if (!departamentoAtualizado.getNome().equals(".")) {
            updatedFieldNames.add("departamento");
        }

        if (!departamentoAtualizado.getSigla().equals(".")) {
            updatedFieldNames.add("sigla");
        }

        Departamentos.update(updatedFieldNames, departamentoAtualizado);
        System.out.println("Departamento atualizado com sucesso!");
    }

    @Override
    public void delete() throws SQLException {
        int idDepartamento = Menu.buscaOpcaoInteira("Digite o id do departamento");
        Departamentos.delete(idDepartamento);
        System.out.println("Departamento deletado com sucesso!");
    }

}
