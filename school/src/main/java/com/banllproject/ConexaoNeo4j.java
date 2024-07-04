package com.banllproject;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConexaoNeo4j {

    private static ConexaoNeo4j instance;
    private final Driver driver;

    private ConexaoNeo4j() {
        Properties prop = getProperties();
        final String url = prop.getProperty("banco.url");
        final String usuario = prop.getProperty("banco.usuario");
        final String senha = prop.getProperty("banco.senha");

        driver = GraphDatabase.driver(url, AuthTokens.basic(usuario, senha));
    }

    public static ConexaoNeo4j getInstance() {
        if (instance == null) {
            synchronized (ConexaoNeo4j.class) {
                if (instance == null) {
                    instance = new ConexaoNeo4j();
                }
            }
        }
        return instance;
    }

    public Session getSession() {
        return driver.session();
    }

    public void close() {
        driver.close();
    }

    private static Properties getProperties() {
        try {
            Properties prop = new Properties();
            Path caminho = Files.walk(Paths.get(".").normalize().toAbsolutePath())
                                .filter(path -> path.toString().contains("project.properties"))
                                .findFirst()
                                .get();
            prop.load(Files.newInputStream(caminho));
            return prop;	  
        } catch (IOException e) {
            System.out.println("Erro! Ajuste seu project.properties!");
        }
        return null;	
	}
}

