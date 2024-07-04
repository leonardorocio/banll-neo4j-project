// package com.banllproject.model;

// import java.sql.Statement;
// import java.sql.Connection;
// import java.sql.Date;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.Map.Entry;

// import org.neo4j.driver.Session;

// import com.banllproject.ConexaoNeo4j;
// import com.banllproject.view.Menu;
// import com.google.gson.Gson;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;

// public class Operacoes {

//     private static Session conexao = ConexaoNeo4j.getInstance().getSession();

//     public static void buscaDisciplinaPorCurso(int idCurso) throws SQLException {
//         String sql = "select d.id_disciplina, d.nome, d.carga_horaria from cursos_disciplinas cd " +
//                 "join disciplinas d on cd.id_disciplina = d.id_disciplina " +
//                 "where id_curso = ? or ? = -1 order by 2;";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idCurso);
//         statement.setInt(2, idCurso);
//         ResultSet result = statement.executeQuery();
//         Disciplinas disciplinas = new Disciplinas();
//         while (result.next()) {
//             if (result.isFirst()) {
//                 Cursos.getById(idCurso).imprimeCurso();
//             }
//             disciplinas = new Disciplinas(
//                     result.getInt("id_disciplina"),
//                     result.getString("nome"),
//                     result.getInt("carga_horaria"));
//             disciplinas.imprimeDisciplina();
//         }
//     }

//     public static void buscaProfessoresPorDepartamento(int idDepartamento) throws SQLException {
//         String sql = "select p.id_professor, p.nome, p.sobrenome, p.cpf, p.sexo_biologico, p.dt_nascimento, p.fk_departamento from professores p "
//                 +
//                 "where p.fk_departamento = ? or ? = -1 order by 2";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idDepartamento);
//         statement.setInt(2, idDepartamento);
//         ResultSet resultList = statement.executeQuery();
//         Professores professores = new Professores();
//         while (resultList.next()) {
//             int fk = resultList.getInt("fk_departamento");
//             Departamentos departamentos = new Departamentos();
//             if (fk != 0) {
//                 departamentos = Departamentos.getById(fk);
//             }
//             professores = new Professores(
//                     resultList.getInt("id_professor"),
//                     resultList.getString("nome"),
//                     resultList.getString("sobrenome"),
//                     resultList.getString("sexo_biologico"),
//                     resultList.getString("cpf"),
//                     resultList.getDate("dt_nascimento"),
//                     fk,
//                     departamentos);
//             professores.imprimeProfessor();
//         }
//     }

//     public static void buscaAlunosPorCurso(int idCurso) throws SQLException {
//         String sql = "select a.id_aluno, a.nome, a.sobrenome, a.cpf, a.fk_curso, a.dt_nascimento, a.dt_ingresso, a.sexo_biologico from alunos a "
//                 +
//                 "where a.fk_curso = ? or ? = -1 order by 2";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idCurso);
//         statement.setInt(2, idCurso);
//         ResultSet result = statement.executeQuery();
//         Alunos aluno = new Alunos();
//         while (result.next()) {
//             int fk = result.getInt("fk_curso");
//             Cursos cursos = new Cursos();
//             if (fk != 0) {
//                 cursos = Cursos.getById(fk);
//             }
//             aluno = new Alunos(
//                     result.getInt("id_aluno"),
//                     result.getString("nome"),
//                     result.getString("sobrenome"),
//                     result.getDate("dt_nascimento"),
//                     result.getString("cpf"),
//                     result.getString("sexo_biologico"),
//                     result.getDate("dt_ingresso"),
//                     fk,
//                     cursos);
//             aluno.imprimeAluno();
//         }
//     }

//     public static void buscaTurmasPorSemestre(String anoSemestre) throws SQLException {
//         String sql = "select t.id_turma, t.ano_semestre, t.local_aula, t.fk_disciplina, d.nome from turmas t " +
//                 "join disciplinas d on d.id_disciplina = t.fk_disciplina " +
//                 "where t.ano_semestre = ? or ? = '' order by 3";
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setString(1, anoSemestre);
//         statement.setString(2, anoSemestre);
//         ResultSet resultSet = statement.executeQuery();
//         Turmas turma = new Turmas();
//         while (resultSet.next()) {
//             int fk = resultSet.getInt("fk_disciplina");
//             Disciplinas disciplinas = new Disciplinas();
//             if (fk != 0) {
//                 disciplinas = Disciplinas.getById(fk);
//             }
//             turma = new Turmas(
//                     resultSet.getInt("id_turma"),
//                     resultSet.getString("ano_semestre"),
//                     resultSet.getString("local_aula"),
//                     fk,
//                     disciplinas);
//             turma.imprimeTurma();
//         }
//     }

