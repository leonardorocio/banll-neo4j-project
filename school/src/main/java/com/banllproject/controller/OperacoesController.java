// package com.banllproject.controller;

// import java.sql.SQLException;

// import com.banllproject.model.Operacoes;
// import com.banllproject.view.Menu;

// public class OperacoesController {
    
//     public void consultaDisciplinaPorCurso() throws SQLException {
//         int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso (-1 para buscar todos): ");
//         Operacoes.buscaDisciplinaPorCurso(idCurso);
//     }

//     public void consultaProfessoresPorDepartamento() throws SQLException {
//         int idDepartamento = Menu.buscaOpcaoInteira("Digite o id do departamento (-1 para buscar todos): ");
//         Operacoes.buscaProfessoresPorDepartamento(idDepartamento);
//     }

//     public void consultaAlunosPorCurso() throws SQLException {
//         int idCurso = Menu.buscaOpcaoInteira("Digite o id do curso (-1 para buscar todos): ");
//         Operacoes.buscaAlunosPorCurso(idCurso);
//     }

//     public void consultaTurmasPorSemestre() throws SQLException {
//         String anoSemestre = Menu.buscaDadoString("Digite o ano e semestre da disciplina (AAAA/S) (vazio para buscar todas): ");
//         Operacoes.buscaTurmasPorSemestre(anoSemestre);
//     }
    
//     public void consultaTaxaDeReprovacaoDeDisciplinaPorSemestre() throws SQLException {
//         int idDisciplina = Menu.buscaOpcaoInteira("Digite o id da disciplina: ");
//         Operacoes.buscaTaxaDeReprovacaoDeDisciplinaPorSemestre(idDisciplina);
//     }

//     public void consultaAlunoPorTurma() throws SQLException {
//         int idTurma = Menu.buscaOpcaoInteira("Digite o id da turma: ");
//         Operacoes.buscaAlunoPorTurma(idTurma);
//     }

//     public void consultaPercentualGeneroAlunosPorTurma() throws SQLException {
//         int idTurma = Menu.buscaOpcaoInteira("Digite o id da turma: ");
//         Operacoes.buscaPercentualGeneroAlunosPorTurma(idTurma);
//     }

//     public void consultaDisciplinasCursadasPorAluno() throws SQLException {
//         int idAluno = Menu.buscaOpcaoInteira("Digite o id do aluno: ");
//         Operacoes.buscaDisciplinasCursadasPorAluno(idAluno);
//     }

//     public void consultaDisciplinasLecionadasPorProfessorNoSemestre() throws SQLException {
//         int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor: ");
//         String anoSemestre = Menu.buscaDadoString("Digite o ano e semestre desejado (AAAA/S): ");
//         Operacoes.buscaDisciplinasLecionadasPorProfessorNoSemestre(idProfessor, anoSemestre);
//     }
    
//     public void consultaAtividadesAplicadasPorProfessor() throws SQLException {
//         int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor: ");
//         Operacoes.buscaAtividadesAplicadasPorProfessor(idProfessor);
//     }

//     public void consultaAtividadesPorTurmaEProfessor() throws SQLException {
//         int idProfessor = Menu.buscaOpcaoInteira("Digite o id do professor (-1 para todos): ");
//         Operacoes.buscaAtividadePorTurmaEProfessor(idProfessor);
//     }

//     public void consultaAtividadePorAlunoPorDisciplina() throws SQLException {
//         int idAluno = Menu.buscaOpcaoInteira("Digite o id do aluno: ");
//         int idDisciplina = Menu.buscaOpcaoInteira("Digite o id da disciplina: ");
//         Operacoes.buscaAtividadePorAlunoPorDisciplina(idAluno, idDisciplina);
//     }

//     public void consultaMediaDeNotasDeAtividade() throws SQLException {
//         Operacoes.buscaMediaDeNotasDaAtividade();
//     }
// }
