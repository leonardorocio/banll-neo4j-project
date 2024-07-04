package com.banllproject.controller;

import java.sql.SQLException;

import com.banllproject.model.Operacoes;
import com.banllproject.view.Menu;

public class OperacoesController {
    
    public void consultaDisciplinaPorCurso() throws SQLException {
        int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso (-1 para buscar todos): ");
        Operacoes.buscaDisciplinaPorCurso(idCurso);
    }

    public void consultaProfessoresPorDepartamento() throws SQLException {
        int idDepartamento = Menu.buscaOpcaoInteira("Digite o id do departamento (-1 para buscar todos): ");
        Operacoes.buscaProfessoresPorDepartamento(idDepartamento);
    }

    public void consultaAlunosPorCurso() throws SQLException {
        int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso (-1 para buscar todos): ");
        Operacoes.buscaAlunosPorCurso(idCurso);
    }

    public void consultaTurmasPorSemestre() throws SQLException {
        String anoSemestre = Menu.buscaDadoString("Digite o ano e semestre da disciplina (AAAA/S) (vazio para buscar todas): ");
        Operacoes.buscaTurmasPorSemestre(anoSemestre);
    }
    
}
