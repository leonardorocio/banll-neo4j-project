// package com.banllproject.model;

// import java.sql.Statement;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

// import org.neo4j.driver.Session;

// import com.banllproject.ConexaoNeo4j;
// import com.banllproject.view.Menu;

// public class Notas {

//     private static Session conexao = ConexaoNeo4j.getInstance().getSession();
//     private int idAluno;
//     private int idAtividade;
//     private double nota;

//     public Notas(int idAluno, int idAtividade, double nota) {
//         this.idAluno = idAluno;
//         this.idAtividade = idAtividade;
//         this.nota = nota;
//     }

//     public Notas() {
//     }

//     public int getIdAluno() {
//         return idAluno;
//     }

//     public void setIdAluno(int idAluno) {
//         this.idAluno = idAluno;
//     }

//     public int getIdAtividade() {
//         return idAtividade;
//     }

//     public void setIdAtividade(int idAtividade) {
//         this.idAtividade = idAtividade;
//     }

//     public double getNota() {
//         return nota;
//     }

//     public void setNota(double nota) {
//         this.nota = nota;
//     }

//     public void imprimeNota() {
//         if (this.getIdAluno() == 0 || this.getIdAtividade() == 0)
//             return;
//         try {
//             Alunos aluno = Alunos.getById(this.idAluno);
//             Atividades atividade = Atividades.getById(this.idAtividade);
    
//             System.out.println(
//                     String.format("\nAluno: %s\nAtividade: %s\nNota: %.2f",
//                             String.format("%s %s", aluno.getNome(), aluno.getSobrenome()),
//                                     atividade.getDescricaoAtividade(), this.nota));
//             Menu.pausaMenu();
//         } catch (SQLException e) {
//             System.out.println("Erro ao buscar aluno ou atividade!");
//             e.printStackTrace();
//         }
//     }

//     public static Notas getById(int idAluno, int idAtividade) throws SQLException {
//         String sql = "SELECT * FROM atividade_aluno WHERE id_aluno = ? AND id_atividade = ?";
//         PreparedStatement statment = conexao.prepareStatement(sql);
//         statment.setInt(1, idAluno);
//         statment.setInt(2, idAtividade);
//         ResultSet resultSet = statment.executeQuery();
//         if (resultSet.next()) {
//             return new Notas(
//                     resultSet.getInt("id_aluno"),
//                     resultSet.getInt("id_atividade"),
//                     resultSet.getDouble("nota")

//             );
//         } else {
//             System.out.println("Não foi encontrada nota para esse aluno e essa atividade");
//             return new Notas();
//         }
//     }

//     public static List<Notas> getAll() throws SQLException {
//         String sql = "SELECT * FROM atividade_aluno;";
//         Statement statement = conexao.createStatement();
//         ResultSet resultSet = statement.executeQuery(sql);
//         List<Notas> notas = new ArrayList<>();
//         while (resultSet.next()) {
//             notas.add(new Notas(
//                     resultSet.getInt("id_aluno"),
//                     resultSet.getInt("id_atividade"),
//                     resultSet.getDouble("nota")));
//         }
//         return notas;
//     }

//     public static void create(Notas nota) throws SQLException {
//         String sql = "INSERT INTO atividade_aluno (id_aluno, id_atividade, nota) VALUES (?, ?, ?)";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, nota.getIdAluno());
//         statement.setInt(2, nota.getIdAtividade());
//         statement.setDouble(3, nota.getNota());
//         statement.execute();
//         ResultSet keys = statement.getGeneratedKeys();
//         statement.close();
//     }

//     public static void update(Notas nota) throws SQLException {
//         String sql = "UPDATE atividade_aluno SET nota = ? WHERE id_aluno = ? AND id_atividade = ?";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setDouble(1, nota.getNota());
//         statement.setInt(2, nota.getIdAluno());
//         statement.setInt(3, nota.getIdAtividade());
//         statement.executeUpdate();
//         statement.close();
//     }

//     public static void delete(int idAluno, int idAtividade) throws SQLException {
//         String sql = "DELETE FROM atividade_aluno WHERE id_aluno = ? AND id_atividade = ?";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idAluno);
//         statement.setInt(2, idAtividade);
//         statement.executeUpdate();
//         statement.close();
//     }
// }
