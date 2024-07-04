// package com.banllproject.controller;

// import java.sql.SQLException;

// import com.banllproject.model.Notas;
// import com.banllproject.view.Menu;

// public class NotasController extends Controller {

//     @Override
//     public void create() throws SQLException {
//         Notas novaNota = new Notas(
//             Menu.buscaOpcaoInteira("Digite o id do aluno: "),
//             Menu.buscaOpcaoInteira("Digite o id da atividade: "),
//             Menu.buscaDadoDouble("Digite a nota do aluno nesta atividade: ")
//         );
//         Notas.create(novaNota);
//         System.out.println("Nota inserida com sucesso!");
//     }

//     @Override
//     public void delete() throws SQLException {
//         int idAluno = Menu.buscaOpcaoInteira("Digite o id do aluno: ");
//         int idAtividade = Menu.buscaOpcaoInteira("Digite o id da atividade: ");
//         Notas.delete(idAluno, idAtividade);
//         System.out.println("Nota deletada com sucesso!");
//     }

//     @Override
//     public void getAll() throws SQLException {
//         Notas.getAll().forEach(Notas::imprimeNota);
//     }

//     @Override
//     public void getById() throws SQLException {
//         int idAluno = Menu.buscaOpcaoInteira("Digite o id do aluno: ");
//         int idAtividade = Menu.buscaOpcaoInteira("Digite o id da atividade: ");
//         Notas.getById(idAluno, idAtividade).imprimeNota();
//     }

//     @Override
//     public void update() throws SQLException {
//         Notas notaAtualizada = new Notas(
//             Menu.buscaOpcaoInteira("Digite o id do aluno: "),
//             Menu.buscaOpcaoInteira("Digite o id da atividade: "),
//             Menu.buscaDadoDouble("Digite a nota do aluno nesta atividade: ")
//         );
//         Notas.update(notaAtualizada);
//         System.out.println("Nota atualizada com sucesso!");
//     }
    
// }
