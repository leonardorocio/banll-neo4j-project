package com.banllproject.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banllproject.model.Cursos;
import com.banllproject.view.Menu;

public class CursosController extends Controller {

    public void getById() throws SQLException {
        int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso:");
        Cursos.getById(idCurso).imprimeCurso();
    }

    public void getAll() throws SQLException {
        Cursos.getAll().forEach(Cursos::imprimeCurso);
    }

    public void create() throws SQLException {
        Cursos novoCurso = new Cursos(
                Menu.buscaDadoString("Digite o nome do novo curso: "),
                Menu.buscaOpcaoInteira("Digite a duração mínima (semestres) do novo curso: "),
                Menu.buscaOpcaoInteira("Digite a duração máxima (semestres) do novo curso: "),
                Menu.buscaDadoString("Digite a sigla do novo curso: "));
        int tableKey = Cursos.create(novoCurso);
        System.out.println("Curso " + tableKey + " criado com sucesso!");
    }

    public void update() throws SQLException {
        int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso: ");
        Cursos cursoAtualizado = new Cursos(
                idCurso,
                Menu.buscaDadoString("Digite o nome do curso (Digite . para manter o atual): "),
                Menu.buscaOpcaoInteira("Digite a duração mínima (semestres) do curso (Digite 0 para manter o atual): "),
                Menu.buscaOpcaoInteira("Digite a duração máxima (semestres) do curso (Digite 0 para manter o atual): "),
                Menu.buscaDadoString("Digite a sigla do curso (Digite . para manter o atual): "));
        List<String> updatedFieldNames = new ArrayList<>();

        if (!cursoAtualizado.getNome().equals(".")) {
            updatedFieldNames.add("nome");
        }

        if (cursoAtualizado.getDuracaoMinima() != 0) {
            updatedFieldNames.add("duracao_minima");
        }

        if (cursoAtualizado.getDuracaoMaxima() != 0) {
            updatedFieldNames.add("duracao_maxima");
        }

        if (!cursoAtualizado.getSigla().equals(".")) {
            updatedFieldNames.add("sigla");
        }

        Cursos.update(updatedFieldNames, cursoAtualizado);
        System.out.println("Curso atualizado com sucesso!");
    }

    public void delete() throws SQLException {
        int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso:");
        Cursos.delete(idCurso);
        System.out.println("Curso deletado com sucesso!");
    }
}