//     private static void mostraTaxaDeReprovacao(int idTurma, String anoSemestre, int idDisciplina, String disciplina,
//             double aprovacao, double reprovacao) {
//         System.out.println(
//                 String.format("""

//                         ID da turma: %d
//                         Ano e semestre: %s
//                         ID da disciplina: %d
//                         Disciplina: %s
//                         Taxa de Aprovação: %.2f%%
//                         Taxa de Reprovação: %.2f%%
//                         """, idTurma, anoSemestre, idDisciplina, disciplina, aprovacao, reprovacao));
//     }

//     public static void buscaTaxaDeReprovacaoDeDisciplinaPorSemestre(int idDisciplina) throws SQLException {
//         String sql = """
//                     select
//                     n.id_turma,
//                     n.ano_semestre,
//                     n.nome_disciplina as nome_disciplina,
//                     n.id_disciplina,
//                     (count(
//                         case
//                             when n.media >= 7 then 1
//                         end
//                     )/count(*)::decimal) * 100 as aprovados,
//                     (count(
//                         case
//                             when n.media <= 7 then 1
//                         end
//                     )/ count(*)::decimal) * 100 as reprovados
//                 from (
//                     select
//                         aa.id_aluno,
//                         t.id_turma,
//                         d.id_disciplina,
//                         d.nome as nome_disciplina,
//                         t.ano_semestre,
//                         avg(aa.nota) as media
//                     from atividade_aluno aa
//                         join atividades a on a.id_atividade = aa.id_atividade
//                         join turmas t on t.id_turma = a.fk_turma
//                         join disciplinas d on d.id_disciplina = t.fk_disciplina
//                         and d.id_disciplina = ?
//                     group by aa.id_aluno, t.id_turma, d.id_disciplina
//                     order by 1
//                 ) n
//                 where n.id_disciplina = ?
//                 group by n.id_turma, n.ano_semestre, n.nome_disciplina, n.id_disciplina
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idDisciplina);
//         statement.setInt(2, idDisciplina);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Disciplinas.getById(idDisciplina).imprimeDisciplina();
//             }
//             Operacoes.mostraTaxaDeReprovacao(
//                     resultSet.getInt("id_turma"),
//                     resultSet.getString("ano_semestre"),
//                     resultSet.getInt("id_disciplina"),
//                     resultSet.getString("nome_disciplina"),
//                     resultSet.getDouble("aprovados"),
//                     resultSet.getDouble("reprovados"));
//             Menu.pausaMenu();
//         }
//     }

//     private static void mostraAlunoComMedia(int idAluno, String nomeCompleto, double media) {
//         System.out.println(
//                 String.format("""
//                         ID do aluno: %d
//                         Nome completo: %s
//                         Média: %.2f
//                         """, idAluno, nomeCompleto, media));
//     }

//     public static void buscaAlunoPorTurma(int idTurma) throws SQLException {
//         String sql = """
//                     select
//                     a.id_aluno,
//                     a.nome, a.sobrenome,
//                     avg(aa.nota) as nota
//                 from turma_aluno ta
//                     join alunos a on ta.id_aluno = a.id_aluno
//                     join atividades at on at.fk_turma = 1
//                     join atividade_aluno aa on aa.id_atividade = at.id_atividade
//                         and aa.id_aluno = a.id_aluno
//                 where ta.id_turma = ?
//                 group by 1
//                 order by 2
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idTurma);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Turmas.getById(idTurma).imprimeTurma();
//             }
//             mostraAlunoComMedia(
//                     resultSet.getInt("id_aluno"),
//                     resultSet.getString("nome") + " " + resultSet.getString("sobrenome"),
//                     resultSet.getDouble("nota"));
//             Menu.pausaMenu();
//         }
//     }

//     public static void buscaPercentualGeneroAlunosPorTurma(int idTurma) throws SQLException {
//         String sql = """
//                     select
//                     case
//                         when count(*) = 0 then null
//                         else (count(
//                         case
//                             when a.sexo_biologico = 'M' then 1
//                         end
//                     ) / count(*)::decimal) * 100
//                     end as percentual_meninos,
//                     case
//                         when count(*) = 0 then null
//                         else (count(
//                         case
//                             when a.sexo_biologico = 'F' then 1
//                         end
//                     ) / count(*)::decimal) * 100
//                     end as percentual_meninas
//                 from turma_aluno ta
//                     join alunos a on a.id_aluno = ta.id_aluno
//                 where ta.id_turma = ?
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idTurma);
//         ResultSet resultSet = statement.executeQuery();
//         if (resultSet.next()) {
//             Turmas.getById(idTurma).imprimeTurma();
//             System.out.println(
//                     String.format(
//                             "Meninos: %.2f%%\nMeninas: %.2f%%",
//                             resultSet.getDouble("percentual_meninos"), resultSet.getDouble("percentual_meninas")));
//             Menu.pausaMenu();
//         } else {
//             System.out.println("Não foi possível buscar os dados para a turma informada!");
//         }
//     }

