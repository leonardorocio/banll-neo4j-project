// Criação de nós para alunos
LOAD CSV WITH HEADERS FROM 'file:///backups/alunos.csv' AS row
CREATE (:Aluno {
    id_aluno: toInteger(row.id_aluno),
    nome: row.nome,
    sobrenome: row.sobrenome,
    dt_nascimento: date(row.dt_nascimento),
    cpf: row.cpf,
    sexo_biologico: row.sexo_biologico,
    dt_ingresso: date(row.dt_ingresso)
});

// Criação de nós para cursos
LOAD CSV WITH HEADERS FROM 'file:///backups/cursos.csv' AS row
CREATE (:Curso {
    id_curso: toInteger(row.id_curso),
    nome: row.nome,
    duracao_minima: toInteger(row.duracao_minima),
    duracao_maxima: toInteger(row.duracao_maxima),
    sigla: row.sigla
});

// Criação de nós para departamentos
LOAD CSV WITH HEADERS FROM 'file:///backups/departamentos.csv' AS row
CREATE (:Departamento {
    id_departamento: toInteger(row.id_departamento),
    departamento: row.departamento,
    sigla: row.sigla
});

// Criação de nós para disciplinas
LOAD CSV WITH HEADERS FROM 'file:///backups/disciplinas.csv' AS row
CREATE (:Disciplina {
    id_disciplina: toInteger(row.id_disciplina),
    nome: row.nome,
    carga_horaria: toInteger(row.carga_horaria)
});

// Criação de nós para professores
LOAD CSV WITH HEADERS FROM 'file:///backups/professores.csv' AS row
CREATE (:Professor {
    id_professor: toInteger(row.id_professor),
    nome: row.nome,
    sobrenome: row.sobrenome,
    dt_nascimento: date(row.dt_nascimento),
    cpf: row.cpf,
    sexo_biologico: row.sexo_biologico
});

// Criação de nós para turmas
LOAD CSV WITH HEADERS FROM 'file:///backups/turmas.csv' AS row
CREATE (:Turma {
    id_turma: toInteger(row.id_turma),
    ano_semestre: row.ano_semestre,
    local_aula: row.local_aula
});

// Criação de nós para atividades
LOAD CSV WITH HEADERS FROM 'file:///backups/atividades.csv' AS row
CREATE (:Atividade {
    id_atividade: toInteger(row.id_atividade),
    descricao_atividade: row.descricao_atividade,
    dt_entrega: date(row.dt_entrega)
});

// Criação de nós para tipo de atividade
LOAD CSV WITH HEADERS FROM 'file:///backups/tipo_atividade.csv' AS row
CREATE (:TipoAtividade {
    id_tipo_atividade: toInteger(row.id_tipo_atividade),
    descricao: row.descricao
});

// Relacionamento entre alunos e cursos
LOAD CSV WITH HEADERS FROM 'file:///backups/alunos.csv' AS row
MATCH (a:Aluno {id_aluno: toInteger(row.id_aluno)})
MATCH (c:Curso {id_curso: toInteger(row.fk_curso)})
CREATE (a)-[:ESTUDA_EM]->(c);

// Relacionamento entre atividades e alunos
LOAD CSV WITH HEADERS FROM 'file:///backups/atividade_aluno.csv' AS row
MATCH (a:Aluno {id_aluno: toInteger(row.id_aluno)})
MATCH (at:Atividade {id_atividade: toInteger(row.id_atividade)})
CREATE (a)-[:PARTICIPA_EM]->(at);

LOAD CSV WITH HEADERS FROM 'file:///backups/atividade_aluno.csv' AS row
CREATE (:Nota {
    nota: toFloat(row.nota)
});

LOAD CSV WITH HEADERS FROM 'file:///backups/atividade_aluno.csv' AS row
MATCH (n:Nota {nota: row.nota})
MATCH (a:Aluno {id_aluno: toInteger(row.id_aluno)})
MATCH (at:Atividade {id_atividade: toInteger(row.id_atividade)})
CREATE (a)-[:NOTA_ALUNO]->(n);
CREATE (at)-[:NOTA_ATIVIDADE]->(n);

// Relacionamento entre cursos e disciplinas
LOAD CSV WITH HEADERS FROM 'file:///backups/cursos_disciplinas.csv' AS row
MATCH (c:Curso {id_curso: toInteger(row.id_curso)})
MATCH (d:Disciplina {id_disciplina: toInteger(row.id_disciplina)})
CREATE (c)-[:INCLUI]->(d);

// Relacionamento entre turmas e disciplinas
LOAD CSV WITH HEADERS FROM 'file:///backups/turmas.csv' AS row
MATCH (t:Turma {id_turma: toInteger(row.id_turma)})
MATCH (d:Disciplina {id_disciplina: toInteger(row.fk_disciplina)})
CREATE (t)-[:LECIONA]->(d);

// Relacionamento entre professores e departamentos
LOAD CSV WITH HEADERS FROM 'file:///backups/professores.csv' AS row
MATCH (p:Professor {id_professor: toInteger(row.id_professor)})
MATCH (d:Departamento {id_departamento: toInteger(row.fk_departamento)})
CREATE (p)-[:PERTENCE_A]->(d);

// Relacionamento entre atividades e tipos de atividade
LOAD CSV WITH HEADERS FROM 'file:///backups/atividades.csv' AS row
MATCH (at:Atividade {id_atividade: toInteger(row.id_atividade)})
MATCH (ta:TipoAtividade {id_tipo_atividade: toInteger(row.fk_tipo_atividade)})
CREATE (at)-[:TIPO_DE]->(ta);

// Relacionamento entre atividades e professores
LOAD CSV WITH HEADERS FROM 'file:///backups/atividades.csv' AS row
MATCH (at:Atividade {id_atividade: toInteger(row.id_atividade)})
MATCH (p:Professor {id_professor: toInteger(row.fk_professor)})
CREATE (at)-[:APLICADA_POR]->(p);

// Relacionamento entre atividades e turmas
LOAD CSV WITH HEADERS FROM 'file:///backups/atividades.csv' AS row
MATCH (at:Atividade {id_atividade: toInteger(row.id_atividade)})
MATCH (t:Turma {id_turma: toInteger(row.fk_turma)})
CREATE (at)-[:APLICADA_EM]->(t);

// Relacionamento entre turmas e professores
LOAD CSV WITH HEADERS FROM 'file:///backups/turma_professor.csv' AS row
MATCH (t:Turma {id_turma: toInteger(row.id_turma)})
MATCH (p:Professor {id_professor: toInteger(row.id_professor)})
CREATE (t)-[:ENSINADA_POR]->(p);

// Relacionamento entre turmas e alunos
LOAD CSV WITH HEADERS FROM 'file:///backups/turma_aluno.csv' AS row
MATCH (t:Turma {id_turma: toInteger(row.id_turma)})
MATCH (a:Aluno {id_aluno: toInteger(row.id_aluno)})
CREATE (t)-[:FREQUENTADA_POR]->(a);

