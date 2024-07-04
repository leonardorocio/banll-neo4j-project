## Projeto BANII - Sistema de Gerenciamento - Escola - Usando Neo4j

# Como executar?
Para executar este projeto existem alguns pré-requisitos:
<ol>
  <li>Java SDK (JDK) instalado com a variável de ambiente JAVA_HOME configurada para o caminho de instalação do JDK;
      <br>Link: https://www.oracle.com/br/java/technologies/downloads/
  </li>
  <li>Maven configurado na variável de ambiente Path do sistema operacional, para que possa ser executado por linha de comando;</li>
  <br>
  <ol>
      <li>Para isto, basta baixar o Maven versão bin, conforme seu sistema operacional no link: https://maven.apache.org/download.cgi</li>
      <li>Extrair o conteúdo e configurar o Path adicionando a pasta bin. Mais informações: https://maven.apache.org/install.html</li>
  </ol>
  <li>Criar um banco de dados Neo4j e para isso é necessário seguir os seguintes passos:</li>
  <ul>
    <li>
      Baixar o Neo4j no seguinte link: https://neo4j.com/download/
    </li>
    <li>
      Na IDE do Neo4j, criar um novo projeto;
    </li>
    <li>
      Após a criação do projeto, criar um novo banco, clicando no botão de "Add" e em seguida "Local DBMS";
    </li>
    <li>
      Com o banco criado, iniciar uma conexão, ir no botão "Open Folder" que fica nos 3 pontos ao lado do botão "Open" para abrir uma pasta e abrir a pasta "Import"
    </li>
    <li>
      Copiar a pasta de backup deste repositório para a pasta "Import";
    </li>
    <li>
      Em seguida, abrir o Neo4j Browser e executar o script de dump encontrado no arquivo "dump.cypher", neste repositório.
    </li>
    <li>
      Novamente, no mesmo botão onde fica "Open Folder", abrir as "settings" e procurar por "dbms.security.auth_enabled=true" e trocar para false.
    </li>
  </ul>

  <li>Após configurados Java e Maven, em um terminal na pasta do projeto, mais especificamente em /banII-project/school,  utilizar o seguinte comando: mvn compile exec:java</li>
  <li>Pronto! O projeto já está executando.</li>
</ol>