//     private static void mostraDisciplinaParticipada(int idTurma, String nome, int cargaHoraria, String anoSemestre) {
//         System.out.println(
//                 String.format(
//                         """
//                                 ID da turma: %d
//                                 Disciplina: %s
//                                 Carga horária: %d
//                                 Ano e semestre: %s
//                                 """, idTurma, nome, cargaHoraria, anoSemestre));
//     }

//     public static void buscaDisciplinasCursadasPorAluno(int idAluno) throws SQLException {
//         String sql = """
//                     select
//                     t.id_turma,
//                     d.nome,
//                     d.carga_horaria,
//                     t.ano_semestre
//                 from turma_aluno ta
//                     join turmas t on t.id_turma = ta.id_turma
//                     join disciplinas d on d.id_disciplina = t.fk_disciplina
//                 where ta.id_aluno = ?
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idAluno);
//         ResultSet resultSet = statement.executeQuery();

//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Alunos.getById(idAluno).imprimeAluno();
//             }
//             mostraDisciplinaParticipada(
//                     resultSet.getInt("id_turma"),
//                     resultSet.getString("nome"),
//                     resultSet.getInt("carga_horaria"),
//                     resultSet.getString("ano_semestre"));
//             Menu.pausaMenu();
//         }
//     }

//     public static void buscaDisciplinasLecionadasPorProfessorNoSemestre(int idProfessor, String anoSemestre)
//             throws SQLException {
//         String sql = """
//                     select
//                     t.id_turma,
//                     d.nome,
//                     d.carga_horaria,
//                     t.ano_semestre
//                 from turma_professor tp
//                     join turmas t on t.id_turma = tp.id_turma and t.ano_semestre = ?
//                     join disciplinas d on d.id_disciplina = t.fk_disciplina
//                 where tp.id_professor = ?
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setString(1, anoSemestre);
//         statement.setInt(2, idProfessor);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Professores.getById(idProfessor).imprimeProfessor();
//             }
//             mostraDisciplinaParticipada(
//                     resultSet.getInt("id_turma"),
//                     resultSet.getString("nome"),
//                     resultSet.getInt("carga_horaria"),
//                     resultSet.getString("ano_semestre"));
//             Menu.pausaMenu();
//         }
//     }

//     private static void mostraAtividadeAplicada(int idAtividade, String tipo, String descricao, Date dataEntrega) {
//         System.out.println(
//                 String.format("""
//                             ID da atividade: %d
//                             Tipo: %s
//                             Descrição: %s
//                             Data: %s
//                         """, idAtividade, tipo, descricao, dataEntrega.toString()));
//     }

//     public static void buscaAtividadesAplicadasPorProfessor(int idProfessor) throws SQLException {
//         String sql = """
//                     select
//                     a.id_atividade,
//                     ta.descricao,
//                     a.descricao_atividade ,
//                     a.dt_entrega
//                 from atividades a
//                     join tipo_atividade ta on ta.id_tipo_atividade = a.fk_tipo_atividade
//                 where a.fk_professor = ?
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idProfessor);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Professores.getById(idProfessor).imprimeProfessor();
//             }
//             mostraAtividadeAplicada(
//                     resultSet.getInt("id_atividade"), resultSet.getString("descricao"),
//                     resultSet.getString("descricao_atividade"), resultSet.getDate("dt_entrega"));
//             Menu.pausaMenu();
//         }
//     }

//     private static void mostraTurmaParticipada(int idTurma, String nome, String anoSemestre, String localAula) {
//         System.out.println(
//                 String.format(
//                         """
//                                 ID da turma: %d
//                                 Disciplina: %s
//                                 Ano e semestre: %s
//                                 Local de aula: %s
//                                 """, idTurma, nome, anoSemestre, localAula));
//     }

