package com.metricai.gamevault.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("db.properties"));
        } catch (IOException e) {
            System.out.println("ERRO: arquivo db.properties não encontrado na raiz do projeto!");
            System.out.println("Crie o arquivo com: db.url, db.user, db.password");
        }
        URL = props.getProperty("db.url", "");
        USER = props.getProperty("db.user", "");
        PASSWORD = props.getProperty("db.password", "");
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
