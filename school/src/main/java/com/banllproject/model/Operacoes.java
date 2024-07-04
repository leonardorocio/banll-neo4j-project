package com.banllproject.model;

import java.time.LocalDate;
import java.sql.SQLException;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Record;

import com.banllproject.ConexaoNeo4j;

public class Operacoes {

    private static Session conexao = ConexaoNeo4j.getInstance().getSession();

    public static void buscaDisciplinaPorCurso(int idCurso) {
        conexao.readTransaction(tx-> {
            String cypherQuery = "MATCH (c:Curso {id_curso: $idCurso})-[:INCLUI]->(d:Disciplina) " +
                                 "RETURN d.id_disciplina AS id_disciplina, d.nome AS nome, d.carga_horaria AS carga_horaria " +
                                 "ORDER BY d.nome";

            // Executando a consulta
            Result result = tx.run(cypherQuery, Values.parameters("idCurso", idCurso));

            // Iterando sobre os resultados
            Disciplinas disciplinas = new Disciplinas();
            while (result.hasNext()) {
                Record record = result.next();
                disciplinas = new Disciplinas(
                        record.get("id_disciplina").asInt(),
                        record.get("nome").asString(),
                        record.get("carga_horaria").asInt());
                disciplinas.imprimeDisciplina();
            }
            return null;
        });      
    }

    public static void buscaProfessoresPorDepartamento(int idDepartamento) throws SQLException {
        conexao.readTransaction(tx -> {
            String cypherQuery = "MATCH (p:Professor)-[:PERTENCE_A]->(d:Departamento {id_departamento: $idDepartamento}) " +
                                 "RETURN p.id_professor AS id_professor, p.nome AS nome, p.sobrenome AS sobrenome, p.cpf AS cpf, " +
                                 "p.sexo_biologico AS sexo_biologico, p.dt_nascimento AS dt_nascimento, d.id_departamento AS fk_departamento " +
                                 "ORDER BY p.nome";

            // Executando a consulta
            Result result = tx.run(cypherQuery, Values.parameters("idDepartamento", idDepartamento));

            // Iterando sobre os resultados
            while (result.hasNext()) {
                Record record = result.next();
                int idProfessor = record.get("id_professor").asInt();
                String nome = record.get("nome").asString();
                String sobrenome = record.get("sobrenome").asString();
                String cpf = record.get("cpf").asString();
                String sexoBiologico = record.get("sexo_biologico").asString();
                LocalDate dtNascimento = record.get("dt_nascimento").asLocalDate();
                int fkDepartamento = record.get("fk_departamento").asInt();

                // Recuperando o departamento
                Departamentos departamentos = Departamentos.getById(fkDepartamento);

                // Criando uma instância de Professores e imprimindo
                Professores professores = new Professores(idProfessor, nome, sobrenome, sexoBiologico, cpf, dtNascimento, fkDepartamento, departamentos);
                professores.imprimeProfessor();
            }
            return null;
        });
    }

    public static void buscaAlunosPorCurso(int idCurso) throws SQLException {
        conexao.readTransaction(tx -> {
            String cypherQuery;
            Result result; 
            if (idCurso == -1) {
                cypherQuery = "MATCH (a:Aluno) " +
                              "RETURN a.id_aluno AS id_aluno, a.nome AS nome, a.sobrenome AS sobrenome, a.cpf AS cpf, a.fk_curso AS fk_curso, " +
                              "a.dt_nascimento AS dt_nascimento, a.dt_ingresso AS dt_ingresso, a.sexo_biologico AS sexo_biologico " +
                              "ORDER BY a.nome";
                result = tx.run(cypherQuery);
            } else {
                cypherQuery = "MATCH (a:Aluno)-[:ESTUDA_EM]->(c:Curso {id_curso: $idCurso}) " +
                              "RETURN a.id_aluno AS id_aluno, a.nome AS nome, a.sobrenome AS sobrenome, a.cpf AS cpf, " +
                              "a.dt_nascimento AS dt_nascimento, a.dt_ingresso AS dt_ingresso, a.sexo_biologico AS sexo_biologico " +
                              "ORDER BY a.nome";
                result = tx.run(cypherQuery, Values.parameters("idCurso", idCurso));
            }

            while (result.hasNext()) {
                Record record = result.next();
                int idAluno = record.get("id_aluno").asInt();
                String nome = record.get("nome").asString();
                String sobrenome = record.get("sobrenome").asString();
                String cpf = record.get("cpf").asString();
                LocalDate dtNascimento = record.get("dt_nascimento").asLocalDate();
                LocalDate dtIngresso = record.get("dt_ingresso").asLocalDate();
                String sexoBiologico = record.get("sexo_biologico").asString();

                Cursos cursos = Cursos.getById(idCurso);

                Alunos aluno = new Alunos(idAluno, nome, sobrenome, dtNascimento, cpf, sexoBiologico, dtIngresso, idCurso, cursos);
                aluno.imprimeAluno();
            }
            return null;
        });
    }

    public static void buscaTurmasPorSemestre(String anoSemestre) throws SQLException {
        conexao.readTransaction(tx -> {
            String cypherQuery;
            if (anoSemestre.isEmpty()) {
                cypherQuery = "MATCH (t:Turma)-[:LECIONA]->(d:Disciplina) " +
                              "RETURN t.id_turma AS id_turma, t.ano_semestre AS ano_semestre, t.local_aula AS local_aula, " +
                              "d.id_disciplina AS fk_disciplina, d.nome AS nome " +
                              "ORDER BY t.local_aula";
            } else {
                cypherQuery = "MATCH (t:Turma)-[:LECIONA]->(d:Disciplina) " +
                              "WHERE t.ano_semestre = $anoSemestre OR $anoSemestre = '' " +
                              "RETURN t.id_turma AS id_turma, t.ano_semestre AS ano_semestre, t.local_aula AS local_aula, " +
                              "d.id_disciplina AS fk_disciplina, d.nome AS nome " +
                              "ORDER BY t.local_aula";
            }

            Result result = tx.run(cypherQuery, Values.parameters("anoSemestre", anoSemestre));

            while (result.hasNext()) {
                Record record = result.next();
                int idTurma = record.get("id_turma").asInt();
                String anoSem = record.get("ano_semestre").asString();
                String localAula = record.get("local_aula").asString();
                int fkDisciplina = record.get("fk_disciplina").asInt();

                Disciplinas disciplinas = Disciplinas.getById(fkDisciplina);

                Turmas turma = new Turmas(idTurma, anoSem, localAula, fkDisciplina, disciplinas);
                turma.imprimeTurma();
            }
            return null;
        });
    }

}