//     public static void buscaAtividadePorTurmaEProfessor(int idProfessor) throws SQLException {
//         String sql = """
//                     select
//                     t.id_turma,
//                     d.nome,
//                     t.ano_semestre,
//                     t.local_aula,
//                     array_agg(
//                         jsonb_build_object(
//                             'id', a.id_atividade,
//                             'tipo', ta.descricao,
//                             'descricao', a.descricao_atividade,
//                             'entrega', a.dt_entrega
//                         )
//                     ) as atividades
//                 from atividades a
//                     join turmas t on t.id_turma = a.fk_turma
//                     join disciplinas d on t.fk_disciplina = d.id_disciplina
//                     join tipo_atividade ta on ta.id_tipo_atividade = a.fk_tipo_atividade
//                 where a.fk_professor = ? or ? = -1
//                 group by t.id_turma, d.nome
//                         """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idProfessor);
//         statement.setInt(2, idProfessor);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Professores.getById(idProfessor).imprimeProfessor();
//             }

//             mostraTurmaParticipada(resultSet.getInt("id_turma"), resultSet.getString("nome"),
//                     resultSet.getString("ano_semestre"), resultSet.getString("local_aula"));

//             ResultSet resultArray = resultSet.getArray("atividades").getResultSet();
//             while (resultArray.next()) {
//                 JsonObject json = new Gson().fromJson(resultArray.getString(2), JsonObject.class);
//                 for (Entry<String, JsonElement> entry : json.entrySet()) {
//                     System.out.println(
//                             String.format("%s: %s", entry.getKey(), entry.getValue().getAsString()));
//                 }
//             }
//             Menu.pausaMenu();
//         }
//     }

//     public static void buscaAtividadePorAlunoPorDisciplina(int idAluno, int idDisciplina) throws SQLException {
//         String sql = """
//                     select
//                     a2.id_aluno,
//                     a2.nome,
//                     a2.sobrenome,
//                     array_agg(
//                         jsonb_build_object(
//                             'Id', a.id_atividade,
//                             'Tipo', ta.descricao,
//                             'Descrição', a.descricao_atividade,
//                             'Data de entrega', a.dt_entrega,
//                             'Nota', aa.nota
//                         )
//                     ) as atividades
//                 from atividade_aluno aa
//                     join atividades a on a.id_atividade = aa.id_atividade
//                     join turmas t on t.id_turma = a.fk_turma
//                         and t.fk_disciplina = ?
//                     join alunos a2 on a2.id_aluno = aa.id_aluno
//                     join tipo_atividade ta on ta.id_tipo_atividade = a.fk_tipo_atividade
//                 where a2.id_aluno = ?
//                 group by 1
//                                 """;
//         PreparedStatement statement = conexao.prepareStatement(sql);
//         statement.setInt(1, idDisciplina);
//         statement.setInt(2, idAluno);
//         ResultSet resultSet = statement.executeQuery();
//         while (resultSet.next()) {
//             if (resultSet.isFirst()) {
//                 Disciplinas.getById(idDisciplina).imprimeDisciplina();
//             }

//             System.out.println(
//                     String.format("""

//                             ID do aluno: %d
//                             Nome completo: %s %s
//                             """, resultSet.getInt("id_aluno"), resultSet.getString("nome"),
//                             resultSet.getString("sobrenome")));

//             ResultSet resultArray = resultSet.getArray("atividades").getResultSet();
//             while (resultArray.next()) {
//                 JsonObject json = new Gson().fromJson(resultArray.getString(2), JsonObject.class);
//                 for (Entry<String, JsonElement> entry : json.entrySet()) {
//                     System.out.println(
//                             String.format("%s: %s", entry.getKey(), entry.getValue().getAsString()));
//                 }
//                 System.out.println();
//             }
//             Menu.pausaMenu();
//         }
//     }

//     private static void mostraMediaAtividade(int idAtividade, String descricao, String tipo, double media) {
//         System.out.println(
//                 String.format("""

//                         ID da atividade: %d
//                         Descrição: %s
//                         Tipo: %s
//                         Média: %.2f
//                         """, idAtividade, descricao, tipo, media));
//     }

//     public static void buscaMediaDeNotasDaAtividade() throws SQLException {
//         String sql = """
//                     select
//                     a.id_atividade,
//                     a.descricao_atividade,
//                     ta.descricao,
//                     avg(aa.nota) media
//                 from atividade_aluno aa
//                     join atividades a on a.id_atividade = aa.id_atividade
//                     join tipo_atividade ta on ta.id_tipo_atividade = a.fk_tipo_atividade
//                 group by 1, 3
//                 order by 1
//                         """;
//         Statement statement = conexao.createStatement();
//         ResultSet resultSet = statement.executeQuery(sql);
//         while (resultSet.next()) {
//             mostraMediaAtividade(
//                     resultSet.getInt("id_atividade"), resultSet.getString("descricao_atividade"),
//                     resultSet.getString("descricao"), resultSet.getDouble("media"));
//             Menu.pausaMenu();
//         }
//     }

// }
