package com.banllproject.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banllproject.model.Professores;
import com.banllproject.view.Menu;

public class ProfessoresController extends Controller {

    @Override
    public void getById() throws SQLException {
        int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor: ");
        Professores.getById(idProfessor).imprimeProfessor();
    }

    @Override
    public void getAll() throws SQLException {
        Professores.getAll().forEach(Professores::imprimeProfessor);
    }

    @Override
    public void create() throws SQLException {
        Professores novoProfessor = new Professores(
                Menu.buscaDadoString("Digite o nome do novo professor: "),
                Menu.buscaDadoString("Digite o sobrenome do novo professor: "),
                Menu.buscaDadoString("Digite o sexo biológico do novo professor (M/F): "),
                Menu.buscaDadoString("Digite o CPF do novo professor (com máscara): "),
                convertStringToSQLDate(
                        Menu.buscaDadoString("Digite a data de nascimento do novo professor (dd/MM/aaaa): ")),
                Menu.buscaOpcaoInteira("Digite o departamento desse professor: "));
        int tableKey = Professores.create(novoProfessor);
        System.out.println("Professor " + tableKey + " criado com sucesso!");
    }

    @Override
    public void update() throws SQLException {
        int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor: ");
        Professores professorAtualizado = new Professores(
                idProfessor,
                Menu.buscaDadoString("Digite o nome do novo professor (Digite . para manter o atual): "),
                Menu.buscaDadoString("Digite o sobrenome do novo professor (Digite . para manter o atual): "),
                Menu.buscaDadoString("Digite o sexo biológico do novo professor (M/F) (Digite . para manter o atual): "),
                convertStringToSQLDate(Menu.buscaDadoString(
                        "Digite a data de nascimento do novo professor (dd/MM/aaaa) (Digite . para manter o atual): ")));
        List<String> updatedFieldNames = new ArrayList<>();

        if (!professorAtualizado.getNome().equals(".")) {
            updatedFieldNames.add("nome");
        }
        if (!professorAtualizado.getSobrenome().equals(".")) {
            updatedFieldNames.add("sobrenome");
        }
        if (!professorAtualizado.getSexoBiologico().equals(".")) {
            updatedFieldNames.add("sexo_biologico");
        }
        if (!professorAtualizado.getDtNascimento().toString().equals("1969-12-31")) {
            updatedFieldNames.add("dt_nascimento");
        }

        Professores.update(updatedFieldNames, professorAtualizado);
        System.out.println("Professor atualizado com sucesso!");
    }

    @Override
    public void delete() throws SQLException {
        int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor: ");
        Professores.delete(idProfessor);
        System.out.println("Professor deletado com sucesso!");
    }

}
